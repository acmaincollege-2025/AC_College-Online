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
import model.BasicFee;
import model.OtherFeeType;
import model.TuitionFee;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class FeeConfigDAO {

    // --- Tuition Fee ---
    public TuitionFee getTuitionFee() throws SQLException {
        String sql = "SELECT * FROM tuition_fee LIMIT 1";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                TuitionFee fee = new TuitionFee();
                fee.setId(rs.getInt("id"));
                fee.setAmountPerUnit(rs.getDouble("amount_per_unit"));
                return fee;
            }
        }
        return null;
    }

    public boolean updateTuitionFee(double newAmount) throws SQLException {
        String sql = "UPDATE tuition_fee SET amount_per_unit = ? WHERE id = 1";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            return ps.executeUpdate() > 0;
        }
    }

    // --- Basic Fees ---
    public List<BasicFee> getAllBasicFees() throws SQLException {
        List<BasicFee> list = new ArrayList<>();
        String sql = "SELECT * FROM basic_fee";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BasicFee fee = new BasicFee();
                fee.setId(rs.getInt("id"));
                fee.setFeeType(rs.getString("fee_type"));
                fee.setAmount(rs.getDouble("amount"));
                list.add(fee);
            }
        }
        return list;
    }

    public boolean updateBasicFee(int id, double newAmount) throws SQLException {
        String sql = "UPDATE basic_fee SET amount = ? WHERE id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --- Other Fees ---
    public List<OtherFeeType> getAllOtherFees() throws SQLException {
        List<OtherFeeType> list = new ArrayList<>();
        String sql = "SELECT * FROM other_fee_type";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OtherFeeType fee = new OtherFeeType();
                fee.setFeeTypeId(rs.getInt("id"));
                fee.setFeeCode(rs.getString("fee_code"));
                fee.setDescriptions(rs.getString("description"));
                fee.setAmount(rs.getDouble("amount"));
                list.add(fee);
            }
        }
        return list;
    }

    public boolean addOtherFee(OtherFeeType fee) throws SQLException {
        String sql = "INSERT INTO other_fee_type (fee_code, description, amount) VALUES (?, ?, ?)";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fee.getFeeCode());
            ps.setString(2, fee.getDescriptions());
            ps.setDouble(3, fee.getAmount());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateOtherFee(OtherFeeType fee) throws SQLException {
        String sql = "UPDATE other_fee_type SET description = ?, amount = ? WHERE id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fee.getDescriptions());
            ps.setDouble(2, fee.getAmount());
            ps.setInt(3, fee.getFeeTypeId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteOtherFee(int id) throws SQLException {
        String sql = "DELETE FROM other_fee_type WHERE id = ?";
        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
