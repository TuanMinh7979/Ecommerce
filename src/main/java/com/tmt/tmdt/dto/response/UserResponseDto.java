package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDto  {
    private Long id;
    private String username;
    private String email;
    private String imageLink;

}
