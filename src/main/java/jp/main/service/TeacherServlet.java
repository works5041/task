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

        String idStr = request.getParameter("tid");
        LOGGER.log(Level.INFO, "Received teacher ID: {0}", idStr);

        if (idStr == null || idStr.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Teacher ID is missing.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Teacher ID is missing.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid Teacher ID format: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Teacher ID format.");
            return;
        }

        boolean exists;
        try {
            exists = teacherDAO.existsTeacher(id);
            LOGGER.log(Level.INFO, "Teacher ID exists: {0}", exists);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking teacher ID existence: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking teacher ID existence.");
            return;
        }

        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        if (name == null || ageStr == null || sex == null || course == null) {
            LOGGER.log(Level.SEVERE, "One or more parameters are missing.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "One or more parameters are missing.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Number format exception: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format for age.");
            return;
        }

        Teacher teacher = new Teacher(id, name, age, sex, course);
        LOGGER.log(Level.INFO, "Teacher object created: {0}", teacher);

        try {
            if (exists) {
                boolean rowUpdated = teacherDAO.updateTeacher(teacher);
                if (!rowUpdated) {
                    LOGGER.log(Level.WARNING, "No teacher found with ID: {0}", id);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No teacher found with the given ID.");
                    return;
                }
                request.setAttribute("teacher", teacher);
                LOGGER.log(Level.INFO, "Teacher object set as request attribute: {0}", teacher);
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_updatesuccess.jsp");
                dispatcher.forward(request, response);
            } else {
                teacherDAO.insertTeacher(teacher);
                request.setAttribute("registeredTeacher", teacher);
                LOGGER.log(Level.INFO, "Registered teacher: {0}", teacher);
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registersuccess.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception occurred: {0}", e.getMessage());
            throw new ServletException(e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred: {0}", e.getMessage());
            throw new ServletException(e);
        }
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

    private void checkTeacherId(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idStr = request.getParameter("tid");
        boolean exists = false;

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            exists = teacherDAO.existsTeacher(id);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
    }

    private void searchTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String course = request.getParameter("course");

        Integer id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null;

        try {
            if (name != null) {
                name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
            }
            if (course != null) {
                course = URLDecoder.decode(course, StandardCharsets.UTF_8.toString());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "URL decoding error: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter encoding.");
            return;
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
        String idStr = request.getParameter("tid");
        if (idStr == null || idStr.isEmpty()) {
            throw new ServletException("IDパラメータが存在しません");
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid ID format.");
        }

        Teacher existingTeacher = teacherDAO.selectTeacher(id);
        if (existingTeacher == null) {
            throw new ServletException("教師が存在しません: ID " + id);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_update.jsp");
        request.setAttribute("teacher", existingTeacher);
        dispatcher.forward(request, response);
    }

    private void insertTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("tid");
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        int id;
        int age;
        try {
            id = Integer.parseInt(idStr);
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid ID or age format: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or age format.");
            return;
        }

        Teacher newTeacher = new Teacher(id, name, age, sex, course);
        teacherDAO.insertTeacher(newTeacher);

        request.setAttribute("registeredTeacher", newTeacher);
        request.getRequestDispatcher("teacher_registersuccess.jsp").forward(request, response);
    }

    private void updateTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("tid");
        LOGGER.log(Level.INFO, "Received teacher ID: {0}", idStr);

        if (idStr == null || idStr.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Teacher ID is missing.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Teacher ID is missing.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid Teacher ID format: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Teacher ID format.");
            return;
        }

        boolean exists;
        try {
            exists = teacherDAO.existsTeacher(id);
            LOGGER.log(Level.INFO, "Teacher ID exists: {0}", exists);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking teacher ID existence: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking teacher ID existence.");
            return;
        }

        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");

        if (name == null || ageStr == null || sex == null || course == null) {
            LOGGER.log(Level.SEVERE, "One or more parameters are missing.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "One or more parameters are missing.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Number format exception: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format for age.");
            return;
        }

        Teacher teacher = new Teacher(id, name, age, sex, course);
        LOGGER.log(Level.INFO, "Updated teacher object: {0}", teacher); // 追加: 更新されたTeacherオブジェクトをログに記録

        try {
            if (exists) {
                boolean rowUpdated = teacherDAO.updateTeacher(teacher);
                if (!rowUpdated) {
                    LOGGER.log(Level.WARNING, "No teacher found with ID: {0}", id);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No teacher found with the given ID.");
                    return;
                }
                request.setAttribute("teacher", teacher);
                LOGGER.log(Level.INFO, "Updated teacher object set as request attribute: {0}", teacher); // 追加: リクエスト属性に設定されたTeacherオブジェクトをログに記録
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_updatesuccess.jsp");
                dispatcher.forward(request, response);
            } else {
                teacherDAO.insertTeacher(teacher);
                request.setAttribute("registeredTeacher", teacher);
                LOGGER.log(Level.INFO, "Registered teacher: {0}", teacher);
                RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_registersuccess.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception occurred: {0}", e.getMessage());
            throw new ServletException(e);
        }
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is missing.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
            return;
        }

        teacherDAO.deleteTeacher(id);
        response.sendRedirect("list");
    }
}
