package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnAReply;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemQnADto {

    private Long id;
    private Item item;
    private Member member;
    private ItemQnAReply itemQnAReply;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 2048, message = "최대 길이는 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    @NotBlank(message = "비공개 여부를 설정해주세요", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "private|public", message = "비공개여부를 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;

    private String title;
    private String writer;
    private boolean replyEmpty;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
