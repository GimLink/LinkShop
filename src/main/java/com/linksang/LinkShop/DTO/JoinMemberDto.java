package com.linksang.LinkShop.DTO;


import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMemberDto {

    private Long id;

    @NotBlank(message = "아이디는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "5~20자의 영문 소문자, 숫자만 사용 가능합니다.(띄어쓰기 불가능)", groups = ValidationGroups.PatternGroup.class)
    private String userId;

    @NotBlank(message = "패스워드는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9~`!@#$%\\^&*()-]{8,25}$", message = "8~25자리 영문 소문자, 숫자, 특수문자를 사용하세요. (띄어쓰기 불가)", groups = ValidationGroups.PatternGroup.class)
    private String password;

    @NotBlank(message = "패스워드를 다시 작석해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    private String pswdCheck;

    @NotBlank(message = "전화번호는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(010[1-9][0-9]{7})$", message = "형식에 맞지 않는 전화번호입니다.", groups = ValidationGroups.PatternGroup.class)
    private String phoneNum;

    @NotBlank(message = "인증번호를 입력하세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String authNum;
}
