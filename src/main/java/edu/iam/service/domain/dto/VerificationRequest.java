package edu.iam.service.domain.dto;

public record VerificationRequest
        (
                String email,
                String code
        )
{}
