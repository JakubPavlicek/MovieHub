package com.moviehub.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INVALID_VALUE = "Invalid Value";
    private static final String CONTEXT_INFO = "contextInfo";

    @ExceptionHandler({FilterException.class, SortException.class, ParseException.class})
    public ProblemDetail handleFilterAndSortAndParseException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle(INVALID_VALUE);

        return problemDetail;
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFoundException(MovieNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Movie Not Found");

        return problemDetail;
    }

    @ExceptionHandler(DirectorNotFoundException.class)
    public ProblemDetail handleDirectorNotFoundException(DirectorNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Director Not Found");

        return problemDetail;
    }

    @ExceptionHandler(DirectorAlreadyExistsException.class)
    public ProblemDetail handleDirectorAlreadyExistsException(DirectorAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Director Already Exists");

        return problemDetail;
    }

    @ExceptionHandler(ActorNotFoundException.class)
    public ProblemDetail handleActorNotFoundException(ActorNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Actor Not Found");

        return problemDetail;
    }

    @ExceptionHandler(ActorAlreadyExistsException.class)
    public ProblemDetail handleActorAlreadyExistsException(ActorAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Actor Already Exists");

        return problemDetail;
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ProblemDetail handleGenreNotFoundException(GenreNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Genre Not Found");

        return problemDetail;
    }

    @ExceptionHandler(GenreAlreadyExistsException.class)
    public ProblemDetail handleGenreAlreadyExistsException(GenreAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Genre Already Exists");

        return problemDetail;
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ProblemDetail handleCountryNotFoundException(CountryNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Country Not Found");

        return problemDetail;
    }

    @ExceptionHandler(CountryAlreadyExistsException.class)
    public ProblemDetail handleCountryAlreadyExistsException(CountryAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Country Already Exists");

        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(INVALID_VALUE);
        problemDetail.setProperty(CONTEXT_INFO, getErrors(ex));

        return ResponseEntity.of(problemDetail)
                             .build();
    }

    private Map<String, List<String>> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                 .getFieldErrors()
                 .stream()
                 .collect(Collectors.groupingBy(
                     FieldError::getField,
                     Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                 ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(INVALID_VALUE);
        problemDetail.setProperty(CONTEXT_INFO, getConstraintViolations(ex));

        return problemDetail;
    }

    private Map<String, List<String>> getConstraintViolations(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                 .stream()
                 .collect(Collectors.groupingBy(
                     violation -> ((PathImpl) violation.getPropertyPath()).getLeafNode().toString(),
                     Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                 ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMostSpecificCause().getMessage());
        problemDetail.setTitle(INVALID_VALUE);

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Internal Server Error");

        ex.printStackTrace();
        return problemDetail;
    }

}
