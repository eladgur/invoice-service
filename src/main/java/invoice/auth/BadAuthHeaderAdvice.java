package invoice.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class BadAuthHeaderAdvice {

    @ResponseBody
    @ExceptionHandler(BadAuthHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String BadAuthHeaderAdviceHandler(BadAuthHeaderException ex) {
        return ex.getMessage();
    }
}