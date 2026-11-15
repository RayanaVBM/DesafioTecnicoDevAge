<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name="label.compromisso.titulo.consulta"/></title>
	<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>
<body class="bg-secondary">
	<jsp:include page="/menu/menu.jsp" />
	<div class="container mt-5">
		<s:if test="hasActionErrors()">
			<div class="row mb-2">
				<div class="col-12">
					<div class="alert alert-danger" role="alert">
						<s:actionerror escape="false" />
					</div>
				</div>
			</div>
		</s:if>

		<table class="table table-light table-striped align-middle">
			<thead>
				<tr>
					<th><s:text name="label.data"/></th>
					<th><s:text name="label.horario"/></th>
					<th><s:text name="label.funcionario"/></th>
					<th><s:text name="label.agenda"/></th>
					<th class="text-end"><s:text name="label.acao"/></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="compromissos">
					<tr>
						<td>${dataFormatada}</td>
						<td>${horaFormatada}</td>
						<td>${funcionario.nome}</td>
						<td>${agenda.nome}</td>
						<td class="text-end">
							<s:url action="editarCompromissos" var="editar">
								<s:param name="compromissoVo.rowid" value="rowid"></s:param>
							</s:url>
							<a href="${editar}" class="btn btn-warning text-white"><s:text name="label.editar"/></a>
							
							<s:url action="excluirCompromissos" var="excluir">
								<s:param name="compromissoVo.rowid" value="rowid"></s:param>
							</s:url>
							<a href="${excluir}" class="btn btn-danger text-white" onclick="return confirm('Excluir este compromisso?');"><s:text name="label.excluir"/></a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot class="table-secondary">
				<tr>
					<td colspan="5">
						<s:url action="novoCompromissos" var="novo"/>
						<a href="${novo}" class="btn btn-success"><s:text name="label.compromisso.novo"/></a>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>

	<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>
</html>
