package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ReviewDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Review;
import com.linksang.LinkShop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional(readOnly = true)
    public Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId, String sort) {

        if(reviewList.isEmpty()) return lastReviewId;

        if (sort.equals("old")) {
            return reviewList.stream()
                    .max(Comparator.comparingLong(ReviewDto::getId))
                    .get().getId();
        } else {
            return reviewList.stream()
                    .min(Comparator.comparingLong(ReviewDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional
    public Long saveReview(ReviewDto reviewDto, Long itemId) {

        Member member = memberService.getCurrentMember();
        Item item = itemService.findById(itemId);
        Review review = mapper.map(reviewDto, Review.class);

        review.setWriter(member.getUserId().substring(0,3) + "***");
        item.addReviewList(review);
        member.addReviewList(review);

        return reviewRepository.save(review).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByItem(Item item) {
        return reviewRepository.countByItem(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort) {
        return reviewRepository.searchAll(itemId, lastReviewId, sort);
    }
}
