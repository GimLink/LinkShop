package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "카테고리는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "top|bottom|cap", message = "정확한 카테고리를 입력해주세요.", groups = ValidationGroups.PatternGroup.class)
    @Length(max = 10, groups = ValidationGroups.LengthGroup.class)
    private String category;

    @NotBlank(message = "상품명을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "상품명은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String itemName;

    @NotNull(message = "가격을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @PositiveOrZero(message = "0원 이상만 가능합니다.", groups = ValidationGroups.PositiveOrZero.class)
    private Integer price;

    @NotBlank(message = "색상을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "색상 이름은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String color;

    @NotBlank(message = "사이즈를 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "사이즈는 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String size;

    @Length(max = 2048, message = "상품 정보의 내용은 최대 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String itemInfo;

    @Length(max = 100, message = "모델 정보의 내용은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String model;

    @NotBlank(message = "제품 상태를 선택해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "onsale|soldout", message = "정확한 제품 상태를 선택해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String saleStatus;

    private String imageUrl;

    private String noOffset;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private int quantity;

    private int totalPrice;

}
