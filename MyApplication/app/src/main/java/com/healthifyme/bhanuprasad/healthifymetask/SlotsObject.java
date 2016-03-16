package com.healthifyme.bhanuprasad.healthifymetask;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by bhanuprasad on 3/15/2016.
 */
public class SlotsObject implements Parcelable {
    String date;
    int afternoon_slots;
    int morning_slots;
    int evening_slots;
    List<DayObject> morning;
    List<DayObject> afternoon;
    List<DayObject> evening;


    protected SlotsObject(Parcel in) {
        date = in.readString();
        morning = in.createTypedArrayList(DayObject.CREATOR);
        afternoon = in.createTypedArrayList(DayObject.CREATOR);
        evening = in.createTypedArrayList(DayObject.CREATOR);
    }

    public static final Creator<SlotsObject> CREATOR = new Creator<SlotsObject>() {
        @Override
        public SlotsObject createFromParcel(Parcel in) {
            return new SlotsObject(in);
        }

        @Override
        public SlotsObject[] newArray(int size) {
            return new SlotsObject[size];
        }
    };

    public SlotsObject() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeTypedList(morning);
        dest.writeTypedList(afternoon);
        dest.writeTypedList(evening);
    }
}
