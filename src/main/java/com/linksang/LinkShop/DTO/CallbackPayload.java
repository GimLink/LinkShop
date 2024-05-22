package com.linksang.LinkShop.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallbackPayload {

    private String secret;
    private String status;
    private String orderId;

}
