/*
* DTO is short of Data Transfer Object, which is used to transfer data between front and back end and provide
* an interface to the outside.
* DTO is just a POJO, but plays role in transfer some organized data
* */
package com.example.mystoreapidev.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserDTO {
    private Integer id;

    @NotBlank(message = "email can't be empty")
    private String email;
    @NotBlank(message = "phone can't be empty")
    private String phone;

    private String question;
    private String answer;
}
