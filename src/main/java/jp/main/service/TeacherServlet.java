package jp.main.service;

import jp.main.dao.TeacherDAO;
import jp.main.model.Teacher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/")
public class TeacherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TeacherDAO teacherDAO;
    private static final Logger LOGGER = Logger.getLogger(TeacherServlet.class.getName());

    public void init() {
        teacherDAO = new TeacherDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("tid"); // 教師番号を取得
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        int id = 0;
        int age = 0;
        try {
            id = Integer.parseInt(idStr);
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Number format exception: {0}", e.getMessage());
        }

        // 教師情報の更新処理
        Teacher teacher = new Teacher(id, name, age, sex, course);
        teacherDAO.updateTeacher(teacher);
        // 更新成功時の教師情報をリクエスト属性に設定
        request.setAttribute("updatedTeacher", teacher);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_updatesuccess.jsp");
        dispatcher.forward(request, response);
    }

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertTeacher(request, response);
                    break;
                case "/delete":
                    deleteTeacher(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateTeacher(request, response);
                    break;
                case "/search":
                    searchTeacher(request, response);
                    break;
                case "/checkId":
                    checkTeacherId(request, response);
                    break;
                default:
                    listTeacher(request, response);
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Exception occurred: {0}", ex.getMessage());
            throw new ServletException(ex);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Exception occurred: {0}", ex.getMessage());
            throw new ServletException(ex);
        }
    }

    private void checkTeacherId(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        LOGGER.log(Level.INFO, "Checking teacher ID...");
        int tid = Integer.parseInt(request.getParameter("tid"));
        LOGGER.log(Level.INFO, "Received teacher ID: {0}", tid);
        boolean exists = teacherDAO.existsTeacher(tid);
        LOGGER.log(Level.INFO, "Teacher ID exists: {0}", exists);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"exists\": " + exists + "}");
        out.close();
    }

    private void searchTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String course = request.getParameter("course");

        Integer id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null;

        if (name != null) {
            name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        }
        if (course != null) {
            course = URLDecoder.decode(course, StandardCharsets.UTF_8.toString());
        }

        List<Teacher> listTeacher = teacherDAO.searchTeachers(id, name, course);
        request.setAttribute("listTeacher", listTeacher);

        if (listTeacher.isEmpty()) {
            request.setAttribute("message", "該当するデータがありません");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_list.jsp");
        dispatcher.forward(request, response);
    }

    private void listTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Teacher> listTeacher = teacherDAO.selectAllTeachers();
        request.setAttribute("listTeacher", listTeacher);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_register.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Teacher existingTeacher = teacherDAO.selectTeacher(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_update.jsp");
        request.setAttribute("teacher", existingTeacher);
        dispatcher.forward(request, response);
    }

    private void insertTeacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tidStr = request.getParameter("tid"); // 教師番号を取得
        LOGGER.log(Level.INFO, "Received teacher ID: {0}", tidStr); // デバッグ情報をログに出力

        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        int tid = 0;
        if (tidStr != null) {
            try {
                tid = Integer.parseInt(tidStr);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Number format exception: {0}", e.getMessage());
            }
        } else {
            // 教師番号がnullの場合はエラーメッセージを設定してエラーページにフォワードする
            request.setAttribute("errorMessage", "教師番号が入力されていません。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registererror.jsp?error=null");
            dispatcher.forward(request, response);
            return; // メソッドを終了する
        }

        Teacher newTeacher = new Teacher(tid, name, age, sex, course);

        // 未入力項目がある場合はエラーメッセージを表示して登録画面に戻る
        if (name.isEmpty() || sex.isEmpty() || course.isEmpty()) {
            request.setAttribute("errorMessage", "全ての項目を入力してください");
            request.setAttribute("teacher", newTeacher);
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_register.jsp");
            dispatcher.forward(request, response);
            return; // メソッドを終了する
        }

        // 重複チェックを行う
        boolean isDuplicate;
        try {
            isDuplicate = teacherDAO.existsTeacher(tid);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "教師番号のチェック中にエラーが発生しました。");
            request.setAttribute("teacher", newTeacher);
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registererror.jsp?error=sql");
            dispatcher.forward(request, response);
            return; // メソッドを終了する
        }

        if (isDuplicate) {
            // 重複した場合はエラーメッセージを表示してエラー画面にフォワードする
            request.setAttribute("errorMessage", "教師番号が既に存在します。");
            request.setAttribute("teacher", newTeacher);
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registererror.jsp?error=duplicate");
            dispatcher.forward(request, response);
        } else {
            try {
                // 重複しない場合は新しい教師を登録し、成功画面にフォワードする
                teacherDAO.insertTeacher(newTeacher);

                // 登録成功時の教師情報をリクエスト属性に設定
                request.setAttribute("registeredTeacher", newTeacher);

                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registersuccess.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException e) {
                // SQLエラーが発生した場合
                request.setAttribute("errorMessage", "教師の挿入中にエラーが発生しました。");
                request.setAttribute("teacher", newTeacher);
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registererror.jsp?error=sql");
                dispatcher.forward(request, response);
            } catch (Exception e) {
                // その他のエラーが発生した場合
                request.setAttribute("errorMessage", "予期しないエラーが発生しました。");
                request.setAttribute("teacher", newTeacher);
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registererror.jsp?error=general");
                dispatcher.forward(request, response);
            }
        }
    }

    private void updateTeacher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        Teacher existingTeacher = teacherDAO.selectTeacher(id);

        if (name != null && !name.isEmpty()) {
            existingTeacher.setName(name);
        }
        if (ageStr != null && !ageStr.isEmpty()) {
            existingTeacher.setAge(Integer.parseInt(ageStr));
        }
        if (sex != null && !sex.isEmpty()) {
            existingTeacher.setSex(sex);
        }
        if (course != null && !course.isEmpty()) {
            existingTeacher.setCourse(course);
        }

        teacherDAO.updateTeacher(existingTeacher);
        // 更新成功時の教師情報をリクエスト属性に設定
        request.setAttribute("updatedTeacher", existingTeacher);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_updatesuccess.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        teacherDAO.deleteTeacher(id);
        response.sendRedirect("list");
    }
}
