package uk.co.squadlist.web.api;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import uk.co.eelpieconsulting.common.http.*;
import uk.co.squadlist.web.exceptions.*;
import uk.co.squadlist.web.model.*;

import java.io.IOException;
import java.util.*;

public class SquadlistApi {

	private final static Logger log = Logger.getLogger(SquadlistApi.class);

	private final ApiUrlBuilder apiUrlBuilder;
	private final RequestBuilder requestBuilder;
	private final HttpFetcher httpFetcher;
	private final JsonDeserializer jsonDeserializer;

	private final Joiner colonJoiner = Joiner.on(":");
	
	private final String accessToken;
	private final HttpClient client;		// TODO why is this in use - operations which HttpFetcher does not support?

	public SquadlistApi(RequestBuilder requestBuilder, ApiUrlBuilder urlBuilder, HttpFetcher httpFetcher, JsonDeserializer jsonDeserializer, String accessToken) {
		this.apiUrlBuilder = urlBuilder;
		this.requestBuilder = requestBuilder;
		this.httpFetcher = httpFetcher;
		this.jsonDeserializer = jsonDeserializer;
		this.accessToken = accessToken;
		this.client = HttpClients.createDefault();
	}
	
	public SquadlistApi(String apiUrl) {
		this.apiUrlBuilder  = new ApiUrlBuilder(apiUrl);
		this.requestBuilder = new RequestBuilder(apiUrlBuilder);
		this.httpFetcher = new HttpFetcher();
		this.jsonDeserializer = new JsonDeserializer();
		this.accessToken = null;
		this.client = HttpClients.createDefault();
	}
	
	public SquadlistApi(String apiUrl, String accessToken) {
		this.apiUrlBuilder  = new ApiUrlBuilder(apiUrl);
		this.requestBuilder = new RequestBuilder(apiUrlBuilder);
		this.httpFetcher = new HttpFetcher();
		this.jsonDeserializer = new JsonDeserializer();
		this.accessToken = accessToken;
		this.client = HttpClients.createDefault();
	}

	public List<Instance> getInstances() {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstancesUrl(), accessTokenHeader());
			return jsonDeserializer.deserializeListOfInstances(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<Instance> getInstances(String q) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstancesUrl(q), accessTokenHeader());
			return jsonDeserializer.deserializeListOfInstances(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<SubscriptionRequest> getSubscriptionRequests() {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSubscriptionRequestsUrl(), accessTokenHeader());
			return jsonDeserializer.deserializeListOfSubscriptionRequests(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public SubscriptionRequest getSubscriptionRequest(String id) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSubscriptionRequestUrl(id), accessTokenHeader());
			return jsonDeserializer.deserializeSubscriptionRequest(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteSubscriptionRequest(String id) throws InvalidInstanceException {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteSubscriptionRequestRequest(id);
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}

			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Instance createInstance(String id, String name, String timeZone, boolean availabilityVisible, String governingBody) throws InvalidInstanceException {
		try {
			final HttpPost post = requestBuilder.buildCreateInstanceRequest(id, name, timeZone, availabilityVisible, governingBody);
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

	public AvailabilityOption updateAvailabilityOption(String instance, AvailabilityOption availabilityOption) throws InvalidAvailabilityOptionException {
		try {
			final HttpPost post = requestBuilder.buildUpdateAvailabilityOptionRequest(instance, availabilityOption);
			addAccessToken(post);
			return jsonDeserializer.deserializeAvailabilityOption(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidAvailabilityOptionException(e.getResponseBody());

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Instance getInstance(String id) throws UnknownInstanceException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstanceUrl(id), accessTokenHeader());
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
			final String json = httpFetcher.get(apiUrlBuilder.getInstanceStatisticsUrl(instance), accessTokenHeader());
			return jsonDeserializer.deserializeMap(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownInstanceException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> getStatistics() {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getStatisticsUrl());
			return jsonDeserializer.deserializeMap(json);
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteInstance(String id) throws InvalidInstanceException {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteInstanceRequest(id);
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}

			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteOuting(String id) throws InvalidInstanceException {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteOutingRequest(id);
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}
			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteAvailabilityOption(String instance, AvailabilityOption availabilityOption) {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteAvailabilityOptionRequest(instance, availabilityOption);
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}
			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteAvailabilityOption(String instance, AvailabilityOption availabilityOption, AvailabilityOption alternative) {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteAvailabilityOptionRequest(instance, availabilityOption, alternative);
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}
			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Member verify(String token) {
		try {
			final HttpPost post = requestBuilder.buildVerifyPost(token);
			final HttpResponse response = client.execute(post);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return jsonDeserializer.deserializeMemberDetails(EntityUtils.toString(response.getEntity()));
			}

			consumeAndLogErrorResponse(response);
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
			if (statusCode == HttpStatus.SC_OK) {
				return jsonDeserializer.deserializeMemberDetails(EntityUtils.toString(response.getEntity()));
			}

			consumeAndLogErrorResponse(response);
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

	public String resetMemberPassword(String instance, String memberId) throws UnknownMemberException {
		try {
			final HttpPost post = requestBuilder.buildResetMemberPasswordRequest(instance, memberId);
			addAccessToken(post);

			final HttpResponse response = client.execute(post);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return jsonDeserializer.deserializeString(EntityUtils.toString(response.getEntity()));
			}

			consumeAndLogErrorResponse(response);
			return null;
			// TODO 404

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

	public boolean changePassword(String memberId, String currentPassword, String newPassword) {
		try {
			final HttpPost post = requestBuilder.buildChangePasswordPost(memberId, currentPassword, newPassword);
			addAccessToken(post);

			final HttpResponse response = client.execute(post);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return true;
			}

			log.warn("Change password response status was: " + statusCode);
			consumeAndLogErrorResponse(response);

			return false;

		} catch (Exception e) {
			log.error("Error while attempting to make auth call", e);
			throw new RuntimeException(e);
		}
	}

	public List<OutingAvailability> getAvailabilityFor(String memberId, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMembersAvailabilityUrl(memberId, fromDate, toDate), accessTokenHeader());
			return jsonDeserializer.deserializeListOfOutingAvailability(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<Boat> getBoats(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getBoatsUrl(instance), accessTokenHeader());
			return jsonDeserializer.deserializeListOfBoats(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<Squad> getSquads(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadsUrl(instance), accessTokenHeader());
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

	public List<Outing> getOutings(String instance, List<Squad> squads, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingsUrl(instance, squads, fromDate, toDate), accessTokenHeader());
			return jsonDeserializer.deserializeListOfOutings(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Map<String, Integer> getOutingMonths(String instance, List<Squad> squads, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingsMonthsUrl(instance, squads, fromDate, toDate), accessTokenHeader());
			return jsonDeserializer.deserializeOutingsMonthsMap(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Map<String, Integer> getMemberOutingMonths(String memberId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMembersUrl(memberId) + "/outings/months", accessTokenHeader());
			return jsonDeserializer.deserializeOutingsMonthsMap(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Map<String, AvailabilityOption> getOutingAvailability(String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingAvailabilityUrl(outingId), accessTokenHeader());
			return jsonDeserializer.deserializeListOfOutingAvailabilityMap(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownOutingException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Member getMember(String memberId) throws UnknownMemberException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMemberUrl(memberId), accessTokenHeader());
			return jsonDeserializer.deserializeMemberDetails(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownMemberException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Boat getBoat(String instance, String id) throws UnknownSquadException, UnknownBoatException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getBoatUrl(instance, id), accessTokenHeader());
			return jsonDeserializer.deserializeBoat(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownBoatException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Squad getSquad(String squadId) throws UnknownSquadException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadUrl(squadId), accessTokenHeader());
			return jsonDeserializer.deserializeSquad(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownSquadException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Outing getOuting(String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingUrl(outingId), accessTokenHeader());
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
			final String json = httpFetcher.get(apiUrlBuilder.getMembersUrl(instance), accessTokenHeader());
			return jsonDeserializer.deserializeListOfMembers(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<Member> getSquadMembers(String squadId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadMembersUrl(squadId), accessTokenHeader());
			return jsonDeserializer.deserializeListOfMembers(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<OutingWithSquadAvailability> getSquadAvailability(String squadId, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadAvailabilityUrl(squadId, fromDate, toDate), accessTokenHeader());
			return jsonDeserializer.deserializeSquadAvailability(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<AvailabilityOption> getAvailabilityOptions(String instance) throws HttpFetchException, JsonParseException, JsonMappingException, IOException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getAvailabilityOptionsUrl(instance), accessTokenHeader());
			return jsonDeserializer.deserializeAvailabilityOptions(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public OutingAvailability setOutingAvailability(Member member, Outing outing, AvailabilityOption availabilityOption) {
		try {
			final HttpPost post = requestBuilder.buildSetAvailabilityRequest(member, outing, availabilityOption);
			addAccessToken(post);
			return jsonDeserializer.deserializeOutingAvailability(httpFetcher.post(post));

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Member createMember(String instance, Member member) throws InvalidMemberException {
		try {
			final HttpPost post = requestBuilder.buildCreateMemberRequest(instance, member);
			addAccessToken(post);

			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidMemberException();
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public Member createMember(String instance, String firstName, String lastName, List<Squad> squads, String email, String password, Date dateOfBirth, String role) throws InvalidMemberException {
		final Member member = new Member(firstName, lastName, squads, email, password, dateOfBirth);
		member.setRole(role);

		return createMember(instance, member);
	}

	public Boat createBoat(String instance, Boat boat) throws InvalidBoatException {
		try {
			final HttpPost post = requestBuilder.buildCreateBoatRequest(instance, boat);
			addAccessToken(post);

			return jsonDeserializer.deserializeBoat(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidBoatException();

		} catch (HttpFetchException e) {
			log.error(e);
			throw new RuntimeException(e);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Squad createSquad(Instance instance, String name) throws InvalidSquadException {
		try {
			Squad squad = new Squad(name, instance);
			final HttpPost post = requestBuilder.buildCreateSquadRequest(squad);
			addAccessToken(post);

			return jsonDeserializer.deserializeSquadDetails(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidSquadException();

		} catch (HttpFetchException e) {
			log.error(e);
			throw new RuntimeException(e);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Member updateMemberDetails(Member member) {
		try {
			final HttpPost post = requestBuilder.buildUpdateMemberRequest(member);
			addAccessToken(post);

			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteMember(Member member) {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteMemberRequest(member);
			addAccessToken(delete);
			httpFetcher.delete(delete);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Member updateMemberProfileImage(Member member, byte[] image) throws InvalidImageException {	// TODO requires a longer blocking timeout for large image; connection times out before image resize is completed
		try {
			final HttpPost post = requestBuilder.buildUpdateMemberProfileImageRequest(member, image);
			addAccessToken(post);
			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidImageException();

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Squad updateSquad(Squad squad) {
		try {
			final HttpPost post = requestBuilder.buildUpdateSquadRequest(squad);
			addAccessToken(post);

			return jsonDeserializer.deserializeSquad(httpFetcher.post(post));

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteSquad(Squad squad) {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteSquadRequest(squad.getId());
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}
			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Outing createOuting(Outing outing, Integer repeats) throws InvalidOutingException {
		try {
			final HttpPost post = requestBuilder.buildCreateOutingPost(outing, repeats);
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

	public Outing updateOuting(Outing outing) throws InvalidOutingException {
		try {
			final HttpPost post = requestBuilder.buildUpdateOutingPost(outing);
			addAccessToken(post);

			return jsonDeserializer.deserializeOutingDetails(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidOutingException(e.getResponseBody());

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public Squad setSquadMembers(String squadId, Set<String> members) throws JsonGenerationException, JsonMappingException, IOException, HttpNotFoundException, HttpBadRequestException, HttpForbiddenException, HttpFetchException {
		final HttpPost post = requestBuilder.buildSetSquadMembersRequest(squadId, members);
		addAccessToken(post);

		return jsonDeserializer.deserializeSquadDetails(httpFetcher.post(post));
	}

	public void setAdmins(String instance, Set<String> admins) throws JsonGenerationException, JsonMappingException, IOException, HttpNotFoundException, HttpBadRequestException, HttpForbiddenException, HttpFetchException {
		final HttpPost post = requestBuilder.buildSetAdminsRequest(instance, admins);
		addAccessToken(post);
		httpFetcher.post(post);
	}

	@Deprecated
	public AvailabilityOption createAvailabilityOption(String instance, String label) throws InvalidAvailabilityOptionException {
		final AvailabilityOption availabilityOption = new AvailabilityOption(label);
		return createAvailabilityOption(instance, availabilityOption);
	}

	public AvailabilityOption createAvailabilityOption(String instance, final AvailabilityOption availabilityOption) throws InvalidAvailabilityOptionException {
		try {
			final HttpPost post = requestBuilder.buildCreateAvailabilityOptionRequest(instance, availabilityOption);
			addAccessToken(post);

			return jsonDeserializer.deserializeAvailabilityOption(httpFetcher.post(post));

		} catch (HttpBadRequestException e) {
			throw new InvalidAvailabilityOptionException(e.getResponseBody());

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteAvailablityOption(String instance, AvailabilityOption availabilityOption) {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteAvailablityOptionRequest(instance, availabilityOption.getId());
			addAccessToken(delete);

			HttpResponse response = client.execute(delete);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				EntityUtils.consume(response.getEntity());
				return;
			}
			
			consumeAndLogErrorResponse(response);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public String requestClientAccessToken(String clientId, String clientSecret) throws IOException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getRequestTokenUrl());

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		applyHeadersTo(post, clientAuthHeader(clientId, clientSecret));

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Map<String, Object> stringObjectMap = jsonDeserializer.deserializeMap(EntityUtils.toString(response.getEntity()));
			return (String) stringObjectMap.get("access_token");
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
			consumeAndLogErrorResponse(response);	// TODO not strictly an error
			return null;
		} else {
			consumeAndLogErrorResponse(response);
			throw new RuntimeException();
		}
	}

	public String requestAccessToken(String instance, String username, String password, String clientId, String clientSecret) throws IOException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getRequestTokenUrl());
		
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();	// TODO push to request builder
		nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
		nameValuePairs.add(new BasicNameValuePair("username", instance + "/" + username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
		nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
		applyHeadersTo(post, clientAuthHeader(clientId, clientSecret));

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Map<String, Object> stringObjectMap = jsonDeserializer.deserializeMap(EntityUtils.toString(response.getEntity()));
			return (String) stringObjectMap.get("access_token");
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
			consumeAndLogErrorResponse(response);	// TODO not strictly an error
			return null;
		} else {
			consumeAndLogErrorResponse(response);
			throw new RuntimeException();
		}
	}

	public String requestAccessTokenWithFacebook(String instance, String facebookAccessToken, String clientId, String clientSecret) throws ClientProtocolException, IOException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getRequestTokenUrl());

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();	// TODO push to request builder
		nameValuePairs.add(new BasicNameValuePair("grant_type", "facebook"));
		nameValuePairs.add(new BasicNameValuePair("instance", instance));
		nameValuePairs.add(new BasicNameValuePair("token", facebookAccessToken));
		nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
		nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		applyHeadersTo(post, clientAuthHeader(clientId, clientSecret));

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Map<String, Object> stringObjectMap = jsonDeserializer.deserializeMap(EntityUtils.toString(response.getEntity()));
			return (String) stringObjectMap.get("access_token");
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
			consumeAndLogErrorResponse(response);	// TODO not stricly an error
			return null;
		} else {
			consumeAndLogErrorResponse(response);
			throw new RuntimeException();
		}
	}

	private void addAccessToken(final HttpRequestBase request) {
		final Map<String, String> authHeaders = accessTokenHeader();
		applyHeadersTo(request, authHeaders);
	}

	private void applyHeadersTo(final HttpRequestBase request, final Map<String, String> headers) {
		for (String header : headers.keySet()) {
			request.addHeader(header, headers.get(header));
		}
	}

	private Map<String, String> accessTokenHeader() {
		final Map<String, String> authHeaders = Maps.newHashMap();
		authHeaders.put("Authorization", "Bearer " + accessToken);
		return authHeaders;
	}
	
	private Map<String, String> clientAuthHeader(String clientId, String clientSecret) {
		final Map<String, String> authHeaders = Maps.newHashMap();
		final String base64EncodedClientIdAndSecret = Base64.encodeBase64String(colonJoiner.join(clientId, clientSecret).getBytes());
		authHeaders.put("Authorization", "Basic " + base64EncodedClientIdAndSecret);
		return authHeaders;
	}
	
	private void consumeAndLogErrorResponse(HttpResponse response) throws IOException {
		log.error(response.getStatusLine());
		log.error(EntityUtils.toString(response.getEntity()));
	}

}