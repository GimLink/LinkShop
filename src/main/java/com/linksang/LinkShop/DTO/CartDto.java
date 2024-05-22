package com.linksang.LinkShop.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

    @NotNull(message = "정확한 값을 입력해주세요.")
    @Positive(message = "정확한 상품 번호를 입력해주세요.")
    private Long id;

    @Positive(message = "최소 개수는 1개입니다.")
    @Max(value = 100, message = "최대 개수는 100개입니다.")
    private int quantity;
}
