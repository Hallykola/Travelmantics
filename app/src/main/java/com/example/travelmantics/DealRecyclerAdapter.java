package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealRecyclerAdapter extends RecyclerView.Adapter<DealRecyclerAdapter.ViewHolder>  {
    Context ctx;
    ArrayList<TravelDeal>  deals = new ArrayList<>();
    //String[] test ={"Kareem", "Lateef"};
    DatabaseReference ref = FirebaseUtil.mDatabaseReference;
    ChildEventListener myChildmagic = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            /*for(DataSnapshot child: dataSnapshot.getChildren() ){
                //TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);

            }*/
            deals.add(dataSnapshot.getValue(TravelDeal.class));
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           // deals.add(dataSnapshot.getValue(TravelDeal.class));
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
           // deals.add(dataSnapshot.getValue(TravelDeal.class));
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public DealRecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        //FirebaseUtil.add();
        ref.addChildEventListener(myChildmagic);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(ctx).inflate(R.layout.deal_template,parent,false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(deals.get(position).title);
        holder.desc.setText(deals.get(position).description);
        holder.price.setText(deals.get(position).price);
        Picasso.get()
                .load(deals.get(position).imageurl)
                .resize(160, 160)
                .centerCrop()
                .into(holder.img);
        //holder.img.setImageBitmap(deals.get(position).title);
        //holder.title.setText(test[position]);


    }

    @Override
    public int getItemCount() {
        return deals.size();
        //return test.length;

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img ;
        TextView title, desc, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.dealimg);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent i1 = new Intent(ctx,Viewedit.class);
                    i1.putExtra("deal",deals.get(position));
                    ctx.startActivity(i1);
                }
            });
        }
    }

}
