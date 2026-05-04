package org.example.breakoutdrop.Responses;

import java.time.LocalDateTime;

public record AllErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message

) { }
