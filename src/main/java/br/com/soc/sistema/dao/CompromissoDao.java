package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoDao extends Dao {

	private static final Logger LOGGER = LogManager.getLogger(CompromissoDao.class);

	public void insertCompromisso(CompromissoVo vo) {
		String sql = "INSERT INTO compromisso (id_funcionario, id_agenda, dt_compromisso, hr_compromisso) VALUES (?, ?, ?, ?)";
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, Integer.parseInt(vo.getFuncionario().getRowid()));
			ps.setInt(2, Integer.parseInt(vo.getAgenda().getRowid()));
			ps.setDate(3, Date.valueOf(LocalDate.parse(vo.getData())));
			ps.setTime(4, Time.valueOf(LocalTime.parse(vo.getHora())));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao inserir compromisso", e);
			throw new TechnicalException("Erro ao inserir compromisso no banco de dados", e);
		}
	}

	public void updateCompromisso(CompromissoVo vo) {
		String sql = "UPDATE compromisso SET id_funcionario = ?, id_agenda = ?, dt_compromisso = ?, hr_compromisso = ? WHERE rowid = ?";
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, Integer.parseInt(vo.getFuncionario().getRowid()));
			ps.setInt(2, Integer.parseInt(vo.getAgenda().getRowid()));
			ps.setDate(3, Date.valueOf(LocalDate.parse(vo.getData())));
			ps.setTime(4, Time.valueOf(LocalTime.parse(vo.getHora())));
			ps.setInt(5, Integer.parseInt(vo.getRowid()));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao atualizar compromisso", e);
			throw new TechnicalException("Erro ao atualizar compromisso no banco de dados", e);
		}
	}

	public void deleteCompromisso(String rowid) {
		String sql = "DELETE FROM compromisso WHERE rowid = ?";
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, Integer.parseInt(rowid));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao excluir compromisso", e);
			throw new TechnicalException("Erro ao excluir compromisso no banco de dados", e);
		}
	}

	public boolean existeConflitoHorario(CompromissoVo vo) {
		boolean isEdicao = vo.getRowid() != null && !vo.getRowid().trim().isEmpty();
		String sql = "SELECT COUNT(*) AS total FROM compromisso "
				+ "WHERE dt_compromisso = ? AND hr_compromisso = ? "
				+ "AND (id_agenda = ? OR id_funcionario = ?)"
				+ (isEdicao ? " AND rowid <> ?" : "");

		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(LocalDate.parse(vo.getData())));
			ps.setTime(2, Time.valueOf(LocalTime.parse(vo.getHora())));
			ps.setInt(3, Integer.parseInt(vo.getAgenda().getRowid()));
			ps.setInt(4, Integer.parseInt(vo.getFuncionario().getRowid()));
			if (isEdicao) {
				ps.setInt(5, Integer.parseInt(vo.getRowid()));
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("total") > 0;
				}
			}
			return false;
		} catch (SQLException e) {
			LOGGER.error("Erro ao verificar conflito de horário de compromisso", e);
			throw new TechnicalException("Erro ao verificar conflito de horário", e);
		}
	}

	public List<CompromissoVo> findAllCompromissos() {
		String sql = "SELECT c.rowid, c.dt_compromisso, c.hr_compromisso, "
				+ "f.rowid AS id_func, f.nm_funcionario, "
				+ "a.rowid AS id_agenda, a.nm_agenda "
				+ "FROM compromisso c "
				+ "INNER JOIN funcionario f ON c.id_funcionario = f.rowid "
				+ "INNER JOIN agenda a ON c.id_agenda = a.rowid "
				+ "ORDER BY c.dt_compromisso, c.hr_compromisso";

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
			return lista;
		} catch (SQLException e) {
			LOGGER.error("Erro ao listar compromissos", e);
			throw new TechnicalException("Erro ao listar compromissos do banco de dados", e);
		}
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
			return null;
		} catch (SQLException e) {
			LOGGER.error("Erro ao buscar compromisso por código", e);
			throw new TechnicalException("Erro ao buscar compromisso por código", e);
		}
	}

	public List<CompromissoVo> findCompromissosByPeriodo(String dataInicial, String dataFinal) {
		String sql = "SELECT c.rowid, c.dt_compromisso, c.hr_compromisso, "
				+ "f.rowid AS id_func, f.nm_funcionario, "
				+ "a.rowid AS id_agenda, a.nm_agenda "
				+ "FROM compromisso c "
				+ "INNER JOIN funcionario f ON c.id_funcionario = f.rowid "
				+ "INNER JOIN agenda a ON c.id_agenda = a.rowid "
				+ "WHERE c.dt_compromisso BETWEEN ? AND ? "
				+ "ORDER BY c.dt_compromisso, c.hr_compromisso";

		List<CompromissoVo> lista = new ArrayList<>();
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(LocalDate.parse(dataInicial)));
			ps.setDate(2, Date.valueOf(LocalDate.parse(dataFinal)));

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
			return lista;
		} catch (SQLException e) {
			LOGGER.error("Erro ao buscar compromissos por período", e);
			throw new TechnicalException("Erro ao buscar compromissos por período", e);
		}
	}
}
