package com.smartorder.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository — pure infrastructure concern.
 * The domain never sees this interface. It only knows OrderRepository (the port).
 * The JpaOrderRepositoryAdapter implements the port using this.
 */
interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
