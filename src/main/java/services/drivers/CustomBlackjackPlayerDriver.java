package services.drivers;

import utility.Column;
import utility.ColumnValue;
import services.drivers.dto.CustomPokerPlayerDTO;
import utility.query.ColumnObject;
import utility.query.QueryClause;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CustomBlackjackPlayerDriver extends DriverBase{

    private static final CustomBlackjackPlayerDriver instance = new CustomBlackjackPlayerDriver();

    public static CustomBlackjackPlayerDriver getInstance() {
        return instance;
    }

    private CustomBlackjackPlayerDriver() {
        super("custom_blackjack_players", Arrays.asList(
                new Column("name", Column.ColumnType.TEXT, "UNIQUE"),
                new Column("strategy", Column.ColumnType.TEXT, "NOT NULL"),
                new Column("params", Column.ColumnType.TEXT, "")));
    }

    public Optional<CustomPokerPlayerDTO> get(String name) {
        var player = select(Arrays.asList(
                new Column("name", Column.ColumnType.TEXT, ""),
                new Column("strategy", Column.ColumnType.TEXT, ""),
                new Column("params", Column.ColumnType.TEXT, "")), new QueryClause("name = ?", new ColumnObject[]{new ColumnObject(name, Column.ColumnType.TEXT)}));
        if (player.isEmpty()) {
            return Optional.empty();
        }
        var p = player.getFirst();
        return Optional.of(new CustomPokerPlayerDTO((String) p.get("name"), (String) p.get("strategy"), (String) p.get("params")));
    }

    public void insert(String name, String strategy, String params) {
        insert(Arrays.asList(new ColumnValue("name", name), new ColumnValue("strategy", strategy), new ColumnValue("params", params)));
    }

    public void update(String name, String strategy, String params) {
        update(Arrays.asList(new ColumnValue("strategy", strategy), new ColumnValue("params", params)),
                new QueryClause("name = ?",
                        new ColumnObject[]{
                                new ColumnObject(name, Column.ColumnType.TEXT)}));
    }

    public List<CustomPokerPlayerDTO> getAll() {
        return selectAll().stream().map(p -> new CustomPokerPlayerDTO((String) p.get("name"), (String) p.get("strategy"), (String) p.get("params"))).toList();
    }

    public void delete(String name) {
        delete(new QueryClause("name = ?", new ColumnObject[]{new ColumnObject(name, Column.ColumnType.TEXT)}));
    }
}
