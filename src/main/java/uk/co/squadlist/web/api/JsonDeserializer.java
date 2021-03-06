package uk.co.squadlist.web.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import uk.co.squadlist.web.model.Availability;
import uk.co.squadlist.web.model.AvailabilityOption;
import uk.co.squadlist.web.model.Boat;
import uk.co.squadlist.web.model.Instance;
import uk.co.squadlist.web.model.Member;
import uk.co.squadlist.web.model.Outing;
import uk.co.squadlist.web.model.OutingAvailability;
import uk.co.squadlist.web.model.OutingWithSquadAvailability;
import uk.co.squadlist.web.model.Squad;
import uk.co.squadlist.web.model.SubscriptionRequest;

public class JsonDeserializer {

	private final ObjectMapper mapper;
	
	public JsonDeserializer() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<Outing> deserializeListOfOutings(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Outing>) mapper.readValue(json, new TypeReference<Collection<Outing>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> deserializeOutingsMonthsMap(String json) throws JsonParseException, JsonMappingException, IOException {
		return (Map<String, Integer>) mapper.readValue(json, Map.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<OutingAvailability> deserializeListOfOutingAvailability(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<OutingAvailability>) mapper.readValue(json, new TypeReference<Collection<OutingAvailability>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, AvailabilityOption> deserializeListOfOutingAvailabilityMap(String json) throws JsonParseException, JsonMappingException, IOException {
		return (Map<String, AvailabilityOption>) mapper.readValue(json, new TypeReference<Map<String, AvailabilityOption>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public List<Member> deserializeListOfMembers(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Member>) mapper.readValue(json,new TypeReference<Collection<Member>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public List<Availability> deserializeListOfAvailability(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Availability>) mapper.readValue(json, new TypeReference<Collection<Availability>>() {});
	}
	
	public OutingAvailability deserializeOutingAvailability(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, OutingAvailability.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Boat> deserializeListOfBoats(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Boat>) mapper.readValue(json, new TypeReference<Collection<Boat>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public List<Squad> deserializeListOfSquads(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Squad>) mapper.readValue(json, new TypeReference<Collection<Squad>>() {});
	}
	
	public List<String> deserializeListOfStrings(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, new TypeReference<Collection<String>>() {});
	}
	
	public Member deserializeMemberDetails(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Member.class);
	}
	
	public Outing deserializeOuting(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Outing.class);
	}

	public Squad deserializeSquad(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Squad.class);
	}

	@SuppressWarnings("unchecked")
	public List<OutingWithSquadAvailability> deserializeSquadAvailability(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<OutingWithSquadAvailability>) mapper.readValue(json, new TypeReference<Collection<OutingWithSquadAvailability>>() {});
	}

	@SuppressWarnings("unchecked")
	public List<Instance> deserializeListOfInstances(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<Instance>) mapper.readValue(json, new TypeReference<Collection<Instance>>() {});
	}
	
	@SuppressWarnings("unchecked")
	public List<SubscriptionRequest> deserializeListOfSubscriptionRequests(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<SubscriptionRequest>) mapper.readValue(json, new TypeReference<Collection<SubscriptionRequest>>() {});
	}
	
	public SubscriptionRequest deserializeSubscriptionRequest(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, SubscriptionRequest.class);
	}
	
	public Instance deserializeInstanceDetails(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Instance.class);
	}

	public Squad deserializeSquadDetails(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Squad.class);
	}

	public Outing deserializeOutingDetails(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Outing.class);
	}

	public AvailabilityOption deserializeAvailabilityOption(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, AvailabilityOption.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AvailabilityOption> deserializeAvailabilityOptions(String json) throws JsonParseException, JsonMappingException, IOException {
		return (List<AvailabilityOption>) mapper.readValue(json, new TypeReference<Collection<AvailabilityOption>>() {});
	}

	public String deserializeString(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, String.class);
	}
	
	public Map<String, Object> deserializeMap(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Map.class);
	}

	public Boat deserializeBoat(String json) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, Boat.class);
	}

}