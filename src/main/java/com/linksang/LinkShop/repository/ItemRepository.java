package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.repository.custom.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
