package com.techplanner.clientes_service.delivery.controller;

import com.techplanner.clientes_service.domain.entity.Cliente;
import com.techplanner.clientes_service.domain.exception.ClienteNotFoundException;
import com.techplanner.clientes_service.domain.service.ClienteService;
import com.techplanner.clientes_service.delivery.dto.ClienteRequest;
import com.techplanner.clientes_service.delivery.dto.ClienteResponse;
import com.techplanner.clientes_service.delivery.mapper.ClienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar()
                .stream()
                .map(clienteMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(clienteMapper.entityToResponse(cliente)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> guardar(@Valid @RequestBody ClienteRequest request, BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validación inválida en request: {}", br.getAllErrors());
            throw new IllegalArgumentException("Datos de cliente inválidos");
        }
        Cliente cliente = clienteMapper.requestToEntity(request);
        Cliente saved = clienteService.guardar(cliente);
        return ResponseEntity.ok(clienteMapper.entityToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request,
            BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validación inválida en request: {}", br.getAllErrors());
            throw new IllegalArgumentException("Datos de cliente inválidos");
        }
        return clienteService.buscarPorId(id)
                .map(existente -> {
                    clienteMapper.updateEntityFromRequest(request, existente);
                    Cliente updated = clienteService.guardar(existente);
                    return ResponseEntity.ok(clienteMapper.entityToResponse(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.buscarPorId(id).orElseThrow(() -> new ClienteNotFoundException(id));
        clienteService.eliminar(id);
        return ResponseEntity.noContent().<Void>build();
    }
}
