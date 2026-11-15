package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaDao extends Dao {

	private static final Logger LOGGER = LogManager.getLogger(AgendaDao.class);

	public void insertAgenda(AgendaVo agendaVo) {
		StringBuilder query = new StringBuilder("INSERT INTO agenda (nm_agenda, periodo_disponivel) VALUES (?, ?)");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel());
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao inserir agenda", e);
			throw new TechnicalException("Erro ao inserir agenda no banco de dados", e);
		}
	}

	public void updateAgenda(AgendaVo agendaVo) {
		StringBuilder query = new StringBuilder(
				"UPDATE agenda SET nm_agenda = ?, periodo_disponivel = ? WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel());
			ps.setInt(3, Integer.parseInt(agendaVo.getRowid()));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao atualizar agenda", e);
			throw new TechnicalException("Erro ao atualizar agenda no banco de dados", e);
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
			return agendas;
		} catch (SQLException e) {
			LOGGER.error("Erro ao listar agendas", e);
			throw new TechnicalException("Erro ao listar agendas do banco de dados", e);
		}
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
			return null;
		} catch (SQLException e) {
			LOGGER.error("Erro ao buscar agenda por código", e);
			throw new TechnicalException("Erro ao buscar agenda por código", e);
		}
	}

	public boolean isAgendaComCompromissos(String rowid) {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) AS total FROM compromisso WHERE id_agenda = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setInt(1, Integer.parseInt(rowid));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("total") > 0;
				}
			}
			return false;
		} catch (SQLException e) {
			LOGGER.error("Erro ao verificar compromissos vinculados à agenda", e);
			throw new TechnicalException("Erro ao verificar compromissos vinculados à agenda", e);
		}
	}

	public boolean possuiCompromissoForaDoPeriodo(String rowidAgenda, String novoPeriodo) {
		if ("Ambos".equalsIgnoreCase(novoPeriodo)) {
			return false;
		}
		String condicaoHorario = "Manhã".equalsIgnoreCase(novoPeriodo)
				? "hr_compromisso >= '12:00:00'"
				: "hr_compromisso < '12:00:00'";
		String sql = "SELECT COUNT(*) AS total FROM compromisso WHERE id_agenda = ? AND " + condicaoHorario;
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, Integer.parseInt(rowidAgenda));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("total") > 0;
				}
			}
			return false;
		} catch (SQLException e) {
			LOGGER.error("Erro ao verificar compatibilidade de período da agenda com compromissos existentes", e);
			throw new TechnicalException("Erro ao verificar compromissos da agenda", e);
		}
	}

	public void deleteAgenda(String rowid) {
		StringBuilder query = new StringBuilder("DELETE FROM agenda WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setInt(1, Integer.parseInt(rowid));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao excluir agenda", e);
			throw new TechnicalException("Erro ao excluir agenda no banco de dados", e);
		}
	}
}
