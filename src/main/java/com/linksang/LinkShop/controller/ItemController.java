package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.DTO.ItemQnAReplyDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.repository.ReviewRepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import com.linksang.LinkShop.util.PaginationShowSizeTen;
import com.linksang.LinkShop.util.PaginationShowSizeThree;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Path;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final MemberService memberService;
    private final CommonService commonService;
    private final ReviewRepository reviewRepository;
    private final ItemQnAService qnaService;
    private final ItemQnAReplyService qnAReplyService;
    private final ReviewService reviewService;
    private final ItemService itemService;
    private final SecurityService securityService;
    private final ModelMapper mapper;


    @GetMapping("/category/{category}")
    @ApiOperation(value = "카테고리별 상품 반환")
    public String itemListPage(@PathVariable String category,
                               @RequestParam(name = "lastId", required = false) Long lastId,
                               @RequestParam(name = "more", required = false) String more, Model model) {

        Pageable pageable = PageRequest.ofSize(12);

        List<ItemDto> itemList = itemService.searchAllNoOffset(category, lastId, pageable);
        lastId = itemService.getLastId(itemList, lastId);

        model.addAttribute("itemList", itemList);
        model.addAttribute("lastId", lastId);
        model.addAttribute("category", category);

        if (more != null) return "item/tab/tab_itemMore";
        return "item/itemList";
    }

    @GetMapping("/items/{id}")
    @ApiOperation(value = "상품 상세보기")
    public String itemDetails(@PathVariable Long id, Model model) {

        Item item = itemService.findById(id);
        ItemDto itemDto = mapper.map(item, ItemDto.class);

        List<ItemImageDto> images = itemService.searchAllItemImage(item);

        Long qnaSize = qnaService.countByItem(item);
        Long reviewSize = reviewService.countByItem(item);

        model.addAttribute("item", itemDto);
        model.addAttribute("images", images);
        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("reviewSize", reviewSize);

        return "item/itemDetails";
    }

    @GetMapping("itme/get/qnaList")
    @ApiOperation(value = "상품 상세보기에 QnA html 리턴", notes = "ajax")
    public String getQnaList(@RequestParam(name = "itemId") Long itemId, Model model,
                             @RequestParam(name = "page", defaultValue = "1") int curPage) {

        Item item = itemService.findById(itemId);
        Long qnaSize = qnaService.countByItem(item);

        PaginationShowSizeThree page = new PaginationShowSizeThree(qnaSize, curPage);
        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());

        List<ItemQnADto> qnaList = qnaService.searchAllByItem(item, pageable);
        List<ItemQnAReplyDto> qnaReplyList = qnAReplyService.findAllByQnA(qnaList);
        qnaList = qnaService.edit(qnaList);
        qnaReplyList = qnAReplyService.edit(qnaReplyList);

        model.addAttribute("page", page);
        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("qnaReplyList", qnaReplyList);

        return "item/tab/tab_qna";
    }
}
