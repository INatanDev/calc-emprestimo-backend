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
		LocalDate dtCompetencia = LocalDate.of(2024, 03, 31);
		LocalDate dtPrimeiroPagto = LocalDate.of(2024, 10, 15);
		LocalDate dtAnterior = LocalDate.of(2024, 01, 01);
		LocalDate dtProxPagto = LocalDate.of(2024, 03, 15);

		/*LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth());
		System.out.println(ultimoDiaMes);*/

		/*Period periodo = Period.between(dtAnterior, dtCompetencia);
		Integer dias = periodo.getDays();
		System.out.println(dias);*/

		EmprestimoService emprestimoService = new EmprestimoService();

		LocalDate verificaData = emprestimoService.calcularDataPagamento(dtCompetencia, dtPrimeiroPagto);

		System.out.println(verificaData);
		System.out.println(dtCompetencia.isBefore(dtProxPagto));

	}

}
