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
//
//    public void salvarCompromisso(CompromissoVo vo) {
//        try {
//            if(vo.getFuncionario().getRowid() == null || vo.getFuncionario().getRowid().isEmpty()) 
//                throw new IllegalArgumentException("Funcionário obrigatório");
//            if(vo.getAgenda().getRowid() == null || vo.getAgenda().getRowid().isEmpty()) 
//                throw new IllegalArgumentException("Agenda obrigatória");
//            if(vo.getData() == null || vo.getData().isEmpty()) 
//                throw new IllegalArgumentException("Data obrigatória");
//            if(vo.getHora() == null || vo.getHora().isEmpty()) 
//                throw new IllegalArgumentException("Horário obrigatório");
//
//            if(vo.getRowid() == null || vo.getRowid().isEmpty()) {
//                dao.insertCompromisso(vo);
//            } else {
//                dao.updateCompromisso(vo);
//            }
//        } catch (Exception e) {
//            throw new BusinessException("Não foi possível salvar o compromisso: " + e.getMessage());
//        }
//    }

    public void salvarCompromisso(CompromissoVo vo) {
        try {
            // 1. Validações de campos obrigatórios
            if(vo.getFuncionario().getRowid() == null || vo.getFuncionario().getRowid().isEmpty()) 
                throw new IllegalArgumentException("Funcionário obrigatório");
            if(vo.getAgenda().getRowid() == null || vo.getAgenda().getRowid().isEmpty()) 
                throw new IllegalArgumentException("Agenda obrigatória");
            if(vo.getData() == null || vo.getData().isEmpty()) 
                throw new IllegalArgumentException("Data obrigatória");
            if(vo.getHora() == null || vo.getHora().isEmpty()) 
                throw new IllegalArgumentException("Horário obrigatório");

            // 2. REGRA DE NEGÓCIO: Validação de Disponibilidade de Horário da Agenda
            br.com.soc.sistema.dao.AgendaDao agendaDao = new br.com.soc.sistema.dao.AgendaDao();
            br.com.soc.sistema.vo.AgendaVo agendaBanco = agendaDao.findByCodigo(Integer.parseInt(vo.getAgenda().getRowid()));
            
            if(agendaBanco != null) {
                // Converte a String de hora (HH:mm) para LocalTime (Recurso do Java 8)
                java.time.LocalTime horario = java.time.LocalTime.parse(vo.getHora());
                String periodo = agendaBanco.getPeriodoDisponivel();
                
                // Define o limiar: Manhã é antes das 12h, Tarde é 12h em diante
                boolean isManha = horario.getHour() < 12;
                boolean isTarde = horario.getHour() >= 12;
                
                if (periodo.contains("Manhã") && !isManha) {
                    throw new BusinessException("A agenda selecionada só permite compromissos no período da Manhã (antes das 12:00).");
                }
                
                if (periodo.contains("Tarde") && !isTarde) {
                    throw new BusinessException("A agenda selecionada só permite compromissos no período da Tarde (a partir das 12:00).");
                }
                // Se for "Ambos", nenhuma exceção é lançada e o fluxo segue normalmente.
            }

            // 3. Salva ou atualiza no banco
            if(vo.getRowid() == null || vo.getRowid().isEmpty()) {
                dao.insertCompromisso(vo);
            } else {
                dao.updateCompromisso(vo);
            }
            
        } catch (BusinessException be) {
            throw be; // Repassa nossa exceção de regra de negócio para a tela
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
    
    public List<CompromissoVo> buscarCompromissosPorPeriodo(br.com.soc.sistema.filter.RelatorioFilter filter) {
        if (filter.getDataInicial() == null || filter.getDataInicial().isEmpty() ||
            filter.getDataFinal() == null || filter.getDataFinal().isEmpty()) {
            throw new BusinessException("As datas inicial e final são obrigatórias para gerar o relatório.");
        }
        return dao.findCompromissosByPeriodo(filter.getDataInicial(), filter.getDataFinal());
    }
}