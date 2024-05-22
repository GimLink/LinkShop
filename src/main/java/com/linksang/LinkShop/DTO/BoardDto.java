package com.linksang.LinkShop.DTO;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private Member member;

    @NotBlank(message = "제목을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "제목은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String title;

    @NotBlank(message = "본문 내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 4096, message = "본문 내용은 최대 4096자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    private String writer;

    @NotBlank(message = "비공개 여부를 설정해주세요", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "최대 길이는 10자입니다.", groups = ValidationGroups.LengthGroup.class)
    @Pattern(regexp = "private|public", message = "비공개여부를 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
