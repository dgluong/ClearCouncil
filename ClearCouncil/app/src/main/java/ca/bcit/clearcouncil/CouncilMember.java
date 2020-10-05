package ca.bcit.clearcouncil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a council member
 */
public class CouncilMember implements Parcelable {
    private static final String IN_FAVOUR_STR = "In Favour";
    private static final String IN_OPPOSITION_STR = "In Opposition";
    private static final String ABSENT_STR = "Absent";
    private static final String ABSTAIN_STR = "Abstain";
    private static final String NO_VOTE_STR = "No Vote";
    private static final String DECLARED_CONFLICT_STR = "Declared Conflict";

    private String name;
    private Position position;

    public CouncilMember(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    protected CouncilMember(Parcel in) {
        name = in.readString();
        position = Position.valueOf(in.readString());
    }

    public static final Creator<CouncilMember> CREATOR = new Creator<CouncilMember>() {
        @Override
        public CouncilMember createFromParcel(Parcel in) {
            return new CouncilMember(in);
        }

        @Override
        public CouncilMember[] newArray(int size) {
            return new CouncilMember[size];
        }
    };

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name + ": " + position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.position.name());
    }

    public enum Position {
        IN_FAVOUR(IN_FAVOUR_STR),
        IN_OPPOSITION(IN_OPPOSITION_STR),
        ABSENT(ABSENT_STR),
        ABSTAIN(ABSTAIN_STR),
        NO_VOTE(NO_VOTE_STR),
        DECLARED_CONFLICT(DECLARED_CONFLICT_STR);

        private final String text;

        /**
         * @param text String for ENUM value
         */
        Position(final String text) {
            this.text = text;
        }

        public static Position get(String code) {
            switch (code) {
                case IN_FAVOUR_STR:
                    return IN_FAVOUR;
                case IN_OPPOSITION_STR:
                    return IN_OPPOSITION;
                case ABSENT_STR:
                    return ABSENT;
                case ABSTAIN_STR:
                    return ABSTAIN;
                case NO_VOTE_STR:
                    return NO_VOTE;
                case DECLARED_CONFLICT_STR:
                    return DECLARED_CONFLICT;
            }
            return null;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
