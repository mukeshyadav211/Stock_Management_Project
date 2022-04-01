package com.mks.sageit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mks.sageit.model.Item;

@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Integer> {
	Item findByItemname(String name);

	@Query(value = "select * from items i where i.item_id = ?1", nativeQuery = true)
	Item findByItemID(int id);

}
