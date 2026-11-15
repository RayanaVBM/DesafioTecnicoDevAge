package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaAction extends Action {

	private static final long serialVersionUID = 1L;

	private List<AgendaVo> agendas = new ArrayList<>();
	private AgendaBusiness business = new AgendaBusiness();
	private AgendaVo agendaVo = new AgendaVo();

	public String todos() {
		agendas = business.trazerTodasAsAgendas();
		return SUCCESS;
	}

	public String novo() {
		if (agendaVo.getNome() == null) {
			return INPUT;
		}

		try {
			business.salvarAgenda(agendaVo);
			return REDIRECT;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	public String editar() {
		if (agendaVo.getRowid() == null || agendaVo.getRowid().trim().isEmpty()) {
			return REDIRECT;
		}

		try {
			agendaVo = business.buscarAgendaPor(agendaVo.getRowid());
			if (agendaVo == null) {
				addActionError("Agenda não encontrada.");
				return todos();
			}
			return INPUT;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			return todos();
		}
	}

	public String excluir() {
		if (agendaVo.getRowid() != null && !agendaVo.getRowid().trim().isEmpty()) {
			try {
				business.excluirAgenda(agendaVo.getRowid());
			} catch (BusinessException e) {
				addActionError(e.getMessage());
			}
		}
		return todos();
	}

	public Map<String, String> getOpcoesPeriodo() {
		Map<String, String> opcoes = new LinkedHashMap<>();
		opcoes.put("Manhã", "Manhã");
		opcoes.put("Tarde", "Tarde");
		opcoes.put("Ambos", "Ambos");
		return opcoes;
	}

	public List<AgendaVo> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<AgendaVo> agendas) {
		this.agendas = agendas;
	}

	public AgendaVo getAgendaVo() {
		return agendaVo;
	}

	public void setAgendaVo(AgendaVo agendaVo) {
		this.agendaVo = agendaVo;
	}
}
