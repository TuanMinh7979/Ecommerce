package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientFilterResponseDto {
    private String optionCode;

    private String optionClientName;
    private String optionValue;
}
