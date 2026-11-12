package br.com.soc.sistema.business;

import java.util.List;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoBusiness {
    private CompromissoDao dao;

    public CompromissoBusiness() {
        this.dao = new CompromissoDao();
    }

    public List<CompromissoVo> trazerTodosOsCompromissos() {
        return dao.findAllCompromissos();
    }

    public void salvarCompromisso(CompromissoVo vo) {
        try {
            if(vo.getFuncionario().getRowid() == null || vo.getFuncionario().getRowid().isEmpty()) 
                throw new IllegalArgumentException("Funcionário obrigatório");
            if(vo.getAgenda().getRowid() == null || vo.getAgenda().getRowid().isEmpty()) 
                throw new IllegalArgumentException("Agenda obrigatória");
            if(vo.getData() == null || vo.getData().isEmpty()) 
                throw new IllegalArgumentException("Data obrigatória");
            if(vo.getHora() == null || vo.getHora().isEmpty()) 
                throw new IllegalArgumentException("Horário obrigatório");

            if(vo.getRowid() == null || vo.getRowid().isEmpty()) {
                dao.insertCompromisso(vo);
            } else {
                dao.updateCompromisso(vo);
            }
        } catch (Exception e) {
            throw new BusinessException("Não foi possível salvar o compromisso: " + e.getMessage());
        }
    }

    public void excluirCompromisso(String rowid) {
        try {
            dao.deleteCompromisso(rowid);
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar a exclusão do compromisso.");
        }
    }

    public CompromissoVo buscarCompromissoPor(String codigo) {
        return dao.findByCodigo(Integer.parseInt(codigo));
    }
}