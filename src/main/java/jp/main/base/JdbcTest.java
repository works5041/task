package jp.main.base;

import java.sql.*;

public class JdbcTest {

    private static final String URL = "jdbc:mysql://localhost:3306/tsm?useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "5041";

    // データベース接続を取得するメソッド
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // JDBCドライバの読み込み
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBCドライバのロードに失敗しました", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    //  SQL実行
    public static ResultSet executeQuery(String sql, String param) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, param);

            // ログの出力
            System.out.println("Executing SQL: " + preparedStatement.toString());

            return preparedStatement.executeQuery();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    //  SQL実行
    public static ResultSet executeQuery(String sql, String sclass, String name) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sclass);
            preparedStatement.setString(2, name);
            return preparedStatement.executeQuery();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    //   SQL実行（更新系）
    public static void executeUpdate(String sql, int id, String name) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
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
