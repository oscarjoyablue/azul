package sv.com.dte.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CustomerException(String message){
        super(message);
    }
    
    public CustomerException(String message,Throwable cause){
        super(message,cause);
    }
}