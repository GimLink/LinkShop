package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.util.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    @NotBlank(message = "주문자 이름은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "주문자 이름의 최대 길이는 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String customerName;

    @NotBlank(message = "주문자 전화번호를 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(010[1-9][0-9]{7})$", message = "주문자 전화번호를 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String customerPhoneNum;

    @NotBlank(message = "수령인 이름은 필수입력 값입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "수령인 이름의 최대 길이는 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String recipientName;

    @NotBlank(message = "수령인 전화번호를 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(010[1-9][0-9]{7})$", message = "수령인 전화번호를 확인해주세요.",groups = ValidationGroups.PatternGroup.class)
    private String recipientPhoneNum;

    @Length(max = 6, message = "우편번호의 최대 길이는 6자리입니다.", groups = ValidationGroups.LengthGroup.class)
    private String zipcode;

    @NotBlank(message = "주소를 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 200, message = "주소의 최대 길이는 200자입니다.", groups = ValidationGroups.LengthGroup.class)
    private String address;

    @Length(max = 200, message = "상세주소의 최대 길이는 200자입니다.", groups = ValidationGroups.LengthGroup.class)
    private String detailAddress;

    private Delivery delivery;
}
