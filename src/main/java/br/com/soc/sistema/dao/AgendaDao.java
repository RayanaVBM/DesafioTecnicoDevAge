package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaDao extends Dao {

	public void insertAgenda(AgendaVo agendaVo) {
		StringBuilder query = new StringBuilder("INSERT INTO agenda (nm_agenda, periodo_disponivel) VALUES (?, ?)");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateAgenda(AgendaVo agendaVo) {
		StringBuilder query = new StringBuilder(
				"UPDATE agenda SET nm_agenda = ?, periodo_disponivel = ? WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel());
			ps.setString(3, agendaVo.getRowid());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<AgendaVo> findAllAgendas() {
		StringBuilder query = new StringBuilder(
				"SELECT rowid id, nm_agenda nome, periodo_disponivel periodo FROM agenda");
		List<AgendaVo> agendas = new ArrayList<>();
		try (Connection con = getConexao();
				PreparedStatement ps = con.prepareStatement(query.toString());
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				agendas.add(new AgendaVo(rs.getString("id"), rs.getString("nome"), rs.getString("periodo")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return agendas;
	}

	public AgendaVo findByCodigo(Integer codigo) {
		StringBuilder query = new StringBuilder(
				"SELECT rowid id, nm_agenda nome, periodo_disponivel periodo FROM agenda WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setInt(1, codigo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new AgendaVo(rs.getString("id"), rs.getString("nome"), rs.getString("periodo"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isAgendaComCompromissos(String rowid) {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) AS total FROM compromisso WHERE id_agenda = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, rowid);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("total") > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteAgenda(String rowid) {
		StringBuilder query = new StringBuilder("DELETE FROM agenda WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, rowid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}