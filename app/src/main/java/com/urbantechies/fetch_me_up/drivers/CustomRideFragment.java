package com.urbantechies.fetch_me_up.drivers;


import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.UserClient;
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
    private User user;
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();

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
//    private void generateApplyList(String parentname, String childname, String childlevel, String childedulevel, String location, String address, String subject, String date) {
//        LinearLayout mainLayout = findViewById(R.id.jobAppPendingLayout);
//
//        LayoutInflater inflater = getLayoutInflater();
//        View myLayout = inflater.inflate(R.layout.tutor_job_app_pending, mainLayout, false);
//
//        Button contactbtn = myLayout.findViewById(R.id.contactParentbtn);
//        contactbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toMessage = new Intent(TutorJobAppPending.this, MessageContentTutor.class);
//                startActivity(toMessage);
//            }
//        });
//
//        TextView parentName = myLayout.findViewById(R.id.parentNametxt);
//        parentName.setText(parentname);
//
//        TextView childName = myLayout.findViewById(R.id.childNametxt);
//        childName.setText("Child Name: " + childname);
//
//        TextView childLevel = myLayout.findViewById(R.id.childEdulvltxt);
//        if (childedulevel.equals("Primary School")) {
//            childLevel.setText("Standard: " + childlevel);
//        } else {
//            childLevel.setText("Form: " + childlevel);
//        }
//
//        TextView subject_text = myLayout.findViewById(R.id.subjecttxt);
//        subject_text.setText("Subject: " + subject);
//
//        // TextView session = myLayout.findViewById(R.id.sessiontxt);
//        // session.setText("Subject: " + subject);
//
//        TextView dateTime = myLayout.findViewById(R.id.datetxt);
//        dateTime.setText("Start Date: " + date);
//
//        TextView locationName = myLayout.findViewById(R.id.loctxt);
//        if (location.equals("In my location")) {
//            locationName.setText("Location: " + address);
//        } else {
//            locationName.setText("Location: " + location);
//        }
//
//        mainLayout.addView(myLayout);
//
//    }

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
