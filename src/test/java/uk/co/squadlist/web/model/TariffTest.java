package uk.co.squadlist.web.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TariffTest {

	@Test
	public void displayPricesRenderCorrectly() throws Exception {
		assertEquals("Free", Tariff.ONE_MONTH_FREE_TRIAL.getDisplayPrice());		
		assertEquals("£36 inc VAT", Tariff.STANDARD_THREE_MONTHS.getDisplayPrice());		
	}
	
}
