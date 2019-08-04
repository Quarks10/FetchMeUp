package com.urbantechies.fetch_me_up.drivers;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.model.RideData;
import com.urbantechies.fetch_me_up.model.User;
import com.urbantechies.fetch_me_up.model.UserLocation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomRideFragment extends Fragment implements View.OnClickListener {

    private String TAG = "CustomRideFragment";

    private Button btnAddRide;
    private FirebaseFirestore mDb;
    private ListenerRegistration mRideAvailableEventListener;
    private User user;
    private ArrayList<RideData> mRideDataList = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private LinearLayout linearLayout;

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

        View view = inflater.inflate(R.layout.fragment_custom_ride, container, false);
        btnAddRide = view.findViewById(R.id.addcustombtn);
        btnAddRide.setOnClickListener(this);
        linearLayout =  view.findViewById(R.id.ride_list_layout);
        linearLayout.removeAllViews();
        getAllJobUser(view);

        return view;
    }

    public static CustomRideFragment newInstance() {
        return new CustomRideFragment();
    }

    private void inflateAddRideFragment() {

        AddCustomRide fragment = AddCustomRide.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void getAllJobUser(View view) {

        CollectionReference usersRef = mDb.collection(getString(R.string.collection_ride_available));

        mRideAvailableEventListener = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {

                    // Clear the list and add all the users again
                 //   mRideDataList.clear();
                  //  mRideDataList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        RideData rideData = doc.toObject(RideData.class);

                   //     Log.d(TAG, "got value luar: " + rideData.getDestination());
                  //      Log.d(TAG, "got value id: " + rideData.getId());
                        if (rideData.getDriver().getUser_id().equals(user.getUser_id())) {
                            //   mRideDataList.add(rideData);
                            Log.d(TAG, "got value: " + rideData.getDestination());
                            generateRideList(rideData,view);
                        }
                    }

                  //  Log.d(TAG, "onEvent: user list size: " + mUserList.size());
                }
            }
        });
    }


    //    private void generateAllApplyList(final String post_key, final ArrayList<Parent> parent) {
//
//        DatabaseReference refposting = FirebaseDatabase.getInstance().getReference("jobposting");
//        refposting.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    for (DataSnapshot datasnap : ds.getChildren()) {
//
//                        if (datasnap.child("status").getValue().toString().equals("Pending") && datasnap.getKey().equals(post_key)) {
//
//                            Parent currparent = new Parent();
//
//                            for (Parent p : parent) {
//                                if (ds.getKey().equals(p.parentID)) {
//                                    currparent = p;
//                                }
//                            }
//
//                            String parentname = currparent.getFirst_name() + " " + currparent.getLast_name();
//                            String childname = datasnap.child("child_name").getValue().toString();
//                            String childlevel = datasnap.child("level").getValue().toString();
//                            String childedulevel = datasnap.child("edu_level").getValue().toString();
//                            String location = datasnap.child("location").getValue().toString();
//                            String address = currparent.getAddress();
//                            String subject = datasnap.child("subject").getValue().toString();
//                            String date = datasnap.child("date").getValue().toString();
//
//                            generateApplyList(parentname, childname, childlevel, childedulevel, location, address, subject, date);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
    private void generateRideList(RideData rideData, View view) {

        LinearLayout mainLayout = view.findViewById(R.id.ride_list_layout);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.ridelistcard, mainLayout, false);

        Button contactbtn = myLayout.findViewById(R.id.edit_ride_btn);
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent toMessage = new Intent(TutorJobAppPending.this, MessageContentTutor.class);
                //  startActivity(toMessage);
            }
        });

        TextView id = myLayout.findViewById(R.id.id_text_ride);
        id.setText("ID: " + rideData.getId());

        TextView destination = myLayout.findViewById(R.id.destination_text_ride);
        destination.setText("Destination: " + rideData.getDestination());

        TextView pickup = myLayout.findViewById(R.id.pickup_text_ride);
        pickup.setText("Pick Up: " + rideData.getPickup());

        TextView passenger = myLayout.findViewById(R.id.passenger_text_ride);
        passenger.setText("Passenger: " + rideData.getPassenger().size() + "/" + rideData.getMaxpassenger());

        TextView datetime = myLayout.findViewById(R.id.datetime_text_ride);
        datetime.setText("DateTime: " + rideData.getDate() + " " + rideData.getTime());

        TextView fare = myLayout.findViewById(R.id.fare_text_ride);
        fare.setText("Fare: " + rideData.getFare());

        mainLayout.addView(myLayout);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addcustombtn: {
                inflateAddRideFragment();
                break;
            }
        }
    }
}
