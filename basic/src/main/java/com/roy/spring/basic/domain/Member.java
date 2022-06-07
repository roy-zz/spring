package com.roy.spring.basic.domain;

import com.roy.spring.basic.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Member {
    private Long id;
    private String name;
    private Grade grade;
}
