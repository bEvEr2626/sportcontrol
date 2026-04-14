package com.example.sportcontrol.exception;

import com.example.sportcontrol.dto.ErrorResponseDto;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception ex, HttpServletRequest request) {
        LOG.warn("Not found error on path={} message={}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
        IllegalArgumentException ex,
        HttpServletRequest request
    ) {
        LOG.warn("Illegal argument on path={} message={}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String message = ex.getMostSpecificCause().getMessage();
        LOG.warn("Data integrity violation on path={} message={}", request.getRequestURI(), message);
        return buildErrorResponse(HttpStatus.CONFLICT, message, request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request
    ) {
        String message = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue();
        LOG.warn("Type mismatch on path={} message={}", request.getRequestURI(), message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        LOG.warn("Validation failed on path={} errors={}", request.getRequestURI(), errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request, errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        LOG.warn("Bind validation failed on path={} errors={}", request.getRequestURI(), errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request, errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleHandlerMethodValidation(
        HandlerMethodValidationException ex,
        HttpServletRequest request
    ) {
        LOG.warn("Handler method validation failed on path={} message={}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        LOG.warn("Constraint violation on path={} errors={}", request.getRequestURI(), errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        String message = "Invalid request body format";
        LOG.warn("Malformed request body on path={} message={}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler({
        TransactionSystemException.class,
        UnexpectedRollbackException.class,
        TransactionException.class,
        JpaSystemException.class,
        PersistenceException.class
    })
    public ResponseEntity<ErrorResponseDto> handleTransactionSystemException(
        Exception ex,
        HttpServletRequest request
    ) {
        Throwable rootCause = findRootCause(ex);
        String message = rootCause.getMessage() == null
            ? "Transaction failed due to invalid data"
            : rootCause.getMessage();
        LOG.warn("Transactional error on path={} message={}", request.getRequestURI(), message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(BulkOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleBulkOperation(
        BulkOperationException ex,
        HttpServletRequest request
    ) {
        String message = "Some matches were not saved. successCount=%d, failedCount=%d"
            .formatted(ex.getSuccessCount(), ex.getFailedItems().size());
        LOG.warn("Bulk operation partially failed on path={} errors={}", request.getRequestURI(), ex.getFailedItems());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_CONTENT, message, request, ex.getFailedItems());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        LOG.error("Unexpected error on path={}", request.getRequestURI(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected server error", request, null);
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
        HttpStatus status,
        String message,
        HttpServletRequest request,
        Map<String, String> validationErrors
    ) {
        ErrorResponseDto response = new ErrorResponseDto(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            validationErrors
        );
        return ResponseEntity.status(status).body(response);
    }

    private Throwable findRootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }
}
