<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Compromissos</title>
    <link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>
<body class="bg-secondary">
    <jsp:include page="/menu/menu.jsp" />
    <div class="container mt-5">
        <table class="table table-light table-striped align-middle">
            <thead>
                <tr>
                    <th>Data</th>
                    <th>Horário</th>
                    <th>Funcionário</th>
                    <th>Agenda</th>
                    <th class="text-end">Ação</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="compromissos">
                    <tr>
                        <td>${data}</td>
                        <td>${hora}</td>
                        <td>${funcionario.nome}</td>
                        <td>${agenda.nome}</td>
                        <td class="text-end">
                            <s:url action="editarCompromissos" var="editar">
                                <s:param name="compromissoVo.rowid" value="rowid"></s:param>
                            </s:url>
                            <a href="${editar}" class="btn btn-warning text-white">Editar</a>
                            
                            <s:url action="excluirCompromissos" var="excluir">
                                <s:param name="compromissoVo.rowid" value="rowid"></s:param>
                            </s:url>
                            <a href="${excluir}" class="btn btn-danger text-white" onclick="return confirm('Excluir este compromisso?');">Excluir</a>
                        </td>
                    </tr>
                </s:iterator>
            </tbody>
            <tfoot class="table-secondary">
                <tr>
                    <td colspan="5">
                        <s:url action="novoCompromissos" var="novo"/>
                        <a href="${novo}" class="btn btn-success">Novo Compromisso</a>
                    </td>
                </tr>
            </tfoot>
        </table>
    </div>
</body>
</html>