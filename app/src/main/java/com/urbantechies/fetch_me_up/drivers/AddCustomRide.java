package com.urbantechies.fetch_me_up.drivers;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.model.RideData;
import com.urbantechies.fetch_me_up.model.User;
import com.urbantechies.fetch_me_up.model.UserLocation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomRide extends Fragment implements View.OnClickListener {

    private String TAG = "AddCustomRide";

    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private ArrayList<User> mPassenger = new ArrayList<>();
    private User user;
    private FirebaseFirestore mDb;
    private EditText mDestination, mPickUp, mDate, mTime, mFare, mMaxPassenger;
    private Button btnaddRide;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            final ArrayList<UserLocation> locations = getArguments().getParcelableArrayList(getString(R.string.intent_user_locations));
            mUserLocations.clear();
            mUserLocations.addAll(locations);
        }


        for (UserLocation userLocation : mUserLocations) {
            if (userLocation.getUser().getUser_id().equals(FirebaseAuth.getInstance().getUid())) {
                user = userLocation.getUser();
            }
        }

        mDb = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_custom_ride, container, false);

        btnaddRide = view.findViewById(R.id.addNewRide);
        btnaddRide.setOnClickListener(this);

        mDestination = view.findViewById(R.id.txt_destination_ride);
        mPickUp = view.findViewById(R.id.txt_pickup_ride);
        mDate = view.findViewById(R.id.txt_date_ride);
        mTime = view.findViewById(R.id.txt_time_ride);
        mFare = view.findViewById(R.id.txt_fare_ride);
        mMaxPassenger = view.findViewById(R.id.txt_person_ride);

        return view;
    }

    public static AddCustomRide newInstance() {
        return new AddCustomRide();
    }



    private void addNewTrip(){

        String id = mDb.collection(getString(R.string.collection_ride_available)).document().getId();
        RideData rideData = new RideData(user,mPassenger, id, "available", mDestination.getText().toString(),
                mPickUp.getText().toString(), mDate.getText().toString(), mTime.getText().toString(),
                mMaxPassenger.getText().toString(), mFare.getText().toString());


        DocumentReference endRef = mDb.collection(getString(R.string.collection_ride_available))
                .document(id);

        endRef.set(rideData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        inflateCustomRideFragment();
                        Log.d(TAG, "onComplete: Added to database ride available!");
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

        CustomRideFragment fragment = CustomRideFragment.newInstance();
        //Bundle bundle = new Bundle();
       // bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
       // fragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewRide: {
                addNewTrip();
                break;
            }
        }
    }
}
