package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    Long searchTotal(String itemName, String category, String saleStatus);

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    List<ItemDto> searchAllNoOffset(String category, Long id, Pageable pageable);

    List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable);
}
