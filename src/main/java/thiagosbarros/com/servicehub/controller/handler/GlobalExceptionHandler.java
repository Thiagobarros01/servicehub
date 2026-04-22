package thiagosbarros.com.servicehub.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import thiagosbarros.com.servicehub.controller.dto.ApiErrorResponse;
import thiagosbarros.com.servicehub.controller.dto.FieldErrorResponse;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.exception.EmpresaNaoEncontradaException;
import thiagosbarros.com.servicehub.exception.ServicoNaoEncontradoException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmpresaNaoEncontradaException.class,
            ClienteNaoEncontradoException.class,
            ServicoNaoEncontradoException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(buildError(status, exception.getMessage(), request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(status).body(buildError(status, exception.getMessage(), request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                             HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<FieldErrorResponse> fields = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldErrorResponse)
                .toList();

        ApiErrorResponse response = buildError(
                status,
                "Request invalido.",
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.status(status).body(response);
    }

    private FieldErrorResponse toFieldErrorResponse(FieldError fieldError) {
        return new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private ApiErrorResponse buildError(HttpStatus status, String message, String path, List<FieldErrorResponse> fields) {
        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fields
        );
    }
}
