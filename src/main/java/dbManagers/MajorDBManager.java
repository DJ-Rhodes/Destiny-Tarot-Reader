package dbManagers;

import domain.MajorCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MajorDBManager {

    private String databasePath;

    public MajorDBManager(String databasePath) {
        this.databasePath = databasePath;
    }


    //Selects a major card from the database with selected ID
    public MajorCard drawById(int id) throws SQLException, ClassNotFoundException {
        MajorCard mc =  new MajorCard(0, "", "", "", "", "", "", "");
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = createConnectionAndEnsureDatabase().prepareStatement("SELECT * FROM MajorCards WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                mc = new MajorCard(rs.getInt("id"), rs.getString("imageFile"), rs.getString("arcana"), rs.getString("name"), rs.getString("keyword"), rs.getString("hebrew"), rs.getString("correspondence"), rs.getString("desc"));
            }
        }
        return mc;
    }

    //Creates table if none exists then creates connection to table
    public Connection createConnectionAndEnsureDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(this.databasePath, "test", "test");
        try {
            conn.prepareStatement("CREATE TABLE MajorCards (id int auto_increment primary key, imageFile varchar(255), arcana varchar(255), name varchar(255), keyword varchar(255), hebrew varchar(255), correspondence varchar(255), desc varchar(65535))").execute();
        } catch (SQLException t) {
        }

        return conn;
    }
}
