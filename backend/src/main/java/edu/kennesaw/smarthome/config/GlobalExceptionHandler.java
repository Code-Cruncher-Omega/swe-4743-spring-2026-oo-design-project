package edu.kennesaw.smarthome.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String BASE_URI = "https://kennesaw.edu/smarthome/problems";

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        LOG.error("Invalid argument: {}", e.getMessage(), e);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create(BASE_URI + "/invalid-request"));
        problem.setTitle("Invalid Request");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException e) {
        LOG.error("Invalid state: {}", e.getMessage(), e);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(URI.create(BASE_URI + "/conflict"));
        problem.setTitle("Conflict");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handleNullPointer(NullPointerException e) {
        LOG.error("Null pointer exception: {}", e.getMessage(), e);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create(BASE_URI + "/internal-error"));
        problem.setTitle("Internal Server Error");
        problem.setDetail("An unexpected error occurred. Please try again later.");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception e) {
        LOG.error("Unexpected error: {}", e.getMessage(), e);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create(BASE_URI + "/internal-error"));
        problem.setTitle("Internal Server Error");
        problem.setDetail("An unexpected error occurred. Please try again later.");
        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                                    MethodArgumentNotValidException e,
                                    HttpHeaders headers,
                                    HttpStatusCode status,
                                    WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        String errors = e.getBindingResult()
                .getFieldErrors()
                        .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                        .collect(Collectors.joining(", "));
        problem.setDetail(errors);
        return ResponseEntity.badRequest().body(problem);
    }
}