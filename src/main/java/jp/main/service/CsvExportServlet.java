package jp.main.service;

import jp.main.dao.TeacherDAO;
import jp.main.model.Teacher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/csvExport")
public class CsvExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TeacherDAO teacherDAO;

    public void init() {
        teacherDAO = new TeacherDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=teachers.csv");

        try (PrintWriter writer = response.getWriter()) {
            List<Teacher> listTeacher = teacherDAO.selectAllTeachers();

            writer.println("教師番号,名前,年齢,性別,コース");

            for (Teacher teacher : listTeacher) {
                writer.printf("%d,%s,%d,%s,%s%n",
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getAge(),
                        teacher.getSex(),
                        teacher.getCourse());
            }
        }
    }
}
