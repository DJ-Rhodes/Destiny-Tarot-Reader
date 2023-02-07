package dbManagers;

import domain.MajorCard;
import domain.MinorCard;

import java.sql.*;

public class MinorDBManager {

    private String databasePath;

    public MinorDBManager(String databasePath) {
        this.databasePath = databasePath;
    }


    //Selects a minor card from database based on selected ID
    public MinorCard drawById(int id) throws SQLException, ClassNotFoundException {
        MinorCard mc =  new MinorCard(0,"", "", "", "", "", "", "");
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = createConnectionAndEnsureDatabase().prepareStatement("SELECT * FROM MinorCards WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                mc = new MinorCard(rs.getInt("id"), rs.getString("imageFile"), rs.getString("arcana"), rs.getString("name"), rs.getString("suit"), rs.getString("keyword"), rs.getString("correspondence"), rs.getString("desc"));
            }
        }
        return mc;
    }



    //Creates table if none exists, then creates connection to table
    public Connection createConnectionAndEnsureDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(this.databasePath, "test", "test");
        try {
            conn.prepareStatement("CREATE TABLE MinorCards (id int auto_increment primary key, imageFile varchar(255), arcana varchar(255), name varchar(255), suit varchar(255), keyword varchar(255), correspondence varchar(255), desc varchar(65535))").execute();
        } catch (SQLException t) {
        }

        return conn;
    }
}
