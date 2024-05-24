package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.entity.Item;

import java.util.List;

public interface ItemImageRepositoryCustom {

    List<ItemImageDto> searchAll(Item item);
}
