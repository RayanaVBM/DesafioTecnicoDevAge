package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.FuncionarioVo;
public class FuncionarioAction extends Action {
	
	private List<FuncionarioVo> funcionarios = new ArrayList<>();
	private FuncionarioBusiness business = new FuncionarioBusiness();
	private FuncionarioFilter filtrar = new FuncionarioFilter();
	private FuncionarioVo funcionarioVo = new FuncionarioVo();
	
	public String todos() {
		funcionarios.addAll(business.trazerTodosOsFuncionarios());	

		return SUCCESS;
	}
	
	public String filtrar() {
	    // 1. Se não escolher nenhuma opção no combo (select vazio), recarrega a lista toda
	    if (filtrar.isNullOpcoesCombo()) {
	        return todos();
	    }
	    
	    // 2. Comportamento de "Limpar Filtros": se o campo de busca estiver vazio, 
	    // traz a listagem inteira sem exibir mensagens de erro.
	    if (filtrar.getValorBusca() == null || filtrar.getValorBusca().trim().isEmpty()) {
	        return todos(); 
	    }

	    // 3. Validação de números no campo de Nome usando o Enum correto
	    if (filtrar.getOpcoesCombo() == OpcoesComboBuscar.NOME) {
	        if (filtrar.getValorBusca().matches(".*\\d.*")) {
	            addActionError("O campo nome aceita apenas letras.");
	            return todos();
	        }
	    }

	    // 4. Tenta realizar a busca específica
	    try {
	        funcionarios = business.filtrarFuncionarios(filtrar);
	    } catch (br.com.soc.sistema.exception.BusinessException e) {
	        addActionError(e.getMessage());
	        return todos();
	    }
	    
	    return SUCCESS;
	}
	
//	public String novo() {
//		if(funcionarioVo.getNome() == null)
//			return INPUT;
//		
//		business.salvarFuncionario(funcionarioVo);
//		
//		return REDIRECT;
//	}
	public String novo() {
	    // Retorna para a tela de formulário se for o primeiro acesso
	    if(funcionarioVo.getNome() == null) {
	        return INPUT; 
	    }
	    
	    try {
	        business.salvarFuncionario(funcionarioVo);
	        return REDIRECT; // Sucesso: volta para a tabela
	    } catch (br.com.soc.sistema.exception.BusinessException e) {
	        // Falha na validação: adiciona o alerta e mantém o usuário no formulário
	        addActionError(e.getMessage());
	        return INPUT; 
	    }
	}
	
	public String editar() {
		if(funcionarioVo.getRowid() == null)
			return REDIRECT;
		
		funcionarioVo = business.buscarFuncionarioPor(funcionarioVo.getRowid());
		
		return INPUT;
	}
	
	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return Arrays.asList(OpcoesComboBuscar.values());
	}
	
	public List<FuncionarioVo> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<FuncionarioVo> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public FuncionarioFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(FuncionarioFilter filtrar) {
		this.filtrar = filtrar;
	}

	public FuncionarioVo getFuncionarioVo() {
		return funcionarioVo;
	}

	public void setFuncionarioVo(FuncionarioVo funcionarioVo) {
		this.funcionarioVo = funcionarioVo;
	}
	
	public String excluir() {
	    if(funcionarioVo.getRowid() != null && !funcionarioVo.getRowid().isEmpty()) {
	        try {
	            business.excluirFuncionario(funcionarioVo.getRowid());
	        } catch (br.com.soc.sistema.exception.BusinessException e) {
	            addActionError(e.getMessage());
	        }
	    }
	    
	    return todos(); 
	}
}
