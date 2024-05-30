package utility.query;

import utility.Column;

import java.sql.PreparedStatement;

public record ColumnObject(Object value, Column.ColumnType type) {
    public ColumnObject(Object value) {
        this(value, Column.ColumnType.OTHER);
    }

    public void addParameter(PreparedStatement statement, int index) {
        try {
            switch (type) {
                case TEXT -> statement.setString(index, (String) value);
                case INTEGER -> statement.setInt(index, (int) value);
                case REAL -> statement.setDouble(index, (double) value);
                case OTHER -> statement.setObject(index, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
