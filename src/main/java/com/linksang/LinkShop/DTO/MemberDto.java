package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.enums.Sns;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userId;
    private String password;
    private Role role;
    private Sns sns;
    private String phoneNum;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
}
