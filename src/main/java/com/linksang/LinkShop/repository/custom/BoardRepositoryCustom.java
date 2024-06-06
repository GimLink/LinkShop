package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardDto> searchAll(Pageable pageable);
}
