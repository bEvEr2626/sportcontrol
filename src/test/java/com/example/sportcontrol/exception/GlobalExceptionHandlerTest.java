package com.example.sportcontrol.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
}
