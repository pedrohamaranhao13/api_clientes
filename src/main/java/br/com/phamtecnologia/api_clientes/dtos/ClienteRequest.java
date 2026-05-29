package br.com.phamtecnologia.api_clientes.dtos;

public record ClienteRequest(
        String nome,
        String cpf
) {
}
