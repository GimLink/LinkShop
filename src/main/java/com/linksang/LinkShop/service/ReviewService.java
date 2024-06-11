package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ReviewDto;
import com.linksang.LinkShop.entity.Item;

import java.util.List;

public interface ReviewService {

    Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId, String sort);

    Long saveReview(ReviewDto reviewDto, Long itemId);

    Long countByItem(Item item);

    List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort);
}
