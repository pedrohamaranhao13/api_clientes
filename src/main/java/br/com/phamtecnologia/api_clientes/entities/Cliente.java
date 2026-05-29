package br.com.phamtecnologia.api_clientes.entities;

import lombok.Data;

import java.util.List;

@Data
public class Cliente {

    private Integer id;
    private String nome;
    private String cpf;
    private List<Endereco> enderecos;

}
