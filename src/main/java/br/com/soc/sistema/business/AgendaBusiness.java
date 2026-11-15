package br.com.soc.sistema.business;

import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaBusiness {
	private static final List<String> PERIODOS_VALIDOS = Arrays.asList("Manhã", "Tarde", "Ambos");
	private AgendaDao dao;

	public AgendaBusiness() {
		this.dao = new AgendaDao();
	}

	public List<AgendaVo> trazerTodasAsAgendas() {
		return dao.findAllAgendas();
	}

	public void salvarAgenda(AgendaVo agendaVo) {
		if (agendaVo == null || agendaVo.getNome() == null || agendaVo.getNome().trim().isEmpty()) {
			throw new BusinessException("O campo Nome da Agenda é obrigatório e não pode ficar em branco.");
		}

		String nome = agendaVo.getNome().trim();
		agendaVo.setNome(nome);

		if (nome.length() > 70) {
			throw new BusinessException("O campo Nome da Agenda ultrapassou o limite máximo de 70 caracteres.");
		}

		if (agendaVo.getPeriodoDisponivel() == null || agendaVo.getPeriodoDisponivel().trim().isEmpty()) {
			throw new BusinessException("A seleção do Período Disponível é obrigatória.");
		}

		String periodo = agendaVo.getPeriodoDisponivel().trim();
		if (!PERIODOS_VALIDOS.contains(periodo)) {
			throw new BusinessException("Período Disponível inválido. Escolha entre Manhã, Tarde ou Ambos.");
		}
		agendaVo.setPeriodoDisponivel(periodo);

		boolean isNovo = agendaVo.getRowid() == null || agendaVo.getRowid().trim().isEmpty();
		if (!isNovo) {
			AgendaVo existente = buscarAgendaPor(agendaVo.getRowid());
			if (existente == null) {
				throw new BusinessException(
						"Operação cancelada: Não existe agenda cadastrada com o código " + agendaVo.getRowid() + ".");
			}
			if (dao.possuiCompromissoForaDoPeriodo(agendaVo.getRowid().trim(), periodo)) {
				throw new BusinessException(
						"Não é possível alterar o período desta agenda para '" + periodo
						+ "', pois já existem compromissos cadastrados fora desse horário.");
			}
		}

		try {
			if (isNovo) {
				dao.insertAgenda(agendaVo);
			} else {
				dao.updateAgenda(agendaVo);
			}
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a gravação da agenda no banco de dados.", e);
		}
	}

	public void excluirAgenda(String rowid) {
		AgendaVo agenda = buscarAgendaPor(rowid);
		if (agenda == null) {
			throw new BusinessException("Operação cancelada: Não existe agenda cadastrada com o código " + rowid + ".");
		}

		if (dao.isAgendaComCompromissos(rowid.trim())) {
			throw new BusinessException("Não é possível excluir: existem compromissos cadastrados nesta agenda.");
		}

		try {
			dao.deleteAgenda(rowid.trim());
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a exclusão da agenda.", e);
		}
	}

	public AgendaVo buscarAgendaPor(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new BusinessException("O código da agenda deve ser informado.");
		}
		try {
			return dao.findByCodigo(Integer.parseInt(codigo.trim()));
		} catch (NumberFormatException e) {
			throw new BusinessException("Foi informado um caracter no lugar de um numero", e);
		}
	}
}
