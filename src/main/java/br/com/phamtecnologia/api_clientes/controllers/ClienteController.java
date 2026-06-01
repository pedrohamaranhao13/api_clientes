package br.com.phamtecnologia.api_clientes.controllers;

import br.com.phamtecnologia.api_clientes.dtos.ClienteRequest;
import br.com.phamtecnologia.api_clientes.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("criar")
    public ResponseEntity<String> criar(@RequestBody ClienteRequest request) {
        try {
            clienteService.cadastrarCliente(request);

            return ResponseEntity.status(201).body( "Cliente " + request.nome() + " cadastrado com sucesso!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar(@RequestParam String nome) {
        try {
            var lista = clienteService.pesquisarClientes(nome);

            return ResponseEntity.status(200).body(lista);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

   @DeleteMapping("excluir/{id}")
   public ResponseEntity<String> excluir(@PathVariable Integer id) {
        try {
            clienteService.excluirCliente(id);
            return ResponseEntity.status(200).body("Cliente excluído com sucesso.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
   }
}
