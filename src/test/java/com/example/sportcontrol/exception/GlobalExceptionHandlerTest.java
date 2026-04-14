package com.example.sportcontrol.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleNotFoundReturnsNotFoundResponse() {
        ResponseEntity<ErrorResponseDto> response =
            handler.handleNotFound(new NoSuchElementException("Not found"), request);

        assertBaseResponse(response, HttpStatus.NOT_FOUND, "Not found");
    }

    @Test
    void handleIllegalArgumentReturnsBadRequestResponse() {
        ResponseEntity<ErrorResponseDto> response =
            handler.handleIllegalArgument(new IllegalArgumentException("Bad argument"), request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Bad argument");
    }

    @Test
    void handleDataIntegrityViolationReturnsConflictResponse() {
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException("Integrity", new RuntimeException("Unique key violation"));

        ResponseEntity<ErrorResponseDto> response = handler.handleDataIntegrityViolation(exception, request);

        assertBaseResponse(response, HttpStatus.CONFLICT, "Unique key violation");
    }

    @Test
    void handleTypeMismatchReturnsBadRequestResponse() {
        MethodArgumentTypeMismatchException exception =
            new MethodArgumentTypeMismatchException("abc", Long.class, "id", null, null);

        ResponseEntity<ErrorResponseDto> response = handler.handleTypeMismatch(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Invalid value for parameter 'id': abc");
    }

    @Test
    void handleMethodArgumentNotValidReturnsValidationErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "must not be blank"));
        MethodParameter parameter = org.mockito.Mockito.mock(MethodParameter.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponseDto> response = handler.handleMethodArgumentNotValid(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Validation failed");
        assertNotNull(response.getBody());
        assertEquals(Map.of("name", "must not be blank"), response.getBody().getValidationErrors());
    }

    @Test
    void handleBindExceptionReturnsValidationErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "location", "must not be blank"));
        BindException exception = new BindException(bindingResult);

        ResponseEntity<ErrorResponseDto> response = handler.handleBindException(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Validation failed");
        assertNotNull(response.getBody());
        assertEquals(Map.of("location", "must not be blank"), response.getBody().getValidationErrors());
    }

    @Test
    void handleConstraintViolationReturnsValidationErrors() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = (ConstraintViolation<Object>) org.mockito.Mockito.mock(ConstraintViolation.class);
        Path path = org.mockito.Mockito.mock(Path.class);
        when(path.toString()).thenReturn("teamId");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ErrorResponseDto> response = handler.handleConstraintViolation(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Validation failed");
        assertNotNull(response.getBody());
        assertEquals(Map.of("teamId", "must not be null"), response.getBody().getValidationErrors());
    }

    @Test
    void handleBulkOperationReturnsUnprocessableEntityWithFailedItems() {
        Map<String, String> failed = new LinkedHashMap<>();
        failed.put("match_2", "name: must not be blank");

        BulkOperationException exception =
            new BulkOperationException("partial failure", 1, failed);

        ResponseEntity<ErrorResponseDto> response = handler.handleBulkOperation(exception, request);

        assertBaseResponse(response, HttpStatus.UNPROCESSABLE_CONTENT,
            "Some matches were not saved. successCount=1, failedCount=1");
        assertNotNull(response.getBody());
        assertEquals(failed, response.getBody().getValidationErrors());
    }

    @Test
    void handleMessageNotReadableReturnsBadRequest() {
        HttpInputMessage inputMessage = org.mockito.Mockito.mock(HttpInputMessage.class);
        HttpMessageNotReadableException exception =
            new HttpMessageNotReadableException("JSON parse error", inputMessage);

        ResponseEntity<ErrorResponseDto> response = handler.handleMessageNotReadable(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Invalid request body format");
    }

    @Test
    void handleTransactionSystemExceptionReturnsBadRequest() {
        TransactionSystemException exception =
            new TransactionSystemException("transaction failed", new RuntimeException("not-null property references a null value"));

        ResponseEntity<ErrorResponseDto> response = handler.handleTransactionSystemException(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "not-null property references a null value");
    }

    @Test
    void handleUnexpectedRollbackExceptionReturnsBadRequest() {
        UnexpectedRollbackException exception =
            new UnexpectedRollbackException("transaction rolled back due to invalid state");

        ResponseEntity<ErrorResponseDto> response = handler.handleTransactionSystemException(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "transaction rolled back due to invalid state");
    }

    @Test
    void handlePersistenceExceptionReturnsBadRequestWithRootCause() {
        PersistenceException exception =
            new PersistenceException("persistence failed", new RuntimeException("constraint violation at flush"));

        ResponseEntity<ErrorResponseDto> response = handler.handleTransactionSystemException(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "constraint violation at flush");
    }

    @Test
    void handleHandlerMethodValidationReturnsBadRequest() {
        HandlerMethodValidationException exception = org.mockito.Mockito.mock(HandlerMethodValidationException.class);
        when(exception.getMessage()).thenReturn("validation failed in handler");

        ResponseEntity<ErrorResponseDto> response = handler.handleHandlerMethodValidation(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Validation failed");
    }

    @Test
    void handleTransactionSystemExceptionUsesFallbackMessageWhenRootCauseMessageIsNull() {
        Exception exception = new SelfCauseException();

        ResponseEntity<ErrorResponseDto> response = handler.handleTransactionSystemException(exception, request);

        assertBaseResponse(response, HttpStatus.BAD_REQUEST, "Transaction failed due to invalid data");
    }

    @Test
    void handleUnexpectedExceptionReturnsInternalServerError() {
        ResponseEntity<ErrorResponseDto> response =
            handler.handleUnexpectedException(new RuntimeException("boom"), request);

        assertBaseResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
    }

    private void assertBaseResponse(ResponseEntity<ErrorResponseDto> response, HttpStatus status, String message) {
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(status.value(), response.getBody().getStatus());
        assertEquals(status.getReasonPhrase(), response.getBody().getError());
        assertEquals(message, response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    private static final class SelfCauseException extends Exception {
        @Override
        public synchronized Throwable getCause() {
            return this;
        }
    }
}
