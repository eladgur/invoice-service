package invoice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
class ConstraintViolationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeNotFoundHandler(ConstraintViolationException ex) {
        return ex.getMessage();
    }
}