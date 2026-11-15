<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
	<div class="container-fluid">
		<s:url action="todosFuncionarios" var="urlFuncionarios" />
		<s:url action="todosAgendas" var="urlAgendas" />
		<s:url action="todosCompromissos" var="urlCompromissos" />
		<s:url action="abrirRelatorio" var="urlRelatorio" />

		<a class="navbar-brand" href="${urlFuncionarios}"><s:text name="label.menu.titulo"/></a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav">
				<li class="nav-item">
					<a class="nav-link" href="${urlFuncionarios}"><s:text name="label.menu.funcionarios"/></a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="${urlAgendas}"><s:text name="label.menu.agendas"/></a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="${urlCompromissos}"><s:text name="label.menu.compromissos"/></a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="${urlRelatorio}"><s:text name="label.menu.relatorio"/></a>
				</li>
			</ul>
		</div>
	</div>
</nav>
