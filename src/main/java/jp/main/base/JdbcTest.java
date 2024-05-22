package jp.main.base;

import java.sql.*;

public class JdbcTest {

    private static final String URL = "jdbc:mysql://localhost:3306/tsm";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "5041";

    // DB接続、コレクションを取得
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // データベースに対する処理
//            System.out.println("データベースに接続に成功");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
//            System.out.println("データベースに接続に失敗");
            throw new SQLException("Database driver not found", e);
        }
    }

    //  SQL実行
    public static ResultSet executeQuery(String sql, String name) throws SQLException {
        Connection connection = null;
        try {
            connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery();
        } catch (SQLException we) {
            throw we;
        }
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery();
        } catch (SQLException we) {
            throw we;
        }
    }

    //  SQL実行
    public static ResultSet executeQuery(String sql, String sclass, String name) throws SQLException {
        Connection connection = null;
        try {
            connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sclass);
            preparedStatement.setString(2, name);
            return preparedStatement.executeQuery();
        } catch (SQLException we) {
            throw we;
        }
    }

    //   SQL実行（更新系）
    public static void executeUpdate(String sql, int id, String name) throws SQLException {
        Connection connection = null;
        try {
            connection = JdbcTest.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException we) {
            throw we;
        }
    }

    //   Connectionクローズ
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
