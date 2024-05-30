package utility;

import utility.query.ColumnObject;

public record ColumnValue(String column, Object value, Column.ColumnType type) {

    public ColumnValue(String column, Object value) {
        this(column, value, Column.ColumnType.OTHER);
    }
    public ColumnValue(String column, Object value, Column.ColumnType type) {
        this.column = column;
        this.value = value;
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public ColumnObject getValue() {
        return new ColumnObject(value, type);
    }
}
