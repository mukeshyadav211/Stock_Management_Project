package com.mks.sageit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mks.sageit.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(value = "select * from orders o where o.item_id = ?1", nativeQuery = true)
	Order findByItemId(int id);
}
