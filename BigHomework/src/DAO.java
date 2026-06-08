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
        String sql = "select * from TopSecretArchives where name = ?";
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
    public boolean addStudent(String num, String name, String nickname, String workplace, String hobby, String notes) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "insert into TopSecretArchives(number, name, nickname, workplace, hobby, remark) values(?, ?, ?, ?, ?, ?)";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, num);
            pstmt.setString(2, name);
            pstmt.setString(3, nickname);
            pstmt.setString(4, workplace);
            pstmt.setString(5, hobby);
            pstmt.setString(6, notes);
            int rows = pstmt.executeUpdate();
            return rows > 0; // 插入成功返回true
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, null);
        }
        return false;
    }

    // 通过查找姓名，修改学生
    public boolean updateStudent(String num, String name, String nickname, String workplace, String hobby, String notes) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "update TopSecretArchives set number=?, nickname=?, workplace=?, hobby=?, remark=? where name=?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, num);
            pstmt.setString(2, nickname);
            pstmt.setString(3, workplace);
            pstmt.setString(4, hobby);
            pstmt.setString(5, notes);
            pstmt.setString(6, name);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.close(conn, pstmt, null);
        }
        return false;
    }

    // 通过查找姓名，删除学生
    public boolean deleteStudent(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "delete from TopSecretArchives where name=?";
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
        String sql = "select * from TopSecretArchives";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("workplace"),
                        rs.getString("hobby"),
                        rs.getString("remark")
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
        String sql = "select * from TopSecretArchives where name=?";
        try {
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("workplace"),
                        rs.getString("hobby"),
                        rs.getString("remark")
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

