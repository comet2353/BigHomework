import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAO {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
   //姓名是否存在于数据库
    public boolean checkNameExist(String name) {
    boolean isExist = false;
    String sql = "select * from student where name = ?";
    try {
    conn = DB.getConnection();//数据库连接
    pstmt = conn.prepareStatement(sql);//编制SQL语句
    pstmt.setString(1, name);//占位符赋值
    rs = pstmt.executeQuery();
    // rs.next()：判断查询结果里有没有数据
    if (rs.next()) {
    isExist = true;
    }
     } catch (Exception e) {
            // 捕获异常：数据库断开、SQL错误等
            e.printStackTrace();
        }
    return isExist;
    }


}
