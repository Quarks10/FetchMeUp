package com.urbantechies.fetch_me_up.drivers;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.RunCustomRide;
import com.urbantechies.fetch_me_up.model.RideData;
import com.urbantechies.fetch_me_up.model.User;
import com.urbantechies.fetch_me_up.model.UserLocation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditCustomRide extends Fragment implements View.OnClickListener {

    private String TAG = "AddCustomRide";

    private FirebaseFirestore mDb;
    private EditText mDestination, mPickUp, mDate, mTime, mFare, mMaxPassenger;
    private Button btnEditRide, btnDeleteRide;
    private RideData mRideData;
    private ArrayList<RideData> mRideDataList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            final ArrayList<RideData> tempRideDataList = getArguments().getParcelableArrayList("ridedata");
            mRideDataList.clear();
            mRideDataList.addAll(tempRideDataList);
        }


        for (RideData tempRideData: mRideDataList) {
                mRideData = tempRideData;
        }

        mDb = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_custom_ride, container, false);

        btnEditRide = view.findViewById(R.id.UpdateRide);
        btnEditRide.setOnClickListener(this);

        btnDeleteRide = view.findViewById(R.id.DeleteRide);
        btnDeleteRide.setOnClickListener(this);

        mDestination = view.findViewById(R.id.txt_destination_ride);
        mPickUp = view.findViewById(R.id.txt_pickup_ride);
        mDate = view.findViewById(R.id.txt_date_ride);
        mTime = view.findViewById(R.id.txt_time_ride);
        mFare = view.findViewById(R.id.txt_fare_ride);
        mMaxPassenger = view.findViewById(R.id.txt_person_ride);

        mDestination.setText(mRideData.getDestination());
        mPickUp.setText(mRideData.getPickup());
        mDate.setText(mRideData.getDate());
        mTime.setText(mRideData.getTime());
        mFare.setText(mRideData.getFare());
        mMaxPassenger.setText(mRideData.getMaxpassenger());


        return view;
    }

    public static EditCustomRide newInstance() {
        return new EditCustomRide();
    }



    private void updateTrip(RideData rideData){

        RideData tempRideData = new RideData (rideData.getDriver(),rideData.getPassenger(), rideData.getId(), rideData.getStatus(), mDestination.getText().toString(),
                mPickUp.getText().toString(), mDate.getText().toString(), mTime.getText().toString(),
                mMaxPassenger.getText().toString(), mFare.getText().toString());


        DocumentReference endRef = mDb.collection(getString(R.string.collection_ride_available))
                .document(rideData.getId());

        endRef.set(tempRideData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        inflateCustomRideFragment();
                        Log.d(TAG, "onComplete: finish trip");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void deleteTrip(RideData rideData){

        DocumentReference endRef = mDb.collection(getString(R.string.collection_ride_available))
                .document(rideData.getId());

        endRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        inflateCustomRideFragment();
                        Log.d(TAG, "onComplete: finish trip");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


    private void inflateCustomRideFragment() {

        RunCustomRide fragment = RunCustomRide.newInstance();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UpdateRide: {
                updateTrip(mRideData);
                break;
            }

            case R.id.DeleteRide: {
                deleteTrip(mRideData);
                break;
            }
        }
    }
}
