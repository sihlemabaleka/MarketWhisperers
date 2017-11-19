package com.android.markwhisperers.marketwhispererstradingjournal.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.markwhisperers.marketwhispererstradingjournal.R;
import com.android.markwhisperers.marketwhispererstradingjournal.ScrollingActivity;
import com.android.markwhisperers.marketwhispererstradingjournal.model.Trade;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyTradeRecyclerViewAdapter extends RecyclerView.Adapter<MyTradeRecyclerViewAdapter.ViewHolder> {

    private final List<Trade> mValues;
    private final List<DataSnapshot> snapshots;
    Activity activity;
    String tradeType;

    public MyTradeRecyclerViewAdapter(Activity activity, List<Trade> items, String tradeType, List<DataSnapshot> snapshots) {
        mValues = items;
        this.activity = activity;
        this.tradeType = tradeType;
        this.snapshots = snapshots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (tradeType == "closed") {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.closed_trade_adapter_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_trade, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mPair.setText(mValues.get(position).getTradePair());
        holder.mPosition.setText(mValues.get(position).getTradePosition() + " @ " + mValues.get(position).getTradePrice());
        holder.mStopLoss.setText("SL: " + mValues.get(position).getTradeStopLoss());
        holder.mTakeProfit.setText("TP: " + mValues.get(position).getTradeTakeProfit());

        Glide.with(activity).load(mValues.get(position).getTradeImage()).into(holder.mTradeImage);


        final DatabaseReference mDatabase;
        switch (tradeType) {
            case "pending":
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trades").child("active");
                break;
            case "active":
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trades").child("closed");
                break;
            default:
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trades").child("closed");
                break;
        }

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDatabase.push().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            dataSnapshot.getRef().setValue(mValues.get(position).toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        snapshots.get(position).getRef().removeValue();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return false;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ScrollingActivity.class);
                intent.putExtra("url", mValues.get(position).getTradeImage());
                intent.putExtra("pair", mValues.get(position).getTradePair());
                intent.putExtra("position", mValues.get(position).getTradePosition());
                intent.putExtra("price", mValues.get(position).getTradePrice());
                intent.putExtra("stop_loss", mValues.get(position).getTradeStopLoss());
                intent.putExtra("take_profit", mValues.get(position).getTradeTakeProfit());
                intent.putExtra("pips_to_sl", mValues.get(position).getTradePipsToStopLoss());
                intent.putExtra("pips_to_tp", mValues.get(position).getTradePipsToTakeProfit());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPosition;
        public final TextView mStopLoss;
        public final TextView mPair;
        public final TextView mTakeProfit;
        public final ImageView mTradeImage;
        public Trade mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosition = view.findViewById(R.id.position);
            mPair = view.findViewById(R.id.pair);
            mStopLoss = view.findViewById(R.id.stop_loss);
            mTakeProfit = view.findViewById(R.id.take_profit);
            mTradeImage = view.findViewById(R.id.picture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPair.getText() + "'";
        }
    }
}
