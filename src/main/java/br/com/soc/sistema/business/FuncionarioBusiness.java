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
	    if(funcionarioVo.getNome() == null || funcionarioVo.getNome().trim().isEmpty()) {
	        throw new BusinessException("O campo Nome é obrigatório e não pode ficar em branco.");
	    }
	    
	    if(funcionarioVo.getNome().length() > 70) {
	        throw new BusinessException("O campo Nome ultrapassou o limite máximo de 70 caracteres.");
	    }
	    
	    try {
	        if(funcionarioVo.getRowid() == null || funcionarioVo.getRowid().isEmpty()) {
	            dao.insertFuncionario(funcionarioVo);
	        } else {
	            dao.updateFuncionario(funcionarioVo);
	        }
	    } catch (Exception e) {
	        throw new BusinessException("Não foi possível realizar a gravação do registro no banco de dados.");
	    }
	}

	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter) {
		List<FuncionarioVo> funcionarios = new ArrayList<>();

		switch (filter.getOpcoesCombo()) {
		case ID:
			try {
				Integer codigo = Integer.parseInt(filter.getValorBusca());
				funcionarios.add(dao.findByCodigo(codigo));
			} catch (NumberFormatException e) {
				throw new BusinessException(FOI_INFORMADO_CARACTER_NO_LUGAR_DE_UM_NUMERO);
			}
			break;

		case NOME:
			funcionarios.addAll(dao.findAllByNome(filter.getValorBusca()));
			break;
		}

		return funcionarios;
	}

	public FuncionarioVo buscarFuncionarioPor(String codigo) {
		try {
			Integer cod = Integer.parseInt(codigo);
			return dao.findByCodigo(cod);
		} catch (NumberFormatException e) {
			throw new BusinessException(FOI_INFORMADO_CARACTER_NO_LUGAR_DE_UM_NUMERO);
		}
	}

//	public void excluirFuncionario(String rowid) {
//	    try {
//	        dao.deleteFuncionario(rowid);
//	    } catch (Exception e) {
//	        throw new BusinessException("Não foi possível realizar a exclusão do registro.");
//	    }
//	}

	public void excluirFuncionario(String rowid) {
		// 1. Reaproveita o método que converte o ID e busca no banco.
		// Se receber "i", ele já vai barrar e lançar a exceção de caractere inválido.
		FuncionarioVo funcionario = buscarFuncionarioPor(rowid);

		// 2. Trava de Qualidade: Se retornou nulo, o ID é um número, mas não existe na
		// base.
		if (funcionario == null) {
			throw new BusinessException(
					"Operação cancelada: Não existe funcionário cadastrado com o código " + rowid + ".");
		}

		// 3. Se passou pelas validações acima, o ID é válido e existe. Executa o
		// DELETE.
		try {
			dao.deleteFuncionario(rowid);
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a exclusão do registro no banco de dados.");
		}
	}
}
