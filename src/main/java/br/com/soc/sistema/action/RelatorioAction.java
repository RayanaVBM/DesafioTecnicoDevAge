package br.com.soc.sistema.action;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.RelatorioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.CompromissoVo;

public class RelatorioAction extends Action {

	private static final long serialVersionUID = 1L;

	private RelatorioFilter filtrar = new RelatorioFilter();
	private List<CompromissoVo> compromissos = new ArrayList<>();
	private CompromissoBusiness business = new CompromissoBusiness();
	private boolean pesquisaRealizada = false;

	private InputStream fileInputStream;
	private String fileName;

	public String abrir() {
		return SUCCESS;
	}

	public String gerar() {
		try {
			compromissos = business.buscarCompromissosPorPeriodo(filtrar);
			pesquisaRealizada = true;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
		}
		return SUCCESS;
	}

	public String exportar() {
		try {
			fileInputStream = business.gerarRelatorioExcel(filtrar);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
			String timestamp = LocalDateTime.now().format(formatter);
			fileName = "Relatorio_Compromissos_" + timestamp + ".xlsx";

			return "excel";
		} catch (BusinessException be) {
			addActionError(be.getMessage());
			return SUCCESS;
		} catch (Exception e) {
			addActionError("Erro ao gerar arquivo Excel: " + e.getMessage());
			return SUCCESS;
		}
	}

	public RelatorioFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(RelatorioFilter filtrar) {
		this.filtrar = filtrar;
	}

	public List<CompromissoVo> getCompromissos() {
		return compromissos;
	}

	public void setCompromissos(List<CompromissoVo> compromissos) {
		this.compromissos = compromissos;
	}

	public boolean isPesquisaRealizada() {
		return pesquisaRealizada;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String getFileName() {
		return fileName;
	}
}
