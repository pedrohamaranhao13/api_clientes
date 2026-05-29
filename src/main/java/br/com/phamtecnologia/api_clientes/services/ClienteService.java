package br.com.phamtecnologia.api_clientes.services;

import br.com.phamtecnologia.api_clientes.dtos.ClienteRequest;
import br.com.phamtecnologia.api_clientes.entities.Cliente;
import br.com.phamtecnologia.api_clientes.repositories.ClienteRepository;

import java.util.List;

public class ClienteService {

    public void cadastrarCliente (ClienteRequest request) throws Exception{

        if (request.nome() == null || request.nome().trim().length() < 6) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório e deve ter pelo menos 6 caracteres");
        }

        if (request.cpf() == null) {
            throw new IllegalArgumentException("O cpf do cliente é obrigatório");
        }

        var clienteRepository = new ClienteRepository();
        if (clienteRepository.cpfExistente(request.cpf())) {
            throw new IllegalArgumentException("O cpf já está cadastrado. Tente outro.");
        }

        var cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setCpf(request.cpf());

        clienteRepository.inserir(cliente);

    }

    public List<Cliente> pesquisarClientes(String nome) throws Exception{

        if (nome == null || nome.trim().length() < 5) {
            throw new IllegalArgumentException("O nome do cliente para pesquisa deve ter menos 5 caracteres.");
        }
        var clienteRespository = new ClienteRepository();
        var lista = clienteRespository.listar(nome);

        return lista;
    }
}
