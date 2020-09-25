package cafe;

import java.sql.*;

public class conexionBD {

    Connection con;

    public conexionBD() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://db4free.net:3306/integracionrj?autoReconnect=true&useSSL=false";
        String user = "integracionrj";
        String pass = "12345678";

        try {
            con = (Connection) DriverManager.getConnection(
                    url, user, pass);
            System.out.println("Conexion con la base datos realizada\n");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desconexion() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/*
public static void main(String[] args) throws SQLException {
        try {
            co = new conexionBD();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        co.desconexion();
    }
 */
