package QuanLyKhachSan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSQL {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String url = "jdbc:sqlserver://LAPTOP-OTLG85NJ\\MSSQLSERVER2022:1433;databaseName=QuanLyKhachSan;encrypt=false;trustServerCertificate=true";
            String userName = "sa";
            String password = "123456789";
   
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối cơ sở dữ liệu:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi không tìm thấy driver JDBC.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } else {
            System.out.println("Không thể kết nối cơ sở dữ liệu.");
        }
    }
}
