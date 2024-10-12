package com.gesplan.desafio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoRequest {
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private LocalDate dataPrimeiroPagamento;
    private Double valorEmprestimo;
    private Double taxaJuros;
}
