package services.drivers;

import cards.CardHand;
import poker.PokerPlayerState;
import utility.Column;
import utility.ColumnValue;
import poker.PokerRound;
import utility.query.ColumnObject;
import utility.query.QueryBuilder;
import utility.query.QueryClause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class PokerLoggingDriver extends DriverBase {

    private static final PokerLoggingDriver instance = new PokerLoggingDriver();

    private PokerLoggingDriver() {
        super("poker_game", Arrays.asList(
                new Column("timestamp", Column.ColumnType.TEXT, ""),
                new Column("num_players", Column.ColumnType.INTEGER, "")
        ));
    }

    public static PokerLoggingDriver getInstance() {
        return instance;
    }

    public void createPokerGame(Collection<PokerRound> rounds, int numPlayers) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        var gameId = insert(Arrays.asList(
                new ColumnValue("timestamp", timestamp),
                new ColumnValue("num_players", numPlayers)
        ));
        var roundDriver = PokerRoundDriver.getInstance();
        for (var round : rounds) {
            roundDriver.createPokerRound(gameId, round);
        }
    }

    public ArrayList<PokerRound> getPokerGame(int gameId) {
        var result = QueryBuilder.select(this)
                .all()
                .join(getTable(), "id", PokerRoundDriver.getInstance().getTable(), "game_id")
                .join(PokerRoundDriver.getInstance().getTable(), "id", PokerHandDriver.getInstance().getTable(), "round_id")
                .where(new QueryClause("poker_game.id = ?", new ColumnObject[]{new ColumnObject(gameId, Column.ColumnType.INTEGER)}))
                .run();
        if (result.isEmpty()) {
            throw new IllegalArgumentException(STR."No game found with id \{gameId}");
        }
        var rounds = new HashMap<PokerRound.Round, PokerRound>();
        for (var row : result) {
            var roundType = PokerRound.Round.valueOf(row.get("round").toString());
            var tableCardsString = (String) row.get("table_cards");
            // Remove brackets from table cards
            var tableCards = CardHand.fromString(tableCardsString);
            if (!rounds.containsKey(roundType)) {
                rounds.put(roundType, new PokerRound(roundType, tableCards));
            }
            var player = row.get("player_name").toString();
            var playerCurrentBet = (double) row.get("current_bet");
            var playerTotalBet = (double) row.get("total_bet");
            var playerAllIn = (int) row.get("all_in") != 0;
            var playerFolded = (int) row.get("folded") != 0;
            var playerCardsString = (String) row.get("hand");
            // Remove brackets from player cards
            var playerCards = CardHand.fromString(playerCardsString);
            var round = rounds.get(roundType);
            if (!round.playerStates().containsKey(player)) {
                round.playerStates().put(player, new PokerPlayerState(player, playerCards, playerCurrentBet, playerTotalBet, playerAllIn, playerFolded));
            }
        }
        return new ArrayList<>(rounds.values());
    }

}
