package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Viewedit extends AppCompatActivity {
    TravelDeal deal;
    Button upload ;
    ImageView image;
    Activity act = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewedit);
        Intent i1 = getIntent();
         deal = (TravelDeal) i1.getSerializableExtra("deal");
        if (deal!=null){
            //new TravelDeal();
            placeDeal(deal);
        }else{
            deal = new TravelDeal();
        }
        image = findViewById(R.id.imgholder);
  upload = findViewById(R.id.uploadbutton);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_GET_CONTENT);
                i1.setType("images/jpeg");
                i1.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i1,"Insert picture"),42);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==42){
            FirebaseUtil.connectStorage();
            Uri imageUri = data.getData();
            StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getStorage().getDownloadUrl().toString();
                    deal.setImageurl(url);
                    showImage(url);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        FirebaseUtil.check(FirebaseUtil.uid,act);
        if(FirebaseUtil.isAdmin){
            menu.findItem(R.id.menusave).setVisible(true);
            menu.findItem(R.id.menudelete).setVisible(true);
            enableEditTexts(true);

        }else{
            menu.findItem(R.id.menusave).setVisible(false);
            menu.findItem(R.id.menudelete).setVisible(false);
            enableEditTexts(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menusave:
               save();
               clean();
                break;
            case R.id.menudelete:
                delete();
                gotolist();
                break;
            default:
                //
                break;
        }
        return super.onOptionsItemSelected(item);
    }
     public void save(){
         EditText title = findViewById(R.id.titleholder);
         EditText price = findViewById(R.id.priceholder);
         EditText desc = findViewById(R.id.descholder);
         ImageView image = findViewById(R.id.imgholder);
         deal.setDescription(desc.getText().toString());
         deal.setPrice(price.getText().toString());
         deal.setTitle(title.getText().toString());
         //TravelDeal newdeal = new TravelDeal(title.getText().toString(),desc.getText().toString(),price.getText().toString(),"");
         //ref.setValue(newdeal);
         if(deal.getId()==null){
             DatabaseReference ref = FirebaseUtil.mDatabaseReference.push();
             deal.setId(ref.getKey());
             ref.setValue(deal);
             //Toast.makeText(this,ref.getKey(),Toast.LENGTH_LONG).show();
         }else{
             FirebaseUtil.mDatabaseReference.child(deal.getId()).setValue(deal);
         }

    }
    public void clean(){
        EditText title = findViewById(R.id.titleholder);
        EditText price = findViewById(R.id.priceholder);
        EditText desc = findViewById(R.id.descholder);
        ImageView image = findViewById(R.id.imgholder);
        title.setText("");
        price.setText("");
        desc.setText("");
        Drawable db =  getResources().getDrawable(R.drawable.ic_launcher_foreground);
        image.setImageDrawable(db);
    }
    public void placeDeal(TravelDeal deal){
        EditText title = findViewById(R.id.titleholder);
        EditText price = findViewById(R.id.priceholder);
        EditText desc = findViewById(R.id.descholder);
        ImageView image = findViewById(R.id.imgholder);
        title.setText(deal.getTitle());
        price.setText(deal.getPrice());
        desc.setText(deal.getDescription());
        //Drawable db =  getResources().getDrawable(R.drawable.ic_launcher_foreground);
        //image.setImageDrawable(db);
        showImage(deal.getId());
    }
    public void delete(){
        if (deal.getId()==null){
            Toast.makeText(this,"Please save deal before deleting",Toast.LENGTH_LONG).show();
        }else {
            FirebaseUtil.mDatabaseReference.child(deal.getId()).removeValue();
           /* Query q =FirebaseUtil.mDatabaseReference.orderByKey().equalTo(deal.getId());
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //FirebaseUtil.mDatabaseReference.child(deal.getId()).removeValue();*/
        }
    }
    public void gotolist(){
        Intent i2 = new Intent(this, Deals.class);
        startActivity(i2);
    }
    public void showmenu(){
        invalidateOptionsMenu();
    }
    public void enableEditTexts(Boolean isEnabled){
        EditText title = findViewById(R.id.titleholder);
        EditText price = findViewById(R.id.priceholder);
        EditText desc = findViewById(R.id.descholder);
        title.setEnabled(isEnabled);
        price.setEnabled(isEnabled);
        desc.setEnabled(isEnabled);
    }
    public void showImage(String urle){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        image = findViewById(R.id.imgholder);
        if(urle!=null&&!urle.isEmpty()) {
            Picasso.get()
                    .load(urle)
                    .resize(width, width * 2 / 3)
                    .centerCrop()
                    .into(image);
        }
    }
}
