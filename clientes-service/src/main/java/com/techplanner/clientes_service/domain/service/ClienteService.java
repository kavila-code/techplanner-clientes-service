package com.techplanner.clientes_service.domain.service;

import com.techplanner.clientes_service.domain.entity.Cliente;
import com.techplanner.clientes_service.domain.exception.ClienteNotFoundException;
import com.techplanner.clientes_service.domain.exception.ClienteServiceException;
import com.techplanner.clientes_service.domain.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        try {
            // normalizar email (trim + lowercase) para evitar duplicados por
            // mayúsculas/espacios
            if (cliente.getEmail() != null) {
                String normalized = cliente.getEmail().trim().toLowerCase();
                cliente.setEmail(normalized);
            }

            // evitar duplicados por email: si existe otro cliente con el mismo email,
            // fallar
            clienteRepository.findByEmail(cliente.getEmail()).ifPresent(existing -> {
                if (cliente.getId() == null || !existing.getId().equals(cliente.getId())) {
                    throw new ClienteServiceException("Email ya existe: " + cliente.getEmail());
                }
            });

            Cliente saved = clienteRepository.save(cliente);
            log.info("Cliente guardado: {}", saved.getId());
            return saved;
        } catch (DataAccessException e) {
            log.error("Error guardando cliente", e);
            throw new ClienteServiceException("Error guardando cliente", e);
        }
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public void eliminar(Long id) {
        try {
            if (!clienteRepository.existsById(id)) {
                throw new ClienteNotFoundException(id);
            }
            clienteRepository.deleteById(id);
            log.info("Cliente eliminado: {}", id);
        } catch (DataAccessException e) {
            log.error("Error eliminando cliente {}", id, e);
            throw new ClienteServiceException("Error eliminando cliente", e);
        }
    }
}
