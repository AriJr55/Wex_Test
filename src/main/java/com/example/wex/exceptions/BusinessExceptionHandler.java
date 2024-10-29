package com.example.wex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BusinessExceptionHandler {

    public static ResponseEntity<Map> throwNoDataError(String message) {
        Map<String,Object> response = new HashMap();
        response.put("Message", message);
        response.put("Exception", BusinessException.class);
        response.put("Status", HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<Map>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static ResponseEntity<Map> throwNoDataError(String message, String country, String currency, LocalDate dateBegin, LocalDate dateEnd) {
        Map<String,Object> response = new HashMap();
        response.put("Message", message);
        response.put("Exception", BusinessException.class);
        response.put("DateBegin", dateEnd);
        response.put("DateEnd", dateBegin);
        response.put("Country", country);
        response.put("Currency", currency);
        response.put("Status", HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<Map>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
