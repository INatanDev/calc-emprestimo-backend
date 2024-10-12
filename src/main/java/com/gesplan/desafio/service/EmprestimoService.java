package com.gesplan.desafio.service;

import com.gesplan.desafio.entities.EmprestimoRequest;
import com.gesplan.desafio.entities.EmprestimoResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmprestimoService {

    public List<EmprestimoResponse> calcularEmprestimo(EmprestimoRequest dadosCalculo) {
        int i = 0;
        List<EmprestimoResponse> emprestimos = new ArrayList<>();
        Integer qtdParcelas = calculaQtdParcelas(dadosCalculo.getDataInicial(), dadosCalculo.getDataFinal());
        LocalDate dataCompetencia = dadosCalculo.getDataInicial();
        LocalDate dataPagamento = dadosCalculo.getDataPrimeiroPagamento();

        while(i <= 6) {

            if(i == 0) { //primeira linha só retornar os dados
                emprestimos.add(new EmprestimoResponse(dadosCalculo.getDataInicial(), qtdParcelas));
                System.out.println(i + " " + emprestimos);
            } else {

                dataCompetencia = calcularCompetencia(dataCompetencia, dataPagamento, dadosCalculo.getDataInicial());
                System.out.println("dtCompet" + dataCompetencia);

                emprestimos.add(new EmprestimoResponse(dataCompetencia, qtdParcelas));
                System.out.println(i + " " + emprestimos);
            }
            i++;
        }

        return emprestimos;
    }

    private LocalDate calcularCompetencia(LocalDate dtCompetencia, LocalDate dtPrimeiroPagto, LocalDate dtInicial) {

        boolean mesmoMesInicial = dtCompetencia.getMonth() == dtInicial.getMonth();

        LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth()); //verifica se o dtCompetencia ja nao é o ultimo dia do mes
        LocalDate dtPagamento = dtCompetencia.withDayOfMonth(15);

        if(mesmoMesInicial && dtCompetencia.equals(ultimoDiaMes)) {
            return dtPrimeiroPagto;
        } else if(!dtCompetencia.equals(ultimoDiaMes)) {
            return ultimoDiaMes;
        } else {
            return dtPagamento.plusMonths(1);
        }
    }

    private Integer calculaQtdParcelas(LocalDate dtInicial, LocalDate dtFinal) {

        Period periodo = Period.between(dtInicial, dtFinal);
        Integer qtdParcelas = periodo.getYears() * 12 + periodo.getMonths();

        return qtdParcelas;
    }
}
