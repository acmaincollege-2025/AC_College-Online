/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import model.Course;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author hrkas
 */


public class ExcelUtil {

    public static List<Course> parseCourses(InputStream input) throws Exception {
        List<Course> courseList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(input);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rows = sheet.iterator();
        boolean skipHeader = true;

        while (rows.hasNext()) {
            Row row = rows.next();

            if (skipHeader) {
                skipHeader = false;
                continue; // skip header
            }

            Course course = new Course();
            course.setCourseCode(getCellValue(row.getCell(0)));
            course.setCourseTitle(getCellValue(row.getCell(1)));
            course.setUnits(Double.parseDouble(getCellValue(row.getCell(2))));
            course.setProgramId(Integer.parseInt(getCellValue(row.getCell(3))));
            course.setSemester(getCellValue(row.getCell(4)));
            course.setYearLevel(Integer.parseInt(getCellValue(row.getCell(5))));

            courseList.add(course);
        }

        workbook.close();
        return courseList;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
