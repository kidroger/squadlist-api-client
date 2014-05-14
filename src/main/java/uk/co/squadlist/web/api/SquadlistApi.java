package uk.co.squadlist.web.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.eelpieconsulting.common.http.HttpBadRequestException;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpNotFoundException;
import uk.co.squadlist.web.exceptions.InvalidInstanceException;
import uk.co.squadlist.web.exceptions.InvalidMemberException;
import uk.co.squadlist.web.exceptions.InvalidOutingException;
import uk.co.squadlist.web.exceptions.InvalidSquadException;
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

@Service("squadlistApi")	// TODO shouldn't be Spring dependant
public class SquadlistApi {
	
	private final static Logger log = Logger.getLogger(SquadlistApi.class);
		
	private final ApiUrlBuilder apiUrlBuilder;
	private final RequestBuilder requestBuilder;
	private final HttpFetcher httpFetcher;
	private final JsonDeserializer jsonDeserializer;
	
	@Autowired
	public SquadlistApi(RequestBuilder requestBuilder, ApiUrlBuilder urlBuilder, HttpFetcher httpFetcher, JsonDeserializer jsonDeserializer) {
		this.requestBuilder = requestBuilder;
		this.apiUrlBuilder = urlBuilder;
		this.httpFetcher = httpFetcher;
		this.jsonDeserializer = jsonDeserializer;
	}
	
	public SquadlistApi(String apiUrl) {
		this.apiUrlBuilder  = new ApiUrlBuilder(apiUrl);
		this.requestBuilder = new RequestBuilder(apiUrlBuilder);
		this.httpFetcher = new HttpFetcher();
		this.jsonDeserializer = new JsonDeserializer();
	}

	public List<Instance> getInstances() {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstancesUrl());
			return jsonDeserializer.deserializeListOfInstances(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Instance createInstance(String id, String name, String timeZone) throws InvalidInstanceException {
		try {
			final HttpPost post = requestBuilder.buildCreateInstanceRequest(id, name, timeZone);		
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
			return jsonDeserializer.deserializeInstanceDetails(httpFetcher.put(put));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Instance getInstance(String id) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getInstanceUrl(id));
			return jsonDeserializer.deserializeInstanceDetails(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public void deleteInstance(String id) throws InvalidInstanceException {
		try {
			final HttpDelete delete = requestBuilder.buildDeleteInstanceRequest(id);

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
			httpFetcher.post(requestBuilder.buildResetPasswordRequest(instance, username));
			
		} catch (HttpNotFoundException e) {
			throw new UnknownMemberException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public String confirmResetPassword(String instance, String token) {
		try {
			return jsonDeserializer.deserializeString(httpFetcher.post(requestBuilder.buildConfirmPasswordRequest(instance, token)));
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
			final String json = httpFetcher.get(apiUrlBuilder.getMembersAvailabilityUrl(instance, memberId, fromDate, toDate));
			return jsonDeserializer.deserializeListOfOutingAvailability(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Squad> getSquads(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadsUrl(instance));		
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
			final String json = httpFetcher.get(apiUrlBuilder.getSquadOutingsUrl(instance, squadId, fromDate, toDate));
			return jsonDeserializer.deserializeListOfOutings(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}			
	}
	
	public Map<String, Integer> getSquadOutingMonths(String instance, String squadId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadOutingsMonthsUrl(instance, squadId));
			return jsonDeserializer.deserializeOutingsMonthsMap(json);
		
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}	
	}
	
	public Map<String, Integer> getMemberOutingMonths(String instance, String memberId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getMemberDetailsUrl(instance, memberId) + "/outings/months");
			return jsonDeserializer.deserializeOutingsMonthsMap(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}	
	}
		
	public Map<String, String> getOutingAvailability(String instance, String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingAvailabilityUrl(instance, outingId));
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
			final String json = httpFetcher.get(apiUrlBuilder.getMemberDetailsUrl(instance, memberId));
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
			final String json = httpFetcher.get(apiUrlBuilder.getSquadUrl(instance, squadId));
			return jsonDeserializer.deserializeSquad(json);

		} catch (HttpNotFoundException e) {
			throw new UnknownSquadException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Outing> getOutings(String instance) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingsUrl(instance));
			return jsonDeserializer.deserializeListOfOutings(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Outing getOuting(String instance, String outingId) throws UnknownOutingException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getOutingUrl(instance, outingId));
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
			final String json = httpFetcher.get(apiUrlBuilder.getMembersUrl(instance));
			return jsonDeserializer.deserializeListOfMembers(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public List<Member> getSquadMembers(String instance, String squadId) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadMembersUrl(instance, squadId));
			return jsonDeserializer.deserializeListOfMembers(json);

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public List<OutingWithSquadAvailability> getSquadAvailability(String instance, String squadId, Date fromDate, Date toDate) {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getSquadAvailabilityUrl(instance, squadId, fromDate, toDate));
			return jsonDeserializer.deserializeSquadAvailability(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public List<AvailabilityOption> getAvailabilityOptions(String instance) throws HttpFetchException, JsonParseException, JsonMappingException, IOException {
		try {
			final String json = httpFetcher.get(apiUrlBuilder.getAvailabilityOptionsUrl(instance));
			return jsonDeserializer.deserializeAvailabilityOptions(json);
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public OutingAvailability setOutingAvailability(String instance, String memberId, String outingId, String availability) {
		try {
			final HttpPost post = new HttpPost(apiUrlBuilder.getOutingAvailabilityUrl(instance, outingId));
		
			final List<NameValuePair> nameValuePairs = Lists.newArrayList();
			nameValuePairs.add(new BasicNameValuePair("member", memberId));
			nameValuePairs.add(new BasicNameValuePair("availability", availability));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			return jsonDeserializer.deserializeOutingAvailability(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public Member createMember(String instance, String firstName, String lastName, Squad squad, String email, String password, Date dateOfBirth) throws InvalidMemberException {
		try {
			final HttpPost post = requestBuilder.buildCreateMemberRequest(instance, firstName, lastName, squad, email, password, dateOfBirth);
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
			final HttpPost post = new HttpPost(apiUrlBuilder.getSquadsUrl(instance));
		
			final List<NameValuePair> nameValuePairs = Lists.newArrayList();
			nameValuePairs.add(new BasicNameValuePair("name", name));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
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
			return jsonDeserializer.deserializeMemberDetails(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public Outing createOuting(String instance, String squad, LocalDateTime outingDate, String notes) throws InvalidOutingException {
		try {
			final HttpPost post = requestBuilder.buildCreateOutingPost(instance, squad, outingDate, notes);			
			return jsonDeserializer.deserializeOutingDetails(httpFetcher.post(post));
			
		} catch (HttpBadRequestException e) {
			throw new InvalidOutingException();
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
	public AvailabilityOption createAvailabilityOption(String instance, String label) {
		try {
			final HttpPost post = new HttpPost(apiUrlBuilder.getAvailabilityOptionsUrl(instance));
		
			final List<NameValuePair> nameValuePairs = Lists.newArrayList();
			nameValuePairs.add(new BasicNameValuePair("label", label));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));			
			return jsonDeserializer.deserializeAvailabilityOption(httpFetcher.post(post));
			
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}		
	}
	
}
