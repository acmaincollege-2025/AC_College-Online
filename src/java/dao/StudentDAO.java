package dao;

import java.sql.*;
import java.util.*;
import model.Student;
import util.DatabaseUtil; // adapt to your project's DB connection util

/**
 * Production-ready StudentDAO with commonly used methods
 */
public class StudentDAO {

    // List all students
    public List<Student> listAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // List pending students (by status / student_type - adjust column name to your schema)
    public List<Student> listPendingStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE studentType = 'Pending' ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // Add student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO student (srcode, studno, lastname, firstname, middlename, gender, dob, address, contactno, email, program_id, photo, guardian, guardianNumber, fathername, fatherOccupation, mothername, motherOccupation, parentAddress, parentContactNumber, studentType, program_enrolled, year_level, gpa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getSrcode());
            ps.setString(2, student.getStudno());
            ps.setString(3, student.getLastname());
            ps.setString(4, student.getFirstname());
            ps.setString(5, student.getMiddlename());
            ps.setString(6, student.getGender());
            if (student.getDob() != null) {
                ps.setDate(7, new java.sql.Date(student.getDob().getTime()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            ps.setString(8, student.getAddress());
            ps.setString(9, student.getContactno());
            ps.setString(10, student.getEmail());
            ps.setInt(11, student.getProgramId());
            ps.setString(12, student.getPhoto());
            ps.setString(13, student.getGuardian());
            ps.setString(14, student.getGuardianNumber());
            ps.setString(15, student.getFathername());
            ps.setString(16, student.getFatherOccupation());
            ps.setString(17, student.getMothername());
            ps.setString(18, student.getMotherOccupation());
            ps.setString(19, student.getParentAddress());
            ps.setString(20, student.getParentContactNumber());
            ps.setString(21, student.getStudentType());
            ps.setString(22, student.getProgram_enrolled());
            ps.setString(23, student.getYear_level());
            ps.setDouble(24, student.getGpa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET srcode=?, studno=?, lastname=?, firstname=?, middlename=?, gender=?, dob=?, address=?, contactno=?, email=?, program_id=?, photo=?, guardian=?, guardianNumber=?, fathername=?, fatherOccupation=?, mothername=?, motherOccupation=?, parentAddress=?, parentContactNumber=?, studentType=?, program_enrolled=?, year_level=?, gpa=? WHERE sid=?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getSrcode());
            ps.setString(2, student.getStudno());
            ps.setString(3, student.getLastname());
            ps.setString(4, student.getFirstname());
            ps.setString(5, student.getMiddlename());
            ps.setString(6, student.getGender());
            if (student.getDob() != null) {
                ps.setDate(7, new java.sql.Date(student.getDob().getTime()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            ps.setString(8, student.getAddress());
            ps.setString(9, student.getContactno());
            ps.setString(10, student.getEmail());
            ps.setInt(11, student.getProgramId());
            ps.setString(12, student.getPhoto());
            ps.setString(13, student.getGuardian());
            ps.setString(14, student.getGuardianNumber());
            ps.setString(15, student.getFathername());
            ps.setString(16, student.getFatherOccupation());
            ps.setString(17, student.getMothername());
            ps.setString(18, student.getMotherOccupation());
            ps.setString(19, student.getParentAddress());
            ps.setString(20, student.getParentContactNumber());
            ps.setString(21, student.getStudentType());
            ps.setString(22, student.getProgram_enrolled());
            ps.setString(23, student.getYear_level());
            ps.setDouble(24, student.getGpa());
            ps.setInt(25, student.getSid());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    // Delete student by studno
    public boolean deleteStudent(String studno) {
        String sql = "DELETE FROM student WHERE studno = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studno);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    // Check duplicates
    public boolean isSrcodeOrStudnoDuplicate(String srcode, String studno, Integer sid) {
        String sql = "SELECT COUNT(*) FROM student WHERE (srcode = ? OR studno = ?) " + (sid == null ? "" : " AND sid != ?");
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, srcode);
            ps.setString(2, studno);
            if (sid != null) {
                ps.setInt(3, sid);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    // Generate SR code
    public String generateNewSrCode() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String prefix = year + "-";
        String sql = "SELECT srcode FROM student WHERE srcode LIKE ? ORDER BY srcode DESC LIMIT 1";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            int next = 1;
            if (rs.next()) {
                String last = rs.getString("srcode");
                if (last != null && last.contains("-")) {
                    String[] p = last.split("-");
                    if (p.length == 2) {
                        next = Integer.parseInt(p[1]) + 1;
                    }
                }
            }
            return String.format("%d-%04d", year, next);
        } catch (SQLException e) {
            e.getMessage();
        }
        return year + "-0001";
    }

    // Find by studno
    public Student findByStudentNo(String studno) {
        String sql = "SELECT * FROM student WHERE studno = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    // Find by username (email/srcode/studno)
    public Student findByUsername(String username) {
        String sql = "SELECT * FROM student WHERE email = ? OR srcode = ? OR studno = ? LIMIT 1";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    // Get by Id (sid)
    public Student getStudentById(int sid) {
        String sql = "SELECT * FROM student WHERE sid = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    // Search by last name (returns list)
    public List<Student> findByLastName(String lastname) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE lastname LIKE ? ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (lastname == null ? "" : lastname) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // Search by studno substring (returns list)
    public List<Student> findByStudno(String studno) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE studno LIKE ? ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (studno == null ? "" : studno) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // getStudentsByProgram
    public List<Student> getStudentsByProgram(String programId) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE programId = ? ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // generic search across fields
    public List<Student> searchStudentsByKeyword(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE lastname LIKE ? OR firstname LIKE ? OR middlename LIKE ? OR studno LIKE ? OR srcode LIKE ? ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + (keyword == null ? "" : keyword) + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, kw);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // searchStudentsByProgramAndKeyword (autocomplete support)
    public List<Student> searchStudentsByProgramAndKeyword(String programId, String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE programId = ? AND (lastname LIKE ? OR firstname LIKE ? OR studno LIKE ? OR srcode LIKE ?) ORDER BY lastname, firstname";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + (keyword == null ? "" : keyword) + "%";
            ps.setString(1, programId);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ps.setString(5, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    // updateAdmissionStatus
    public boolean updateAdmissionStatus(Student student) {
        String sql = "UPDATE student SET studentType = ? WHERE studno = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getStudentType());
            ps.setString(2, student.getStudno());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    // Helper mapper to convert ResultSet row -> Student
    private Student extractStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setSid(rs.getInt("sid"));
        s.setSrcode(rs.getString("srcode"));
        s.setStudno(rs.getString("studno"));
        s.setLastname(rs.getString("lastname"));
        s.setFirstname(rs.getString("firstname"));
        s.setMiddlename(rs.getString("middlename"));
        s.setGender(rs.getString("gender"));
        s.setDob(rs.getDate("dob"));
        s.setAddress(rs.getString("address"));
        s.setContactno(rs.getString("contactno"));
        s.setEmail(rs.getString("email"));
        s.setProgramId(rs.getInt("program_id"));
        s.setPhoto(rs.getString("photo"));
        s.setGuardian(rs.getString("guardian"));
        s.setGuardianNumber(rs.getString("guardianNumber"));
        s.setFathername(rs.getString("fathername"));
        s.setFatherOccupation(rs.getString("fatherOccupation"));
        s.setMothername(rs.getString("mothername"));
        s.setMotherOccupation(rs.getString("motherOccupation"));
        s.setParentAddress(rs.getString("parentAddress"));
        s.setParentContactNumber(rs.getString("parentContactNumber"));
        s.setStudentType(rs.getString("studentType"));
        s.setProgram_enrolled(rs.getString("program_enrolled"));
        s.setYear_level(rs.getString("year_level"));
        try {
            s.setGpa(rs.getDouble("gpa"));
        } catch (SQLException ex) {
            s.setGpa(0.0);
        }
        return s;
    }
}
