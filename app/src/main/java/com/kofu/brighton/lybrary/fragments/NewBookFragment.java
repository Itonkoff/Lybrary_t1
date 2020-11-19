package com.kofu.brighton.lybrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kofu.brighton.lybrary.MainActivityCallBacks;
import com.kofu.brighton.lybrary.R;
import com.kofu.brighton.lybrary.models.resources.Book;
import com.kofu.brighton.lybrary.network.APIService;
import com.kofu.brighton.lybrary.network.APIServiceBuilder;

import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextInputEditText mTitleField;
    private TextInputEditText mVersionField;
    private TextInputEditText mAuthorField;
    private APIService mApiService;

    public NewBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewBookFragment newInstance(String param1, String param2) {
        NewBookFragment fragment = new NewBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleField = view.findViewById(R.id.tiet_title);
        mVersionField = view.findViewById(R.id.tiet_version);
        mAuthorField = view.findViewById(R.id.tiet_author);

        MainActivityCallBacks activity = (MainActivityCallBacks) getActivity();
        activity.hideFab();

        mApiService = APIServiceBuilder.buildService(APIService.class);

        view.findViewById(R.id.bn_save_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBook();
            }
        });
    }

    private void saveBook() {
        String title = mTitleField.getText().toString();
        String version = mVersionField.getText().toString();
        String author = mAuthorField.getText().toString();

        Call<Book> bookCall = mApiService.saveNewBook(new Book(title, version, author));

        bookCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Book saved", Toast.LENGTH_LONG).show();
                    mTitleField.setText("");
                    mVersionField.setText("");
                    mAuthorField.setText("");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(getActivity(), "Could not save new book", Toast.LENGTH_LONG).show();
            }
        });
    }
}