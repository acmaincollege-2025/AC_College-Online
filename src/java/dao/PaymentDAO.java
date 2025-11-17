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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Payment;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class PaymentDAO {

    public boolean savePayment(Payment payment) {
        String sql = "INSERT INTO payment (enrollment_id, studentNo, payment_type, payment_reference_number, amount_paid, payment_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getEnrollmentId());
            ps.setString(2, payment.getStudentNo());
            ps.setString(3, payment.getPaymentType());
            ps.setString(4, payment.getPaymentReferenceNumber());
            ps.setDouble(5, payment.getAmountPaid());
            ps.setTimestamp(6, new java.sql.Timestamp(payment.getPaymentDate().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

//    public List<Payment> findPaymentsByStudent(String studentNo) {
//        List<Payment> list = new ArrayList<>();
//        String sql = "SELECT * FROM payment WHERE studentNo = ?";
//
//        try (Connection con = DatabaseUtil.getConnection();
//                PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setString(1, studentNo);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                Payment p = new Payment();
//                p.setId(rs.getInt("id"));
//                p.setEnrollmentId(rs.getInt("enrollment_id"));
//                p.setStudentNo(rs.getString("studentNo"));
//                p.setPaymentType(rs.getString("payment_type"));
//                p.setPaymentReferenceNumber(rs.getString("payment_reference_number"));
//                p.setAmountPaid(rs.getDouble("amount_paid"));
//                p.setPaymentDate(rs.getTimestamp("payment_date"));
//                list.add(p);
//            }
//
//        } catch (SQLException ex) {
//            ex.getMessage();
//        }
//        return list;
//    }
    public List<Payment> findPendingEnrollments() {
        List<Payment> list = new ArrayList<>();

        String sql = "SELECT e.id AS enrollment_id, s.studNo AS studentNo "
                + "FROM enrollment e "
                + "JOIN student s ON e.student_id = s.studRecId "
                + "LEFT JOIN payment p ON p.enrollment_id = e.id "
                + "WHERE p.id IS NULL";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setEnrollmentId(rs.getInt("enrollment_id"));
                p.setStudentNo(rs.getString("studentNo"));
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;
    }

    public List<Payment> findPaymentsByStudent(String studentNo) {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT p.*, "
                + "       CONCAT(s.last_name, ', ', s.first_name, ' ', COALESCE(s.middle_name, '')) AS studentFullName,"
                + "       pr.program_name "
                + "FROM payment p "
                + "JOIN student s ON p.studentNo = s.studNo "
                + "JOIN program pr ON s.program_id = pr.id "
                + "WHERE p.studentNo = ?";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setStudentName(rs.getString("studentFullName"));
                    p.setProgramName(rs.getString("program_name"));
                    p.setId(rs.getInt("id"));
                    p.setEnrollmentId(rs.getInt("enrollment_id"));
                    p.setStudentNo(rs.getString("studentNo"));
                    p.setPaymentType(rs.getString("payment_type"));
                    p.setPaymentReferenceNumber(rs.getString("payment_reference_number"));
                    p.setAmountPaid(rs.getDouble("amount_paid"));
                    p.setPaymentDate(rs.getTimestamp("payment_date"));
//                    p.setStudentName(rs.getString("student_name")); // set the full name

                    payments.add(p);
                }
            }

        } catch (SQLException ex) {
            ex.getMessage(); // log properly in production
        }

        return payments;
    }

    public List<Payment> findAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, s.first_name, s.last_name, pr.program_name "
                + "FROM payment p "
                + "JOIN enrollment e ON p.enrollment_id = e.id "
                + "JOIN student s ON e.studentNo = s.studNo "
                + "JOIN program pr ON s.program_id = pr.id "
                + "ORDER BY p.payment_date DESC";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setEnrollmentId(rs.getInt("enrollment_id"));
                p.setStudentNo(rs.getString("studentNo"));
                p.setPaymentType(rs.getString("payment_type"));
                p.setPaymentReferenceNumber(rs.getString("payment_reference_number"));
                p.setAmountPaid(rs.getDouble("amount_paid"));
                p.setPaymentDate(rs.getTimestamp("payment_date"));

                p.setStudentName(rs.getString("last_name") + ", " + rs.getString("first_name"));
                p.setProgramName(rs.getString("program_name"));

                list.add(p);
            }

        } catch (SQLException ex) {
            System.out.println("Error fetching payments: " + ex);
        }

        return list;
    }

    public List<Payment> getPaymentsByStudentId(String studno) {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM payment WHERE studno = ? ORDER BY paymentDate DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setStudentNo(rs.getString("studentNo"));
                p.setAmountPaid(rs.getDouble("amount_paid"));
                p.setPaymentType(rs.getString("payment_type"));
                p.setPaymentDate(rs.getTimestamp("payment_date"));
                p.setReceivedBy(rs.getString("received_by"));
                p.setProgramName(rs.getString("program_name"));
                p.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                // map other fields as needed
                payments.add(p);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return payments;
    }

    public List<Payment> findPaymentsWithFilters(Date startDate, Date endDate, String programName, String cashierName) {
        List<Payment> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, s.first_name, s.last_name, pr.program_name "
                + "FROM payment p "
                + "JOIN enrollment e ON p.enrollment_id = e.id "
                + "JOIN student s ON e.studentNo = s.studNo "
                + "JOIN program pr ON s.program_id = pr.id "
                + "WHERE 1=1"
        );

        if (startDate != null && endDate != null) {
            sql.append(" AND p.payment_date BETWEEN ? AND ?");
        }
        if (programName != null && !programName.isEmpty()) {
            sql.append(" AND pr.program_name = ?");
        }
        if (cashierName != null && !cashierName.isEmpty()) {
            sql.append(" AND p.received_by = ?");
        }

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (startDate != null && endDate != null) {
                ps.setTimestamp(index++, new java.sql.Timestamp(startDate.getTime()));
                ps.setTimestamp(index++, new java.sql.Timestamp(endDate.getTime()));
            }
            if (programName != null && !programName.isEmpty()) {
                ps.setString(index++, programName);
            }
            if (cashierName != null && !cashierName.isEmpty()) {
                ps.setString(index++, cashierName);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setStudentNo(rs.getString("studentNo"));
                p.setAmountPaid(rs.getDouble("amount_paid"));
                p.setPaymentType(rs.getString("payment_type"));
                p.setPaymentDate(rs.getTimestamp("payment_date"));
                p.setReceivedBy(rs.getString("received_by"));
                p.setProgramName(rs.getString("program_name"));
                p.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                list.add(p);
            }

        } catch (SQLException ex) {
            System.out.println("Error fetching payments: " + ex);
        }

        return list;
    }

    public Payment getPaymentByEnrollmentId(int enrollmentId) {
        Payment payment = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM payment WHERE enrollment_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollmentId);
            rs = ps.executeQuery();

            if (rs.next()) {
                payment = new Payment();
                payment.setId(rs.getInt("payment_id"));
                payment.setEnrollmentId(rs.getInt("enrollment_id"));
                payment.setPaymentReferenceNumber(rs.getString("payment_reference_number"));
                payment.setAmountPaid(rs.getDouble("amount_paid"));
                payment.setPaymentDate(rs.getDate("payment_date"));
//                payment.setCashier_name(rs.getString("cashier_name"));
                // Add other fields as needed
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return payment;
    }

}
