package ca.bcit.clearcouncil.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.bcit.clearcouncil.CouncilMember;
import ca.bcit.clearcouncil.Vote;

/**
 * Utilities for parsing api response
 */
public class JSONParseUtils {
    /**
     * Parse JSON response containing vote details and converts it to list of votes
     *
     * @param response JSON response
     * @return ArrayList of Votes
     */
    public static ArrayList<Vote> parseAllVotes(String response) {
        ArrayList<Vote> allVotes = new ArrayList<>();
        try {
            JSONObject responseObject = new JSONObject(response);
            JSONArray voteRecords = responseObject.getJSONArray("records");
            String currentVoteNumber = "";
            for (int i = 0; i < voteRecords.length(); i++) {
                JSONObject voteObject = voteRecords.getJSONObject(i);
                JSONObject voteFields = voteObject.getJSONObject("fields");
                String voteNumber = voteFields.getString("vote_number");

                if (!voteNumber.equals(currentVoteNumber)) {
                    String meetingType = voteFields.getString("meeting_type");
                    String voteDate = voteFields.getString("vote_date");
                    String description = voteFields.getString("agenda_description");
                    String decision = voteFields.getString("decision");
                    Vote vote = new Vote(voteNumber, meetingType, voteDate, description, decision);

                    allVotes.add(vote);
                    currentVoteNumber = voteNumber;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allVotes;
    }

    /**
     * Parse JSON response for individual vote and returns council members
     * Use existing Vote from votes list and append council members
     *
     * @param response JSON response
     * @return Single Vote with details
     */
    public static ArrayList<CouncilMember> parseCouncilMembers(String response) {
        ArrayList<CouncilMember> members = new ArrayList<>();
        JSONObject responseObject;
        try {
            responseObject = new JSONObject(response);
            JSONArray voteRecords = responseObject.getJSONArray("records");

            for (int i = 0; i < voteRecords.length(); i++) {
                JSONObject voteObject = voteRecords.getJSONObject(i);
                JSONObject voteFields = voteObject.getJSONObject("fields");
                String memberName = voteFields.getString("council_member");
                String vote = voteFields.getString("vote");
                CouncilMember.Position position = CouncilMember.Position.get(vote);
                members.add(new CouncilMember(memberName, position));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return members;
    }
}
