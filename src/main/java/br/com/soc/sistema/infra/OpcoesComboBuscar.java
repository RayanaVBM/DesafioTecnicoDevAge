package br.com.soc.sistema.infra;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import br.com.soc.sistema.exception.BusinessException;

public enum OpcoesComboBuscar {
	ID("1", "ID"), 
	NOME("2", "NOME");
	
	private String codigo;
	private String descricao;
	private final static Map<String, OpcoesComboBuscar> opcoes = new HashMap<>();
	
	static {
		Arrays.asList(OpcoesComboBuscar.values())
		.forEach(
			opcao -> opcoes.put(opcao.getCodigo(), opcao)
		);
	}
	
	private OpcoesComboBuscar(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static OpcoesComboBuscar buscarPor(String codigo) {
		if (codigo == null)
			throw new BusinessException("Informe um código válido");
		
		return getOpcao(codigo)
				.orElseThrow(() -> new BusinessException("Código informado não existe"));
	}
	
	private static Optional<OpcoesComboBuscar> getOpcao(String codigo) {
		return Optional.ofNullable(opcoes.get(codigo));
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
