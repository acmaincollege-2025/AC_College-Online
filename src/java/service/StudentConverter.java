package service;

import dao.StudentDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Student;

/**
 * JSF Converter for Student entity to be used in selection components. Converts
 * between Student objects and their unique student number (studno).
 *
 * Compatible with JSF 2.2 / PrimeFaces 5+.
 *
 * @author hrkas
 */
@FacesConverter(value = "studentConverter")
public class StudentConverter implements Converter {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return studentDAO.findByStudno(value); // Or use findByStudno(value) if that’s your DAO method name
        } catch (Exception e) {
            System.err.println("[StudentConverter] Conversion Error: Unable to find Student for value: " + value);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Student) {
            Student student = (Student) value;
            return student.getStudno() != null ? student.getStudno() : "";
        } else {
            System.err.println("[StudentConverter] Invalid object type: " + value.getClass().getName());
            return "";
        }
    }
}
