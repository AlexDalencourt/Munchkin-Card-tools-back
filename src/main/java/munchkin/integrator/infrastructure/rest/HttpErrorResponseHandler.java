package munchkin.integrator.infrastructure.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.MissingResourceException;

@ControllerAdvice
public class HttpErrorResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HttpErrorResponseHandler.class);

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<String> handleFileTypeError(InvalidMediaTypeException imte) {
        LOG.debug(imte.toString());
        return new ResponseEntity<>(imte.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleFileTypeError(IllegalArgumentException iae) {
        LOG.debug(iae.toString());
        return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingResourceException.class)
    public ResponseEntity<String> handleMissionRessourceError(MissingResourceException mre) {
        LOG.debug(mre.toString());
        return new ResponseEntity<>(mre.getMessage(), HttpStatus.NOT_FOUND);
    }
}
