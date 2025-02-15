package exception.handler;

import exception.AppError;
import exception.ResourceNotFoundException;
import exception.ResourceSaveFailed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(new AppError(e.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceSaveException(ResourceSaveFailed e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(new AppError(e.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }



}
