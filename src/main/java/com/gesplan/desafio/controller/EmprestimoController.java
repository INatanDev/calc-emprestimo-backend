package com.gesplan.desafio.controller;

import com.gesplan.desafio.entities.EmprestimoRequest;
import com.gesplan.desafio.entities.EmprestimoResponse;
import com.gesplan.desafio.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping("/calcular")
    //@RequestMapping(value = "/calcular", method = RequestMethod.POST)
    public ResponseEntity<List<EmprestimoResponse>> calcular(@RequestBody EmprestimoRequest emprestimo) {
        List<EmprestimoResponse> listagem= emprestimoService.calcularEmprestimo(emprestimo);
        return ResponseEntity.ok().body(listagem);
    }

}
