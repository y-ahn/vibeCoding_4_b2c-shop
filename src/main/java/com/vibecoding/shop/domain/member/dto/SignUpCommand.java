package com.vibecoding.shop.domain.member.dto;

import jakarta.validation.constraints.*;

public record SignUpCommand(
    @NotBlank @Email String email,
    @NotBlank @Size(min=8, max=20) String password,
    @NotBlank String name
) {}
