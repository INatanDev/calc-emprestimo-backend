package com.gesplan.desafio.service;

import com.gesplan.desafio.entities.Emprestimo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmprestimoService {

    public List<Emprestimo> calcularEmprestimo() {

        List<Emprestimo> emprestimos = new ArrayList<>();

        emprestimos.add(new Emprestimo(LocalDate.now(), LocalDate.now(), LocalDate.now(), 150.000, 0.07));
        emprestimos.add(new Emprestimo(LocalDate.now(), LocalDate.now(), LocalDate.now(), 120.000, 0.07));
        emprestimos.add(new Emprestimo(LocalDate.now(), LocalDate.now(), LocalDate.now(), 110.000, 0.07));

        return emprestimos;
    }
}
