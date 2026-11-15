<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title><s:text name="label.agenda.titulo.cadastro"/></title>
	<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>

<body class="bg-secondary">
	<jsp:include page="/menu/menu.jsp" />
	<div class="container">
		<s:form action="/novoAgendas.action">
			<div class="card mt-5">
				<div class="card-header">
					<div class="row">
						<div class="col-sm-5">
							<s:url action="todosAgendas" var="todos" />
							<a href="${todos}" class="btn btn-success"><s:text name="label.menu.agendas"/></a>
						</div>
						<div class="col-sm">
							<h5 class="card-title">
								<s:if test="agendaVo.rowid != null && !agendaVo.rowid.isEmpty()">
									<s:text name="label.agenda.editar"/>
								</s:if>
								<s:else>
									<s:text name="label.agenda.nova"/>
								</s:else>
							</h5>
						</div>
					</div>
				</div>
				<div class="card-body">
					<s:if test="hasActionErrors()">
						<div class="alert alert-danger" role="alert">
							<s:actionerror escape="false" />
						</div>
					</s:if>
					<div class="row align-items-center mb-3">
						<label for="id" class="col-sm-2 col-form-label text-end"><s:text name="label.codigo"/></label>
						<div class="col-sm-2">
							<s:textfield cssClass="form-control" id="id" name="agendaVo.rowid"
								readonly="true" />
						</div>
					</div>
					<div class="row align-items-center mb-3">
						<label for="nome" class="col-sm-2 col-form-label text-end"><s:text name="label.nome.campo"/></label>
						<div class="col-sm-5">
							<s:textfield cssClass="form-control" id="nome" name="agendaVo.nome" maxlength="70" />
						</div>
					</div>
					<div class="row align-items-center mb-3">
						<label for="periodo" class="col-sm-2 col-form-label text-end"><s:text name="label.periodo.disponivel"/></label>
						<div class="col-sm-4">
							<s:select cssClass="form-select" id="periodo" name="agendaVo.periodoDisponivel" 
									  list="opcoesPeriodo" 
									  headerKey="" headerValue="Selecione um período..." />
						</div>
					</div>
				</div>
				<div class="card-footer text-center">
					<button class="btn btn-primary"><s:text name="label.salvar"/></button>
					<button type="reset" class="btn btn-secondary"><s:text name="label.limpar"/></button>
				</div>
			</div>
		</s:form>
	</div>

	<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
</body>

</html>
