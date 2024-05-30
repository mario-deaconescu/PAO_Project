package services.drivers;

import utility.Column;
import utility.ColumnValue;
import poker.PokerPlayerState;

import java.util.Arrays;

@DriverDependency(dependency = "PokerRoundDriver")
public class PokerHandDriver extends DriverBase{

    private static final PokerHandDriver instance = new PokerHandDriver();
    private PokerHandDriver() {
        super("poker_hand", Arrays.asList(
                new Column("round_id", Column.ColumnType.INTEGER, ""),
                new Column("player_name", Column.ColumnType.TEXT, ""),
                new Column("hand", Column.ColumnType.TEXT, ""),
                new Column("current_bet", Column.ColumnType.REAL, ""),
                new Column("total_bet", Column.ColumnType.REAL, ""),
                new Column("folded", Column.ColumnType.INTEGER, ""),
                new Column("all_in", Column.ColumnType.INTEGER, "")
        ), "FOREIGN KEY(round_id) REFERENCES poker_round(id) ON DELETE CASCADE");
    }

    public static PokerHandDriver getInstance() {
        return instance;
    }

    public void createPokerHand(int roundId, PokerPlayerState playerState) {
        insert(Arrays.asList(
                new ColumnValue("round_id", roundId),
                new ColumnValue("player_name", playerState.name()),
                new ColumnValue("hand", playerState.hand().toString()),
                new ColumnValue("current_bet", playerState.currentBet()),
                new ColumnValue("total_bet", playerState.totalBet()),
                new ColumnValue("folded", playerState.folded() ? 1 : 0),
                new ColumnValue("all_in", playerState.allIn() ? 1 : 0)
        ));
    }
}
