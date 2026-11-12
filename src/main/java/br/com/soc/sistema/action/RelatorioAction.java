package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.filter.RelatorioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.CompromissoVo;

public class RelatorioAction extends Action {
    private RelatorioFilter filtrar = new RelatorioFilter();
    private List<CompromissoVo> compromissos = new ArrayList<>();
    private CompromissoBusiness business = new CompromissoBusiness();

    public String abrir() {
        return SUCCESS;
    }

    public String gerar() {
        try {
            compromissos = business.buscarCompromissosPorPeriodo(filtrar);
        } catch (br.com.soc.sistema.exception.BusinessException e) {
            addActionError(e.getMessage());
        }
        return SUCCESS;
    }

    public RelatorioFilter getFiltrar() { return filtrar; }
    public void setFiltrar(RelatorioFilter filtrar) { this.filtrar = filtrar; }
    public List<CompromissoVo> getCompromissos() { return compromissos; }
    public void setCompromissos(List<CompromissoVo> compromissos) { this.compromissos = compromissos; }
}