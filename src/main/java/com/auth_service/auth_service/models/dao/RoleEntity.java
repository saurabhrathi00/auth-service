package com.auth_service.auth_service.models.dao;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RoleEntity {

    @Id
    private String name; // ROLE_ADMIN, ROLE_USER

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_scopes", joinColumns = @JoinColumn(name = "role_name"))
    @Column(name = "scope")
    private List<String> scopes; // READ, WRITE, DELETE

    // Constructors, getters & setters
    public RoleEntity() {}
    public RoleEntity(String name, List<String> scopes) {
        this.name = name;
        this.scopes = scopes;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getScopes() { return scopes; }
    public void setScopes(List<String> scopes) { this.scopes = scopes; }
}
