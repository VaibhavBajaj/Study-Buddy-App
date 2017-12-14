package edu.illinois.finalproject.parser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Meeting implements Parcelable {

    private String id;
    private String name;
    private String location;
    private String time;
    private List<String> buddyIds;
    private List<String> buddyNames;

    public Meeting() {}

    public Meeting(String id, String name, String location, String time, List<String> buddyIds,
                   List<String> buddyNames) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.time = time;
        this.buddyIds = buddyIds;
        this.buddyNames = buddyNames;
    }

    protected Meeting(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readString();
        time = in.readString();
        buddyIds = in.createStringArrayList();
        buddyNames = in.createStringArrayList();
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public List<String> getBuddyIds() {
        return buddyIds;
    }

    /**
     * Returns buddy names as a comma-separated list
     * @return String of comma-separated buddy names.
     */
    public String extractBuddyNamesStr() {
        if (buddyNames == null || buddyNames.size() == 0) {
            return "";
        }

        StringBuilder buddyNamesStr = new StringBuilder();
        for (int i = 0; i < buddyNames.size() - 1; i++) {
            buddyNamesStr.append(buddyNames.get(i));
            buddyNamesStr.append(", ");
        }
        buddyNamesStr.append(buddyNames.get(buddyNames.size() - 1));

        return buddyNamesStr.toString();
    }

    public List<String> getBuddyNames() {
        return buddyNames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(time);
        parcel.writeStringList(buddyIds);
        parcel.writeStringList(buddyNames);
    }
}
