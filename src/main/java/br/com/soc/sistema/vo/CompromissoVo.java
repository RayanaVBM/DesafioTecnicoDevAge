package br.com.soc.sistema.vo;

public class CompromissoVo {
    private String rowid;
    private FuncionarioVo funcionario;
    private AgendaVo agenda;
    private String data;
    private String hora;

    public CompromissoVo() {
        this.funcionario = new FuncionarioVo();
        this.agenda = new AgendaVo();
    }

    public String getRowid() { return rowid; }
    public void setRowid(String rowid) { this.rowid = rowid; }
    public FuncionarioVo getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioVo funcionario) { this.funcionario = funcionario; }
    public AgendaVo getAgenda() { return agenda; }
    public void setAgenda(AgendaVo agenda) { this.agenda = agenda; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}