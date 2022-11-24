package com.example.secondappfromgb;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note  implements Parcelable{
    private static final String NO_TEXT = "";
    private String name;
    private String description;
    private Calendar date;
    private String content;



    public Note(String name, String description, Calendar date, String content) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.content = content;
    }

    public Note(String name, Calendar date) {
        this.name = name;
        this.date = date;
        this.description = NO_TEXT;
        this.content = NO_TEXT;

    }


    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
        content = in.readString();
        date = (Calendar) in.readSerializable();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(content);
        parcel.writeSerializable(date);
    }
}
