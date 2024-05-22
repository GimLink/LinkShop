package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;
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
public class BoardReCommentDto {

    private Long id;
    private Board board;
    private Member member;
    private BoardComment comment;

    @NotBlank(message = "댓글 내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "댓글의 최대 길이는 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    @NotBlank(message = "비공개 여부를 설정해주세요", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "최대 길이는 10자입니다.", groups = ValidationGroups.LengthGroup.class)
    @Pattern(regexp = "private|public", message = "비공개여부를 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;

    private String writer;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
