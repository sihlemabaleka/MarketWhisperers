package com.android.markwhisperers.marketwhispererstradingjournal;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.markwhisperers.marketwhispererstradingjournal.adapter.MyTradeRecyclerViewAdapter;
import com.android.markwhisperers.marketwhispererstradingjournal.model.Trade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClosedTradeFragment extends Fragment {


    List<Trade> trades = new ArrayList<>();
    MyTradeRecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    FirebaseUser user;
    List<DataSnapshot> snapshots = new ArrayList<>();
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public ClosedTradeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_list, container, false);

        adapter = new MyTradeRecyclerViewAdapter(getActivity(), trades, "closed", snapshots);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);

            mDatabase.child("users").child(user.getUid()).child("trades").child("history").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    trades.clear();
                    System.out.println(dataSnapshot.getValue());
                    for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                        System.out.println(snapShot.getValue());
                        Trade trade = new Trade();
                        trade.setTradePair(snapShot.child("pair").getValue().toString().trim());
                        trade.setTradePrice(String.valueOf(snapShot.child("price").getValue()));
                        trade.setTradePosition(snapShot.child("position").getValue().toString().trim());
                        trade.setTradeStopLoss(String.valueOf(snapShot.child("stop_loss").getValue()));
                        trade.setTradeTakeProfit(String.valueOf(snapShot.child("take_profit").getValue()));
                        trade.setTradeImage(String.valueOf(snapShot.child("image").getValue()));
                        trade.setTradePipsToTakeProfit(String.valueOf(snapShot.child("pips_to_tp").getValue()));
                        trade.setTradePipsToStopLoss(String.valueOf(snapShot.child("pip_to_sl").getValue()));
                        trade.setTradeImage(String.valueOf(snapShot.child("image").getValue()));
                        trades.add(trade);
                        snapshots.add(snapShot);
                    }

                    Collections.reverse(snapshots);
                    Collections.reverse(trades);

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }
}
