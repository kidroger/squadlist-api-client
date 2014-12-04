package uk.co.squadlist.web.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import uk.co.squadlist.web.model.Availability;
import uk.co.squadlist.web.model.AvailabilityOption;
import uk.co.squadlist.web.model.Instance;
import uk.co.squadlist.web.model.Member;
import uk.co.squadlist.web.model.Outing;
import uk.co.squadlist.web.model.Squad;
import uk.co.squadlist.web.model.SubscriptionRequest;

import com.google.common.collect.Lists;

public class RequestBuilder {

	private static final String UTF8 = "UTF8";

	private final ApiUrlBuilder apiUrlBuilder;
	private final ObjectMapper objectMapper;

	public RequestBuilder(ApiUrlBuilder apiUrlBuilder) {
		this.apiUrlBuilder = apiUrlBuilder;
		this.objectMapper = new ObjectMapper();
	}

	public HttpPost buildCreateInstanceRequest(String id, String name, String timeZone, boolean availabilityVisible) throws JsonGenerationException, JsonMappingException, IOException {
		HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(new Instance(id, name, timeZone, availabilityVisible)), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getInstancesUrl());
		post.setEntity(entity);
		return post;
	}

	public HttpPut buildUpdateInstanceRequest(Instance instance) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpPut put = new HttpPut(apiUrlBuilder.getInstancesUrl() + "/" + instance.getId());
		HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(instance), UTF8);
		put.setEntity(entity);
		return put;
	}

	public HttpPost buildUpdateSubscriptionRequestRequest(SubscriptionRequest subscriptonRequest) throws UnsupportedCharsetException, JsonGenerationException, JsonMappingException, IOException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getSubscriptionRequestUrl(subscriptonRequest.getId()));
		HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(subscriptonRequest), UTF8);
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateMemberRequest(String instance, Member member) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(member), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getMembersUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateAvailabilityOptionRequest(String instance, AvailabilityOption availabilityOption) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(availabilityOption), UTF8);
		String availabilityOptionsUrl = apiUrlBuilder.getAvailabilityOptionsUrl(instance);
		final HttpPost post = new HttpPost(availabilityOptionsUrl);
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateMemberRequest(String instance, Member member) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(member), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberDetailsUrl(instance, member.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateSquadRequest(String instance, Squad squad) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(squad), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadsUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateMemberProfileImageRequest(String instance, Member member, byte[] image) {
		final HttpEntity entity = MultipartEntityBuilder.create().
				setMode(HttpMultipartMode.BROWSER_COMPATIBLE).
				addPart("image", new ByteArrayBody(image, "image")).
				build();

		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberDetailsUrl(instance, member.getId()) + "/profileimage");
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateSquadRequest(String instance, Squad squad) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(squad), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadUrl(instance, squad.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildSetSquadMembersRequest(String instance, String squadId, Set<String> members) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(members), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadUrl(instance, squadId) + "/members");
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildSetAvailabilityRequest(String instance, Member member, Outing outing, AvailabilityOption availabilityOption) throws JsonGenerationException, JsonMappingException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(new Availability(member, outing, availabilityOption)), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getOutingAvailabilityUrl(instance, outing.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateOutingPost(String instance, Outing outing, int repeats) throws JsonGenerationException, JsonMappingException, IOException {
		return buildOutingPostTo(outing, apiUrlBuilder.getOutingsUrl(instance), repeats);
	}

	public HttpPost buildUpdateOutingPost(String instance, Outing outing) throws JsonGenerationException, JsonMappingException, IOException {
		return buildOutingPostTo(outing, apiUrlBuilder.getOutingUrl(instance, outing.getId()), null);
	}

	public HttpPost buildResetPasswordRequest(String instance, String username) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getResetPasswordUrl(instance));

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildResetMemberPasswordRequest(String instance, String memberId) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getResetMemberPasswordUrl(instance, memberId));

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("member", memberId));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpDelete buildDeleteInstanceRequest(String id) {
		return new HttpDelete(apiUrlBuilder.getInstanceUrl(id));
	}

	public HttpDelete buildDeleteOutingeRequest(String id) {
		return new HttpDelete(apiUrlBuilder.getOutingsUrl(id));
	}
	
	public HttpDelete buildDeleteSubscriptionRequestRequest(String id) {
		return new HttpDelete(apiUrlBuilder.getSubscriptionRequestUrl(id));
	}

	public HttpPost buildConfirmPasswordRequest(String instance, String token) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getConfirmResetPasswordUrl(instance));

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildAuthPost(String instance, String username, String password) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getAuthUrlFor(instance));
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildAuthFacebookPost(String instance, String token) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getAuthUrlFor(instance) + "/facebook");
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildChangePasswordPost(String instance, String memberId, String currentPassword, String newPassword) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberDetailsUrl(instance, memberId) + "/password");
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("currentPassword", currentPassword));
		nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildCreateSubscriptionRequestRequest(SubscriptionRequest subscriptionRequest) throws UnsupportedCharsetException, JsonGenerationException, JsonMappingException, IOException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getSubscriptionRequestsUrl());
		post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(subscriptionRequest), UTF8));
		return post;
	}

	private HttpPost buildOutingPostTo(Outing outing, String url, Integer repeats) throws IOException, JsonGenerationException, JsonMappingException {
		final HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(outing), UTF8));
		if (repeats != null) {
			Header header = new BasicHeader("repeats", Integer.toString(repeats));
			post.addHeader(header);
		}
		return post;
	}

}