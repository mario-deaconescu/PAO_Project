package utility.query;

import services.drivers.DriverBase;
import utility.Column;
import utility.ColumnValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class InsertQueryBuilder extends QueryBuilder {

    private final ArrayList<ColumnValue> columnValues = new ArrayList<>();
    private boolean returnId = false;

    public InsertQueryBuilder(DriverBase fromTable) {
        super(fromTable);
    }

    public InsertQueryBuilder addColumnValues(Collection<ColumnValue> columnValues) {
        this.columnValues.addAll(columnValues);
        return this;
    }

    public InsertQueryBuilder addColumnValue(ColumnValue columnValue) {
        this.columnValues.add(columnValue);
        return this;
    }

    public InsertQueryBuilder returnId() {
        this.returnId = true;
        return this;
    }

    @Override
    protected QueryClause buildCoreQuery() {
        var columnString = String.join(", ", columnValues.stream().map(ColumnValue::getColumn).toArray(String[]::new));
        var valueString = String.join(", ", columnValues.stream().map(_ -> "?").toArray(String[]::new));
        var values = columnValues.stream().map(ColumnValue::getValue).toArray(ColumnObject[]::new);
        return new QueryClause(STR."INSERT INTO \{sourceTable.getTable()} (\{columnString}) VALUES (\{valueString})", values);
    }

    @Override
    protected QueryClause buildPostQuery(QueryClause query) {
        if (returnId) {
            return new QueryClause(STR."\{query.clause()} RETURNING id", query.values());
        }
        return query;
    }

    @Override
    protected ArrayList<HashMap<String, Object>> processSet(ResultSet set) throws SQLException {
        if (returnId) {
            if (set.next()) {
                var map = new HashMap<String, Object>();
                map.put("id", set.getInt(1));
                return new ArrayList<>(List.of(map));
            }
        }
        return new ArrayList<>();
    }
}
