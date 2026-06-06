import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    // 数据库连接参数
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static String uri = "jdbc:mysql://localhost:3306/who%20is%20liar%3F?useSSL=false&serverTimezone=UTC";
    static String userName = "root";
    static String password = "123456";

    // 获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driverName); // 加载驱动
            conn = DriverManager.getConnection(uri, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭资源
    public static void close(Connection conn, java.sql.PreparedStatement pstmt, java.sql.ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
