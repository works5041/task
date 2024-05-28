package jp.main.dao;

import jp.main.model.Teacher;
import jp.main.base.JdbcTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    private static final String INSERT_TEACHERS_SQL = "INSERT INTO teachers (id, name, age, sex, course) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_TEACHER_BY_ID = "SELECT id, name, age, sex, course FROM teachers WHERE id = ?";
    private static final String SELECT_ALL_TEACHERS = "SELECT * FROM teachers";
    private static final String DELETE_TEACHERS_SQL = "DELETE FROM teachers WHERE id = ?";
    private static final String UPDATE_TEACHERS_SQL = "UPDATE teachers SET name = ?, age = ?, sex = ?, course = ? WHERE id = ?";

    // 新規追加：教師番号の重複チェックメソッド
    public boolean existsTeacher(int id) throws SQLException {
        boolean exists = false;
        try (Connection connection = JdbcTest.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEACHER_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean insertTeacher(Teacher teacher) {
        boolean rowInserted = false;
        try {
            Connection connection = JdbcTest.getConnection();

            // デバッグステートメントの追加
            System.out.println("教師番号: " + teacher.getId());
            System.out.println("名前: " + teacher.getName());
            // 他の属性に関する情報もログに追加できます

            // 重複する教師番号が存在しないかチェック
            if (!existsTeacher(teacher.getId())) {
                PreparedStatement statement = connection.prepareStatement(INSERT_TEACHERS_SQL);
                statement.setInt(1, teacher.getId());
                statement.setString(2, teacher.getName());
                statement.setInt(3, teacher.getAge());
                statement.setString(4, teacher.getSex());
                statement.setString(5, teacher.getCourse());

                // executeUpdate()メソッドが1以上を返す場合、挿入成功と判定
                rowInserted = statement.executeUpdate() > 0;

                // ステートメントをクローズ
                statement.close();
            } else {
                System.out.println("教師番号が既に存在します。");
            }

            connection.close();
        } catch (SQLException e) {
            // 例外をログに記録
            e.printStackTrace();
        }
        // 挿入の成否を返す
        return rowInserted;
    }


    public Teacher selectTeacher(int id) {
        Teacher teacher = null;
        try {
            Connection connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEACHER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String sex = rs.getString("sex");
                String course = rs.getString("course");
                teacher = new Teacher();
                teacher.setId(id);
                teacher.setName(name);
                teacher.setAge(age);
                teacher.setSex(sex);
                teacher.setCourse(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teacher;
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

    public boolean updateTeacher(Teacher teacher) {
        boolean rowUpdated = false;
        try {
            Connection connection = JdbcTest.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_TEACHERS_SQL);
            statement.setString(1, teacher.getName());
            statement.setInt(2, teacher.getAge());
            statement.setString(3, teacher.getSex());
            statement.setString(4, teacher.getCourse());
            statement.setInt(5, teacher.getId());
            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
}
