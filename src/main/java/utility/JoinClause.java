package utility;

import utility.query.QueryBuilder;

public record JoinClause(String sourceTable, String sourceColumn, String table, String destinationColumn, QueryBuilder.JoinType type){
    public JoinClause(String sourceTable, String sourceColumn, String table, String destinationColumn){
        this(sourceTable, sourceColumn, table, destinationColumn, QueryBuilder.JoinType.INNER);
    }
}
