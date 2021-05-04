package munchkin.integrator.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpErrorResponseHandler {

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<InvalidMediaTypeException> handleFileTypeError(InvalidMediaTypeException imte) {
        return new ResponseEntity<>(imte, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleFileTypeError(IllegalArgumentException iae) {
        return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
