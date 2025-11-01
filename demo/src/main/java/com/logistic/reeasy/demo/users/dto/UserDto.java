package com.logistic.reeasy.demo.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullname;
    private String email;
    private String address;
    private int points;
}
