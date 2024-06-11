package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnA;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemQnAReplyDto {

    private Long id;
    private Item item;
    private Member member;
    private ItemQnA itemQnA;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 2048, message = "최대 길이는 2048자입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "public|private", message = "비공개여부를 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;

    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
