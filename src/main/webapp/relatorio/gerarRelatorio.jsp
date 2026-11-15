<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title><s:text name="label.relatorio.titulo"/></title>
	<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>

<body class="bg-secondary">
	<jsp:include page="/menu/menu.jsp" />
	<div class="container mt-4">
		<div class="card mb-4">
			<div class="card-header">
				<h5><s:text name="label.relatorio.filtro"/></h5>
			</div>
			<div class="card-body">
				<s:if test="hasActionErrors()">
					<div class="alert alert-danger">
						<s:actionerror escape="false" />
					</div>
				</s:if>

				<s:url action="gerarRelatorio" var="urlGerarRelatorio" />
				<s:url action="exportarRelatorio" var="urlExportarRelatorio" />

				<s:form action="/gerarRelatorio.action">
					<div class="row align-items-center">
						<div class="col-md-4">
							<label class="form-label"><s:text name="label.relatorio.data.inicial"/></label>
							<s:textfield type="date" cssClass="form-control" name="filtrar.dataInicial" />
						</div>
						<div class="col-md-4">
							<label class="form-label"><s:text name="label.relatorio.data.final"/></label>
							<s:textfield type="date" cssClass="form-control" name="filtrar.dataFinal" />
						</div>
						<div class="col-md-4 mt-4">
							<button class="btn btn-primary w-100 mb-2" type="submit"
								formaction="${urlGerarRelatorio}">
								<s:text name="label.relatorio.gerar.tela"/>
							</button>

							<button class="btn btn-success w-100" type="submit"
								formaction="${urlExportarRelatorio}">
								<s:text name="label.relatorio.exportar.excel"/>
							</button>
						</div>
					</div>
				</s:form>
			</div>
		</div>

		<s:if test="compromissos != null && !compromissos.isEmpty()">
			<div class="card">
				<div class="card-header bg-success text-white">
					<h5><s:text name="label.relatorio.resultados"/></h5>
				</div>
				<div class="card-body p-0">
					<table class="table table-striped table-hover mb-0">
						<thead class="table-dark">
							<tr>
								<th><s:text name="label.data"/></th>
								<th><s:text name="label.horario"/></th>
								<th>Cód. <s:text name="label.funcionario"/></th>
								<th>Nome <s:text name="label.funcionario"/></th>
								<th>Cód. <s:text name="label.agenda"/></th>
								<th>Nome <s:text name="label.agenda"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="compromissos">
								<tr>
									<td>${dataFormatada}</td>
									<td>${horaFormatada}</td>
									<td>${funcionario.rowid}</td>
									<td>${funcionario.nome}</td>
									<td>${agenda.rowid}</td>
									<td>${agenda.nome}</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</s:if>
		<s:elseif test="pesquisaRealizada">
			<div class="alert alert-info" role="alert">
				<s:text name="label.relatorio.sem.resultados"/>
			</div>
		</s:elseif>
	</div>

	<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>

</html>
