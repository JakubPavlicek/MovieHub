package com.moviehub.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Global exception handler to handle exceptions across the application.
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /// Common title for invalid value related exceptions.
    private static final String INVALID_VALUE = "Invalid Value";
    /// Property key for storing additional context information in the error responses.
    private static final String CONTEXT_INFO = "contextInfo";
    /// Log error message format for exceptions.
    private static final String LOG_ERROR_MESSAGE = "request {} raised: ";

    /// Retrieves the request URI from the given {@link WebRequest}.
    ///
    /// @param request the {@link WebRequest} from which to retrieve the URI
    /// @return the URI of the request
    private String getRequestURI(WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        return servletWebRequest.getRequest().getRequestURI();
    }

    /// Handles {@link MovieNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFoundException(MovieNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Movie Not Found");

        return problemDetail;
    }

    /// Handles {@link DirectorNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(DirectorNotFoundException.class)
    public ProblemDetail handleDirectorNotFoundException(DirectorNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Director Not Found");

        return problemDetail;
    }

    /// Handles {@link DirectorAlreadyExistsException} and returns a {@link ProblemDetail} with status 409.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 409 status and an appropriate error message
    @ExceptionHandler(DirectorAlreadyExistsException.class)
    public ProblemDetail handleDirectorAlreadyExistsException(DirectorAlreadyExistsException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Director Already Exists");

        return problemDetail;
    }

    /// Handles {@link ActorNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(ActorNotFoundException.class)
    public ProblemDetail handleActorNotFoundException(ActorNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Actor Not Found");

        return problemDetail;
    }

    /// Handles {@link ActorAlreadyExistsException} and returns a {@link ProblemDetail} with status 409.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 409 status and an appropriate error message
    @ExceptionHandler(ActorAlreadyExistsException.class)
    public ProblemDetail handleActorAlreadyExistsException(ActorAlreadyExistsException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Actor Already Exists");

        return problemDetail;
    }

    /// Handles {@link GenreNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(GenreNotFoundException.class)
    public ProblemDetail handleGenreNotFoundException(GenreNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Genre Not Found");

        return problemDetail;
    }

    /// Handles {@link GenreAlreadyExistsException} and returns a {@link ProblemDetail} with status 409.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 409 status and an appropriate error message
    @ExceptionHandler(GenreAlreadyExistsException.class)
    public ProblemDetail handleGenreAlreadyExistsException(GenreAlreadyExistsException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Genre Already Exists");

        return problemDetail;
    }

    /// Handles {@link CountryNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(CountryNotFoundException.class)
    public ProblemDetail handleCountryNotFoundException(CountryNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Country Not Found");

        return problemDetail;
    }

    /// Handles {@link CountryAlreadyExistsException} and returns a {@link ProblemDetail} with status 409.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 409 status and an appropriate error message
    @ExceptionHandler(CountryAlreadyExistsException.class)
    public ProblemDetail handleCountryAlreadyExistsException(CountryAlreadyExistsException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Country Already Exists");

        return problemDetail;
    }

    /// Handles {@link ProductionCompanyNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(ProductionCompanyNotFoundException.class)
    public ProblemDetail handleProductionCompanyNotFoundException(ProductionCompanyNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Production Company Not Found");

        return problemDetail;
    }

    /// Handles {@link ProductionCompanyAlreadyExistsException} and returns a {@link ProblemDetail} with status 409.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 409 status and an appropriate error message
    @ExceptionHandler(ProductionCompanyAlreadyExistsException.class)
    public ProblemDetail handleProductionCompanyAlreadyExistsException(ProductionCompanyAlreadyExistsException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Production Company Already Exists");

        return problemDetail;
    }

    /// Handles {@link CommentNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(CommentNotFoundException.class)
    public ProblemDetail handleCommentNotFoundException(CommentNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Comment Not Found");

        return problemDetail;
    }

    /// Handles {@link ReplyNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(ReplyNotFoundException.class)
    public ProblemDetail handleReplyNotFoundException(ReplyNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Reply Not Found");

        return problemDetail;
    }

    /// Handles {@link UserNotFoundException} and returns a {@link ProblemDetail} with status 404.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 404 status and an appropriate error message
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("User Not Found");

        return problemDetail;
    }

    /// Handles validation errors for invalid method arguments.
    ///
    /// @param ex the {@link MethodArgumentNotValidException} containing validation errors
    /// @param headers the HTTP headers for the response
    /// @param status the HTTP status code to use
    /// @param request the current request
    /// @return a {@link ResponseEntity} containing the validation errors
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        log.error(LOG_ERROR_MESSAGE, getRequestURI(request), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(INVALID_VALUE);
        problemDetail.setProperty(CONTEXT_INFO, getErrors(ex));

        return ResponseEntity.of(problemDetail)
                             .build();
    }

    /// Extracts error messages from the {@link MethodArgumentNotValidException}.
    ///
    /// @param ex the exception containing validation errors
    /// @return a map where the keys are field names and the values are lists of error messages for each field
    private Map<String, List<String>> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                 .getFieldErrors()
                 .stream()
                 .collect(Collectors.groupingBy(
                     FieldError::getField,
                     Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                 ));
    }

    /// Handles {@link ConstraintViolationException} and returns a {@link ProblemDetail} with status 400.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 400 status and a list of constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(INVALID_VALUE);
        problemDetail.setProperty(CONTEXT_INFO, getConstraintViolations(e));

        return problemDetail;
    }

    /// Extracts constraint violation messages from the {@link ConstraintViolationException}.
    ///
    /// @param ex the exception containing constraint violations
    /// @return a map where the keys are field names and the values are lists of violation messages for each field
    private Map<String, List<String>> getConstraintViolations(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                 .stream()
                 .collect(Collectors.groupingBy(
                     violation -> ((PathImpl) violation.getPropertyPath()).getLeafNode().toString(),
                     Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                 ));
    }

    /// Handles errors related to unreadable HTTP messages.
    ///
    /// @param ex the {@link HttpMessageNotReadableException} indicating an unreadable request
    /// @param headers the HTTP headers for the response
    /// @param status the HTTP status code to use
    /// @param request the current request
    /// @return a {@link ResponseEntity} containing an error message related to unreadable content
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        log.error(LOG_ERROR_MESSAGE, getRequestURI(request), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMostSpecificCause().getMessage());
        problemDetail.setTitle(INVALID_VALUE);

        return ResponseEntity.of(problemDetail).build();
    }

    /// Handles any unhandled exceptions by returning a 500 error.
    ///
    /// @param e the exception to handle
    /// @param request the current request
    /// @return a {@link ProblemDetail} with a 500 status and an appropriate error message
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e, HttpServletRequest request) {
        log.error(LOG_ERROR_MESSAGE, request.getRequestURI(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        problemDetail.setTitle("Internal Server Error");

        return problemDetail;
    }

}
