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
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioDao extends Dao {

	private static final Logger LOGGER = LogManager.getLogger(FuncionarioDao.class);

	public void insertFuncionario(FuncionarioVo funcionarioVo) {
		StringBuilder query = new StringBuilder("INSERT INTO funcionario (nm_funcionario) values (?)");
		try (
			Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())) {

			int i = 1;
			ps.setString(i++, funcionarioVo.getNome());
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao inserir funcionário", e);
			throw new TechnicalException("Erro ao inserir funcionário no banco de dados", e);
		}
	}

	public List<FuncionarioVo> findAllFuncionarios() {
		StringBuilder query = new StringBuilder("SELECT rowid id, nm_funcionario nome FROM funcionario");
		try (
			Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery()) {

			List<FuncionarioVo> funcionarios = new ArrayList<>();
			while (rs.next()) {
				FuncionarioVo vo = new FuncionarioVo();
				vo.setRowid(rs.getString("id"));
				vo.setNome(rs.getString("nome"));

				funcionarios.add(vo);
			}
			return funcionarios;
		} catch (SQLException e) {
			LOGGER.error("Erro ao listar funcionários", e);
			throw new TechnicalException("Erro ao listar funcionários do banco de dados", e);
		}
	}

	public List<FuncionarioVo> findAllByNome(String nome) {
		StringBuilder query = new StringBuilder("SELECT rowid id, nm_funcionario nome FROM funcionario ")
								.append("WHERE lower(nm_funcionario) like lower(?)");

		try (Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())) {
			int i = 1;

			ps.setString(i, "%" + nome + "%");

			try (ResultSet rs = ps.executeQuery()) {
				List<FuncionarioVo> funcionarios = new ArrayList<>();

				while (rs.next()) {
					FuncionarioVo vo = new FuncionarioVo();
					vo.setRowid(rs.getString("id"));
					vo.setNome(rs.getString("nome"));

					funcionarios.add(vo);
				}
				return funcionarios;
			}
		} catch (SQLException e) {
			LOGGER.error("Erro ao buscar funcionários por nome", e);
			throw new TechnicalException("Erro ao buscar funcionários por nome", e);
		}
	}

	public FuncionarioVo findByCodigo(Integer codigo) {
		StringBuilder query = new StringBuilder("SELECT rowid id, nm_funcionario nome FROM funcionario ")
								.append("WHERE rowid = ?");

		try (Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())) {
			int i = 1;

			ps.setInt(i, codigo);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new FuncionarioVo(rs.getString("id"), rs.getString("nome"));
				}
				return null;
			}
		} catch (SQLException e) {
			LOGGER.error("Erro ao buscar funcionário por código", e);
			throw new TechnicalException("Erro ao buscar funcionário por código", e);
		}
	}

	public void updateFuncionario(FuncionarioVo funcionarioVo) {
		StringBuilder query = new StringBuilder("UPDATE funcionario SET nm_funcionario = ? WHERE rowid = ?");
		try (
			Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())
		) {
			ps.setString(1, funcionarioVo.getNome());
			ps.setInt(2, Integer.parseInt(funcionarioVo.getRowid()));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao atualizar funcionário", e);
			throw new TechnicalException("Erro ao atualizar funcionário no banco de dados", e);
		}
	}

	public void deleteFuncionario(String rowid) {
		StringBuilder query = new StringBuilder("DELETE FROM funcionario WHERE rowid = ?");
		try (Connection con = getConexao(); PreparedStatement ps = con.prepareStatement(query.toString())) {
			ps.setInt(1, Integer.parseInt(rowid));
			ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Erro ao excluir funcionário", e);
			throw new TechnicalException("Erro ao excluir funcionário no banco de dados", e);
		}
	}
}
