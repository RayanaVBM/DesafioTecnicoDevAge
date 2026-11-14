package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaAction extends Action {
    private List<AgendaVo> agendas = new ArrayList<>();
    private AgendaBusiness business = new AgendaBusiness();
    private AgendaVo agendaVo = new AgendaVo();

    public String todos() {
        agendas.addAll(business.trazerTodasAsAgendas());
        return SUCCESS;
    }

    public String novo() {
        if(agendaVo.getNome() == null) {
            return INPUT; 
        }
        
        try {
            business.salvarAgenda(agendaVo);
            return REDIRECT;
        } catch (br.com.soc.sistema.exception.BusinessException e) {
            addActionError(e.getMessage());
            return INPUT; 
        }
    }

    public String editar() {
        if(agendaVo.getRowid() == null) return REDIRECT;
        agendaVo = business.buscarAgendaPor(agendaVo.getRowid());
        return INPUT;
    }

    public Map<String, String> getOpcoesPeriodo() {
        Map<String, String> opcoes = new LinkedHashMap<>();
        opcoes.put("Manhã", "1. Manhã");
        opcoes.put("Tarde", "2. Tarde");
        opcoes.put("Ambos", "3. Ambos");
        return opcoes;
    }

    public List<AgendaVo> getAgendas() { return agendas; }
    public void setAgendas(List<AgendaVo> agendas) { this.agendas = agendas; }
    public AgendaVo getAgendaVo() { return agendaVo; }
    public void setAgendaVo(AgendaVo agendaVo) { this.agendaVo = agendaVo; }
    
    public String excluir() {
        if(agendaVo.getRowid() != null && !agendaVo.getRowid().isEmpty()) {
            try {
                business.excluirAgenda(agendaVo.getRowid());
            } catch (br.com.soc.sistema.exception.BusinessException e) {
                addActionError(e.getMessage());
            }
        }
        return todos(); 
    }
}