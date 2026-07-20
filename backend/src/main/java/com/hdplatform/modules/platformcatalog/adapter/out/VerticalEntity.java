package com.hdplatform.modules.platformcatalog.adapter.out;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "verticals")
@Getter @Setter @NoArgsConstructor
public class VerticalEntity {
    @Id private UUID id;
    @Column(nullable = false, unique = true, length = 50) private String code;
    @Column(nullable = false, length = 150) private String name;
    private String description;
    @Column(nullable = false) private boolean active;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
}
