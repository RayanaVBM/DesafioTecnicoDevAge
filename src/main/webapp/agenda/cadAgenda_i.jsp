<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="s" uri="/struts-tags" %>
		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>Cadastro de Agenda</title>
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
									<a href="${todos}" class="btn btn-success">Agendas</a>
								</div>
								<div class="col-sm">
									<h5 class="card-title">Nova Agenda</h5>
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
								<label for="id" class="col-sm-2 col-form-label text-end">Código:</label>
								<div class="col-sm-2">
									<s:textfield cssClass="form-control" id="id" name="agendaVo.rowid"
										readonly="true" />
								</div>
							</div>
							<div class="row align-items-center mb-3">
								<label for="nome" class="col-sm-2 col-form-label text-end">Nome:</label>
								<div class="col-sm-5">
									<s:textfield cssClass="form-control" id="nome" name="agendaVo.nome" maxlength="70" />
								</div>
							</div>
							<div class="row align-items-center mb-3">
							    <label for="periodo" class="col-sm-2 col-form-label text-end">Período Disponível:</label>
							    <div class="col-sm-4">
							        <s:select cssClass="form-select" id="periodo" name="agendaVo.periodoDisponivel" 
							                  list="#{'Manhã':'Manhã', 'Tarde':'Tarde', 'Ambos':'Ambos'}" 
							                  headerKey="" headerValue="Selecione um período..." />
							    </div>
							</div>
						</div>
						<div class="card-footer text-center">
							<button class="btn btn-primary">Salvar</button>
							<button type="reset" class="btn btn-secondary">Limpar Formulário</button>
						</div>
					</div>
				</s:form>
			</div>
		</body>

		</html>