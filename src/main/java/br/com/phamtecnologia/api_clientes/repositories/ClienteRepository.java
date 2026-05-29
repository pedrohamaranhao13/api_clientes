package br.com.phamtecnologia.api_clientes.repositories;

import br.com.phamtecnologia.api_clientes.entities.Cliente;
import br.com.phamtecnologia.api_clientes.factories.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public void inserir(Cliente cliente) throws Exception {
        try (var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("""
                INSERT INTO clientes (nome, CPF)
                VALUES (?, ?)
            """);
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();

            statement.close();
        }
    }

    public boolean cpfExistente(String cpf) throws Exception {
        try (var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("""
                SELECT COUNT(*) AS QTD FROM clientes WHERE cpf = ?
            """);

            statement.setString(1, cpf);

            var result  = statement.executeQuery();
            if (result.next()) {
                return result.getInt("QTD") == 1;
            }

            return false;
        }
    }

    public List<Cliente> listar(String nome) throws Exception {

        try (var connection = ConnectionFactory.getConnection()) {
            var statement = connection.prepareStatement("""
                SELECT * FROM clientes
                WHERE nome LIKE ?
            """);
            statement.setString(1, "%" + nome + "%");
            var result  = statement.executeQuery();

            var lista = new ArrayList<Cliente>();

            while (result.next()) {
                var cliente = new Cliente();

                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("CPF"));

                lista.add(cliente);
            }
            return lista;
        }
    }
}
