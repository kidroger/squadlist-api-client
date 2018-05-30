package uk.co.squadlist.web.api;

import com.google.common.collect.Lists;
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
import org.codehaus.jackson.map.ObjectMapper;
import uk.co.squadlist.web.model.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Set;

public class RequestBuilder {

	private static final String UTF8 = "UTF8";

	private final ApiUrlBuilder apiUrlBuilder;
	private final ObjectMapper objectMapper;

	public RequestBuilder(ApiUrlBuilder apiUrlBuilder) {
		this.apiUrlBuilder = apiUrlBuilder;
		this.objectMapper = new ObjectMapper();
	}

	public HttpPost buildCreateInstanceRequest(String id, String name, String timeZone, boolean availabilityVisible, String governingBody) throws IOException {
		HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(new Instance(id, name, timeZone, availabilityVisible, Lists.<Subscription>newArrayList(), governingBody)), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getInstancesUrl());
		post.setEntity(entity);
		return post;
	}

	public HttpPut buildUpdateInstanceRequest(Instance instance) throws IOException {
		final HttpPut put = new HttpPut(apiUrlBuilder.getInstancesUrl() + "/" + instance.getId());
		HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(instance), UTF8);
		put.setEntity(entity);
		return put;
	}

	public HttpPost buildCreateBoatRequest(String instance, Boat boat) throws UnsupportedCharsetException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(boat), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getBoatsUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateMemberRequest(String instance, Member member) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(member), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getMembersUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateAvailabilityOptionRequest(String instance, AvailabilityOption availabilityOption) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(availabilityOption), UTF8);
		String availabilityOptionsUrl = apiUrlBuilder.getAvailabilityOptionsUrl(instance);
		final HttpPost post = new HttpPost(availabilityOptionsUrl);
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateMemberRequest(Member member) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(member), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberUrl(member.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateSquadRequest(String instance, Squad squad) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(squad), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadsUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateAvailabilityOptionRequest(String instance, AvailabilityOption availabilityOption) throws UnsupportedCharsetException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(availabilityOption), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getAvailabilityOptionUrl(instance, availabilityOption.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateMemberProfileImageRequest(Member member, byte[] image) {
		final HttpEntity entity = MultipartEntityBuilder.create().
				setMode(HttpMultipartMode.BROWSER_COMPATIBLE).
				addPart("image", new ByteArrayBody(image, "image")).
				build();

		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberUrl(member.getId()) + "/profileimage");
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildUpdateSquadRequest(String instance, Squad squad) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(squad), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadUrl(instance, squad.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildSetSquadMembersRequest(String instance, String squadId, Set<String> members) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(members), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getSquadUrl(instance, squadId) + "/members");
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildSetAdminsRequest(String instance, Set<String> admins) throws UnsupportedCharsetException, IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(admins), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getAdminsUrl(instance));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildSetAvailabilityRequest(String instance, Member member, Outing outing, AvailabilityOption availabilityOption) throws IOException {
		final HttpEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(new Availability(member, outing, availabilityOption)), UTF8);
		final HttpPost post = new HttpPost(apiUrlBuilder.getOutingAvailabilityUrl(instance, outing.getId()));
		post.setEntity(entity);
		return post;
	}

	public HttpPost buildCreateOutingPost(String instance, Outing outing, int repeats) throws IOException {
		return buildOutingPostTo(outing, apiUrlBuilder.getOutingsUrl(instance), repeats);
	}

	public HttpPost buildUpdateOutingPost(String instance, Outing outing) throws IOException {
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

	public HttpDelete buildDeleteOutingRequest(String instance, String id) {
		return new HttpDelete(apiUrlBuilder.getOutingUrl(instance, id));
	}

	public HttpDelete buildDeleteSubscriptionRequestRequest(String id) {
		return new HttpDelete(apiUrlBuilder.getSubscriptionRequestUrl(id));
	}

	public HttpDelete buildDeleteAvailablityOptionRequest(String instance, String id) {
		return new HttpDelete(apiUrlBuilder.getAvailabilityOptionUrl(instance, id));
	}

	public HttpDelete buildDeleteMemberRequest(Member member) {
		return new HttpDelete(apiUrlBuilder.getMemberUrl(member.getId()));
	}

	public HttpDelete buildDeleteSquadRequest(String instance, String id) {
		return new HttpDelete(apiUrlBuilder.getSquadUrl(instance, id));
	}

	public HttpDelete buildDeleteAvailabilityOptionRequest(String instance, AvailabilityOption availabilityOption) {
		return new HttpDelete(apiUrlBuilder.getAvailabilityOptionUrl(instance, availabilityOption.getId()));
	}

	public HttpDelete buildDeleteAvailabilityOptionRequest(String instance, AvailabilityOption availabilityOption, AvailabilityOption alternative) {
		return new HttpDelete(apiUrlBuilder.getAvailabilityOptionUrl(instance, availabilityOption.getId()) + "?alternative=" + alternative.getId());
	}

	public HttpPost buildConfirmPasswordRequest(String instance, String token) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getConfirmResetPasswordUrl(instance));

		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildAuthFacebookPost(String instance, String token) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getFacebookAuthUrlFor(instance));
    final List<NameValuePair> nameValuePairs = Lists.newArrayList();
    nameValuePairs.add(new BasicNameValuePair("token", token));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	public HttpPost buildVerifyPost(String token) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getVerifyUrl());
		post.setHeader("Authorization", "Bearer " + token);
		return post;
	}

	public HttpPost buildChangePasswordPost(String memberId, String currentPassword, String newPassword) throws UnsupportedEncodingException {
		final HttpPost post = new HttpPost(apiUrlBuilder.getMemberUrl(memberId) + "/password");
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("currentPassword", currentPassword));
		nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		return post;
	}

	private HttpPost buildOutingPostTo(Outing outing, String url, Integer repeats) throws IOException {
		final HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(outing), UTF8));
		if (repeats != null) {
			Header header = new BasicHeader("repeats", Integer.toString(repeats));
			post.addHeader(header);
		}
		return post;
	}

}