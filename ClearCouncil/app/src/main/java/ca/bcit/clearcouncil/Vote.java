package ca.bcit.clearcouncil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Vote {


    private String meetingType;
    private String voteDate;
    private String voteNumber;
    private String description;
    private String decision;
    private ArrayList<CouncilMember> councilMembers;

    // Holds a list fo Votes
    public static ArrayList<Vote> votes = new ArrayList<>();

    public Vote(String voteNumber, String meetingType, String voteDate, String description, String decision) {
        this.voteNumber = voteNumber;
        this.meetingType = meetingType;
        this.voteDate = voteDate;
        this.description = description;
        this.decision = decision;
    }

    public static void updateVotes(ArrayList<Vote> newVote) {
        if (newVote.size() == 0) {
            return;
        }
        if (votes.size() == 0) {
            votes = newVote;
        } else {
            if (votes.get(votes.size() - 1).getVoteNumber().equals(newVote.get(0).getVoteNumber())) {
                votes.remove(votes.size() - 1);
            }
            votes.addAll(newVote);
        }
    }

    public String getVoteNumber() {
        return voteNumber;
    }

    public String getMeetingType() {
        switch (this.meetingType) {
            case "Regular Council":
                return "Regular";
            case "City Finance & Services":
                return "Finance";
            case "Public Hearing":
                return "Hearing";
            case "Policy & Strategic Priorities":
                return "Policy";
            case "Council":
            case "Special Council":
                return "Council";
            case "Courts of Revision":
                return "Revision";
        }
        return "Misc";
    }

    public String getVoteDate() {
        String formattedDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = dateFormat.parse(voteDate);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM d, yyyy");
            formattedDate = targetFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (formattedDate.isEmpty()) {
            return voteDate;
        } else {
            return formattedDate;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getDecision() {
        if (decision.equals("Carried") || decision.equals("Carried Unanimously")) {
            return "Carried";
        } else {
            return decision;
        }
    }

    public ArrayList<CouncilMember> getCouncilMembers() {
        return councilMembers;
    }

    public void setCouncilMembers(ArrayList<CouncilMember> councilMembers) {
        this.councilMembers = councilMembers;
    }

    public String returnImage() {
        switch (this.meetingType) {
            case "Regular Council":
                return "@drawable/regular";
            case "City Finance & Services":
                return "@drawable/finance";
            case "Public Hearing":
                return "@drawable/hearing";
            case "Policy & Strategic Priorities":
                return "@drawable/policy";
            case "Council":
            case "Special Council":
                return "@drawable/council";
            case "Courts of Revision":
                return "@drawable/revision";
        }
        return "@drawable/error";
    }

    public String toString() {
        return this.voteNumber;
    }
}

