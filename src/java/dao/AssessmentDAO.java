/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Assessment;
import model.OtherFeeType;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class AssessmentDAO {

    public AssessmentDAO() {
    }

    public List<OtherFeeType> getDefaultOtherFees() throws SQLException {
        List<OtherFeeType> fees = new ArrayList<>();
        String sql = "SELECT * FROM other_fee_type";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                fees.add(new OtherFeeType(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                ));
            }
        }
        return fees;
    }

    public void saveAssessment(Assessment assessment) throws SQLException {
        String insertAssessment = "INSERT INTO assessment (enrollment_id, student_id, program_id, total_units, unit_fee, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        String insertOtherFees = "INSERT INTO assessment_other_fees (assessment_id,  fee_code, amount) VALUES (?, ?, ?)";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement psAssessment = con.prepareStatement(insertAssessment, Statement.RETURN_GENERATED_KEYS)) {

            psAssessment.setInt(1, assessment.getEnrollmentId());
            psAssessment.setString(2, assessment.getStudentId());
            psAssessment.setInt(3, assessment.getProgramId());
            psAssessment.setInt(4, assessment.getTotalUnits());
            psAssessment.setDouble(5, assessment.getUnitFee());
            psAssessment.setDouble(6, assessment.getTotalAmount());
            psAssessment.executeUpdate();

            ResultSet generatedKeys = psAssessment.getGeneratedKeys();
            if (generatedKeys.next()) {
                int assessmentId = generatedKeys.getInt(1);

                for (OtherFeeType fee : assessment.getOtherFees()) {
                    try (PreparedStatement psFee = con.prepareStatement(insertOtherFees)) {
                        psFee.setInt(1, assessmentId);
                        psFee.setString(2, fee.getFeeCode());
                        psFee.setDouble(3, fee.getAmount());
                        psFee.executeUpdate();
                    }
                }
            }
        }
    }

    public boolean save_Assessment(Assessment assessment) {
        String sql = "INSERT INTO assessment (enrollment_id, student_id, program_id, total_units, unit_fee, total_fee,created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, assessment.getEnrollmentId());
            stmt.setString(2, assessment.getStudentId());
            stmt.setInt(3, assessment.getProgramId());
            stmt.setInt(4, assessment.getTotalUnits());
            stmt.setDouble(5, assessment.getUnitFee());
            stmt.setDouble(6, assessment.getTotalAmount());

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Assessment> findAll() {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT * FROM assessment";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Assessment assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt(1));
                assessment.setEnrollmentId(rs.getInt(2));
                assessment.setStudentId(rs.getString(3));
                assessment.setProgramId(rs.getInt(4));
                assessment.setTotalUnits(rs.getInt(5));
                assessment.setUnitFee(rs.getDouble(6));
                assessment.setTotalAmount(rs.getDouble(7));

                list.add(assessment);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public boolean deleteAssessment(int id) {
        String sql = "DELETE FROM assessment WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateAssessment(Assessment assessment) {
        String sql = "UPDATE assessment SET enrollment_id = ?, student_id = ?, program_id = ?, total_units = ? "
                + "unit_fee = ?, total_amounte = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, assessment.getEnrollmentId());
            stmt.setString(2, assessment.getStudentId());
            stmt.setInt(3, assessment.getProgramId());
            stmt.setInt(4, assessment.getTotalUnits());
            stmt.setDouble(5, assessment.getUnitFee());
            stmt.setDouble(6, assessment.getTotalAmount());
            stmt.setInt(7, assessment.getAssessmentId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Assessment> findAllForCashier() {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.first_name, s.last_name, s.middle_name, s.program_id, p.program_name "
                + "FROM assessment a "
                + "JOIN student s ON a.student_id = s.stud_id "
                + "JOIN program p ON s.program_id = p.program_id";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Assessment a = new Assessment();
                a.setAssessmentId(rs.getInt(1));
                a.setStudentId(rs.getString(2));
                a.setStudentName(rs.getString(3) + ", " + rs.getString(4) + " " + rs.getString(5));
                a.setProgramName(rs.getString(6));
                a.setTotalAmount(rs.getDouble(7));
                a.setEnrolledOnline(rs.getBoolean(7));
                a.setPaymentAcknowledged(rs.getBoolean(8));
                list.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public boolean markAsAcknowledged(int assessmentId) {
        String sql = "UPDATE assessment SET payment_acknowledged = TRUE WHERE assessment_id = ?";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, assessmentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public double findTotalFeeByEnrollmentId(int enrollmentId) {
        String sql = "SELECT total_amount FROM assessment WHERE enrollment_id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    public Assessment getAssessmentByEnrollmentId(int enrollmentId) {
        Assessment assessment = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM assessment WHERE enrollment_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollmentId);
            rs = ps.executeQuery();

            if (rs.next()) {
                assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt("assessment_id"));
                assessment.setEnrollmentId(rs.getInt("enrollment_id"));
                assessment.setStudentId(rs.getString("student_id"));
                assessment.setProgramId(rs.getInt("program_id"));
                assessment.setTotalUnits(rs.getInt("total_units"));
                assessment.setUnitFee(rs.getDouble("unit_fee"));
                assessment.setTotalAmount(rs.getDouble("total_amount"));

                // Add other fields if needed
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return assessment;
    }

    public Map<String, Double> getBasicFees() {
        Map<String, Double> basicFees = new HashMap<>();
        String sql = "SELECT fee_type, amount FROM basic_fee";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                basicFees.put(rs.getString("fee_type"), rs.getDouble("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return basicFees;
    }

}
/*
public List<OtherFeeType> getDefaultOtherFees() throws SQLException {
        List<OtherFeeType> fees = new ArrayList<>();
        String sql = "SELECT * FROM other_fee_type";
        
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                fees.add(new OtherFeeType(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                ));
            }
        }
        return fees;
    }
    
    public void saveAssessment(Assessment assessment) throws SQLException {
        String insertAssessment = "INSERT INTO assessment (enrollment_id, student_id, program_id, total_units, unit_fee, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        String insertOtherFees = "INSERT INTO assessment_other_fees (assessment_id,  fee_code, amount) VALUES (?, ?, ?)";
        
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement psAssessment = con.prepareStatement(insertAssessment, Statement.RETURN_GENERATED_KEYS)) {
            
            psAssessment.setInt(1, assessment.getEnrollmentId());
            psAssessment.setString(2, assessment.getStudentId());
            psAssessment.setInt(3, assessment.getProgramId());
            psAssessment.setInt(4, assessment.getTotalUnits());
            psAssessment.setDouble(5, assessment.getUnitFee());
            psAssessment.setDouble(6, assessment.getTotalAmount());
            psAssessment.executeUpdate();
            
            ResultSet generatedKeys = psAssessment.getGeneratedKeys();
            if (generatedKeys.next()) {
                int assessmentId = generatedKeys.getInt(1);
                
                for (OtherFeeType fee : assessment.getOtherFees()) {
                    try (PreparedStatement psFee = con.prepareStatement(insertOtherFees)) {
                        psFee.setInt(1, assessmentId);
                        psFee.setString(2, fee.getFeeCode());
                        psFee.setDouble(3, fee.getAmount());
                        psFee.executeUpdate();
                    }
                }
            }
        }
    }
    
    public boolean save_Assessment(Assessment assessment) {
        String sql = "INSERT INTO assessment (enrollment_id, student_id, program_id, total_units, unit_fee, total_fee,created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, assessment.getEnrollmentId());
            stmt.setString(2, assessment.getStudentId());
            stmt.setInt(3, assessment.getProgramId());
            stmt.setInt(4, assessment.getTotalUnits());
            stmt.setDouble(5, assessment.getUnitFee());
            stmt.setDouble(6, assessment.getTotalAmount());
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Assessment> findAll() {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT * FROM assessment";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Assessment assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt(1));
                assessment.setEnrollmentId(rs.getInt(2));
                assessment.setStudentId(rs.getString(3));
                assessment.setProgramId(rs.getInt(4));
                assessment.setTotalUnits(rs.getInt(5));
                assessment.setUnitFee(rs.getDouble(6));
                assessment.setTotalAmount(rs.getDouble(7));
                
                list.add(assessment);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
    
    public boolean deleteAssessment(int id) {
        String sql = "DELETE FROM assessment WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean updateAssessment(Assessment assessment) {
        String sql = "UPDATE assessment SET enrollment_id = ?, student_id = ?, program_id = ?, total_units = ? "
                + "unit_fee = ?, total_amounte = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, assessment.getEnrollmentId());
            stmt.setString(2, assessment.getStudentId());
            stmt.setInt(3, assessment.getProgramId());
            stmt.setInt(4, assessment.getTotalUnits());
            stmt.setDouble(5, assessment.getUnitFee());
            stmt.setDouble(6, assessment.getTotalAmount());
            stmt.setInt(7, assessment.getAssessmentId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Assessment> findAllForCashier() {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.first_name, s.last_name, s.middle_name, s.program_id, p.program_name "
                + "FROM assessment a "
                + "JOIN student s ON a.student_id = s.stud_id "
                + "JOIN program p ON s.program_id = p.program_id";
        
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Assessment a = new Assessment();
                a.setAssessmentId(rs.getInt(1));
                a.setStudentId(rs.getString(2));
                a.setStudentName(rs.getString(3) + ", " + rs.getString(4) + " " + rs.getString(5));
                a.setProgramName(rs.getString(6));
                a.setTotalAmount(rs.getDouble(7));
                a.setEnrolledOnline(rs.getBoolean(7));
                a.setPaymentAcknowledged(rs.getBoolean(8));
                list.add(a);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
    
    public boolean markAsAcknowledged(int assessmentId) {
        String sql = "UPDATE assessment SET payment_acknowledged = TRUE WHERE assessment_id = ?";
        
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, assessmentId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public double findTotalFeeByEnrollmentId(int enrollmentId) {
        String sql = "SELECT total_amount FROM assessment WHERE enrollment_id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }
    
    public Assessment getAssessmentByEnrollmentId(int enrollmentId) {
        Assessment assessment = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM assessment WHERE enrollment_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollmentId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt("assessment_id"));
                assessment.setEnrollmentId(rs.getInt("enrollment_id"));
                assessment.setStudentId(rs.getString("student_id"));
                assessment.setProgramId(rs.getInt("program_id"));
                assessment.setTotalUnits(rs.getInt("total_units"));
                assessment.setUnitFee(rs.getDouble("unit_fee"));
                assessment.setTotalAmount(rs.getDouble("total_amount"));

                // Add other fields if needed
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssessmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return assessment;
    }
    
 */
