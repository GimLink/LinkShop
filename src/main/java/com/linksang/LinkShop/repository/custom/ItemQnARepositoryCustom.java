package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemQnARepositoryCustom {

    List<ItemQnADto> searchAllByItem(Item item, Pageable pageable);

    List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable);
}
