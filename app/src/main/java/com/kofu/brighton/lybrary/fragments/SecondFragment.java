package com.kofu.brighton.lybrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kofu.brighton.lybrary.MainActivity;
import com.kofu.brighton.lybrary.MainActivityCallBacks;
import com.kofu.brighton.lybrary.R;
import com.kofu.brighton.lybrary.adapters.BookAdapter;
import com.kofu.brighton.lybrary.models.resources.Book;
import com.kofu.brighton.lybrary.network.APIService;
import com.kofu.brighton.lybrary.network.APIServiceBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {

    private RecyclerView mBooksRecycler;
    private APIService mApiService;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        MainActivityCallBacks activity = (MainActivityCallBacks) getActivity();
        activity.showFab();

        mBooksRecycler = view.findViewById(R.id.rv_books);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mBooksRecycler.setLayoutManager(layoutManager);

        mApiService = APIServiceBuilder.buildService(APIService.class);

        loadDataFromAPI();
    }

    private void loadDataFromAPI() {
        Call<List<Book>> allBooksCall = mApiService.getAllBooks();
        allBooksCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.code() == 200){
                    BookAdapter bookAdapter = new BookAdapter(getActivity(),response.body());
                    mBooksRecycler.setAdapter(bookAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to show books"
                        , Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}