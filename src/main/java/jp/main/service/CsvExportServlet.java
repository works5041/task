package jp.main.service;

import jp.main.dao.TeacherDAO;
import jp.main.model.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TeacherDAO teacherDAO;

    public void init() {
        teacherDAO = new TeacherDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String teacherIdParam = request.getParameter("teacherId");
        String nameParam = request.getParameter("name");
        String courseParam = request.getParameter("course");
        String ageParam = request.getParameter("age");

        List<Teacher> listTeacher;

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=teachers.csv");

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write("\uFEFF"); // BOM (Byte Order Mark) を書き込むことでExcelでの文字化けを防ぐ

            if (teacherIdParam != null && !teacherIdParam.isEmpty()) {
                int teacherId = Integer.parseInt(teacherIdParam);
                Teacher teacher = teacherDAO.selectTeacherById(teacherId);
                if (teacher != null) {
                    writer.println("教師番号,名前,年齢,性別,コース");
                    writer.printf("%d,%s,%d,%s,%s%n",
                            teacher.getId(),
                            teacher.getName(),
                            teacher.getAge(),
                            teacher.getSex(),
                            teacher.getCourse());
                }
            } else if (nameParam != null && !nameParam.isEmpty()) {
                listTeacher = teacherDAO.selectTeachersByName(nameParam);
                exportTeachers(writer, listTeacher);
            } else if (courseParam != null && !courseParam.isEmpty()) {
                listTeacher = teacherDAO.selectTeachersByCourse(courseParam);
                exportTeachers(writer, listTeacher);
            } else if (ageParam != null && !ageParam.isEmpty()) {
                int age = Integer.parseInt(ageParam);
                listTeacher = teacherDAO.selectTeachersByAge(age);
                exportTeachers(writer, listTeacher);
            } else {
                listTeacher = teacherDAO.selectAllTeachers();
                exportTeachers(writer, listTeacher);
            }
        } catch (Exception e) {
            throw new ServletException("Error exporting CSV", e);
        }
    }

    private void exportTeachers(PrintWriter writer, List<Teacher> listTeacher) {
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
