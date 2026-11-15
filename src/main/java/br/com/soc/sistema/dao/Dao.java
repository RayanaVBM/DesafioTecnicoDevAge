package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.soc.sistema.exception.TechnicalException;

public abstract class Dao implements AutoCloseable {

	private static final Logger LOGGER = LogManager.getLogger(Dao.class);
	private static boolean primeiraInicializacao = true;
	private Connection con = null;

	public Dao() {
	}

	private void conectar() {
		synchronized (Dao.class) {
			StringBuilder urlBuilder = new StringBuilder("jdbc:h2:mem:avaliacao;")
					.append("DB_CLOSE_DELAY=-1;")
					.append("DATABASE_TO_UPPER=false;");

			if (primeiraInicializacao) {
				urlBuilder.append("INIT=runscript from 'classpath:CRIA_TABELAS_E_INSERE_REGISTROS_INICIAIS.sql';");
			}

			try {
				Class.forName("org.h2.Driver");
				con = DriverManager.getConnection(urlBuilder.toString());
				primeiraInicializacao = false;
			} catch (SQLException ex) {
				LOGGER.error("Ocorreu um problema na tentativa de conexão com o banco de dados", ex);
				throw new TechnicalException("Ocorreu um problema na tentativa de conexao", ex);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Driver do banco de dados H2 não encontrado", e);
				throw new TechnicalException("Driver do banco de dados nao encontrado", e);
			}
		}
	}

	private void fechar() throws SQLException {
		if (con == null)
			throw new TechnicalException("Conexao nao foi criada");

		if (con.isClosed())
			throw new TechnicalException("Conexao ja foi encerrada");

		con.close();
	}

	@Override
	public void close() throws Exception {
		try {
			fechar();
		} catch (SQLException e) {
			LOGGER.error("Erro ao encerrar conexão com o banco de dados", e);
		}
	}

	/**
	 * Retorna uma conexão ativa com o banco de dados.
	 * @return Connection
	 * @throws SQLException
	 */
	protected Connection getConexao() throws SQLException {
		if (con == null || con.isClosed())
			conectar();
		return con;
	}
}
