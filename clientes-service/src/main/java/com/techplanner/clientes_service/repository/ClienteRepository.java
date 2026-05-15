package com.techplanner.clientes_service.repository;

import com.techplanner.clientes_service.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}