package uk.co.squadlist.web.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import uk.co.squadlist.web.model.AvailabilityOption;
import uk.co.squadlist.web.model.Member;
import uk.co.squadlist.web.model.Outing;
import uk.co.squadlist.web.model.OutingAvailability;

public class JsonDeserializerTest {

	@Test
	public void canDeserializeListOfOutings() throws Exception {
		final String json = IOUtils.toString(this.getClass().getClassLoader().getResource("outings.json"));
		
		JsonDeserializer deserializer = new JsonDeserializer();
		List<Outing> outings = deserializer.deserializeListOfOutings(json);
		
		assertEquals(106, outings.size());		
		for (Outing outing : outings) {
			assertTrue(outing instanceof Outing);
		}
		
		final Outing firstOuting = outings.get(0);
		assertEquals("241", firstOuting.getId());
		assertEquals("2012-03-16T08:00:00.000Z", new DateTime(firstOuting.getDate(), DateTimeZone.UTC).toString());
		assertEquals("Men's Senior Squad", firstOuting.getSquad().getName());
	}
	
	@Test
	public void canDeserializeOuting() throws Exception {
		final String json = IOUtils.toString(this.getClass().getClassLoader().getResource("outing.json"));
		
		JsonDeserializer deserializer = new JsonDeserializer();
		final Outing outing = deserializer.deserializeOuting(json);
		
		assertEquals("0", outing.getId());
		assertEquals("2012-11-07T22:01:29.000Z", new DateTime(outing.getDate(), DateTimeZone.UTC).toString());
		assertEquals("Novice men", outing.getSquad().getName());
	}
	
	@Test
	public void canDeserializeListOfMembersAvailability() throws Exception {
		final String json = IOUtils.toString(this.getClass().getClassLoader().getResource("availability.json"));
		
		JsonDeserializer deserializer = new JsonDeserializer();
		List<OutingAvailability> availability = deserializer.deserializeListOfOutingAvailability(json);
		
		assertEquals(2, availability.size());
	}
	
	@Test
	public void canDeserializeOutingAvailabilityMap() throws Exception {
		final String json = IOUtils.toString(this.getClass().getClassLoader().getResource("outingAvailability.json"));
		
		JsonDeserializer deserializer = new JsonDeserializer();
		Map<String, AvailabilityOption> availability = deserializer.deserializeListOfOutingAvailabilityMap(json);
		
		assertEquals("Available", availability.get("38").getLabel());
	}
	
	@Test
	public void canDeserializeMember() throws Exception {
		final String json = IOUtils.toString(this.getClass().getClassLoader().getResource("member.json"));
		JsonDeserializer deserializer = new JsonDeserializer();
		
		final Member member = deserializer.deserializeMemberDetails(json);
		
		assertEquals("LILIANB", member.getId());
		assertEquals(1, member.getSquads().size());
		assertEquals("Men's Senior Squad", member.getSquads().get(0).getName());
	}

}
