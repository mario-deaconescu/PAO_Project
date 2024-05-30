package services.drivers;

import utility.Column;
import utility.ColumnValue;
import utility.query.QueryBuilder;
import utility.query.QueryClause;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class DriverBase {
    protected Connection connection = null;
    private final String table;
    private final Collection<Column> columns;
    private final String extraOptions;

    protected DriverBase(String table, Collection<Column> columns, @Nullable String extraOptions) {
        this.table = table;
        this.columns = columns;
        this.extraOptions = extraOptions;
    }

    protected DriverBase(String table, Collection<Column> columns) {
        this(table, columns, null);
    }

    public void initialize(Connection connection) {
        this.connection = connection;
        checkTable();
    }

    private void checkTable() {
        // Check if table exists
        StringBuilder query = new StringBuilder(STR."CREATE TABLE IF NOT EXISTS \{table} (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        for (var column : columns) {
            query.append(STR."\{column.name()} \{column.type()} \{column.extraOptions()}, ");
        }
        query.delete(query.length() - 2, query.length());
        if (extraOptions != null) {
            query.append(", ").append(extraOptions);
        }
        query.append(");");

        try (var statement = connection.prepareStatement(query.toString())) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int insert(Collection<ColumnValue> values) {
        var result = QueryBuilder.insert(this)
                .addColumnValues(values)
                .returnId()
                .run(QueryBuilder.QueryRunType.QUERY);
        if(result.isEmpty()) {
            throw new IllegalStateException("Failed to insert");
        }
        return (int) result.getFirst().get("id");
    }

    protected ArrayList<HashMap<String, Object>> select(Collection<Column> columns, @Nullable QueryClause where) {
        var query = QueryBuilder.select(this)
                .addColumns(columns);
        if (where != null) {
            query.where(where);
        }
        return query.run();
    }

    protected void update(Collection<ColumnValue> values, @Nullable QueryClause where) {
        var query = QueryBuilder.update(this)
                .addColumnValues(values);
        if (where != null) {
            query.where(where);
        }
        query.run(QueryBuilder.QueryRunType.UPDATE);
    }

    protected void delete(QueryClause where) {
        QueryBuilder.delete(this)
                .where(where)
                .run(QueryBuilder.QueryRunType.UPDATE);
    }

    protected ArrayList<HashMap<String, Object>> selectAll() {
        return QueryBuilder.select(this)
                .all()
                .run();
    }

    public String getTable() {
        return table;
    }

    public Collection<Column> getColumns() {
        return columns;
    }

    public Connection getConnection() {
        return connection;
    }
}
