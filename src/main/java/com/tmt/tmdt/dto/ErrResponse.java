package com.tmt.tmdt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrResponse {

    private Meta meta;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Meta {
        private Object message;
    }

    public static ErrResponse error(final Object message) {
        ErrResponse baseResponse = new ErrResponse();
        baseResponse.setMeta(new Meta(message));
        return baseResponse;
    }

}
