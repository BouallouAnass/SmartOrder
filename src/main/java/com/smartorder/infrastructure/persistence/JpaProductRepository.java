package com.smartorder.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByActiveTrue();
}
