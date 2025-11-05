package com.auth_service.auth_service.models.dao;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;


import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    private List<RoleEntity> roles;


    @PrePersist
    public void generateUlid() {
        if (this.id == null) {
            this.id = new ULID().nextULID();  // ✅ sulky ULID generator
        }
    }
}