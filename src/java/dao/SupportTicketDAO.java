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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SupportTicket;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class SupportTicketDAO {

    private Connection conn;

    public SupportTicketDAO() {
        try {
            conn = DatabaseUtil.getConnection(); // Assume this method gets DB connection
        } catch (SQLException ex) {
            Logger.getLogger(SupportTicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean submitTicket(SupportTicket ticket) {
        String sql = "INSERT INTO support_ticket (student_id, subject, message) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticket.getStudentId());
            stmt.setString(2, ticket.getSubject());
            stmt.setString(3, ticket.getMessage());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
             Logger.getLogger(SupportTicketDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<SupportTicket> getTicketsByStudentId(int studentId) {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM support_ticket WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SupportTicket ticket = mapResultSet(rs);
                tickets.add(ticket);
            }
        } catch (SQLException ex) {
             Logger.getLogger(SupportTicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tickets;
    }

    public List<SupportTicket> getAllTickets() {
        List<SupportTicket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM support_ticket";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SupportTicket ticket = mapResultSet(rs);
                tickets.add(ticket);
            }
        } catch (SQLException ex) {
             Logger.getLogger(SupportTicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tickets;
    }

    public boolean respondToTicket(int ticketId, String response, String responder) {
        String sql = "UPDATE support_ticket SET status = 'Resolved', response = ?, responded_by = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, response);
            stmt.setString(2, responder);
            stmt.setInt(3, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
             Logger.getLogger(SupportTicketDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private SupportTicket mapResultSet(ResultSet rs) throws SQLException {
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketId(rs.getInt("ticket_id"));
        ticket.setStudentId(rs.getInt("student_id"));
        ticket.setSubject(rs.getString("subject"));
        ticket.setMessage(rs.getString("message"));
        ticket.setStatus(rs.getString("status"));
        ticket.setResponse(rs.getString("response"));
        ticket.setRespondedBy(rs.getString("responded_by"));
        ticket.setCreatedAt(rs.getTimestamp("created_at"));
        ticket.setUpdatedAt(rs.getTimestamp("updated_at"));
        return ticket;
    }
}
