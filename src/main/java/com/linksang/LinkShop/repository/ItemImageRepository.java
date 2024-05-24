package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.ItemImage;
import com.linksang.LinkShop.repository.custom.ItemImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long>, ItemImageRepositoryCustom {
}
