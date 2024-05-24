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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class TeacherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TeacherDAO teacherDAO;

    public void init() {
        teacherDAO = new TeacherDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String course = request.getParameter("course");

        if (name != null) {
            name = URLDecoder.decode(name, "UTF-8");
        }
        if (course != null) {
            course = URLDecoder.decode(course, "UTF-8");
        }

        System.out.println("Received parameter 'id': " + id);
        System.out.println("Received parameter 'name': " + name);
        System.out.println("Received parameter 'course': " + course);

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
                    searchTeacher(request, response, id, name, course);
                    break;
                default:
                    listTeacher(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void searchTeacher(HttpServletRequest request, HttpServletResponse response, String idStr, String name, String course)
            throws SQLException, IOException, ServletException {
        Integer id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null;

        List<Teacher> listTeacher = teacherDAO.searchTeachers(id, name, course);
        request.setAttribute("listTeacher", listTeacher);

        if (listTeacher.isEmpty()) {
            request.setAttribute("message", "該当するデータがありません");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_list.jsp");
        dispatcher.forward(request, response);
    }

    private void listTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Teacher> listTeacher = teacherDAO.selectAllTeachers();
        request.setAttribute("listTeacher", listTeacher);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_register.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Teacher existingTeacher = teacherDAO.selectTeacher(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("teacher_update.jsp");
        request.setAttribute("teacher", existingTeacher);
        dispatcher.forward(request, response);
    }

    private void insertTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String sex = request.getParameter("sex");
        String course = request.getParameter("course");
        Teacher newTeacher = new Teacher();
        newTeacher.setName(name);
        newTeacher.setAge(age);
        newTeacher.setSex(sex);
        newTeacher.setCourse(course);

        boolean isInserted = teacherDAO.insertTeacher(newTeacher);

        if (isInserted) {
            response.sendRedirect("teacher_registersuccess.jsp");
        } else {
            response.sendRedirect("teacher_registererror.jsp");
        }
    }

    private void updateTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
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

        boolean isUpdated = teacherDAO.updateTeacher(teacher);

        if (isUpdated) {
            response.sendRedirect("teacher_updatesuccess.jsp");
        } else {
            response.sendRedirect("teacher_updateerror.jsp");
        }
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        teacherDAO.deleteTeacher(id);
        response.sendRedirect("list");
    }
}
