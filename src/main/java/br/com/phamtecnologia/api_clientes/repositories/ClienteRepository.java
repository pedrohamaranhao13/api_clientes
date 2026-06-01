package br.com.phamtecnologia.api_clientes.repositories;

import br.com.phamtecnologia.api_clientes.entities.Cliente;
import br.com.phamtecnologia.api_clientes.entities.Endereco;
import br.com.phamtecnologia.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    public void inserir(Cliente cliente) throws Exception {
        try (var connection = connectionFactory.getConnection()) {

            connection.setAutoCommit(false);
            var statement = connection.prepareStatement("""
                INSERT INTO clientes (nome, CPF)
                VALUES (?, ?)
            """,  Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }

            if (cliente.getEnderecos() != null) {
                for (var endereco : cliente.getEnderecos()) {

                    statement = connection.prepareStatement("""
                        INSERT INTO enderecos (logradouro, numero, complemento, bairro, cidade, uf, cep, cliente_id)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """);
                    statement.setString(1, endereco.getLogradouro());
                    statement.setString(2, endereco.getNumero());
                    statement.setString(3, endereco.getComplemento());
                    statement.setString(4, endereco.getBairro());
                    statement.setString(5, endereco.getCidade());
                    statement.setString(6, endereco.getUf());
                    statement.setString(7, endereco.getCep());
                    statement.setInt(8, cliente.getId());

                    statement.execute();

                }
            }

            connection.commit();
            statement.close();
        }
    }

    public boolean cpfExistente(String cpf) throws Exception {
        try (var connection = connectionFactory.getConnection()) {
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

        try (var connection = connectionFactory.getConnection()) {
            var statement = connection.prepareStatement("""
                             SELECT
                                 c.ID AS IDCLIENTE,
                                 c.NOME,
                                 c.CPF,
                                 e.ID AS IDENDERECO,
                                 e.LOGRADOURO,
                                 e.NUMERO,
                                 e.COMPLEMENTO,
                                 e.BAIRRO,
                                 e.CIDADE,
                                 e.UF,
                                 e.CEP
                             FROM CLIENTES c
                                      LEFT JOIN ENDERECOS e
                             ON c.ID = e.CLIENTE_ID
                             WHERE c.NOME ILIKE ?
                             ORDER BY c.NOME;\s
            """);
            statement.setString(1, "%" + nome + "%");
            var result  = statement.executeQuery();

            var lista = new ArrayList<Cliente>();

            var map = new HashMap<Integer, Cliente>();

            while (result.next()) {
                var clienteId = result.getInt("IDCLIENTE");

                Cliente cliente;

                if(map.containsKey(clienteId)) {
                    cliente = map.get(clienteId);
                }
                else {
                    cliente = new Cliente();

                    cliente.setId(result.getInt("IDCLIENTE"));
                    cliente.setNome(result.getString("NOME"));
                    cliente.setCpf(result.getString("CPF"));
                    cliente.setEnderecos(new ArrayList<>());

                    map.put(clienteId, cliente);

                    lista.add(cliente);

                }

                var  endereco = new Endereco();

                endereco.setId(result.getInt("idendereco"));
                endereco.setLogradouro(result.getString("logradouro"));
                endereco.setNumero(result.getString("numero"));
                endereco.setComplemento(result.getString("complemento"));
                endereco.setBairro(result.getString("bairro"));
                endereco.setCidade(result.getString("cidade"));
                endereco.setUf(result.getString("uf"));
                endereco.setCep(result.getString("cep"));

                cliente.getEnderecos().add(endereco);


            }
            return lista;
        }
    }

    public boolean excluir(Integer id) throws Exception {
        try (var connection = connectionFactory.getConnection()) {

            var statement = connection.prepareStatement("""
                    UPDATE clientes
                    SET status = 0,
                        datahoraexclusao = CURRENT_TIMESTAMP
                    WHERE id = ?
                    AND status = 1
            """);
            statement.setInt(1, id);

            var rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        }
    }
}
