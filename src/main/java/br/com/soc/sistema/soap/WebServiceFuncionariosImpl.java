package br.com.soc.sistema.soap;

import javax.jws.WebService;

import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.vo.FuncionarioVo;

@WebService(endpointInterface = "br.com.soc.sistema.soap.WebServiceFuncionarios")
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

		} catch (BusinessException be) {
			return "Erro de validação: " + be.getMessage();
		} catch (Exception e) {
			return "Erro interno ao buscar funcionário.";
		}
	}

	@Override
	public String inserirFuncionario(String nome) {
		try {
			FuncionarioVo vo = new FuncionarioVo(null, nome);
			business.salvarFuncionario(vo);
			return "Funcionário '" + vo.getNome() + "' cadastrado com sucesso via SOAP!";
		} catch (BusinessException be) {
			return "Erro de validação: " + be.getMessage();
		} catch (Exception e) {
			return "Erro interno ao cadastrar funcionário.";
		}
	}

	@Override
	public String editarFuncionario(String codigo, String nome) {
		try {
			FuncionarioVo vo = new FuncionarioVo(codigo, nome);
			business.salvarFuncionario(vo);
			return "Funcionário (ID: " + codigo + ") atualizado para '" + vo.getNome() + "' com sucesso via SOAP!";
		} catch (BusinessException be) {
			return "Erro de validação: " + be.getMessage();
		} catch (Exception e) {
			return "Erro interno ao editar funcionário.";
		}
	}

	@Override
	public String excluirFuncionario(String codigo) {
		try {
			business.excluirFuncionario(codigo);
			return "Funcionário (ID: " + codigo + ") excluído com sucesso via SOAP!";
		} catch (BusinessException be) {
			return "Erro de validação: " + be.getMessage();
		} catch (Exception e) {
			return "Erro interno ao excluir funcionário.";
		}
	}
}
