package com.test.dtos;

import lombok.Data;

@Data
public class UserDetailDto {
    private long id;
    private String username;
    private String password;
    private String creditCard;
}
