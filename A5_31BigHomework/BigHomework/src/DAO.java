import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    // 姓名是否存在于数据库
    public boolean checkNameExist(String name) {
        boolean isExist = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from student where name = ?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, rs); // 关闭资源
        }
        return isExist;
    }

    // 添加学生
    public boolean addStudent(String name, int age, String sex, double height) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "insert into student(name, age, sex, height) values(?, ?, ?, ?)";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, sex);
            pstmt.setDouble(4, height);
            int rows = pstmt.executeUpdate();
            return rows > 0; // 插入成功返回true
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, null);
        }
        return false;
    }

    // 修改学生
    public boolean updateStudent(String name, int age, String sex, double height) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "update student set age=?, sex=?, height=? where name=?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, age);
            pstmt.setString(2, sex);
            pstmt.setDouble(3, height);
            pstmt.setString(4, name);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, null);
        }
        return false;
    }

    // 删除学生
    public boolean deleteStudent(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "delete from student where name=?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, null);
        }
        return false;
    }

    // 查询所有学生
    public List<Object[]> getAllStudents() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select id, name, height, sex, age from student";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("height"),
                        rs.getString("sex"),
                        rs.getInt("age")
                };
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, rs);
        }
        return list;
    }

    // 按姓名查询学生
    public List<Object[]> searchStudent(String name) {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select id, name, height, sex, age from student where name=?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("height"),
                        rs.getString("sex"),
                        rs.getInt("age")
                };
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, rs);
        }
        return list;
    }
}