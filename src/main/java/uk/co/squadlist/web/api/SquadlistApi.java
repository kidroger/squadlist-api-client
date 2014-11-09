package uk.co.squadlist.web.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import uk.co.eelpieconsulting.common.http.HttpBadRequestException;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpFetcher;
import uk.co.eelpieconsulting.common.http.HttpForbiddenException;
import uk.co.eelpieconsulting.common.http.HttpNotFoundException;
import uk.co.squadlist.web.exceptions.InvalidInstanceException;
import uk.co.squadlist.web.exceptions.InvalidMemberException;
import uk.co.squadlist.web.exceptions.InvalidOutingException;
import uk.co.squadlist.web.exceptions.InvalidSquadException;
import uk.co.squadlist.web.exceptions.UnknownInstanceException;
import uk.co.squadlist.web.exceptions.UnknownMemberException;
import uk.co.squadlist.web.exceptions.UnknownOutingException;
import uk.co.squadlist.web.exceptions.UnknownSquadException;
import uk.co.squadlist.web.model.AvailabilityOption;
import uk.co.squadlist.web.model.Instance;
import uk.co.squadlist.web.model.Member;
import uk.co.squadlist.web.model.Outing;
import uk.co.squadlist.web.model.OutingAvailability;
import uk.co.squadlist.web.model.OutingWithSquadAvailability;
import uk.co.squadlist.web.model.Squad;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SquadlistApi {
	
	private final static Logger log = Logger.getLogger(SquadlistApi.class);
		
	private final ApiUrlBuilder apiUrlBuilder;
	private final RequestBuilder requestBuilder;
	private final HttpFetcher httpFetcher;
	private final JsonDeserializer jsonDeserializer;

	private final String accessToken;
	private final HttpClient client;		// TODO why is this in use - operations which HttpFetcher does not support?
	
	public SquadlistApi(RequestBuilder requestBuilder, ApiUrlBuilder urlBuilder, HttpFetcher httpFetcher, JsonDeserializer jsonDeserializer, String accessToken) {
		this.apiUrlBuilder = urlBuilder;
		this.requestBuilder = requestBuilder;
		this.httpFetcher = httpFetcher;
		this.jsonDeserializer = jsonDeserializer;
		this.accessToken = accessToken;
		this.client = new DefaultHttpClient();
	}
	
	public SquadlistApi(String apiUrl, String accessToken) {
		this.apiUrlBuilder  = new ApiUrlBuilder(apiUrl);
		this.requestBuilder = new RequestBuilder(apiUrlBuilder);
		this.httpFetcher = new HttpFetcher();
		this.jsonDeserializer = new JsonDeserializer();
		this.accessToken = accessToken;
		this.client = new DefaultHttpClient();
	}
	
	public List<Instance> getInstances() {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstancesUrl(), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfInstances(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Instance createInstance(String id, String name, String timeZone, boolean availabilityVisible) throws InvalidInstanceException {
		try {
			final HttpPost post = requestBuilder.buildCreateInstanceRequest(id, name, timeZone, availabilityVisible);
			addAccessToken(post);			
			return jsonDeserializer.deserializeInstanceDetails(httpFetcher.post(post));
			
		} catch (HttpBadRequestException e) {
			throw new InvalidInstanceException();
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}

	public Instance updateInstance(Instance instance) {
		try {
			final HttpPut put = requestBuilder.buildUpdateInstanceRequest(instance);
			addAccessToken(put);
			return jsonDeserializer.deserializeInstanceDetails(httpFetcher.put(put));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Instance getInstance(String id) throws UnknownInstanceException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstanceUrl(id), accessTokenHeaders());
			return jsonDeserializer.deserializeInstanceDetails(json);
		
		} catch (HttpNotFoundException e) {	
			throw new UnknownInstanceException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Object> getInstanceStatistics(String instance) throws UnknownInstanceException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstanceStatisticsUrl(instance), accessTokenHeaders());
			return jsonDeserializer.deserializeMap(json);
		
		} catch (HttpNotFoundException e) {	
			throw new UnknownInstanceException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public void deleteInstance(String id) throws InvalidInstanceException {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteInstanceRequest(id);
			addAccessToken(delete);
			
			final HttpClient client = new DefaultHttpClient();			
			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				log.info("Delete returned http ok");
				EntityUtils.consume(response.getEntity());
				return;
			}
			
			log.error(response.getStatusLine());
			log.error(EntityUtils.toString(response.getEntity()));			
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Member auth(String instance, String username, String password) {
		try {
			final HttpClient client = new DefaultHttpClient();	// TODO should be a field?
			final HttpPost post = requestBuilder.buildAuthPost(instance, username, password);
			addAccessToken(post);
			
			final HttpResponse response = client.execute(post);			
			final int statusCode = response.getStatusLine().getStatusCode();
			log.info("Auth attempt status code was: " + statusCode);
			if (statusCode == HttpStatus.SC_OK) {				
				return jsonDeserializer.deserializeMemberDetails(EntityUtils.toString(response.getEntity()));
			}
			return null;
			
		} catch (Exception e) {
			log.error("Error while attempting to make auth call", e);
			throw new RuntimeException(e);
		}
	}
	
	public Member authFacebook(String instance, String token) {
		try {
			final HttpPost post = requestBuilder.buildAuthFacebookPost(instance, token);
			addAccessToken(post);

			final HttpResponse response = client.execute(post);			
			final int statusCode = response.getStatusLine().getStatusCode();
			log.info("Auth attempt status code was: " + statusCode);
			if (statusCode == HttpStatus.SC_OK) {				
				return jsonDeserializer.deserializeMemberDetails(EntityUtils.toString(response.getEntity()));
			}
			return null;
			
		} catch (Exception e) {
			log.error("Error while attempting to make auth call", e);
			throw new RuntimeException(e);
		}
	}
	
	public void resetPassword(String instance, String username) throws UnknownMemberException {
		try {
			final HttpPost post = requestBuilder.buildResetPasswordRequest(instance, username);
			addAccessToken(post);
			httpFetcher.post(post);
			
		} catch (HttpNotFoundException e) {
			throw new UnknownMemberException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public String confirmResetPassword(String instance, String token) {
		try {
			final HttpPost post = requestBuilder.buildConfirmPasswordRequest(instance, token);
			addAccessToken(post);
			return jsonDeserializer.deserializeString(httpFetcher.post(post));
			// TODO catch invalid token
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public boolean changePassword(String instance, String memberId, String currentPassword, String newPassword) {
		try {
			final HttpClient client = new DefaultHttpClient();	// TODO should be a field?
			final HttpPost post = requestBuilder.buildChangePasswordPost(instance, memberId, currentPassword, newPassword);
			addAccessToken(post);

			final HttpResponse response = client.execute(post);			
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {				
				return true;
			}
			log.warn("Change password response status was: " + statusCode);
			return false;
			
		} catch (Exception e) {
			log.error("Error while attempting to make auth call", e);
			throw new RuntimeException(e);
		}
	}
	
	public List<OutingAvailability> getAvailabilityFor(String instance, String memberId, Date fromDate, Date toDate) {
		try {
			log.info("getAvailabilityFor: " + memberId + ", " + fromDate);
			final String json = httpFetcher.get(apiUrlBuilder.getMembersAvailabilityUrl(instance, memberId, fromDate, toDate), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfOutingAvailability(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Squad> getSquads(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadsUrl(instance), accessTokenHeaders());		
			return jsonDeserializer.deserializeListOfSquads(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Squad> getSquadsMap(String instance) {
		try {
			final Map<String, Squad> map = new HashMap<String, Squad>();
			for(Squad squad : getSquads(instance)) {
				map.put(squad.getId(), squad);
			}
			return map;
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Outing> getSquadOutings(String instance, String squadId, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadOutingsUrl(instance, squadId, fromDate, toDate), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfOutings(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}			
	}
	
	public Map<String, Integer> getSquadOutingMonths(String instance, String squadId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadOutingsMonthsUrl(instance, squadId), accessTokenHeaders());
			return jsonDeserializer.deserializeOutingsMonthsMap(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}	
	}
	
	public Map<String, Integer> getMemberOutingMonths(String instance, String memberId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMemberDetailsUrl(instance, memberId) + "/outings/months", accessTokenHeaders());
			return jsonDeserializer.deserializeOutingsMonthsMap(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}	
	}
		
	public Map<String, AvailabilityOption> getOutingAvailability(String instance, String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingAvailabilityUrl(instance, outingId), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfOutingAvailabilityMap(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownOutingException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Member getMemberDetails(String instance, String memberId) throws UnknownMemberException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMemberDetailsUrl(instance, memberId), accessTokenHeaders());
			return jsonDeserializer.deserializeMemberDetails(json);
			
		} catch (HttpNotFoundException e) {
			throw new UnknownMemberException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Squad getSquad(String instance, String squadId) throws UnknownSquadException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadUrl(instance, squadId), accessTokenHeaders());
			return jsonDeserializer.deserializeSquad(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownSquadException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Squad setSquadMembers(String instance, String squadId, Set<String> members) throws JsonGenerationException, JsonMappingException, IOException, HttpNotFoundException, HttpBadRequestException, HttpForbiddenException, HttpFetchException {
		final HttpPost post = requestBuilder.buildSetSquadMembersRequest(instance, squadId, members);		
		return jsonDeserializer.deserializeSquadDetails(httpFetcher.post(post));
	}
	
	public Outing getOuting(String instance, String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingUrl(instance, outingId), accessTokenHeaders());
			return jsonDeserializer.deserializeOuting(json);
						
		} catch (HttpNotFoundException e) {
			throw new UnknownOutingException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Member> getMembers(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMembersUrl(instance), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfMembers(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public List<Member> getSquadMembers(String instance, String squadId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadMembersUrl(instance, squadId), accessTokenHeaders());
			return jsonDeserializer.deserializeListOfMembers(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<OutingWithSquadAvailability> getSquadAvailability(String instance, String squadId, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadAvailabilityUrl(instance, squadId, fromDate, toDate), accessTokenHeaders());
			return jsonDeserializer.deserializeSquadAvailability(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<AvailabilityOption> getAvailabilityOptions(String instance) throws HttpFetchException, JsonParseException, JsonMappingException, IOException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getAvailabilityOptionsUrl(instance), accessTokenHeaders());
			return jsonDeserializer.deserializeAvailabilityOptions(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public OutingAvailability setOutingAvailability(String instance, Member member, Outing outing, AvailabilityOption availabilityOption) {
		try {
			final HttpPost post = requestBuilder.buildSetAvailabilityRequest(instance, member, outing, availabilityOption);			
			addAccessToken(post);
			return jsonDeserializer.deserializeOutingAvailability(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Member createMember(String instance, String firstName, String lastName, List<Squad> squads, String email, String password, Date dateOfBirth, String role) throws InvalidMemberException {
		try {
			final HttpPost post = requestBuilder.buildCreateMemberRequest(instance, firstName, lastName, squads, email, password, dateOfBirth, role);
			addAccessToken(post);

			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));
			
		} catch (HttpBadRequestException e) {
			throw new InvalidMemberException();			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Squad createSquad(String instance, String name) throws InvalidSquadException {
		try {
			final HttpPost post = requestBuilder.buildCreateSquadRequest(instance, new Squad(name));
			addAccessToken(post);
			
			return jsonDeserializer.deserializeSquadDetails(httpFetcher.post(post));
			
		} catch (HttpBadRequestException e) {
			log.info("Bad request response to new squad request: " + e.getResponseBody());
			throw new InvalidSquadException();
			
		} catch (HttpFetchException e) {
			log.error(e);
			throw new RuntimeException(e);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}	
	}
	
	public Member updateMemberDetails(String instance, Member member) {
		try {
			final HttpPost post = requestBuilder.buildUpdateMemberRequest(instance, member);
			addAccessToken(post);

			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Member updateMemberProfileImage(String instance, Member member, byte[] image) {
		try {
			final HttpPost post = requestBuilder.buildUpdateMemberProfileImageRequest(instance, member, image);
			addAccessToken(post);
			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Squad updateSquad(String instance, Squad squad) {
		try {
			final HttpPost post = requestBuilder.buildUpdateSquadRequest(instance, squad);
			addAccessToken(post);

			return jsonDeserializer.deserializeSquad(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Outing createOuting(String instance, Outing outing) throws InvalidOutingException {
		return createOuting(instance, outing, 0);	
	}
	
	public Outing createOuting(String instance, Outing outing, int repeats) throws InvalidOutingException {
		try {
			final HttpPost post = requestBuilder.buildCreateOutingPost(instance, outing, repeats);
			addAccessToken(post);

			return jsonDeserializer.deserializeOutingDetails(httpFetcher.post(post));	// TODO might need to be a list of outings?
			
		} catch (HttpBadRequestException e) {
			throw new InvalidOutingException(e.getResponseBody());
			
		} catch (HttpFetchException e) {
			log.error("Unexpected HTTP exception: " + e.getResponseBody());
			throw new RuntimeException(e);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Outing updateOuting(String instance, Outing outing) throws InvalidOutingException {
		try {
			final HttpPost post = requestBuilder.buildUpdateOutingPost(instance, outing);
			addAccessToken(post);

			return jsonDeserializer.deserializeOutingDetails(httpFetcher.post(post));
			
		} catch (HttpBadRequestException e) {
			throw new InvalidOutingException(e.getResponseBody());
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public AvailabilityOption createAvailabilityOption(String instance, String label) {
		try {
			final HttpPost post = requestBuilder.buildCreateAvailabilityOptionRequest(instance, new AvailabilityOption(label));
			addAccessToken(post);
			
			return jsonDeserializer.deserializeAvailabilityOption(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	private void addAccessToken(final HttpRequestBase request) {
		final Map<String, String> authHeaders = accessTokenHeaders();
		for (String header : authHeaders.keySet()) {
			request.addHeader(header, authHeaders.get(header));			
		}
	}
	
	private Map<String, String> accessTokenHeaders() {
		final Map<String, String> authHeaders = Maps.newHashMap();
		authHeaders.put("Authorization", "Bearer " + accessToken);
		return authHeaders;
	}
	
}