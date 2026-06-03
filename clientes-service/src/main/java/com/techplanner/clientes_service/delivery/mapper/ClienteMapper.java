package com.techplanner.clientes_service.delivery.mapper;

import com.techplanner.clientes_service.domain.entity.Cliente;
import com.techplanner.clientes_service.delivery.dto.ClienteRequest;
import com.techplanner.clientes_service.delivery.dto.ClienteResponse;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente requestToEntity(ClienteRequest dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }

    public ClienteResponse entityToResponse(Cliente entity) {
        return new ClienteResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getEmail());
    }

    public void updateEntityFromRequest(ClienteRequest dto, Cliente entity) {
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
    }
}
