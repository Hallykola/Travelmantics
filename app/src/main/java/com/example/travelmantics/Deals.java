package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Deals extends AppCompatActivity {
RecyclerView r1;
Activity acty = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_one,menu);
        FirebaseUtil.check(FirebaseUtil.uid,acty);
        if(FirebaseUtil.isAdmin){
            menu.findItem(R.id.menunew).setVisible(true);
        }else{
            menu.findItem(R.id.menunew).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        r1 = findViewById(R.id.dealrecycler);
        FirebaseUtil.Firebasesignmethod(this);
        FirebaseUtil.attachListener();
        RecyclerView.Adapter adapter = new DealRecyclerAdapter(this);
        r1.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        r1.setLayoutManager(llm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.dettachListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menunew:
                gotoViewedit();
                break;
            case R.id.menulogout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                FirebaseUtil.attachListener();

                            }
                        });
                //FirebaseUtil.Firebasesignmethod(acty);
                FirebaseUtil.dettachListener();

                break;

            default:
                //
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoViewedit(){
        Intent i2 = new Intent(this, Viewedit.class);
        startActivity(i2);
    }
    public void showmenu(){
        invalidateOptionsMenu();
    }
}
