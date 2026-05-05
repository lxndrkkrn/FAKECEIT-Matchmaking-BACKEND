package org.example.fakeceit.ExceptionResponse;

import java.time.LocalDateTime;

public record AllErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message

) { }
