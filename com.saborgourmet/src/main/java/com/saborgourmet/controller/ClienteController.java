package com.saborgourmet.controller;

import com.saborgourmet.model.Cliente;
import com.saborgourmet.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener clientes activos")
    public List<Cliente> getClientesActivos() {
        return clienteService.findActive();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener un cliente por email")
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        return clienteService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        return ResponseEntity.ok(clienteService.update(id, clienteDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar un cliente")
    public ResponseEntity<?> deactivateCliente(@PathVariable Long id) {
        clienteService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}