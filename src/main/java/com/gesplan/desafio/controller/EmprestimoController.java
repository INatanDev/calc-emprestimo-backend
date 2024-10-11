package com.gesplan.desafio.controller;

import com.gesplan.desafio.entities.Emprestimo;
import com.gesplan.desafio.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping("/calcular")
    public ResponseEntity<List<Emprestimo>> calcular() {
        List<Emprestimo> listagem= emprestimoService.calcularEmprestimo();
        return ResponseEntity.ok().body(listagem);
    }

}
