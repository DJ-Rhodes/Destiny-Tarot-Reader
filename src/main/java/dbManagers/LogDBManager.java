package dbManagers;

import domain.LogItem;
import domain.MajorCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDBManager {
    private String databasePath;

    public LogDBManager(String databasePath) {
        this.databasePath = databasePath;
    }

    //Adds a new log entry to the database
    public void add(LogItem logItem) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO MyLog (date, question, card, rating, journal) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, logItem.getDate());
            stmt.setString(2, logItem.getQuestion());
            stmt.setString(3, logItem.getCard());
            stmt.setInt(4, logItem.getRating());
            stmt.setString(5, logItem.getEntry());
            stmt.executeUpdate();
        }
    }


    //Lists all log items currently stored
    public List<LogItem> list() throws SQLException {
        List<LogItem> items = new ArrayList<>();
        try (Connection connection = createConnectionAndEnsureDatabase();
             ResultSet results = connection.prepareStatement("SELECT * FROM MyLog").executeQuery()) {
            while (results.next()) {
                items.add(new LogItem(results.getInt("id"), results.getString("date"), results.getString("question"), results.getString("card"), results.getInt("rating"), results.getString("journal")));
            }
        }

        return items;
    }


    //Removes log item based on selected ID
    public void removeItem(int id) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM MyLog WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    //Resets the auto_id key back to 1
    public void resetKey() throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("SET @count = 0; UPDATE MyLog SET MyLog.id = @count:= @count + 1");
            stmt.executeUpdate();
        }
    }


    //Creates table if none exists already
    public Connection createConnectionAndEnsureDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(this.databasePath, "test", "test");
        try {
            conn.prepareStatement("CREATE TABLE MyLog (id int auto_increment primary key, date varchar(255), question varchar(255), card varchar(255), rating int(255), journal varchar(65535))").execute();
        } catch(SQLException t) {
        }

        return conn;
    }
}
