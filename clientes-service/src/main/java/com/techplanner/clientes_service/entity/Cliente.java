package com.techplanner.clientes_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolCliente rol;

    @PrePersist
    void asignarRolPorDefecto() {
        if (rol == null) {
            rol = RolCliente.USUARIO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolCliente getRol() {
        return rol;
    }

    public void setRol(RolCliente rol) {
        this.rol = rol;
    }
}
