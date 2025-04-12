package com.saborgourmet.service;

import com.saborgourmet.model.Cliente;
import com.saborgourmet.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findActive() {
        return clienteRepository.findByActivoTrue();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese email");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, Cliente clienteDetails) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteDetails.getNombre());
                    cliente.setEmail(clienteDetails.getEmail());
                    cliente.setTelefono(clienteDetails.getTelefono());
                    cliente.setDireccion(clienteDetails.getDireccion());
                    cliente.setActivo(clienteDetails.getActivo());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
    }

    @Transactional
    public void deactivate(Long id) {
        clienteRepository.findById(id)
                .ifPresent(cliente -> {
                    cliente.setActivo(false);
                    clienteRepository.save(cliente);
                });
    }
}