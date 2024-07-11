package jp.main.dao;

import jp.main.model.Teacher;
import jp.main.base.JdbcTest;
import jp.main.service.TeacherServlet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TeacherDAO {
    private static final String INSERT_TEACHERS_SQL = "INSERT INTO teachers (id, name, age, sex, course) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_TEACHER_BY_ID = "SELECT id, name, age, sex, course FROM teachers WHERE id = ?";
    private static final String SELECT_ALL_TEACHERS = "SELECT * FROM teachers";
    private static final String DELETE_TEACHERS_SQL = "DELETE FROM teachers WHERE id = ?";
    private static final String UPDATE_TEACHERS_SQL = "UPDATE teachers SET name = ?, age = ?, sex = ?, course = ? WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(TeacherServlet.class.getName());

    // 教師が存在するかどうかを確認するメソッド
    public boolean existsTeacher(int tid) throws SQLException {
        String sql = "SELECT COUNT(*) FROM teachers WHERE id = ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tid);

            // クエリをログに出力
            LOGGER.log(Level.INFO, "Executing SQL query: {0}", stmt.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師の存在チェック中にエラーが発生しました。", e);
        }
        return false;
    }

    // 新しい教師を挿入するメソッド
    public void insertTeacher(Teacher teacher) throws SQLException {
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TEACHERS_SQL)) {
            stmt.setInt(1, teacher.getId());
            stmt.setString(2, teacher.getName());
            stmt.setInt(3, teacher.getAge());
            stmt.setString(4, teacher.getSex());
            stmt.setString(5, teacher.getCourse());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("教師の挿入中にエラーが発生しました。", e);
        }
    }

    // 指定したIDの教師情報を取得するメソッド
    public Teacher selectTeacher(int id) throws SQLException {
        Teacher teacher = null;
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int teacherId = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String sex = rs.getString("sex");
                    String course = rs.getString("course");
                    teacher = new Teacher(teacherId, name, age, sex, course);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師の取得中にエラーが発生しました。", e);
        }
        return teacher;
    }

    // 全ての教師情報を取得するメソッド
    public List<Teacher> selectAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TEACHERS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String sex = rs.getString("sex");
                String course = rs.getString("course");
                Teacher teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
                teacher.setAge(age);
                teacher.setSex(sex);
                teacher.setCourse(course);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all teachers", e);
        }
        return teachers;
    }

    // 指定したIDの教師を削除するメソッド
    public boolean deleteTeacher(int id) {
        boolean rowDeleted = false;
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEACHERS_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting teacher with ID: " + id, e);
        }
        return rowDeleted;
    }

    // 指定した教師情報を更新するメソッド
    public boolean updateTeacher(Teacher teacher) throws SQLException {
        StringBuilder sql = new StringBuilder(UPDATE_TEACHERS_SQL);
        boolean firstField = true;

        if (teacher.getName() != null && !teacher.getName().isEmpty()) {
            sql.append(firstField ? " SET name = ?" : ", name = ?");
            firstField = false;
        }
        if (teacher.getAge() > 0) {
            sql.append(firstField ? " SET age = ?" : ", age = ?");
            firstField = false;
        }
        if (teacher.getSex() != null && !teacher.getSex().isEmpty()) {
            sql.append(firstField ? " SET sex = ?" : ", sex = ?");
            firstField = false;
        }
        if (teacher.getCourse() != null && !teacher.getCourse().isEmpty()) {
            sql.append(firstField ? " SET course = ?" : ", course = ?");
        }
        sql.append(" WHERE id = ?");
        boolean rowUpdated = false;
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            if (teacher.getName() != null && !teacher.getName().isEmpty()) {
                statement.setString(parameterIndex++, teacher.getName());
            }
            if (teacher.getAge() > 0) {
                statement.setInt(parameterIndex++, teacher.getAge());
            }
            if (teacher.getSex() != null && !teacher.getSex().isEmpty()) {
                statement.setString(parameterIndex++, teacher.getSex());
            }
            if (teacher.getCourse() != null && !teacher.getCourse().isEmpty()) {
                statement.setString(parameterIndex++, teacher.getCourse());
            }
            statement.setInt(parameterIndex, teacher.getId());

            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("教師情報の更新中にエラーが発生しました。", e);
        }
        return rowUpdated;
    }

    // 名前、コース、年齢で教師情報を検索するメソッド
    public List<Teacher> searchTeachers(Integer id, String name, String course) {
        List<Teacher> teachers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM teachers WHERE 1=1");

        if (id != null) {
            sql.append(" AND id = ?");
        }
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
        }
        if (course != null && !course.trim().isEmpty()) {
            sql.append(" AND course = ?");
        }

        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            if (id != null) {
                preparedStatement.setInt(parameterIndex++, id);
            }
            if (name != null && !name.trim().isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + name.trim() + "%");
            }
            if (course != null && !course.trim().isEmpty()) {
                preparedStatement.setString(parameterIndex++, course);
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int teacherId = rs.getInt("id");
                    String teacherName = rs.getString("name");
                    int age = rs.getInt("age");
                    String sex = rs.getString("sex");
                    String teacherCourse = rs.getString("course");
                    Teacher teacher = new Teacher();
                    teacher.setId(teacherId);
                    teacher.setName(teacherName);
                    teacher.setAge(age);
                    teacher.setSex(sex);
                    teacher.setCourse(teacherCourse);
                    teachers.add(teacher);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching teachers", e);
        }
        return teachers;
    }

    // 名前で教師情報を部分一致検索するメソッド
    public List<Teacher> selectTeachersByName(String name) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE name LIKE ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setName(rs.getString("name"));
                    teacher.setAge(rs.getInt("age"));
                    teacher.setSex(rs.getString("sex"));
                    teacher.setCourse(rs.getString("course"));
                    teachers.add(teacher);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師の名前による検索中にエラーが発生しました。", e);
        }
        return teachers;
    }

    // コースで教師情報を検索するメソッド
    public List<Teacher> selectTeachersByCourse(String course) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE course = ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setName(rs.getString("name"));
                    teacher.setAge(rs.getInt("age"));
                    teacher.setSex(rs.getString("sex"));
                    teacher.setCourse(rs.getString("course"));
                    teachers.add(teacher);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師のコースによる検索中にエラーが発生しました。", e);
        }
        return teachers;
    }

    // 年齢で教師情報を検索するメソッド
    public List<Teacher> selectTeachersByAge(int age) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE age = ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, age);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setName(rs.getString("name"));
                    teacher.setAge(rs.getInt("age"));
                    teacher.setSex(rs.getString("sex"));
                    teacher.setCourse(rs.getString("course"));
                    teachers.add(teacher);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師の年齢による検索中にエラーが発生しました。", e);
        }
        return teachers;
    }

    // 指定したIDで教師情報を取得するメソッド
    public Teacher selectTeacherById(int id) throws SQLException {
        Teacher teacher = null;
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String sex = rs.getString("sex");
                    String course = rs.getString("course");
                    teacher = new Teacher(id, name, age, sex, course);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師のIDによる検索中にエラーが発生しました。", e);
        }
        return teacher;
    }
}
