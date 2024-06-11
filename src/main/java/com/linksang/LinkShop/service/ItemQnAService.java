package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnA;
import com.linksang.LinkShop.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemQnAService {

    ItemQnA findById(Long qnaId);

    Long countByItem(Item item);

    Long getLastQnAId(List<ItemQnADto> qnaList, Long lastQnAId);

    List<ItemQnADto> searchAllByItem(Item item, Pageable pageable);

    List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable);

    List<ItemQnADto> edit(List<ItemQnADto> qnaList);

    Long save(ItemQnADto dto, Long itemId);

    void deleteAllById(List<Long> idList);

}
