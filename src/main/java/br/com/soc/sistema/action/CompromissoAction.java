package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoAction extends Action {

	private static final long serialVersionUID = 1L;

	private List<CompromissoVo> compromissos = new ArrayList<>();
	private CompromissoVo compromissoVo = new CompromissoVo();
	private List<FuncionarioVo> listaFuncionarios = new ArrayList<>();
	private List<AgendaVo> listaAgendas = new ArrayList<>();

	private CompromissoBusiness business = new CompromissoBusiness();
	private FuncionarioBusiness funcBusiness = new FuncionarioBusiness();
	private AgendaBusiness agendaBusiness = new AgendaBusiness();

	public String todos() {
		compromissos = business.trazerTodosOsCompromissos();
		return SUCCESS;
	}

	public String novo() {
		if (compromissoVo.getData() == null) {
			carregarCombos();
			return INPUT;
		}

		try {
			business.salvarCompromisso(compromissoVo);
			return REDIRECT;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			carregarCombos();
			return INPUT;
		}
	}

	public String editar() {
		if (compromissoVo.getRowid() == null || compromissoVo.getRowid().trim().isEmpty()) {
			return REDIRECT;
		}

		try {
			compromissoVo = business.buscarCompromissoPor(compromissoVo.getRowid());
			if (compromissoVo == null) {
				addActionError("Compromisso não encontrado.");
				return todos();
			}
			carregarCombos();
			return INPUT;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			return todos();
		}
	}

	public String excluir() {
		if (compromissoVo.getRowid() != null && !compromissoVo.getRowid().trim().isEmpty()) {
			try {
				business.excluirCompromisso(compromissoVo.getRowid());
			} catch (BusinessException e) {
				addActionError(e.getMessage());
			}
		}
		return todos();
	}

	private void carregarCombos() {
		listaFuncionarios = funcBusiness.trazerTodosOsFuncionarios();
		listaAgendas = agendaBusiness.trazerTodasAsAgendas();
	}

	public List<CompromissoVo> getCompromissos() {
		return compromissos;
	}

	public void setCompromissos(List<CompromissoVo> compromissos) {
		this.compromissos = compromissos;
	}

	public CompromissoVo getCompromissoVo() {
		return compromissoVo;
	}

	public void setCompromissoVo(CompromissoVo compromissoVo) {
		this.compromissoVo = compromissoVo;
	}

	public List<FuncionarioVo> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public List<AgendaVo> getListaAgendas() {
		return listaAgendas;
	}
}
