package edu.iam.service.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationException {

    private String message;
    private int status;
    private Date timestamp;

    public ApplicationException(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = new Date();
    }
}
