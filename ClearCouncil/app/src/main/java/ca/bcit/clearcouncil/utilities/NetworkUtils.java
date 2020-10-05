package ca.bcit.clearcouncil.utilities;

import android.util.Log;

import java.util.Objects;

import okhttp3.HttpUrl;

/**
 * Utilities for urls for api calls
 */
public final class NetworkUtils {
    private static final String baseUrl = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=council-voting-records";
    private static final int ROWS = 100;

    private static String getPage(int page) {
        return Integer.toString(page * ROWS);
    }


    public static HttpUrl allVotesUrl(int page) {
        return Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder()
                .addQueryParameter("rows", Integer.toString(ROWS))
                .addQueryParameter("start", getPage(page))
                .addQueryParameter("sort", "vote_detail_id")
                .build();
    }

    public static HttpUrl searchVoteUrl(String query, int page) {
        return Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder()
                .addQueryParameter("rows", Integer.toString(ROWS))
                .addQueryParameter("start", getPage(page))
                .addQueryParameter("sort", "vote_detail_id")
                .addQueryParameter("q", query)
                .build();
    }

    public static HttpUrl councilMembersUrl(String voteNumber) {
        return Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder()
                .addQueryParameter("rows", Integer.toString(ROWS))
                .addQueryParameter("facet", "council_member")
                .addQueryParameter("facet", "vote")
                .addQueryParameter("refine.vote_number", voteNumber)
                .build();
    }
}
