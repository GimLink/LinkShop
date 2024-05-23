package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.exception.ItemNotFoundException;
import com.linksang.LinkShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public String getLastId(List<ItemDto> itemList, String sort, String value) {

        if(itemList.isEmpty()) return value;

        String lowPrice = "lowPrice";
        if (lowPrice.equals(sort)) {
            return itemList.stream()
                    .max(Comparator.comparingInt(ItemDto::getPrice))
                    .get().getPrice().toString();
        }else{
            return itemList.stream()
                    .min(Comparator.comparingLong(ItemDto::getId))
                    .get().getId().toString();
        }
    }

    @Override
    public Long getLastId(List<ItemDto> itemList, Long lastId) {
        if (itemList.isEmpty()) {
            return lastId;
        }else{
            return itemList.stream()
                    .min(Comparator.comparingLong(ItemDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("해당하는 상품이 존재하지 않습니다. itemId : " + itemId));
    }

    @Override
    public Item saveItem(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws IOException {
        return null;
    }


    @Override
    public Long searchTotal(String itemName, String category, String saleStatus) {
        return null;
    }

    @Override
    public List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemDto> searchAllNoOffset(String category, Long itemId, Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemImageDto> searchAllItemImage(Item item) {
        return null;
    }
}
