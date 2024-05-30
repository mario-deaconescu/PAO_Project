package utility.query;

import services.drivers.DriverBase;
import utility.Column;
import utility.ColumnValue;
import utility.JoinClause;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class SelectQueryBuilder extends QueryBuilder {

    private boolean distinct = false;
    private boolean all = false;
    private final ArrayList<Column> columns = new ArrayList<>();
    private final ArrayList<JoinClause> joins = new ArrayList<>();

    public SelectQueryBuilder(DriverBase fromTable) {
        super(fromTable);
    }

    public SelectQueryBuilder addColumns(Collection<Column> columns) {
        if (this.all) throw new IllegalStateException("Cannot add columns when selecting all columns");
        this.columns.addAll(columns);
        return this;
    }

    public SelectQueryBuilder addColumn(Column column) {
        if (this.all) throw new IllegalStateException("Cannot add columns when selecting all columns");
        this.columns.add(column);
        return this;
    }

    public SelectQueryBuilder all() {
        this.all = true;
        return this;
    }

    public SelectQueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    @Override
    protected QueryClause buildCoreQuery() {
        var columnString = all ? "*" : String.join(", ", columns.stream().map(Column::name).toArray(String[]::new));
        StringBuilder query = new StringBuilder(STR."SELECT\{distinct ? " DISTINCT " : " "}\{columnString} FROM \{sourceTable.getTable()}\n");
        for (var join : joins) {
            query.append(STR."JOIN \{join.table()} ON \{join.sourceTable()}.\{join.sourceColumn()} = \{join.table()}.\{join.destinationColumn()}\n");
        }
        return new QueryClause(query.toString(), new ColumnObject[0]);
    }

    @Override
    protected ArrayList<HashMap<String, Object>> processSet(ResultSet set) throws SQLException {
        var results = new ArrayList<HashMap<String, Object>>();
        var columns = new ArrayList<Column>();
        if(all){
            var metaData = set.getMetaData();
            var columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(new Column(metaData.getColumnName(i), Column.fromMetadata(metaData.getColumnTypeName(i)), ""));
            }
        } else {
            columns.addAll(this.columns);
        }
        while (set.next()) {
            var map = new HashMap<String, Object>();
            for (var column : columns) {
                map.put(column.name(), column.getFromSet(set));
            }
            results.add(map);
        }
        return results;
    }

    public SelectQueryBuilder join(String table, String column, String onTable, String onColumn, JoinType type) {
        joins.add(new JoinClause(table, column, onTable, onColumn, type));
        return this;
    }

    public SelectQueryBuilder join(String table, String column, String onTable, String onColumn) {
        joins.add(new JoinClause(table, column, onTable, onColumn));
        return this;
    }
}
