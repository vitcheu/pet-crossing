
package com.vitcheu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =HttpStatus.OK)
public class UnsupportedOperationException extends RuntimeException{
    public UnsupportedOperationException(String message){
        super(message);
    }

    
}
