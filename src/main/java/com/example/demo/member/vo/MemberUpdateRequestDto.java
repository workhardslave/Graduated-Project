package com.example.demo.member.vo;


import com.example.demo.member.vo.Role;
import com.example.demo.overlap.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String name;
    private String city;
    private String zipcode;
    private String street;
    private String phone;

    @Builder
    public MemberUpdateRequestDto(String name,  String street, String zipcode, String city, String phone) {
        this.name = name;
        this.city = city;
        this.zipcode = zipcode;
        this.street = street;
        this.phone = phone;
    }
}