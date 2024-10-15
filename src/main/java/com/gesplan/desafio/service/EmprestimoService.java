package com.gesplan.desafio.service;

import com.gesplan.desafio.entities.EmprestimoRequest;
import com.gesplan.desafio.entities.EmprestimoResponse;
import com.gesplan.desafio.exceptions.*;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

@Service
public class EmprestimoService {
    public static int parcelaAtual = 0;
    private final HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.BRAZIL);

    public List<EmprestimoResponse> calcularEmprestimo(EmprestimoRequest dadosCalculo) {
        parcelaAtual = 0;

        validaDados(dadosCalculo);

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
        Integer diaPagto = dadosCalculo.getDataPrimeiroPagamento().getDayOfMonth();
        Double taxaJuros = dadosCalculo.getTaxaJuros() / 100;

        //cria primeiro registro
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
        while(parcelaAtual != qtdParcelas) {
            LocalDate dataAnterior = dataCompetencia;
            Double vlrAcumuladoAnterior = valorAcumulado;

            dataCompetencia = calcularCompetencia(dataCompetencia, dataPagamento, dadosCalculo.getDataInicial(), dadosCalculo.getDataFinal(), dadosCalculo.getDataPrimeiroPagamento(), diaPagto, qtdParcelas);
            consolidado = isConsolidado(dataCompetencia, dataPagamento, qtdParcelas, dadosCalculo.getDataInicial(), dadosCalculo.getDataPrimeiroPagamento());
            if(consolidado != null) {
                dataPagamento = dataCompetencia;
            }
            valorAmortizacao = calcularAmortizacao(consolidado, dadosCalculo.getValorEmprestimo(), qtdParcelas);
            valorProvisao = calcularProvisao(dataCompetencia, dataAnterior, saldo, valorAcumulado, taxaJuros);
            saldo = saldo - valorAmortizacao;
            if(saldo < 0) {
                saldo = 0.0;
            }
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
        return emprestimos;
    }

    public LocalDate calcularCompetencia(LocalDate dtCompetencia, LocalDate dtPagto, LocalDate dtInicial, LocalDate dtFinal, LocalDate dtPrimeiroPgto, Integer diaPagto, Integer qtdParcelas) {

        boolean mesmoMesInicial = dtCompetencia.getMonth() == dtInicial.getMonth();
        LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth()); //verifica se o dtCompetencia ja nao é o ultimo dia do mes
        dtPagto = calcularDataPagamento(dtPagto, dtPrimeiroPgto, diaPagto);

        if(mesmoMesInicial && dtCompetencia.equals(ultimoDiaMes)) {
            return dtPagto;
        } else if(!dtCompetencia.equals(ultimoDiaMes)) {
            return ultimoDiaMes;
        } else if(dtPagto.isAfter(dtFinal) && (parcelaAtual + 1) == qtdParcelas) { //garantir que não passe da dataFinal
            return dtFinal;
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

        return (formulaProvisao >= 0) ? formulaProvisao : 0.00;
    }

    private Double calcularAcumulado(String consolidado, Double vlrProvisao, Double vlrAcumuladoAnterior, Double vlrPago) {
        if(consolidado != null) {
            return 0.00;
        } else {
            Double totalAcumulado = vlrAcumuladoAnterior + vlrProvisao - vlrPago;
            return (totalAcumulado >= 0) ? totalAcumulado : 0.00;
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

    public LocalDate calcularDataPagamento(LocalDate dtPgto, LocalDate dtPrimeiroPagto, Integer diaPagto) {
        LocalDate newDtPagto;

        if(parcelaAtual == 0) {
            return dtPgto;
        } else if(diaPagto == dtPgto.getDayOfMonth()) {
            newDtPagto = dtPgto.plusMonths(1);
        } else {
            newDtPagto = dtPgto.plusMonths(1);
        }

        //verificacao exclusiva quando datas de pagamento sao no ultimo dia do mes
        try {
            newDtPagto = newDtPagto.withDayOfMonth(diaPagto);
        } catch (DateTimeException e) {
            Integer ultimoDiaMes = newDtPagto.lengthOfMonth();
            while(ultimoDiaMes != newDtPagto.getDayOfMonth()) {
                newDtPagto = newDtPagto.plusDays(1);
            }
        }

        //verifica se é um dia útil
        while (isDiaNaoUtil(newDtPagto)) {
            newDtPagto = newDtPagto.plusDays(1);
        }

        //verificar se a nova data de pagamento, nao acabou ultrapassando o mes de pagamento e corrigir
        if(newDtPagto.getMonth() != dtPgto.plusMonths(1).getMonth()) {
            newDtPagto = newDtPagto.minusDays(1);

            while (isDiaNaoUtil(newDtPagto)) {
                newDtPagto = newDtPagto.minusDays(1);
            }
        }

        return newDtPagto;
    }

    public String isConsolidado (LocalDate dtCompetencia, LocalDate dtPagtoAnterior, Integer qtdParcelas, LocalDate dtInicial, LocalDate dtPrimeiroPagto){
        LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth());
        LocalDate ultimoDiaMesPagtoAnterior = dtPagtoAnterior.withDayOfMonth(dtPagtoAnterior.lengthOfMonth());

        // Verifica se a data de competencia é anterior ao primeiro pagamento
        if(dtCompetencia.isBefore(dtPrimeiroPagto)) {
            return null;
        }
        // Verifica se o dia da competência coincide com o dia do último pagamento e não é o mesmo mês que dtInicial
        else if(dtCompetencia.getDayOfMonth() == dtPagtoAnterior.getDayOfMonth() && dtCompetencia.getMonth() != dtInicial.getMonth()) {
            parcelaAtual++;
            return parcelaAtual + "/" + qtdParcelas;
        }
        //Verifica casos onde o ultimo dia do mes coincide com um dia de pagamento
        else if(dtCompetencia.equals(ultimoDiaMes)) {
            //Verifica se a data do ultimo pagamento é igual ao ultimo dia do seu mes
            if(dtPagtoAnterior == ultimoDiaMesPagtoAnterior) {
                parcelaAtual++;
                return parcelaAtual + "/" + qtdParcelas;
            }
            //garante que sao meses diferentes para evitar pagamentos no mesmo mes
            else if (dtCompetencia.getMonth() != dtPagtoAnterior.getMonth()) {
                parcelaAtual++;
                return parcelaAtual + "/" + qtdParcelas;
            } else {
                return null;
            }
        }
        else if (dtCompetencia != ultimoDiaMes) {
            parcelaAtual++;
            return parcelaAtual + "/" + qtdParcelas;
        } else {
            return null;
        }
    }

    public boolean isDiaNaoUtil(LocalDate data) {
        boolean isFeriado = manager.isHoliday(data);
        boolean isFinalDeSemana = data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY;

        return isFeriado || isFinalDeSemana;
    }

    private void validaDados(EmprestimoRequest emprestimoRequest) {
        if(isDiaNaoUtil(emprestimoRequest.getDataPrimeiroPagamento())) {
            throw new DataNaoUtilException("Escolher uma data de pagamento em dia útil!");
        }
        if(emprestimoRequest.getDataPrimeiroPagamento().isAfter(emprestimoRequest.getDataFinal()) || emprestimoRequest.getDataPrimeiroPagamento().isBefore(emprestimoRequest.getDataInicial())) {
            throw new DataInvalidaException("A data do primeiro pagamento deve ser maior que a data inicial ou menor que a data final.");
        }
        if(emprestimoRequest.getDataPrimeiroPagamento().getMonth().equals(emprestimoRequest.getDataInicial().getMonth())) {
            throw new DataPagamentoException("O mês do primeiro pagamento deve ser posterior ao mês da data inicial.");
        }
        if(emprestimoRequest.getTaxaJuros() <= 0) {
            throw new TaxaJurosException("Taxa de Juros deve ser maior que zero!");
        }
        if(emprestimoRequest.getValorEmprestimo() <= 0) {
            throw new ValorEmprestimoException("Valor de Empréstimo deve ser maior que zero!");
        }
    }
}
