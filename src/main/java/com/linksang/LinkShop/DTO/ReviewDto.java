package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private Item item;
    private Member member;
    private String writer;

    @NotBlank(message = "본문 내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 2048, message = "본문 내용의 최대 길이는 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
