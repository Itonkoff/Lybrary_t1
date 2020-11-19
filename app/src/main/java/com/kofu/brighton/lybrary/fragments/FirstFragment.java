package com.kofu.brighton.lybrary.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.kofu.brighton.lybrary.MainActivityCallBacks;
import com.kofu.brighton.lybrary.R;
import com.kofu.brighton.lybrary.models.resources.Login;
import com.kofu.brighton.lybrary.models.resources.Token;
import com.kofu.brighton.lybrary.network.APIService;
import com.kofu.brighton.lybrary.network.APIServiceBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    APIService mAPIService;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: Entered");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onViewCreated(view, savedInstanceState);
        mAPIService = APIServiceBuilder.buildService(APIService.class);

        MainActivityCallBacks activity = (MainActivityCallBacks) getActivity();
        activity.hideFab();

        if(!TextUtils.isEmpty(activity.getToken()))
            proceedToHome();

        view.findViewById(R.id.bn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation
                        .findNavController(getActivity().findViewById(R.id.nav_host_fragment))
                        .navigate(FirstFragmentDirections.actionFirstFragmentToRegistrationFragment());
            }
        });

        Log.d(TAG, "onClick: Initialising text fields");
        TextInputEditText emailField = view.findViewById(R.id.tiet_email);
        TextInputEditText passwordField = view.findViewById(R.id.tiet_password);

        view.findViewById(R.id.bn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Getting login fields");
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                Log.d(TAG, "onClick: Constructing call");
                Call<Token> loginCall = mAPIService
                        .login(new Login(email, password));

                Log.d(TAG, "onClick: Enqueuing LOGIN Call");
                loginCall.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        Log.d(TAG, "onResponse: SUCCESS response");
                        if (response.code() == 200) {
                            MainActivityCallBacks activity = (MainActivityCallBacks) getActivity();
                            activity.setToken(response.body().token);
                            proceedToHome();
                        } else {
                            Toast.makeText(getActivity(),"either your email or password is incorrect",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.d(TAG, "onFailure: FAILURE Response");
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void proceedToHome() {
        Navigation
                .findNavController(getActivity().findViewById(R.id.nav_host_fragment))
                .navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment());
    }
}