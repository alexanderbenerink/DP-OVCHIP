package nl.hu.dp;

import java.sql.*;

public class main {
    public static void main(String[] args) {
        try {
            // set up postgresql jdbc connection
            String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=root";
            Connection conn = DriverManager.getConnection(url);

            // sql statements to query all travelers
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger");

            // print the resultset to console
            System.out.print("Alle reizigers:\n");
            while (rs.next())
            {
                String id = "\t#" + rs.getString(1) + ":";
                String firstLetters = rs.getString(2) + ".";
                String affix = rs.getString(3);
                String lastName = rs.getString(4);
                String dob = "(" + rs.getString(5) + ")";

                if (affix == null) {
                    System.out.println(id + " " + firstLetters + " " + lastName + " " + dob);
                } else {
                    System.out.println(id + " " + firstLetters + " " + affix + " " + lastName + " " + dob);
                }
            }

            // close connections
            rs.close();
            st.close();
            conn.close();
        }
        catch (SQLException sqlex) {
            System.err.println("Travelers couldn't be fetched: " + sqlex.getMessage());
        }
    }
}