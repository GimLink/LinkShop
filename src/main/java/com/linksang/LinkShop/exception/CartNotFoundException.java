package com.linksang.LinkShop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String msg) {super(msg);}
}
