package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.DTO.ItemQnAReplyDto;

import java.util.List;

public interface ItemQnAReplyService {

    Long save(ItemQnAReplyDto replyDto, Long itemId, Long qnaId);

    List<ItemQnAReplyDto> findAllByQnA(List<ItemQnADto> qnaList);

    List<ItemQnAReplyDto> edit(List<ItemQnAReplyDto> replyList);
}
