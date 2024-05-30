package utility.query;

public record QueryClause(String clause, ColumnObject[] values) {
    public QueryClause(String clause) {
        this(clause, new ColumnObject[0]);
    }
}
