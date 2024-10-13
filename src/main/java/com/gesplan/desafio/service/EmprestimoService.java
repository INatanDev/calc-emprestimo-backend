package com.gesplan.desafio.service;

import com.gesplan.desafio.entities.EmprestimoRequest;
import com.gesplan.desafio.entities.EmprestimoResponse;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

@Service
public class EmprestimoService {
    public static int j = 0;
    private final HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BRAZIL);

    public List<EmprestimoResponse> calcularEmprestimo(EmprestimoRequest dadosCalculo) {
        int i = 0;
        List<EmprestimoResponse> emprestimos = new ArrayList<>();

        //inicializacao variaveis
        LocalDate dataCompetencia = dadosCalculo.getDataInicial();
        Double saldoDevedor = dadosCalculo.getValorEmprestimo();
        String consolidado = null;
        Double total = 0.00;
        Double valorAmortizacao = 0.00;
        Double saldo = dadosCalculo.getValorEmprestimo();
        Double valorProvisao = 0.00;
        Double valorAcumulado = 0.00;
        Double valorPago = 0.00;
        Integer qtdParcelas = calcularQtdParcelas(dadosCalculo.getDataInicial(), dadosCalculo.getDataFinal());
        Integer baseDias = 360;

        LocalDate dataPagamento = dadosCalculo.getDataPrimeiroPagamento();

        while(i <= 20) {

            if(i == 0) { //primeira linha só retornar os dados
                emprestimos.add(new EmprestimoResponse(dataCompetencia,
                                                       saldoDevedor,
                                                       consolidado,
                                                       total,
                                                       valorAmortizacao,
                                                       saldo,
                                                       valorProvisao,
                                                       valorAcumulado,
                                                       valorPago,
                                                       qtdParcelas,
                                                       baseDias
                                                       ));
            } else {
                LocalDate dataAnterior = dataCompetencia;
                Double vlrAcumuladoAnterior = valorAcumulado;

                dataCompetencia = calcularCompetencia(dataCompetencia, dataPagamento, dadosCalculo.getDataInicial());
                consolidado = isConsolidado(dataCompetencia, dataPagamento, qtdParcelas);
                if(consolidado != null) {
                    dataPagamento = dataCompetencia;
                }
                valorAmortizacao = calcularAmortizacao(consolidado, dadosCalculo.getValorEmprestimo(), qtdParcelas);
                valorProvisao = calcularProvisao(dataCompetencia, dataAnterior, saldo, valorAcumulado, dadosCalculo.getTaxaJuros());
                saldo = saldo - valorAmortizacao;
                valorPago = calcularValorPagamento(consolidado, vlrAcumuladoAnterior, valorProvisao);
                valorAcumulado = calcularAcumulado(consolidado, valorProvisao, vlrAcumuladoAnterior, valorPago);

                emprestimos.add(new EmprestimoResponse(dataCompetencia,
                        Double.parseDouble((String.format("%.2f", saldo + valorAcumulado)).replace(",", ".")), //saldo devedor
                        consolidado,
                        Double.parseDouble((String.format("%.2f", valorAmortizacao + valorPago)).replace(",", ".")), //total
                        Double.parseDouble((String.format("%.2f", valorAmortizacao)).replace(",", ".")),
                        Double.parseDouble((String.format("%.2f", saldo)).replace(",", ".")),
                        Double.parseDouble((String.format("%.2f", valorProvisao)).replace(",", ".")),
                        Double.parseDouble((String.format("%.2f", valorAcumulado)).replace(",", ".")),
                        Double.parseDouble((String.format("%.2f", valorPago)).replace(",", ".")),
                        qtdParcelas,
                        baseDias));
            }
            i++;
        }

        return emprestimos;
    }

    private LocalDate calcularCompetencia(LocalDate dtCompetencia, LocalDate dtPrimeiroPagto, LocalDate dtInicial) {

        boolean mesmoMesInicial = dtCompetencia.getMonth() == dtInicial.getMonth();
        LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth()); //verifica se o dtCompetencia ja nao é o ultimo dia do mes
        LocalDate dtPagto = calcularDataPagamento(dtCompetencia, dtPrimeiroPagto);

        if(mesmoMesInicial && dtCompetencia.equals(ultimoDiaMes)) {
            return dtPrimeiroPagto;
        } else if(!dtCompetencia.equals(ultimoDiaMes)) {
            return ultimoDiaMes;
        } else {
            return dtPagto;
        }
    }

    private Double calcularAmortizacao(String consolidado, Double vlrEmprestimo, Integer qtdParcelas) {
        if(consolidado != null) {
            return vlrEmprestimo / qtdParcelas;
        } else {
            return 0.00;
        }
    }

    private Double calcularProvisao(LocalDate dtCompetencia, LocalDate dtAnterior, Double saldo, Double acumulado, Double txJuros) {

        Period periodo = Period.between(dtAnterior, dtCompetencia);
        Double dias = (periodo.getDays() / 360.00);

        Double formulaProvisao = ((Math.pow(txJuros + 1, dias)) - 1) * (saldo + acumulado);

        return formulaProvisao;
    }

    private Double calcularAcumulado(String consolidado, Double vlrProvisao, Double vlrAcumuladoAnterior, Double vlrPago) {
        if(consolidado != null) {
            return 0.00;
        } else {
            return vlrAcumuladoAnterior + vlrProvisao - vlrPago;
        }
    }

    private Double calcularValorPagamento(String consolidado, Double vlrProvisao, Double vlrAcumulado) {
        if(consolidado != null) {
            return vlrProvisao + vlrAcumulado;
        } else {
            return 0.00;
        }
    }

    private Integer calcularQtdParcelas(LocalDate dtInicial, LocalDate dtFinal) {

        Period periodo = Period.between(dtInicial, dtFinal);
        Integer qtdParcelas = periodo.getYears() * 12 + periodo.getMonths();

        return qtdParcelas;
    }

    public LocalDate calcularDataPagamento(LocalDate dtCompetencia, LocalDate dtPrimeiroPagto) {
        LocalDate newDtPagto = dtPrimeiroPagto.plusMonths(1);

        while (isDiaNaoUtil(newDtPagto)) {
            newDtPagto = newDtPagto.plusDays(1);
        }

        return newDtPagto;
    }

    public String isConsolidado (LocalDate dtCompetencia, LocalDate dtPagtoAnterior, Integer qtdParcelas){
        LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth());
        if(dtCompetencia.getDayOfMonth() == dtPagtoAnterior.getDayOfMonth()) {
            j++;
            return j + "/" + qtdParcelas;
        } else if (dtCompetencia != ultimoDiaMes) {
            j++;
            return j + "/" + qtdParcelas;
        } else {
            return null;
        }
    }


    public boolean isDiaNaoUtil(LocalDate data) {
        boolean isFeriado = manager.isHoliday(data);
        boolean isFinalDeSemana = data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY;

        return isFeriado || isFinalDeSemana;
    }
}
