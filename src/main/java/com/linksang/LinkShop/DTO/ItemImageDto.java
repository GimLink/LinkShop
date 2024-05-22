package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDto {

    private Long id;

    private Item item;

    private String imageUrl;

    private String imageName;
}
