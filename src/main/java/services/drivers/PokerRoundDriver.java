package services.drivers;

import utility.Column;
import utility.ColumnValue;
import poker.PokerRound;

import java.util.Arrays;

@DriverDependency(dependency = "PokerLoggingDriver")
public class PokerRoundDriver extends DriverBase {
    private static final PokerRoundDriver instance = new PokerRoundDriver();

    private PokerRoundDriver() {
        super("poker_round", Arrays.asList(
                new Column("game_id", Column.ColumnType.INTEGER, ""),
                new Column("round", Column.ColumnType.TEXT, ""),
                new Column("table_cards", Column.ColumnType.TEXT, "")
        ), "FOREIGN KEY(game_id) REFERENCES poker_game(id) ON DELETE CASCADE");
    }

    public static PokerRoundDriver getInstance() {
        return instance;
    }

    public void createPokerRound(int gameId, PokerRound round) {
        var roundId = insert(Arrays.asList(
                new ColumnValue("game_id", gameId),
                new ColumnValue("round", round.round()),
                new ColumnValue("table_cards", round.table().toString())
        ));
        var handDriver = PokerHandDriver.getInstance();
        for (var playerState : round.playerStates().values()) {
            handDriver.createPokerHand(roundId, playerState);
        }
    }
}
