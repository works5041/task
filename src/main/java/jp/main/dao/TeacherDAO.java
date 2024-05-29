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
        String sql = "INSERT INTO teachers (id, name, age, sex, course) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT id, name, age, sex, course FROM teachers WHERE id = ?";
        try (Connection conn = JdbcTest.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int teacherId = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String sex = rs.getString("sex");
                    String course = rs.getString("course");
                    return new Teacher(teacherId, name, age, sex, course);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("教師の取得中にエラーが発生しました。", e);
        }
        return null;
    }

    public List<Teacher> selectAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        try {
            Connection connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TEACHERS);
            ResultSet rs = preparedStatement.executeQuery();

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
            e.printStackTrace();
        }
        return teachers;
    }

    public boolean deleteTeacher(int id) {
        boolean rowDeleted = false;
        try {
            Connection connection = JdbcTest.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_TEACHERS_SQL);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    public boolean updateTeacher(Teacher teacher) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE teachers SET");
        boolean firstField = true;
        List<Object> parameters = new ArrayList<>();

        if (teacher.getName() != null && !teacher.getName().isEmpty()) {
            sql.append(firstField ? " name = ?" : ", name = ?");
            parameters.add(teacher.getName());
            firstField = false;
        }
        if (teacher.getAge() > 0) {
            sql.append(firstField ? " age = ?" : ", age = ?");
            parameters.add(teacher.getAge());
            firstField = false;
        }
        if (teacher.getSex() != null && !teacher.getSex().isEmpty()) {
            sql.append(firstField ? " sex = ?" : ", sex = ?");
            parameters.add(teacher.getSex());
            firstField = false;
        }
        if (teacher.getCourse() != null && !teacher.getCourse().isEmpty()) {
            sql.append(firstField ? " course = ?" : ", course = ?");
            parameters.add(teacher.getCourse());
        }

        sql.append(" WHERE id = ?");
        parameters.add(teacher.getId());

        boolean rowUpdated = false;
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public List<Teacher> searchTeachers(Integer id, String name, String course) {
        List<Teacher> teachers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM teachers WHERE 1=1");

        if (id != null) {
            sql.append(" AND id = ?");
        }
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name = ?");
        }
        if (course != null && !course.trim().isEmpty()) {
            sql.append(" AND course = ?");
        }

        try {
            Connection connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            int parameterIndex = 1;
            if (id != null) {
                preparedStatement.setInt(parameterIndex++, id);
            }
            if (name != null && !name.trim().isEmpty()) {
                preparedStatement.setString(parameterIndex++, name.trim());
            }
            if (course != null && !course.trim().isEmpty()) {
                preparedStatement.setString(parameterIndex++, course);
            }

            ResultSet rs = preparedStatement.executeQuery();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public Teacher selectTeacherById(int id) throws SQLException {
        Teacher teacher = null;
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                String course = resultSet.getString("course");
                teacher = new Teacher(id, name, age, sex, course);
            }
        }
        return teacher;
    }
}
