package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindIdDto {

    @NotBlank(message = "번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "010", message = "번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phoneNum1;

    @NotBlank(message = "번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "[0-9]{4}", message = "번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phoneNum2;

    @NotBlank(message = "번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "[0-9]{4}", message = "번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phoneNum3;

    @Pattern(regexp = "[0-9]{6}", message = "인증번호는 6자리 숫자입니다.", groups = ValidationGroups.PatternGroup.class)
    private String authNum;

    public String getNum() {
        return this.phoneNum1 + this.getPhoneNum2() + this.getPhoneNum3();
    }

}
