<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cadastro de Compromisso</title>
    <link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
</head>
<body class="bg-secondary">
    <jsp:include page="/menu/menu.jsp" />
    <div class="container">
        <s:form action="/novoCompromissos.action">
            <div class="card mt-5">
                <div class="card-header">
                    <h5 class="card-title">Novo Compromisso</h5>
                </div>
                <div class="card-body">
                    
                    <s:if test="hasActionErrors()">
                        <div class="alert alert-danger"><s:actionerror escape="false" /></div>
                    </s:if>

                    <s:hidden name="compromissoVo.rowid" />
                    
                    <div class="row align-items-center mb-3">
                        <label class="col-sm-2 col-form-label text-end">Funcionário:</label>
                        <div class="col-sm-5">
                            <s:select cssClass="form-select" name="compromissoVo.funcionario.rowid" 
                                      list="listaFuncionarios" listKey="rowid" listValue="nome" 
                                      headerKey="" headerValue="Selecione um Funcionário..." />
                        </div>
                    </div>
                    
                    <div class="row align-items-center mb-3">
                        <label class="col-sm-2 col-form-label text-end">Agenda:</label>
                        <div class="col-sm-5">
                            <s:select cssClass="form-select" name="compromissoVo.agenda.rowid" 
                                      list="listaAgendas" listKey="rowid" listValue="nome" 
                                      headerKey="" headerValue="Selecione uma Agenda..." />
                        </div>
                    </div>

                    <div class="row align-items-center mb-3">
                        <label class="col-sm-2 col-form-label text-end">Data:</label>
                        <div class="col-sm-3">
                            <s:textfield type="date" cssClass="form-control" name="compromissoVo.data" />
                        </div>
                    </div>

                    <div class="row align-items-center mb-3">
                        <label class="col-sm-2 col-form-label text-end">Horário:</label>
                        <div class="col-sm-3">
                            <s:textfield type="time" cssClass="form-control" name="compromissoVo.hora" />
                        </div>
                    </div>
                </div>
                <div class="card-footer text-center">
                    <button class="btn btn-primary">Salvar</button>
                    <a href="/avaliacao/todosCompromissos.action" class="btn btn-secondary">Cancelar</a>
                </div>
            </div>
        </s:form>
    </div>
</body>
</html>