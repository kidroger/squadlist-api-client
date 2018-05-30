package uk.co.squadlist.web.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import uk.co.squadlist.web.model.Squad;

public class ApiUrlBuilder {

	private static final String UTF_8 = "UTF-8";

	private static DateTimeFormatter dateHourMinute = ISODateTimeFormat.dateHourMinute();

	private final String apiUrl;

	public ApiUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String getBoatUrl(String instance, String id) {
		return getBoatsUrl(instance) + "/" + id;
	}
	
	public String getBoatsUrl(String instance) {
		return getInstanceUrl(instance) + "/boats";
	}
	
	public String getMembersAvailabilityUrl(String memberId, Date fromDate, Date toDate) {
		final StringBuilder url = new StringBuilder(getMemberUrl(memberId) + "/availability");
		appendDates(url, fromDate, toDate);
		return url.toString();
	}

	public String getInstancesUrl() {
		return apiUrl + "/instances";
	}

	public String getInstancesUrl(String q) {
		return getInstancesUrl() + "?q=" + urlEncode(q);
	}

	public String getStatisticsUrl() {
		return apiUrl + "/statistics";
	}

	public String getSubscriptionRequestsUrl() {
		return apiUrl + "/subscription-requests";
	}

	public String getInstanceUrl(String instance) {
		return getInstancesUrl() + "/" + urlEncode(instance);
	}

	public String getSquadsUrl(String instance) {
		return getInstanceUrl(instance) + "/squads";
	}
	
	public String getSquadUrl(String instance, String squadId) {
		return getSquadsUrl(instance) + "/" + squadId;
	}

	public String getSquadAvailabilityUrl(String instance, String squadId, Date fromDate, Date toDate) {
		final StringBuilder url = new StringBuilder(getSquadUrl(instance, squadId) + "/availability");
		appendDates(url, fromDate, toDate);
		return url.toString();
	}

	public String getSquadMembersUrl(String instance, String squadId) {
		return getSquadUrl(instance, squadId) + "/members";
	}

	public String getOutingsUrl(String instance, List<Squad> squads, Date fromDate, Date toDate) {
		final StringBuilder url = new StringBuilder(getOutingsUrl(instance));
		appendDates(url, fromDate, toDate);

		List<String> squadIds = Lists.newArrayList();
		for(Squad squad: squads) {
			squadIds.add(squad.getId());
		}

		url.append("&squads=" + Joiner.on("%2E").join(squadIds));
		return url.toString();
	}

	public String getOutingsMonthsUrl(String instance, List<Squad> squads, Date fromDate, Date toDate) {
		final StringBuilder url = new StringBuilder(getOutingsUrl(instance) + "/months");
		appendDates(url, fromDate, toDate);

		List<String> squadIds = Lists.newArrayList();
		for(Squad squad: squads) {
			squadIds.add(squad.getId());
		}

		url.append("&squads=" + Joiner.on("%2E").join(squadIds));
		return url.toString();
	}

	public String getMembersUrl(String instance) {
		return getInstanceUrl(instance) + "/members";
	}

	public String getMemberUrl(String memberId) {
		return apiUrl + "/members/" + urlEncode(memberId);
	}

	public String getOutingsUrl(String instance) {
		return getInstanceUrl(instance) + "/outings";
	}

	public String getOutingUrl(String instance, String outingId) {
		return getOutingsUrl(instance) + "/" + urlEncode(outingId);
	}

	public String getOutingAvailabilityUrl(String instance, String outingId) {
		return getOutingUrl(instance, outingId) + "/availability";
	}

	public String getAvailabilityOptionsUrl(String instance) {
		return getInstanceUrl(instance) + "/availability/options";
	}

	public String getAvailabilityOptionUrl(String instance, String id) {
		return getAvailabilityOptionsUrl(instance) + "/" + id;
	}

	public String getFacebookAuthUrlFor(String instance){
		return getInstanceUrl(instance) + "/auth/facebook";
	}

	public String getVerifyUrl(){
		return apiUrl + "/verify";
	}

	public String getResetPasswordUrl(String instance) {
		return getInstanceUrl(instance) + "/reset-password";
	}

	public String getResetMemberPasswordUrl(String instance, String memberId) {
		return getInstanceUrl(instance) + "/reset-member-password";
	}

	public String getConfirmResetPasswordUrl(String instance) {
		return getResetPasswordUrl(instance) + "/confirm";
	}

	public String getInstanceStatisticsUrl(String instance) {
		return getInstanceUrl(instance) + "/statistics";
	}

	public String getSubscriptionRequestUrl(String id) {
		return getSubscriptionRequestsUrl() + "/" + id;
	}

	public String getAdminsUrl(String instance) {
		return getInstanceUrl(instance) + "/admins";
	}

	public String getRequestTokenUrl() {
		return apiUrl + "/oauth/token";
	}
	
	private void appendDates(final StringBuilder url, Date fromDate, Date toDate) {
		String joiner = "?";
		if (fromDate != null) {
			url.append(joiner + "fromDate=" + dateHourMinute.print(new DateTime(fromDate)));
			joiner = "&";
		}
		if (toDate != null) {
			url.append(joiner + "toDate=" + dateHourMinute.print(new DateTime(toDate)));
			joiner = "&";
		}
	}

	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}