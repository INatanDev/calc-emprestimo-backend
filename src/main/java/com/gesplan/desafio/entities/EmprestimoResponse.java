package com.gesplan.desafio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoResponse {

    private LocalDate dataCompetencia;
    private Double saldoDevedor;
    private String consolidado;
    private Double total;
    private Double amortizacao;
    private Double saldo;
    private Double valorProvisao;
    private Double valorAcumulado;
    private Double valorPago;
    private Integer qtdParcelas;
    private Integer baseDias;

}
