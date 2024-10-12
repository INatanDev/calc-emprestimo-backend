package com.gesplan.desafio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class DesafioApplicationTests {

	@Test
	void contextLoads() {
		LocalDate dtCompetencia = LocalDate.of(2024, 01, 31);
		LocalDate ultimoDiaMes = dtCompetencia.withDayOfMonth(dtCompetencia.lengthOfMonth());
		System.out.println(ultimoDiaMes);
	}

}
