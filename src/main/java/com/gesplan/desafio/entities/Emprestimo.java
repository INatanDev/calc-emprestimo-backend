package com.gesplan.desafio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emprestimo {
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private LocalDate dataPrimeiroPagamento;
    private Double valorEmprestimo;
    private Double taxaJuros;

    public Emprestimo(LocalDate dataInicial, LocalDate dataFinal, LocalDate dataPrimeiroPagamento, Double valorEmprestimo, Double taxaJuros) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.dataPrimeiroPagamento = dataPrimeiroPagamento;
        this.valorEmprestimo = valorEmprestimo;
        this.taxaJuros = taxaJuros;
    }

    private Integer qtdParcelas; //meses dataFinal - dataInicial
    private Integer baseDias = 360;

}
