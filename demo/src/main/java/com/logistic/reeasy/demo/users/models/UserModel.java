package com.logistic.reeasy.demo.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long user_id;
    private String name;
    private String lastname;
    private String email;
    private String addres;
}
