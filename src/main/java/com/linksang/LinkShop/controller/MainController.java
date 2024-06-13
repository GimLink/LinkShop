package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.service.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    @ApiOperation(value = "웹 기본 페이지")
    public String index(@RequestParam(name = "category", required = false, defaultValue = "all") String category, Model model) {

        Pageable pageable = PageRequest.ofSize(9);

        List<ItemDto> itemList = itemService.searchAllNoOffset(category, null, pageable);
        Long lastId = itemService.getLastId(itemList, null);

        model.addAttribute("itemList", itemList);
        model.addAttribute("lastId", lastId);
        model.addAttribute("category", category);

        return "index";
    }
}
