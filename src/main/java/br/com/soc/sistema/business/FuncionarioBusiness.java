package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusiness {

	private static final String FOI_INFORMADO_CARACTER_NO_LUGAR_DE_UM_NUMERO = "Foi informado um caracter no lugar de um numero";
	private FuncionarioDao dao;

	public FuncionarioBusiness() {
		this.dao = new FuncionarioDao();
	}

	public List<FuncionarioVo> trazerTodosOsFuncionarios() {
		return dao.findAllFuncionarios();
	}

	public void salvarFuncionario(FuncionarioVo funcionarioVo) {
		if (funcionarioVo == null || funcionarioVo.getNome() == null || funcionarioVo.getNome().trim().isEmpty()) {
			throw new BusinessException("O campo Nome é obrigatório e não pode ficar em branco.");
		}

		String nome = funcionarioVo.getNome().trim();
		funcionarioVo.setNome(nome);

		if (nome.length() > 70) {
			throw new BusinessException("O campo Nome ultrapassou o limite máximo de 70 caracteres.");
		}

		if (nome.matches(".*\\d.*")) {
			throw new BusinessException("O campo Nome aceita apenas letras.");
		}

		boolean isNovo = funcionarioVo.getRowid() == null || funcionarioVo.getRowid().trim().isEmpty();
		if (!isNovo) {
			FuncionarioVo existente = buscarFuncionarioPor(funcionarioVo.getRowid());
			if (existente == null) {
				throw new BusinessException(
						"Operação cancelada: Não existe funcionário cadastrado com o código " + funcionarioVo.getRowid() + ".");
			}
		}

		try {
			if (isNovo) {
				dao.insertFuncionario(funcionarioVo);
			} else {
				dao.updateFuncionario(funcionarioVo);
			}
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a gravação do registro no banco de dados.", e);
		}
	}

	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter) {
		List<FuncionarioVo> funcionarios = new ArrayList<>();

		switch (filter.getOpcoesCombo()) {
		case ID:
			try {
				Integer codigo = Integer.parseInt(filter.getValorBusca().trim());
				FuncionarioVo funcionario = dao.findByCodigo(codigo);
				if (funcionario != null) {
					funcionarios.add(funcionario);
				}
			} catch (NumberFormatException e) {
				throw new BusinessException(FOI_INFORMADO_CARACTER_NO_LUGAR_DE_UM_NUMERO, e);
			}
			break;

		case NOME:
			if (filter.getValorBusca().matches(".*\\d.*")) {
				throw new BusinessException("O campo nome aceita apenas letras.");
			}
			funcionarios.addAll(dao.findAllByNome(filter.getValorBusca().trim()));
			break;
		}

		return funcionarios;
	}

	public FuncionarioVo buscarFuncionarioPor(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new BusinessException("O código do funcionário deve ser informado.");
		}
		try {
			Integer cod = Integer.parseInt(codigo.trim());
			return dao.findByCodigo(cod);
		} catch (NumberFormatException e) {
			throw new BusinessException(FOI_INFORMADO_CARACTER_NO_LUGAR_DE_UM_NUMERO, e);
		}
	}

	public void excluirFuncionario(String rowid) {
		FuncionarioVo funcionario = buscarFuncionarioPor(rowid);

		if (funcionario == null) {
			throw new BusinessException(
					"Operação cancelada: Não existe funcionário cadastrado com o código " + rowid + ".");
		}

		try {
			dao.deleteFuncionario(rowid.trim());
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a exclusão do registro no banco de dados.", e);
		}
	}
}
