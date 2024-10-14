package com.gesplan.desafio;

import com.gesplan.desafio.service.EmprestimoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;

@SpringBootTest
class DesafioApplicationTests {

	@Test
	void contextLoads() {
		EmprestimoService emprestimoService = new EmprestimoService();
		LocalDate dtInicial = LocalDate.of(2023, 12, 01);
		LocalDate dtCompetencia = LocalDate.of(2024, 04, 30);
		LocalDate dtUltimoPagto = LocalDate.of(2024, 03, 28);
		LocalDate dtPrimeirPagto = LocalDate.of(2024, 01, 31);
		LocalDate dtFinal = LocalDate.of(2024, 01, 31);
		LocalDate dtAnterior = LocalDate.of(2024, 01, 01);
		LocalDate dtProxPagto = LocalDate.of(2024, 03, 15);

		/*LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth());
		System.out.println(ultimoDiaMes);*/

		/*Period periodo = Period.between(dtAnterior, dtCompetencia);
		Integer dias = periodo.getDays();
		System.out.println(dias);*/

		/*LocalDate verificaData = emprestimoService.calcularDataPagamento(dtCompetencia, dtPrimeiroPagto);

		System.out.println(verificaData);
		System.out.println(dtCompetencia.isBefore(dtProxPagto));*/

		//LocalDate verificaData = emprestimoService.calcularCompetencia(dtCompetencia, dtPagto, dtInicial, dtFinal,31, 120);
		String verificaConsolidado = emprestimoService.isConsolidado(dtCompetencia, dtUltimoPagto, 12, dtInicial, dtPrimeirPagto);
		//LocalDate verificaDtPagto = emprestimoService.calcularDataPagamento(dtUltimoPagto, dtPrimeirPagto, 31);
		System.out.println(verificaConsolidado);
		//System.out.println(verificaDtPagto);

	}

}
