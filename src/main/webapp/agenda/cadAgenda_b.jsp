<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags" %>
		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>Agendas Cadastradas</title>
			<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
		</head>

		<body class="bg-secondary">
			<jsp:include page="/menu/menu.jsp" />
			<div class="container mt-5">

				<!-- Exibe a mensagem de erro caso a exclusão seja bloqueada -->
				<s:if test="hasActionErrors()">
					<div class="alert alert-danger" role="alert">
						<s:actionerror escape="false" />
					</div>
				</s:if>

				<table class="table table-light table-striped align-middle">
					<thead>
						<tr>
							<th>ID</th>
							<th>Nome</th>
							<th>Período</th>
							<th class="text-end">Ação</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="agendas">
							<tr>
								<td>${rowid}</td>
								<td>${nome}</td>
								<td>${periodoDisponivel}</td>
								<td class="text-end">
									<s:url action="editarAgendas" var="editar">
										<s:param name="agendaVo.rowid" value="rowid"></s:param>
									</s:url>
									<a href="${editar}" class="btn btn-warning text-white">Editar</a>

									<!-- Novo Botão de Excluir -->
									<s:url action="excluirAgendas" var="excluir">
										<s:param name="agendaVo.rowid" value="rowid"></s:param>
									</s:url>
									<a href="${excluir}" class="btn btn-danger text-white"
										onclick="return confirm('Deseja excluir esta agenda?');">
										Excluir
									</a>
								</td>
							</tr>
						</s:iterator>
						<!--<div class="container mt-5">-->
						<!--    <table class="table table-light table-striped align-middle">-->
						<!--        <thead>-->
						<!--            <tr>-->
						<!--                <th>ID</th>-->
						<!--                <th>Nome</th>-->
						<!--                <th>Período</th>-->
						<!--                <th class="text-end">Ação</th>-->
						<!--            </tr>-->
						<!--        </thead>-->
						<!--        <tbody>-->
						<!--            <s:iterator value="agendas">-->
						<!--                <tr>-->
						<!--                    <td>${rowid}</td>-->
						<!--                    <td>${nome}</td>-->
						<!--                    <td>${periodoDisponivel}</td>-->
						<!--                    <td class="text-end">-->
						<!--                        <s:url action="editarAgendas" var="editar">-->
						<!--                            <s:param name="agendaVo.rowid" value="rowid"></s:param>-->
						<!--                        </s:url>-->
						<!--                        <a href="${editar}" class="btn btn-warning text-white">Editar</a>-->
						<!--                    </td>-->
						<!--                </tr>-->
						<!--            </s:iterator>-->
					</tbody>
					<tfoot class="table-secondary">
						<tr>
							<td colspan="4">
								<s:url action="novoAgendas" var="novo" />
								<a href="${novo}" class="btn btn-success">Nova Agenda</a>
								<a href="/avaliacao/todosFuncionarios.action" class="btn btn-primary float-end">Ir para
									Funcionários</a>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</body>

		</html>