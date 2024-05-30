package utility.query;

import services.CSVService;
import services.drivers.DatabaseService;
import services.drivers.DriverBase;
import utility.JoinClause;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class QueryBuilder {

    protected QueryBuilder(DriverBase sourceTable) {
        this.sourceTable = sourceTable;
    }

    public enum JoinType {
        INNER,
        LEFT,
        RIGHT,
        FULL
    }

    public enum QueryRunType{
        QUERY,
        UPDATE,
    }

    protected QueryClause whereClause = null;
    protected final DriverBase sourceTable;

    public static SelectQueryBuilder select(DriverBase fromTable) {
        return new SelectQueryBuilder(fromTable);
    }

    public static InsertQueryBuilder insert(DriverBase fromTable) {
        return new InsertQueryBuilder(fromTable);
    }

    public static UpdateQueryBuilder update(DriverBase fromTable) {
        return new UpdateQueryBuilder(fromTable);
    }

    public static DeleteQueryBuilder delete(DriverBase fromTable) {
        return new DeleteQueryBuilder(fromTable);
    }

    protected QueryClause buildQuery() {
        var coreQuery = buildCoreQuery();
        var query = new StringBuilder(coreQuery.clause());
        ArrayList<ColumnObject> values = new ArrayList<>(Arrays.asList(coreQuery.values()));
        if (whereClause != null) {
            query.append(" WHERE ").append(whereClause.clause());
            values.addAll(Arrays.asList(whereClause.values()));
        }
        return new QueryClause(query.toString(), values.toArray(new ColumnObject[0]));
    }

    protected abstract QueryClause buildCoreQuery();

    protected QueryClause buildPostQuery(QueryClause query){
        return query;
    }

    public ArrayList<HashMap<String, Object>> run() {
        return run(QueryRunType.QUERY);
    }

    protected ArrayList<HashMap<String, Object>> processSet(ResultSet set) throws SQLException {
        return new ArrayList<>();
    }

    public ArrayList<HashMap<String, Object>> run(QueryRunType runType) {
        var query = buildPostQuery(buildQuery());
        var connection = sourceTable.getConnection();
        try (var statement = connection.prepareStatement(query.clause())) {
            for(var i = 0; i < query.values().length; i++) {
                query.values()[i].addParameter(statement, i + 1);
            }
            CSVService.getInstance().log(statement.toString(), new Date());
            if(runType == QueryRunType.QUERY) {
                var set = statement.executeQuery();
                return processSet(set);
            } else {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public QueryBuilder where(QueryClause whereClause) {
        this.whereClause = whereClause;
        return this;
    }
}
