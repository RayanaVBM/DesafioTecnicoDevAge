package br.com.soc.sistema.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.filter.RelatorioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.vo.CompromissoVo;

public class RelatorioAction extends Action {
	private RelatorioFilter filtrar = new RelatorioFilter();
	private List<CompromissoVo> compromissos = new ArrayList<>();
	private CompromissoBusiness business = new CompromissoBusiness();

	private InputStream fileInputStream;
	private String fileName;

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

	public String exportar() {
		try {
			compromissos = business.buscarCompromissosPorPeriodo(filtrar);
			try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				Sheet sheet = workbook.createSheet("Relatório de Compromissos");
				Row headerRow = sheet.createRow(0);
				String[] columns = { "Data", "Hora", "Cód. Funcionário", "Nome Funcionário", "Cód. Agenda",
						"Nome Agenda" };
				for (int i = 0; i < columns.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(columns[i]);
				}
				int rowIdx = 1;
				for (CompromissoVo c : compromissos) {
					Row row = sheet.createRow(rowIdx++);
					row.createCell(0).setCellValue(c.getData());
					row.createCell(1).setCellValue(c.getHora());
					row.createCell(2).setCellValue(c.getFuncionario().getRowid());
					row.createCell(3).setCellValue(c.getFuncionario().getNome());
					row.createCell(4).setCellValue(c.getAgenda().getRowid());
					row.createCell(5).setCellValue(c.getAgenda().getNome());
				}
				workbook.write(out);
				fileInputStream = new ByteArrayInputStream(out.toByteArray());
				fileName = "Relatorio_Compromissos.xlsx";
			}
			return "excel";

		} catch (br.com.soc.sistema.exception.BusinessException be) {
			addActionError(be.getMessage());
			return SUCCESS;
		} catch (Exception e) {
			addActionError("Erro ao gerar arquivo Excel.");
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

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String getFileName() {
		return fileName;
	}
}