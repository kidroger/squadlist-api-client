package uk.co.squadlist.web.api;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class ParsingTest {

	@Test
	public void testname() throws Exception {
		LocalDateTime date = new LocalDateTime(2012, 5, 11, 8, 0, 0);
		
		String print = ISODateTimeFormat.dateTimeNoMillis().print(date);
		System.out.println(print);
		
		LocalDateTime localDateTime = new LocalDateTime(print);
		System.out.println(localDateTime);
	}
}
