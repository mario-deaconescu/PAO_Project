package services;

import java.io.*;
import java.util.Date;

public class CSVService {

    private static final String logPath = "log.csv";
    private final PrintWriter logWriter;
    private static final CSVService instance;

    static {
        try {
            instance = new CSVService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CSVService getInstance() {
        return instance;
    }

    private CSVService() throws IOException {
        FileWriter logFile = new FileWriter(logPath, true);
        BufferedWriter logBuffer = new BufferedWriter(logFile);
        logWriter = new PrintWriter(logBuffer);
        // Check if file is empty
        File file = new File(logPath);
        if (file.length() == 0) {
            logWriter.println("nume_actiune,timestamp");
        }
    }

    private String formatMessage(String message) {
        // Escape quotes
        var result = message.replace("\"", "\"\"");
        // Add quotes
        return "\"" + result + "\"";
    }

    public void log(String message, Date date) {
        // Log message to CSV file
        logWriter.println(STR."\{formatMessage(message)},\{date.toString()}");
        logWriter.flush();
    }

}
