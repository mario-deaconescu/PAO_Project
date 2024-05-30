package services.drivers;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DatabaseService {

    private static final String url = "jdbc:sqlite:database.db";
    private static final DatabaseService instance = new DatabaseService();
    private Connection connection;

    private DatabaseService() {

    }

    public static DatabaseService getInstance() {
        return instance;
    }

    private void initializeDependencies(Map<String, List<DriverBase>> dependencies, DriverBase instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (dependencies.containsKey(instance.getClass().getSimpleName())) {
            for (var dependency : dependencies.get(instance.getClass().getSimpleName())) {
                instance.getClass().getSuperclass().getMethod("initialize", Connection.class).invoke(dependency, connection);
                initializeDependencies(dependencies, dependency);
            }
        }
    }

    private void storeConnection() {
        // Store connection in all classes that have the @Driver annotation

        Reflections reflections = new Reflections("services.drivers");
        var classes = reflections.getSubTypesOf(DriverBase.class);
        HashMap<String, List<DriverBase>> dependencies = new HashMap<>();
        List<DriverBase> instances = classes.stream().map(clazz -> {
            try {
                var instance = (DriverBase) clazz.getMethod("getInstance").invoke(null);
                if(clazz.isAnnotationPresent(DriverDependency.class)){
                    var annotation = clazz.getAnnotation(DriverDependency.class);
                    dependencies.putIfAbsent(annotation.dependency(), new ArrayList<>());
                    dependencies.get(annotation.dependency()).add(instance);
                    return null;
                }
                return instance;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        for (var instance : instances) {
            if (instance == null) {
                continue;
            }
            try {
                instance.getClass().getSuperclass().getMethod("initialize", Connection.class).invoke(instance, connection);
                initializeDependencies(dependencies, instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void connect() {
        try {
//            Class.forName("org.sqlite.JDBC");
            // Print cwd
            connection = DriverManager.getConnection(url);
//            connection = new net.sf.log4jdbc.ConnectionSpy(connection);
            storeConnection();
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
