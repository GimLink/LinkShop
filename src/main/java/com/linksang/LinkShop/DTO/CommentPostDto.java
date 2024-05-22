package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostDto {

    @Positive(message = "정확한 값을 입력하세요.", groups = ValidationGroups.PositiveOrZero.class)
    private Long id;

    @NotBlank(message = "댓글 내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "댓글 내용은 최대 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    @NotBlank(message = "비공개 여부를 설정해주세요", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "최대 길이는 10자입니다.", groups = ValidationGroups.LengthGroup.class)
    @Pattern(regexp = "private|public", message = "비공개여부를 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;
}
