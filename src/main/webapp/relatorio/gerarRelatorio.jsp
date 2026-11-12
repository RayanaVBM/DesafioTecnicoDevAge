<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="s" uri="/struts-tags" %>
		<!DOCTYPE html>
		<html>

		<head>
			<title>Relatório de Compromissos</title>
			<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
		</head>

		<body class="bg-secondary">
			<jsp:include page="/menu/menu.jsp" />
			<div class="container mt-4">
				<div class="card mb-4">
					<div class="card-header">
						<h5>Filtro de Relatório</h5>
					</div>
					<div class="card-body">
						<s:if test="hasActionErrors()">
							<div class="alert alert-danger">
								<s:actionerror escape="false" />
							</div>
						</s:if>

						<s:form action="/gerarRelatorio.action">
							<div class="row align-items-center">
								<div class="col-md-4">
									<label class="form-label">Data Inicial:</label>
									<s:textfield type="date" cssClass="form-control" name="filtrar.dataInicial" />
								</div>
								<div class="col-md-4">
									<label class="form-label">Data Final:</label>
									<s:textfield type="date" cssClass="form-control" name="filtrar.dataFinal" />
								</div>
								<div class="col-md-4 d-flex align-items-end mt-4">
									<button class="btn btn-primary w-100">Gerar Relatório em Tela</button>
								</div>
							</div>
						</s:form>
					</div>
				</div>

				<s:if test="compromissos != null && !compromissos.isEmpty()">
					<div class="card">
						<div class="card-header bg-success text-white">
							<h5>Resultados da Pesquisa</h5>
						</div>
						<div class="card-body p-0">
							<table class="table table-striped table-hover mb-0">
								<thead class="table-dark">
									<tr>
										<th>Data</th>
										<th>Hora</th>
										<th>Cód. Funcionário</th>
										<th>Nome Funcionário</th>
										<th>Cód. Agenda</th>
										<th>Nome Agenda</th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="compromissos">
										<tr>
											<td>${data}</td>
											<td>${hora}</td>
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
			</div>
		</body>

		</html>