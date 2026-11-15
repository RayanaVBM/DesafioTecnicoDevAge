package br.com.soc.sistema.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.RelatorioFilter;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoBusiness {
	private CompromissoDao dao;
	private AgendaDao agendaDao;
	private FuncionarioDao funcionarioDao;

	public CompromissoBusiness() {
		this.dao = new CompromissoDao();
		this.agendaDao = new AgendaDao();
		this.funcionarioDao = new FuncionarioDao();
	}

	public List<CompromissoVo> trazerTodosOsCompromissos() {
		return dao.findAllCompromissos();
	}

	public void salvarCompromisso(CompromissoVo vo) {
		if (vo == null) {
			throw new BusinessException("Os dados do compromisso são obrigatórios.");
		}
		if (vo.getFuncionario() == null || vo.getFuncionario().getRowid() == null || vo.getFuncionario().getRowid().trim().isEmpty()) {
			throw new BusinessException("O campo Funcionário é obrigatório.");
		}
		if (vo.getAgenda() == null || vo.getAgenda().getRowid() == null || vo.getAgenda().getRowid().trim().isEmpty()) {
			throw new BusinessException("O campo Agenda é obrigatório.");
		}
		if (vo.getData() == null || vo.getData().trim().isEmpty()) {
			throw new BusinessException("O campo Data é obrigatório.");
		}
		if (vo.getHora() == null || vo.getHora().trim().isEmpty()) {
			throw new BusinessException("O campo Horário é obrigatório.");
		}

		FuncionarioVo funcionarioBanco;
		AgendaVo agendaBanco;
		try {
			funcionarioBanco = funcionarioDao.findByCodigo(Integer.parseInt(vo.getFuncionario().getRowid().trim()));
			agendaBanco = agendaDao.findByCodigo(Integer.parseInt(vo.getAgenda().getRowid().trim()));
		} catch (NumberFormatException e) {
			throw new BusinessException("Código de Funcionário ou Agenda inválido.", e);
		}

		if (funcionarioBanco == null) {
			throw new BusinessException("O funcionário selecionado não existe.");
		}
		if (agendaBanco == null) {
			throw new BusinessException("A agenda selecionada não existe.");
		}

		LocalTime horario;
		try {
			LocalDate.parse(vo.getData().trim());
			horario = LocalTime.parse(vo.getHora().trim());
		} catch (DateTimeParseException e) {
			throw new BusinessException("Formato de data ou horário inválido.", e);
		}

		String periodo = agendaBanco.getPeriodoDisponivel();
		boolean isManha = horario.getHour() < 12;
		boolean isTarde = horario.getHour() >= 12;

		if (periodo != null && periodo.contains("Manhã") && !isManha) {
			throw new BusinessException("A agenda selecionada só permite compromissos no período da Manhã (antes das 12:00).");
		}

		if (periodo != null && periodo.contains("Tarde") && !isTarde) {
			throw new BusinessException("A agenda selecionada só permite compromissos no período da Tarde (a partir das 12:00).");
		}

		boolean isNovo = vo.getRowid() == null || vo.getRowid().trim().isEmpty();
		if (!isNovo) {
			CompromissoVo existente = buscarCompromissoPor(vo.getRowid());
			if (existente == null) {
				throw new BusinessException(
						"Operação cancelada: Não existe compromisso cadastrado com o código " + vo.getRowid() + ".");
			}
		}

		if (dao.existeConflitoHorario(vo)) {
			throw new BusinessException(
					"Já existe um compromisso agendado para este funcionário ou para esta agenda na mesma data e horário.");
		}

		try {
			if (isNovo) {
				dao.insertCompromisso(vo);
			} else {
				dao.updateCompromisso(vo);
			}
		} catch (Exception e) {
			throw new BusinessException("Não foi possível salvar o compromisso no banco de dados.", e);
		}
	}

	public void excluirCompromisso(String rowid) {
		CompromissoVo compromisso = buscarCompromissoPor(rowid);
		if (compromisso == null) {
			throw new BusinessException(
					"Operação cancelada: Não existe compromisso cadastrado com o código " + rowid + ".");
		}

		try {
			dao.deleteCompromisso(rowid.trim());
		} catch (Exception e) {
			throw new BusinessException("Não foi possível realizar a exclusão do compromisso.", e);
		}
	}

	public CompromissoVo buscarCompromissoPor(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new BusinessException("O código do compromisso deve ser informado.");
		}
		try {
			return dao.findByCodigo(Integer.parseInt(codigo.trim()));
		} catch (NumberFormatException e) {
			throw new BusinessException("Foi informado um caracter no lugar de um numero", e);
		}
	}

	public List<CompromissoVo> buscarCompromissosPorPeriodo(RelatorioFilter filter) {
		if (filter == null || filter.getDataInicial() == null || filter.getDataInicial().trim().isEmpty()
				|| filter.getDataFinal() == null || filter.getDataFinal().trim().isEmpty()) {
			throw new BusinessException("As datas inicial e final são obrigatórias para gerar o relatório.");
		}

		LocalDate dtInicial;
		LocalDate dtFinal;
		try {
			dtInicial = LocalDate.parse(filter.getDataInicial().trim());
			dtFinal = LocalDate.parse(filter.getDataFinal().trim());
		} catch (DateTimeParseException e) {
			throw new BusinessException("As datas informadas possuem formato inválido.", e);
		}

		if (dtInicial.isAfter(dtFinal)) {
			throw new BusinessException("A data inicial não pode ser posterior à data final.");
		}

		return dao.findCompromissosByPeriodo(filter.getDataInicial().trim(), filter.getDataFinal().trim());
	}

	public InputStream gerarRelatorioExcel(RelatorioFilter filter) {
		List<CompromissoVo> compromissos = buscarCompromissosPorPeriodo(filter);

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Relatório de Compromissos");

			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 14);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);

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

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setBorderTop(BorderStyle.THIN);
			dataStyle.setBorderBottom(BorderStyle.THIN);
			dataStyle.setBorderLeft(BorderStyle.THIN);
			dataStyle.setBorderRight(BorderStyle.THIN);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Relatório de Compromissos");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

			Row headerRow = sheet.createRow(1);
			String[] columns = { "Data", "Hora", "Cód. Funcionário", "Nome Funcionário", "Cód. Agenda", "Nome Agenda" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIdx = 2;
			for (CompromissoVo c : compromissos) {
				Row row = sheet.createRow(rowIdx++);

				Cell cell0 = row.createCell(0);
				cell0.setCellValue(c.getDataFormatada());
				cell0.setCellStyle(dataStyle);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(c.getHoraFormatada());
				cell1.setCellStyle(dataStyle);

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(c.getFuncionario().getRowid());
				cell2.setCellStyle(dataStyle);

				Cell cell3 = row.createCell(3);
				cell3.setCellValue(c.getFuncionario().getNome());
				cell3.setCellStyle(dataStyle);

				Cell cell4 = row.createCell(4);
				cell4.setCellValue(c.getAgenda().getRowid());
				cell4.setCellStyle(dataStyle);

				Cell cell5 = row.createCell(5);
				cell5.setCellValue(c.getAgenda().getNome());
				cell5.setCellStyle(dataStyle);
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new BusinessException("Erro ao gerar arquivo Excel.", e);
		}
	}
}
