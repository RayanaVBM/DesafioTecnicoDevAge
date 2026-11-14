//package br.com.soc.sistema.soap;
//
//import javax.jws.WebService;
//
//import br.com.soc.sistema.business.FuncionarioBusiness;
//
//@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceFuncionarios" )
//public class WebServiceFuncionariosImpl implements WebServiceFuncionarios {
//
//	private FuncionarioBusiness business;
//	
//	public WebServiceFuncionariosImpl() {
//		this.business = new FuncionarioBusiness();
//	}
//	
//	@Override
//	public String buscarFuncionario(String codigo) {		
//		return business.buscarFuncionarioPor(codigo).toString();
//	}
//}
package br.com.soc.sistema.soap;

import javax.jws.WebService;
import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.vo.FuncionarioVo;

@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceFuncionarios" )
public class WebServiceFuncionariosImpl implements WebServiceFuncionarios {

    private FuncionarioBusiness business;

    public WebServiceFuncionariosImpl() {
        this.business = new FuncionarioBusiness();
    }

    @Override
    public String buscarFuncionario(String codigo) {
        try {
            FuncionarioVo funcionario = business.buscarFuncionarioPor(codigo);
            
            if (funcionario == null) {
                return "Consulta sem resultados: Não existe funcionário cadastrado com o código " + codigo + ".";
            }
            
            return funcionario.toString();
            
        } catch (br.com.soc.sistema.exception.BusinessException be) {
            return "Erro de validação: " + be.getMessage();
        } catch (Exception e) {
            return "Erro interno ao buscar funcionário: " + e.getMessage();
        }
    }

    @Override
    public String inserirFuncionario(String nome) {
        try {
            // Cria um VO novo sem ID (o banco gera automaticamente)
            FuncionarioVo vo = new FuncionarioVo(null, nome);
            business.salvarFuncionario(vo);
            return "Funcionário '" + nome + "' cadastrado com sucesso via SOAP!";
        } catch (Exception e) {
            return "Erro ao cadastrar funcionário: " + e.getMessage();
        }
    }

    @Override
    public String excluirFuncionario(String codigo) {
        try {
            business.excluirFuncionario(codigo);
            return "Funcionário (ID: " + codigo + ") excluído com sucesso via SOAP!";
        } catch (Exception e) {
            return "Erro ao excluir funcionário: " + e.getMessage();
        }
    }
}