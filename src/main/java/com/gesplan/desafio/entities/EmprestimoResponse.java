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
    /*private Double saldoDevedor;
    private String consolidado;
    private Double total;
    private Double amortizacao;
    private Double saldo;*/
    //private Double provisao;
    /*private Double acumulado;
    private Double pago;*/
    private Integer qtdParcelas;

}
