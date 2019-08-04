package com.urbantechies.fetch_me_up.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.maps.model.LatLng;

import java.util.ArrayList;

public class RideData implements Parcelable {


    private User driver;
    private ArrayList<User> passenger;
    private String id;
    private String status;
    private String destination;
    private String pickup;
    private String date;
    private String time;
    private String maxpassenger;
    private String fare;

    public RideData(User driver, ArrayList<User> passenger, String id, String status, String destination, String pickup, String date, String time, String maxpassenger, String fare) {
        this.driver = driver;
        this.passenger = passenger;
        this.id = id;
        this.status = status;
        this.destination = destination;
        this.pickup = pickup;
        this.date = date;
        this.time = time;
        this.maxpassenger = maxpassenger;
        this.fare = fare;
    }

    public RideData() {
    }

    protected RideData(Parcel in) {
        driver = in.readParcelable(User.class.getClassLoader());
        passenger = in.createTypedArrayList(User.CREATOR);
        id = in.readString();
        status = in.readString();
        destination = in.readString();
        pickup = in.readString();
        date = in.readString();
        time = in.readString();
        maxpassenger = in.readString();
        fare = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(driver, flags);
        dest.writeTypedList(passenger);
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(destination);
        dest.writeString(pickup);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(maxpassenger);
        dest.writeString(fare);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RideData> CREATOR = new Creator<RideData>() {
        @Override
        public RideData createFromParcel(Parcel in) {
            return new RideData(in);
        }

        @Override
        public RideData[] newArray(int size) {
            return new RideData[size];
        }
    };

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public ArrayList<User> getPassenger() {
        return passenger;
    }

    public void setPassenger(ArrayList<User> passenger) {
        this.passenger = passenger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaxpassenger() {
        return maxpassenger;
    }

    public void setMaxpassenger(String maxpassenger) {
        this.maxpassenger = maxpassenger;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    @NonNull
    @Override
    public String toString() {
        return "JobData{" +
                "driver='" + driver + '\'' +
                ", passenger='" + passenger + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", destination='" + destination + '\'' +
                ", pickup='" + pickup + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", maxpassenger='" + maxpassenger + '\'' +
                ", fare='" + fare + '\'' +
                '}';
    }
}
