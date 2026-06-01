package br.com.phamtecnologia.api_clientes.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id",
        "nome",
        "cpf",
        "enderecos"
})
public class Cliente {

    private Integer id;
    private String nome;
    private String cpf;
    private List<Endereco> enderecos;

}
