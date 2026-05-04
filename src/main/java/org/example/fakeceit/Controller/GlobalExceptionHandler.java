package org.example.breakoutdrop.Controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

import org.example.breakoutdrop.Errors.Client.*;
import org.example.breakoutdrop.Errors.ClientHTTP.*;
import org.example.breakoutdrop.Errors.Server.CaseIsEmpty;
import org.example.breakoutdrop.Errors.Server.ImpossibleContract;
import org.example.breakoutdrop.Errors.ServerHTTP.NotImplemented501;
import org.example.breakoutdrop.Errors.ServerHTTP.ServiceUnavailable503;
import org.example.breakoutdrop.Responses.AllErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Hidden
@ControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AllErrorResponse> handleBadRequest400(MethodArgumentNotValidException ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Unauthorized401.class)
    public ResponseEntity<AllErrorResponse> handleUnauthorized401(Unauthorized401 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PaymentRequired402.class)
    public ResponseEntity<AllErrorResponse> PaymentRequired402(PaymentRequired402 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.PAYMENT_REQUIRED.value(),
                "Payment Required",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(Forbidden403.class)
    public ResponseEntity<AllErrorResponse> handleForbidden403(Forbidden403 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFound404.class)
    public ResponseEntity<AllErrorResponse> handleNotFound404(NotFound404 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Conflict409.class)
    public ResponseEntity<AllErrorResponse> handleConflict409(Conflict409 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(TooManyRequests429.class)
//    public ResponseEntity<AllErrorResponse> handleTooManyRequests429(TooManyRequests429 ex) {
//
//        AllErrorResponse errorResponse = new AllErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.TOO_MANY_REQUESTS.value(),
//                "Too Many Requests",
//                ex.getMessage()
//        );
//
//        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
//    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<AllErrorResponse> handleServerError500(Exception ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server Error",
                ex.getMessage()
        );

        log.error("Критическая ошибка сервера: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotImplemented501.class)
    public ResponseEntity<AllErrorResponse> handleNotImplemented501(NotImplemented501 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_IMPLEMENTED.value(),
                "Not Implement",
                ex.getMessage()
        );

        log.warn("Ошибка сервера: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(ServiceUnavailable503.class)
    public ResponseEntity<AllErrorResponse> handleServiceUnavailable503(ServiceUnavailable503 ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                ex.getMessage()
        );

        log.warn("Ошибка сервера: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(NegativeBalance.class)
    public ResponseEntity<AllErrorResponse> handleNegativeBalance(NegativeBalance ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Negative Balance",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectEmail.class)
    public ResponseEntity<AllErrorResponse> handleIncorrectEmail(IncorrectEmail ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Incorrect Email",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<AllErrorResponse> handleIncorrectPassword(IncorrectPassword ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Incorrect Password",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectURL.class)
    public ResponseEntity<AllErrorResponse> handleIncorrectURL(IncorrectURL ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Incorrect URL",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CaseIsEmpty.class)
    public ResponseEntity<AllErrorResponse> handleCaseIsEmpty(CaseIsEmpty ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Case Is Empty",
                ex.getMessage()
        );

        log.warn("Ошибка сервера: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(SameSkin.class)
    public ResponseEntity<AllErrorResponse> handleSameSkin(SameSkin ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Same Skin",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidValue.class)
    public ResponseEntity<AllErrorResponse> handleInvalidValue(InvalidValue ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Value",
                ex.getMessage()
        );

        log.warn("Ошибка пользователя: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImpossibleContract.class)
    public ResponseEntity<AllErrorResponse> handleImpossibleContract(ImpossibleContract ex) {

        AllErrorResponse errorResponse = new AllErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Impossible Contract",
                ex.getMessage()
        );

        log.warn("Ошибка сервера: {}; Сообщение: {}", errorResponse.error(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
