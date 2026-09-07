package com.auth.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponse {
    private String token;
    private String type;
    private String validUntil;
}
