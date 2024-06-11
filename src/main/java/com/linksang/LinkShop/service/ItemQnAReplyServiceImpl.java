package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.DTO.ItemQnAReplyDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnA;
import com.linksang.LinkShop.entity.ItemQnAReply;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.repository.QnAReplyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemQnAReplyServiceImpl implements ItemQnAReplyService {

    private final QnAReplyRepository qnAReplyRepository;
    private final MemberService memberService;
    private final ItemQnAService itemQnAService;
    private final ItemService itemService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();


    @Override
    @Transactional
    public Long save(ItemQnAReplyDto replyDto, Long itemId, Long qnaId) {

        Item item = itemService.findById(itemId);
        Member member = memberService.getCurrentMember();
        ItemQnA qna = itemQnAService.findById(qnaId);
        ItemQnAReply qnAReply = mapper.map(replyDto, ItemQnAReply.class);

        qnAReply.setWriter(member.getUserId().substring(0,3) + "***");
        qna.setQnAReply(qnAReply);
        qna.setReplyEmpty(false);
        member.addQnaReplyList(qnAReply);
        item.addQnaReplyList(qnAReply);

        return qnAReplyRepository.save(qnAReply).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> findAllByQnA(List<ItemQnADto> qnaList) {

        List<ItemQnAReplyDto> replyList = new ArrayList<>();
        for (ItemQnADto dto : qnaList) {
            Optional<ItemQnAReply> reply = qnAReplyRepository.findByItemQnAId(dto.getId());
            reply.ifPresent(qnAReply -> replyList.add(mapper.map(qnAReply, ItemQnAReplyDto.class)));
        }
        return replyList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnAReplyDto> edit(List<ItemQnAReplyDto> replyList) {

        if (!replyList.isEmpty()) {
            if (security.isAuthenticated()) {
                replyList.stream()
                        .filter(r -> r.getHide().equals("private"))
                        .filter(r -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(r -> !r.getItemQnA().getMember().getUserId().equals(security.getName()))
                        .forEach(r -> r.setContent("비밀글입니다."));

            } else {
                replyList.stream()
                        .filter(r -> r.getHide().equals("private"))
                        .forEach(r -> r.setContent("비밀글입니다."));
            }
        }
        return replyList;
    }
}
