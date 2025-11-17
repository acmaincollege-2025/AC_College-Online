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
import model.FAQ;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class FAQDAO {

    private Connection conn;

    public FAQDAO() {
        try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FAQDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<FAQ> getAllFAQs() {
        List<FAQ> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM faq ORDER BY updated_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FAQ faq = new FAQ();
                faq.setFaqId(rs.getInt("faq_id"));
                faq.setQuestion(rs.getString("question"));
                faq.setAnswer(rs.getString("answer"));
                faq.setCategory(rs.getString("category"));
                faq.setCreatedAt(rs.getTimestamp("created_at"));
                faq.setUpdatedAt(rs.getTimestamp("updated_at"));
                faq.setCreatedBy(rs.getString("created_by"));
                list.add(faq);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;
    }

    public void insertFAQ(FAQ faq) {
        try {
            String sql = "INSERT INTO faq (question, answer, category, created_by) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, faq.getQuestion());
            ps.setString(2, faq.getAnswer());
            ps.setString(3, faq.getCategory());
            ps.setString(4, faq.getCreatedBy());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void updateFAQ(FAQ faq) {
        try {
            String sql = "UPDATE faq SET question=?, answer=?, category=?, updated_at=NOW() WHERE faq_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, faq.getQuestion());
            ps.setString(2, faq.getAnswer());
            ps.setString(3, faq.getCategory());
            ps.setInt(4, faq.getFaqId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void deleteFAQ(int id) {
        try {
            String sql = "DELETE FROM faq WHERE faq_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
// 1. Get all FAQs

    public List<FAQ> getAllFaqs() {
        List<FAQ> faqs = new ArrayList<>();
        String sql = "SELECT * FROM faq ORDER BY category, question";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FAQ faq = new FAQ();
                faq.setFaqId(rs.getInt("id"));
                faq.setQuestion(rs.getString("question"));
                faq.setAnswer(rs.getString("answer"));
                faq.setCategory(rs.getString("category"));
                faq.setCreatedAt(rs.getTimestamp("created_at"));
                faqs.add(faq);
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return faqs;
    }

    // 2. Get FAQs filtered by category
    public List<FAQ> getFaqsByCategory(String category) {
        List<FAQ> faqs = new ArrayList<>();
        String sql = "SELECT * FROM faq WHERE category = ? ORDER BY question";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FAQ faq = new FAQ();
                    faq.setFaqId(rs.getInt("id"));
                    faq.setQuestion(rs.getString("question"));
                    faq.setAnswer(rs.getString("answer"));
                    faq.setCategory(rs.getString("category"));
                    faq.setCreatedAt(rs.getTimestamp("created_at"));
                    faqs.add(faq);
                }
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return faqs;
    }
}
