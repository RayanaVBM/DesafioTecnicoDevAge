<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name="label.compromisso.titulo.cadastro"/></title>
	<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>
<body class="bg-secondary">
	<jsp:include page="/menu/menu.jsp" />
	<div class="container">
		<s:form action="/novoCompromissos.action">
			<div class="card mt-5">
				<div class="card-header">
					<h5 class="card-title">
						<s:if test="compromissoVo.rowid != null && !compromissoVo.rowid.isEmpty()">
							<s:text name="label.compromisso.editar"/>
						</s:if>
						<s:else>
							<s:text name="label.compromisso.novo"/>
						</s:else>
					</h5>
				</div>
				<div class="card-body">
					
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger"><s:actionerror escape="false" /></div>
					</s:if>

					<s:hidden name="compromissoVo.rowid" />
					
					<div class="row align-items-center mb-3">
						<label class="col-sm-2 col-form-label text-end"><s:text name="label.funcionario"/>:</label>
						<div class="col-sm-5">
							<s:select cssClass="form-select" name="compromissoVo.funcionario.rowid" 
									  list="listaFuncionarios" listKey="rowid" listValue="nome" 
									  headerKey="" headerValue="Selecione um Funcionário..." />
						</div>
					</div>
					
					<div class="row align-items-center mb-3">
						<label class="col-sm-2 col-form-label text-end"><s:text name="label.agenda"/>:</label>
						<div class="col-sm-5">
							<s:select cssClass="form-select" name="compromissoVo.agenda.rowid" 
									  list="listaAgendas" listKey="rowid" listValue="nome" 
									  headerKey="" headerValue="Selecione uma Agenda..." />
						</div>
					</div>

					<div class="row align-items-center mb-3">
						<label class="col-sm-2 col-form-label text-end"><s:text name="label.data"/>:</label>
						<div class="col-sm-3">
							<s:textfield type="date" cssClass="form-control" name="compromissoVo.data" />
						</div>
					</div>

					<div class="row align-items-center mb-3">
						<label class="col-sm-2 col-form-label text-end"><s:text name="label.horario"/>:</label>
						<div class="col-sm-3">
							<s:textfield type="time" cssClass="form-control" name="compromissoVo.hora" />
						</div>
					</div>
				</div>
				<div class="card-footer text-center">
					<s:url action="todosCompromissos" var="urlTodosCompromissos" />
					<button class="btn btn-primary"><s:text name="label.salvar"/></button>
					<a href="${urlTodosCompromissos}" class="btn btn-secondary"><s:text name="label.cancelar"/></a>
				</div>
			</div>
		</s:form>
	</div>

	<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>
</html>
