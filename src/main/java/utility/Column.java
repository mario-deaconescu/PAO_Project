package utility;

import javax.annotation.Nullable;
import java.sql.ResultSet;

public record Column(String name, ColumnType type, String extraOptions) {

    public Column(String name, ColumnType type) {
        this(name, type, "");
    }
    public Column {
        if (name == null || type == null) {
            throw new IllegalArgumentException("Column name and type cannot be null");
        }
    }

    public enum ColumnType {
        TEXT,
        INTEGER,
        REAL,
        OTHER
    }

    public static ColumnType fromMetadata(String metadata) {
        try{
            return ColumnType.valueOf(metadata.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ColumnType.OTHER;
        }
    }

    public Object getFromSet(ResultSet set) {
        try {
            switch (type) {
                case TEXT -> {
                    return set.getString(name);
                }
                case INTEGER -> {
                    return set.getInt(name);
                }
                case REAL -> {
                    return set.getDouble(name);
                }
                default -> {
                    return set.getObject(name);
                }
            }
        } catch (Exception e) {
            return null;
        }
    }
}
