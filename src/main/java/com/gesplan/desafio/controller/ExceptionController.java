package com.gesplan.desafio.controller;

import com.gesplan.desafio.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@CrossOrigin(origins = "*")
public class ExceptionController {

    @ExceptionHandler({
            DataNaoUtilException.class,
            DataInvalidaException.class,
            DataPagamentoException.class,
            TaxaJurosException.class,
            ValorEmprestimoException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String gerenciarErros(RuntimeException ex) {
        return ex.getMessage();
    }

}

