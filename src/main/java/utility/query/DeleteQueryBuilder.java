package utility.query;

import services.drivers.DriverBase;

public class DeleteQueryBuilder extends QueryBuilder{

    public DeleteQueryBuilder(DriverBase fromTable) {
        super(fromTable);
    }

    @Override
    protected QueryClause buildCoreQuery() {
        return new QueryClause(STR."DELETE FROM \{sourceTable.getTable()}");
    }
}
