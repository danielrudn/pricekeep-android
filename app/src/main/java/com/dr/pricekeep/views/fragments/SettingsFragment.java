package com.dr.pricekeep.views.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dr.pricekeep.R;
import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.views.activities.LoginActivity;
import com.dr.pricekeep.views.activities.MainActivity;
import com.firebase.client.Firebase;

public class SettingsFragment extends Fragment {

    private Button mLogoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        mLogoutButton = (Button) settingsView.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return settingsView;
    }

    private void logout() {
        FirebaseBackend.getInstance(this.getActivity()).logout();
        startActivity(new Intent(this.getActivity(), LoginActivity.class));
        this.getActivity().finish();
    }

}
