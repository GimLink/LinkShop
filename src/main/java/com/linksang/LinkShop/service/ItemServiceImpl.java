package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemImage;
import com.linksang.LinkShop.exception.ItemNotFoundException;
import com.linksang.LinkShop.repository.ItemImageRepository;
import com.linksang.LinkShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final AwsS3Service awsS3Service;

    ModelMapper mapper = new ModelMapper();

    @Override
    public String getLastId(List<ItemDto> itemList, String sort, String value) {

        if(itemList.isEmpty()) return value;

        String lowPrice = "lowPrice";
        if (lowPrice.equals(sort)) {
            return itemList.stream()
                    .max(Comparator.comparingInt(ItemDto::getPrice))
                    .get().getPrice().toString();
        }else{
            return itemList.stream()
                    .min(Comparator.comparingLong(ItemDto::getId))
                    .get().getId().toString();
        }
    }

    @Override
    public Long getLastId(List<ItemDto> itemList, Long lastId) {
        if (itemList.isEmpty()) {
            return lastId;
        }else{
            return itemList.stream()
                    .min(Comparator.comparingLong(ItemDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("해당하는 상품이 존재하지 않습니다. itemId : " + itemId));
    }

    @Override
    @Transactional
    public Item saveItem(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws IOException {

        Item item = mapper.map(itemDto, Item.class);

        List<MultipartFile> fileList = mtfRequest.getFiles("upload_image");
        List<ItemImage> itemImageList = new ArrayList<>();

        if (fileList.size() != 0) {
            for (int i = 0; i < fileList.size(); i++) {
                String originalImageName = fileList.get(i).getOriginalFilename();
                String imageName = awsS3Service.createFileName(originalImageName);

                String filePath = "static/assets/images/" + itemDto.getCategory() + "/" + itemDto.getItemName() + "/" + imageName;

                String s3ImageUrl = awsS3Service.upload(fileList.get(i), filePath);

                if (i==0) item.setImageUrl(s3ImageUrl);

                ItemImage itemImage = ItemImage.builder()
                        .imageUrl(filePath)
                        .imageName(originalImageName)
                        .build();
                itemImageList.add(itemImage);
            }

            for (ItemImage itemImage : itemImageList) {
                item.setItemImageList(itemImage);
            }
        }

        return itemRepository.save(item);
    }


    @Override
    @Transactional(readOnly = true)
    public Long searchTotal(String itemName, String category, String saleStatus) {
        return itemRepository.searchTotal(itemName, category, saleStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable) {

        return itemRepository.searchAllBySort(itemName, sort, value, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {

        return itemRepository.searchAll(searchDto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAllNoOffset(String category, Long itemId, Pageable pageable) {

        return itemRepository.searchAllNoOffset(category, itemId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemImageDto> searchAllItemImage(Item item) {

        List<ItemImageDto> imageDtos = itemImageRepository.searchAll(item);

        for (ItemImageDto itemImageDto : imageDtos) {
            itemImageDto.setImageUrl(awsS3Service.getS3FileUrl(itemImageDto.getImageUrl()));
        }
        return imageDtos;
    }
}
