package com.linksang.LinkShop.service;


import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnA;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.exception.QnANotFoundException;
import com.linksang.LinkShop.repository.ItemQnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQnAServiceImpl implements ItemQnAService{

    private final ItemQnARepository qnARepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final SecurityService security;

    @Override
    @Transactional(readOnly = true)
    public ItemQnA findById(Long qnaId) {
        return qnARepository.findById(qnaId).orElseThrow(() -> new QnANotFoundException("해당 QnA는 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByItem(Item item) {
        return qnARepository.countByItem(item);
    }

    @Override
    public Long getLastQnAId(List<ItemQnADto> qnaList, Long lastQnAId) {

        if (qnaList.isEmpty()) {
            return lastQnAId;
        }
        return qnaList.stream().min(Comparator.comparingLong(ItemQnADto::getId))
                .get().getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> searchAllByItem(Item item, Pageable pageable) {
        return qnARepository.searchAllByItem(item, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable) {
        return qnARepository.searchAllByMember(id, member, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemQnADto> edit(List<ItemQnADto> qnaList) {

        if (!qnaList.isEmpty()) {
            if (security.isAuthenticated()) {
                qnaList.stream()
                        .filter(q -> q.getHide().equals("private"))
                        .filter(q -> !q.getMember().getUserId().equals(security.getName()))
                        .filter(q -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .forEach(q -> {
                            q.setContent("비밀글입니다.");
                            q.setTitle("비밀글입니다.");
                        });
            } else {
                qnaList.stream()
                        .filter(q -> q.getHide().equals("private"))
                        .forEach(q -> {
                            q.setContent("비밀글입니다.");
                            q.setTitle("비밀글입니다.");
                        });
            }
        }
        return qnaList;
    }

    @Override
    @Transactional
    public Long save(ItemQnADto dto, Long itemId) {

        Member member = memberService.getCurrentMember();
        Item item = itemService.findById(itemId);

        if(dto.getContent().length() > 30) dto.setTitle(dto.getContent().substring(0, 30) + "...");
        else dto.setTitle(dto.getContent());

        ItemQnA qna = ItemQnA.builder()
                .title(dto.getTitle())
                .content(dto.getContent().replaceAll("\\s+", " "))
                .writer(member.getUserId().substring(0, 3) + "***")
                .hide(dto.getHide())
                .replyEmpty(true)
                .build();
        item.addQnaList(qna);
        member.addQnaList(qna);

        return qnARepository.save(qna).getId();
    }

    @Override
    @Transactional
    public void deleteAllById(List<Long> idList) {
        qnARepository.deleteAllById(idList);
    }
}
