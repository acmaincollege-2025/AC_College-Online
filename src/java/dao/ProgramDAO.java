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
import model.Program;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class ProgramDAO {

    public boolean addProgram(Program program) {
        String sql = "INSERT INTO program (program_code, program_name) VALUES (?, ?)";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, program.getProgram_code());
            ps.setString(2, program.getProgram_name());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    public boolean updateProgram(Program program) {
        String sql = "UPDATE program SET program_code = ?, program_name = ? WHERE program_id = ?";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, program.getProgram_code());
            ps.setString(2, program.getProgram_name());
            ps.setInt(3, program.getProgram_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    public boolean deleteProgram(String code) {
        String sql = "DELETE FROM program WHERE program_code = ?";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, id);
            ps.setString(1, code);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return false;
    }

    public Program getProgramById(int id) {
        String sql = "SELECT * FROM program WHERE program_id = ?";
        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Program p = new Program();
                p.setProgram_id(rs.getInt("program_id"));
                p.setProgram_code(rs.getString("program_code"));
                p.setProgram_name(rs.getString("program_name"));
                return p;
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return null;
    }

    public List<Program> getAllPrograms() {
        List<Program> list = new ArrayList<>();
        String sql = "SELECT * FROM program";

        try (Connection con = DatabaseUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Program p = new Program();
                p.setProgram_id(rs.getInt("program_id"));
                p.setProgram_code(rs.getString("program_code"));
                p.setProgram_name(rs.getString("program_name"));
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;
    }

    public Program findById(int id) {
        Program program = null;
        String sql = "SELECT * FROM program WHERE program_id = ?";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                program = new Program();
                program.setProgram_id(rs.getInt(1));
                program.setProgram_code(rs.getString(2));
                program.setProgram_name(rs.getString(3));
            }

        } catch (SQLException ex) {
            ex.getMessage();;
        }

        return program;
    }

}
