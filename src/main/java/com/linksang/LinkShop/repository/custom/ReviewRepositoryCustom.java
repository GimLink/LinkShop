package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ReviewDto;

import java.util.List;

public interface ReviewRepositoryCustom {

    List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort);
}
