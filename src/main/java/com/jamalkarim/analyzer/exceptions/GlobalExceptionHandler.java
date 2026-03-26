package com.jamalkarim.analyzer.exceptions;


import com.jamalkarim.analyzer.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * Intercepts exceptions thrown by controllers and maps them to appropriate
 * API responses and HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PlayerNotFoundException and returns a 404 NOT FOUND status.
     *
     * @param ex The exception thrown when a player is not found
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePlayerNotFound(PlayerNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Generic exception handler for all other unhandled exceptions.
     * Returns a 500 INTERNAL SERVER ERROR status.
     *
     * @param ex The generic exception
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles TeamNotFoundException and returns a 404 NOT FOUND status.
     *
     * @param ex The exception thrown when a team is not found
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTeamNotFound(TeamNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles TeamAlreadyExistsException and returns a 409 CONFLICT status.
     *
     * @param ex The exception thrown when a team already exists
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleTeamAlreadyExists(TeamAlreadyExistsException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles PlayerAlreadyRosteredException and returns a 409 CONFLICT status.
     *
     * @param ex The exception thrown when a player is already assigned to another team
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(PlayerAlreadyRosteredException.class)
    public ResponseEntity<ApiResponse<Void>> handlePlayerAlreadyExists(PlayerAlreadyRosteredException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles MatchupNotFoundException and returns a 404 NOT FOUND status.
     *
     * @param ex The exception thrown when a matchup is not found
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(MatchupNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMatchupNotFound(MatchupNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidRosterException and returns a 400 BAD REQUEST status.
     *
     * @param ex The exception thrown when a roster is invalid
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(InvalidRosterException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRoster(InvalidRosterException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidMatchupException and returns a 400 BAD REQUEST status.
     */
    @ExceptionHandler(InvalidMatchupException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidMatchup(InvalidMatchupException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ExternalDataMappingException and returns a 500 INTERNAL SERVER ERROR status.
     */
    @ExceptionHandler(ExternalDataMappingException.class)
    public ResponseEntity<ApiResponse<Void>> handleMappingFailure(ExternalDataMappingException ex) {
        return new ResponseEntity<>(ApiResponse.error("Data Mapping Error: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles PlayerSyncException and returns a 500 INTERNAL SERVER ERROR status.
     *
     * @param ex The exception thrown when player syncing fails
     * @return A ResponseEntity containing an error ApiResponse
     */
    @ExceptionHandler(PlayerSyncException.class)
    public ResponseEntity<ApiResponse<Void>> handlePlayerSyncFailure(PlayerSyncException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles Type Mismatch exceptions (e.g., invalid ENUM values in @RequestParam).
     * Returns a 400 BAD REQUEST status.
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'.", ex.getValue(), ex.getName());
        return new ResponseEntity<>(ApiResponse.error(message), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles 404 No Resource Found exceptions (e.g., static resources or invalid paths).
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles Illegal Argument exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
