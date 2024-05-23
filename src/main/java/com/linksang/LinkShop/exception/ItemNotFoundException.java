package com.linksang.LinkShop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message) {super(message);}
}
