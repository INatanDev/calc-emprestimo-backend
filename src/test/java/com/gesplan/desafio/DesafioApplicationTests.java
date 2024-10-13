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
		LocalDate dtCompetencia = LocalDate.of(2024, 01, 31);
		LocalDate dtInicial = LocalDate.of(2024, 01, 01);
		LocalDate dtPagto = LocalDate.of(2024, 02, 15);
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

		LocalDate verificaData = emprestimoService.calcularCompetencia(dtCompetencia, dtPagto, dtInicial, 15);
		System.out.println(verificaData);

	}

}
