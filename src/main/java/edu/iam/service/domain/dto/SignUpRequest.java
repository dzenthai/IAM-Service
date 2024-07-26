package edu.iam.service.domain.dto;

import jakarta.validation.constraints.Email;

/**
 * Registration form
 */

public record SignUpRequest
        (
                String username,
                @Email(message = "Email address must be in the format user@example.com")
                String email,
                String password,
                String confirmPassword
        )
{}
