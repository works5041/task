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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void insertTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");
        int tid = Integer.parseInt(request.getParameter("tid")); // 教師番号を取得

        Teacher newTeacher = new Teacher();
        newTeacher.setId(tid);
        newTeacher.setName(name);
        newTeacher.setAge(age);
        newTeacher.setSex(sex);
        newTeacher.setCourse(course);

        // 未入力項目がある場合はエラーメッセージを表示して登録画面に戻る
        if (name.isEmpty() || sex.isEmpty() || course.isEmpty()) {
            request.setAttribute("errorMessage", "全ての項目を入力してください");
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_register.jsp");
            dispatcher.forward(request, response);
            return; // メソッドを終了する
        }

        // 重複チェックを行う
        boolean isDuplicate = teacherDAO.existsTeacher(tid);
        if (isDuplicate) {
            // 重複した場合はエラーメッセージを表示して登録画面に戻る
            request.setAttribute("errorMessage", "教師番号が既に存在します。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_register.jsp");
            dispatcher.forward(request, response);
        } else {
            // 重複しない場合は新しい教師を登録し、成功画面にフォワードする
            teacherDAO.insertTeacher(newTeacher);

            // 登録成功時の教師情報をリクエスト属性に設定
            request.setAttribute("registeredTeacher", newTeacher);

            RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registersuccess.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void updateTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setSex(sex);
        teacher.setCourse(course);

        teacherDAO.updateTeacher(teacher);
        response.sendRedirect("list");
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        teacherDAO.deleteTeacher(id);
        response.sendRedirect("list");
    }
}
