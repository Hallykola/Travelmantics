package com.example.travelmantics;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseUtil {
    public static FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference mDatabaseReference = mfirebaseDatabase.getReference().child("travelmantics");
    public static ArrayList<TravelDeal> Deals = new ArrayList<>();
    public static FirebaseAuth mfirebaseauth;
    public static FirebaseAuth.AuthStateListener mfirebaseauthListener;
    private static final int RC_SIGN_IN = 123;
    public static Boolean isAdmin = false;
    public static String uid;
    public static FirebaseStorage mFirebasestorage;
    public static StorageReference mStorageReference;


    private FirebaseUtil() {
    }

    public static void Firebasesignmethod(final Activity act1) {
        mfirebaseauth = FirebaseAuth.getInstance();
        mfirebaseauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // already signed in
                    uid = firebaseAuth.getUid();
                    check(uid,act1);
                    Toast.makeText(act1.getBaseContext(),"Welcome back "+firebaseAuth.getCurrentUser().getDisplayName() + "!",Toast.LENGTH_LONG).show();
                } else {
                    // not signed in
                    signin(act1);
                }

            }

        };

        }
        public static void check(String uid, final Activity acty){
        isAdmin = false;
       DatabaseReference ref = FirebaseUtil.mDatabaseReference.child("administrators").child(uid);
       ChildEventListener listener  = new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               isAdmin = true;
               try {
                   ((Viewedit) acty).showmenu();
               }catch(Exception e){

                   ((Deals) acty).showmenu();
               }

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
       ref.addChildEventListener(listener);

        }

        public static void add () {
            ChildEventListener myChildmagic = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                        Deals.add(dataSnapshot.getValue(TravelDeal.class));

                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDatabaseReference.addChildEventListener(myChildmagic);
        }

        public static void attachListener () {
            mfirebaseauth.addAuthStateListener(mfirebaseauthListener);
        }
        public static void dettachListener () {
            mfirebaseauth.removeAuthStateListener(mfirebaseauthListener);
        }


    public static void signin(Activity act2) {
        act2.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }
    public static void connectStorage(){
        mFirebasestorage = mFirebasestorage.getInstance();
        mStorageReference = mFirebasestorage.getReference().child("deals_pictures");
    }
}
