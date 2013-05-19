package uk.co.squadlist.web.api;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ApiUrlBuilderTest {

	private static final String INSTANCE = "test";
	private static final String MEMBER = "SOMEMEMBER";
	
	private ApiUrlBuilder urlBuilder;
	
	@Before
	public void setup() {
		urlBuilder = new ApiUrlBuilder("http://api.local");
	}
	
	@Test
	public void instanceUrlsAreInRestfulFormat() throws Exception {
		final String url = urlBuilder.getInstanceUrl(INSTANCE);
		
		assertEquals("http://api.local/instances/test", url);
	}
	
	@Test
	public void instancesSquadsUrlCorrect() throws Exception {
		final String url = urlBuilder.getSquadsUrl(INSTANCE);
		
		assertEquals("http://api.local/instances/test/squads", url);
	}
	
	@Test
	public void canAppendFromDateToMemberAvailabilityUrl() throws Exception {		
		final String url = urlBuilder.getMembersAvailabilityUrl(INSTANCE, MEMBER, new DateTime(2012, 7, 13, 12, 10).toDate(),  new DateTime(2012, 8, 13, 12, 10).toDate());
		
		assertEquals("http://api.local/instances/test/members/SOMEMEMBER/availability?fromDate=2012-07-13T12:10&toDate=2012-08-13T12:10", url);
	}
	
}
