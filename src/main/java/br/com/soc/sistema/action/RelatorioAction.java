package br.com.soc.sistema.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
                
                // --- 1. CRIANDO OS ESTILOS ---
                // Estilo do Título: Negrito, centralizado e fonte maior
                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 14);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                // Estilo do Cabeçalho: Fundo verde claro, texto negrito e bordas
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Estilo dos Dados: Apenas bordas normais
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);

                // --- 2. LINHA 0: TÍTULO GERAL ---
                Row titleRow = sheet.createRow(0);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Relatório de Compromissos");
                titleCell.setCellStyle(titleStyle);
                // Mescla as células da coluna 0 à 5 (A1 até F1) para o título ficar centralizado
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

                // --- 3. LINHA 1: CABEÇALHOS DAS COLUNAS ---
                Row headerRow = sheet.createRow(1);
                String[] columns = {"Data", "Hora", "Cód. Funcionário", "Nome Funcionário", "Cód. Agenda", "Nome Agenda"};
                for(int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // --- 4. LINHA 2 EM DIANTE: DADOS DOS COMPROMISSOS ---
                int rowIdx = 2;
                for(CompromissoVo c : compromissos) {
                    Row row = sheet.createRow(rowIdx++);
                    
                    Cell cell0 = row.createCell(0); cell0.setCellValue(c.getData()); cell0.setCellStyle(dataStyle);
                    Cell cell1 = row.createCell(1); cell1.setCellValue(c.getHora()); cell1.setCellStyle(dataStyle);
                    Cell cell2 = row.createCell(2); cell2.setCellValue(c.getFuncionario().getRowid()); cell2.setCellStyle(dataStyle);
                    Cell cell3 = row.createCell(3); cell3.setCellValue(c.getFuncionario().getNome()); cell3.setCellStyle(dataStyle);
                    Cell cell4 = row.createCell(4); cell4.setCellValue(c.getAgenda().getRowid()); cell4.setCellStyle(dataStyle);
                    Cell cell5 = row.createCell(5); cell5.setCellValue(c.getAgenda().getNome()); cell5.setCellStyle(dataStyle);
                }
                
                // --- 5. AJUSTE AUTOMÁTICO DE LARGURA DAS COLUNAS ---
                for(int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // --- 6. GERAÇÃO DO ARQUIVO PARA DOWNLOAD ---
                workbook.write(out);
                fileInputStream = new ByteArrayInputStream(out.toByteArray());
                
                // Gera o timestamp no formato AAAAMMDD_HHMMSS
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                String timestamp = java.time.LocalDateTime.now().format(formatter);
                
                fileName = "Relatorio_Compromissos_" + timestamp + ".xlsx";
            }
            return "excel";
            
        } catch (br.com.soc.sistema.exception.BusinessException be) {
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

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String getFileName() {
		return fileName;
	}
}