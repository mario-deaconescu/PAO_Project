package utility.query;

import services.drivers.DriverBase;
import utility.ColumnValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class UpdateQueryBuilder extends QueryBuilder{
    private final ArrayList<ColumnValue> columnValues = new ArrayList<>();
    public UpdateQueryBuilder(DriverBase fromTable) {
        super(fromTable);
    }

    public UpdateQueryBuilder addColumnValues(Collection<ColumnValue> columnValues) {
        this.columnValues.addAll(columnValues);
        return this;
    }

    public UpdateQueryBuilder addColumnValue(ColumnValue columnValue) {
        this.columnValues.add(columnValue);
        return this;
    }

    @Override
    protected QueryClause buildCoreQuery() {
        var valueString = String.join(", ", columnValues.stream().map(column -> STR."\{column.column()} = ?").toArray(String[]::new));
        var values = columnValues.stream().map(ColumnValue::getValue).toArray(ColumnObject[]::new);
        return new QueryClause(STR."UPDATE \{sourceTable.getTable()} SET \{valueString}", values);
    }

}
