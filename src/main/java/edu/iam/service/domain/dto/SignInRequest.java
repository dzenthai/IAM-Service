package edu.iam.service.domain.dto;

/**
 * Login form
 */

public record SignInRequest
        (
                String username,
                String password
        )
{}
