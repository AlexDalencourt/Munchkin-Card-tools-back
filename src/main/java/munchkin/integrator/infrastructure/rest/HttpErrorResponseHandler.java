package munchkin.integrator.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.MissingResourceException;

@ControllerAdvice
public class HttpErrorResponseHandler {

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<String> handleFileTypeError(InvalidMediaTypeException imte) {
        return new ResponseEntity<>(imte.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleFileTypeError(IllegalArgumentException iae) {
        return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingResourceException.class)
    public ResponseEntity<String> handleMissionRessourceError(MissingResourceException mre) {
        return new ResponseEntity<>(mre.getMessage(), HttpStatus.NOT_FOUND);
    }
}
