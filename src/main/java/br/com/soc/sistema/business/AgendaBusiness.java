package br.com.soc.sistema.business;

import java.util.List;
import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaBusiness {
    private AgendaDao dao;

    public AgendaBusiness() {
        this.dao = new AgendaDao();
    }

    public List<AgendaVo> trazerTodasAsAgendas(){
        return dao.findAllAgendas();
    }

    public void salvarAgenda(AgendaVo agendaVo) {
        try {
            if(agendaVo.getNome().isEmpty()) throw new IllegalArgumentException("Nome não pode ser em branco");
            if(agendaVo.getPeriodoDisponivel().isEmpty()) throw new IllegalArgumentException("Período não pode ser em branco");
            
            if(agendaVo.getRowid() == null || agendaVo.getRowid().isEmpty()){
                dao.insertAgenda(agendaVo);
            } else {
                dao.updateAgenda(agendaVo);
            }
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar a gravação do registro");
        }
    }

    public AgendaVo buscarAgendaPor(String codigo) {
        try {
            return dao.findByCodigo(Integer.parseInt(codigo));
        }catch (NumberFormatException e) {
            throw new BusinessException("Foi informado um caracter no lugar de um numero");
        }
    }
}