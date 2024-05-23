package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    String getLastId(List<ItemDto> itemList, String sort, String value);

    Item findById(Long itemId);

    Item saveItem(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws IOException;

    Long getLastId(List<ItemDto> itemList, Long lastId);

    Long searchTotal(String itemName, String category, String saleStatus);

    List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable);

    List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable);

    List<ItemDto> searchAllNoOffset(String category, Long itemId, Pageable pageable);

    List<ItemImageDto> searchAllItemImage(Item item);
}
