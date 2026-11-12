package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoDao extends Dao {

    public void insertCompromisso(CompromissoVo vo) {
        String sql = "INSERT INTO compromisso (id_funcionario, id_agenda, dt_compromisso, hr_compromisso) VALUES (?, ?, ?, ?)";
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vo.getFuncionario().getRowid());
            ps.setString(2, vo.getAgenda().getRowid());
            ps.setString(3, vo.getData());
            ps.setString(4, vo.getHora());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateCompromisso(CompromissoVo vo) {
        String sql = "UPDATE compromisso SET id_funcionario = ?, id_agenda = ?, dt_compromisso = ?, hr_compromisso = ? WHERE rowid = ?";
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vo.getFuncionario().getRowid());
            ps.setString(2, vo.getAgenda().getRowid());
            ps.setString(3, vo.getData());
            ps.setString(4, vo.getHora());
            ps.setString(5, vo.getRowid());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteCompromisso(String rowid) {
        String sql = "DELETE FROM compromisso WHERE rowid = ?";
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rowid);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<CompromissoVo> findAllCompromissos() {
        String sql = "SELECT c.rowid, c.dt_compromisso, c.hr_compromisso, " +
                     "f.rowid AS id_func, f.nm_funcionario, " +
                     "a.rowid AS id_agenda, a.nm_agenda " +
                     "FROM compromisso c " +
                     "INNER JOIN funcionario f ON c.id_funcionario = f.rowid " +
                     "INNER JOIN agenda a ON c.id_agenda = a.rowid";
        
        List<CompromissoVo> lista = new ArrayList<>();
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CompromissoVo vo = new CompromissoVo();
                vo.setRowid(rs.getString("rowid"));
                vo.setData(rs.getString("dt_compromisso"));
                vo.setHora(rs.getString("hr_compromisso"));
                vo.setFuncionario(new FuncionarioVo(rs.getString("id_func"), rs.getString("nm_funcionario")));
                vo.setAgenda(new AgendaVo(rs.getString("id_agenda"), rs.getString("nm_agenda"), null));
                lista.add(vo);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public CompromissoVo findByCodigo(Integer codigo) {
        String sql = "SELECT rowid, id_funcionario, id_agenda, dt_compromisso, hr_compromisso FROM compromisso WHERE rowid = ?";
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CompromissoVo vo = new CompromissoVo();
                    vo.setRowid(rs.getString("rowid"));
                    vo.getFuncionario().setRowid(rs.getString("id_funcionario"));
                    vo.getAgenda().setRowid(rs.getString("id_agenda"));
                    vo.setData(rs.getString("dt_compromisso"));
                    vo.setHora(rs.getString("hr_compromisso"));
                    return vo;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public List<CompromissoVo> findCompromissosByPeriodo(String dataInicial, String dataFinal) {
        String sql = "SELECT c.rowid, c.dt_compromisso, c.hr_compromisso, " +
                     "f.rowid AS id_func, f.nm_funcionario, " +
                     "a.rowid AS id_agenda, a.nm_agenda " +
                     "FROM compromisso c " +
                     "INNER JOIN funcionario f ON c.id_funcionario = f.rowid " +
                     "INNER JOIN agenda a ON c.id_agenda = a.rowid " +
                     "WHERE c.dt_compromisso BETWEEN ? AND ? " +
                     "ORDER BY c.dt_compromisso, c.hr_compromisso";
        
        List<CompromissoVo> lista = new ArrayList<>();
        try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dataInicial);
            ps.setString(2, dataFinal);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompromissoVo vo = new CompromissoVo();
                    vo.setRowid(rs.getString("rowid"));
                    vo.setData(rs.getString("dt_compromisso"));
                    vo.setHora(rs.getString("hr_compromisso"));
                    vo.setFuncionario(new FuncionarioVo(rs.getString("id_func"), rs.getString("nm_funcionario")));
                    vo.setAgenda(new AgendaVo(rs.getString("id_agenda"), rs.getString("nm_agenda"), null));
                    lista.add(vo);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}