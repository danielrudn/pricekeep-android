package com.dr.pricekeep.views.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dr.pricekeep.presenters.ItemListPresenter;
import com.dr.pricekeep.presenters.ItemListPresenterImpl;
import com.dr.pricekeep.views.activities.MainActivity;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.R;
import com.dr.pricekeep.views.activities.SingleItemActivity;
import com.dr.pricekeep.views.adapters.ItemAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ListItemFragment extends Fragment {

    protected ItemListPresenter mItemListPresenter = null;

    private RecyclerView mRecyclerView;
    private View mNoItemsView;

    private ProgressDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundledInstanceState) {
        View listItemView = inflater.inflate(R.layout.fragment_list_item, container, false);
        // create and populate recycler view
        mRecyclerView = (RecyclerView) listItemView.findViewById(R.id.list_item_recycler_view);
        mNoItemsView = listItemView.findViewById(R.id.no_items_view);
     //  recyclerView.setHasFixedSize(true);
        // set layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mItemListPresenter.initializeItems();
        // show loading dialog while items load
        loadingDialog = ProgressDialog.show(this.getActivity(), "", "Loading...");
        return listItemView;
    }

    public void setItemAdapter(ItemAdapter adapter) {
        Log.d(MainActivity.TAG, "ItemCount: " + adapter.getItemCount());
        // show or hide no items view
        if(adapter.getItemCount() == 0) {
            mNoItemsView.setVisibility(View.VISIBLE);
        } else {
          mNoItemsView.setVisibility(View.INVISIBLE);
        }
        mRecyclerView.setAdapter(adapter);
        loadingDialog.dismiss();
    }

    // temporary
    public void handleFABClick() {
        // add item
        final Firebase mRef = new Firebase(MainActivity.FIREBASE_URL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(LayoutInflater.from(this.getActivity())
                    .inflate(R.layout.dialog_add_item, null))
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog addItemDialog = (AlertDialog)dialog;
                        EditText url = (EditText) addItemDialog.findViewById(R.id.add_item_url);
                        EditText price = (EditText) addItemDialog.findViewById(R.id.add_item_price);
                        Spinner updateInterval = (Spinner) addItemDialog.findViewById(R.id.add_item_spinner);
                        Map<String, String> itemMap = new HashMap<String, String>();
                        itemMap.put("url", url.getText().toString());
                        itemMap.put("startPrice", price.getText().toString());
                        itemMap.put("updateInterval", updateInterval.getSelectedItem().toString());
                        itemMap.put("uid", mRef.getAuth().getUid());
                        mRef.child("addQueue/" + UUID.randomUUID().toString()).setValue(itemMap);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // Create and show the dialog
        AlertDialog addItemDialog = builder.create();
        addItemDialog.getWindow().getAttributes().windowAnimations = R.style.ShowDialogAnimation;
        addItemDialog.show();
        // Populate spinner
        Spinner updateIntervalSpinner = (Spinner)addItemDialog.findViewById(R.id.add_item_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(this.getActivity(), R.array.update_intervals, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateIntervalSpinner.setAdapter(spinnerAdapter);
        // Set url EditText to have text from clipboard
        final EditText urlText = (EditText) addItemDialog.findViewById(R.id.add_item_url);
        ClipboardManager clipboardManager = (ClipboardManager)this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        urlText.setText(clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(this.getActivity()));
        // Make the delete button work
        ImageView deleteURLButton = (ImageView) addItemDialog.findViewById(R.id.add_item_clear_url_btn);
        deleteURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlText.setText("");
            }
        });
        // set price EditText to be focused
        EditText priceText = (EditText) addItemDialog.findViewById(R.id.add_item_price);
        priceText.requestFocusFromTouch();
    }
}