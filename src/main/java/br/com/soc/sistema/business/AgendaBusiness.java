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
        if(agendaVo.getNome() == null || agendaVo.getNome().trim().isEmpty()) {
            throw new BusinessException("O campo Nome da Agenda é obrigatório e não pode ficar em branco.");
        }
        
        if(agendaVo.getNome().length() > 70) {
            throw new BusinessException("O campo Nome da Agenda ultrapassou o limite máximo de 70 caracteres.");
        }

        if(agendaVo.getPeriodoDisponivel() == null || agendaVo.getPeriodoDisponivel().trim().isEmpty()) {
            throw new BusinessException("A seleção do Período Disponível é obrigatória.");
        }

        try {
            if(agendaVo.getRowid() == null || agendaVo.getRowid().isEmpty()) {
                dao.insertAgenda(agendaVo);
            } else {
                dao.updateAgenda(agendaVo);
            }
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar a gravação da agenda no banco de dados.");
        }
    }

    public void excluirAgenda(String rowid) {
        AgendaVo agenda = buscarAgendaPor(rowid);
        if (agenda == null) {
            throw new BusinessException("Operação cancelada: Não existe agenda cadastrada com o código " + rowid + ".");
        }

        if (dao.isAgendaComCompromissos(rowid)) {
            throw new BusinessException("Não é possível excluir: existem compromissos cadastrados nesta agenda.");
        }
        
        try {
            dao.deleteAgenda(rowid);
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar a exclusão da agenda.");
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