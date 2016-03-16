package com.healthifyme.bhanuprasad.healthifymetask;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bhanuprasad on 3/15/2016.
 */
public class DayObject implements Parcelable {
    String end_time ;
    boolean is_booked ;
    boolean is_expired;
    String slot_id;
    String start_time ;

    protected DayObject(Parcel in) {
        end_time = in.readString();
        is_booked = in.readByte() != 0;
        is_expired = in.readByte() != 0;
        slot_id = in.readString();
        start_time = in.readString();
    }

    public static final Creator<DayObject> CREATOR = new Creator<DayObject>() {
        @Override
        public DayObject createFromParcel(Parcel in) {
            return new DayObject(in);
        }

        @Override
        public DayObject[] newArray(int size) {
            return new DayObject[size];
        }
    };

    public DayObject() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(end_time);
        dest.writeByte((byte) (is_booked ? 1 : 0));
        dest.writeByte((byte) (is_expired ? 1 : 0));
        dest.writeString(slot_id);
        dest.writeString(start_time);
    }
}
