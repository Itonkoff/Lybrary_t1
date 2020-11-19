package com.kofu.brighton.lybrary.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.kofu.brighton.lybrary.MainActivityCallBacks;
import com.kofu.brighton.lybrary.R;
import com.kofu.brighton.lybrary.models.resources.Book;
import com.kofu.brighton.lybrary.models.resources.BorrowBook;
import com.kofu.brighton.lybrary.network.APIService;
import com.kofu.brighton.lybrary.network.APIServiceBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mBookId;
    private String mStudentRef;
    private APIService mApiService;
    private CodeScanner mCodeScanner;
    private View mView;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
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
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;

        mBookId = ScanFragmentArgs.fromBundle(getArguments()).getBook();

        MainActivityCallBacks activity = (MainActivityCallBacks) getActivity();
        activity.hideFab();

        verifyPermissions();

        mApiService = APIServiceBuilder.buildService(APIService.class);
        Call<Book> bookByIdCall = mApiService.getBookById(mBookId);
        bookByIdCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Toast.makeText(getActivity(), "Book: " + response.body().name + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed Profusely", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpCodeScanner() {
        CodeScannerView codeScannerView = mView.findViewById(R.id.scanner_v);
        mCodeScanner = new CodeScanner(getActivity(), codeScannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStudentRef = result.getText();
                        Button lendButton = mView.findViewById(R.id.bn_borrow);
                        lendButton.setEnabled(true);
                        lendButton.setVisibility(View.VISIBLE);
                        lendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Call<BorrowBook> borrowBookCall =
                                        mApiService.borrowBook(new BorrowBook(mBookId, mStudentRef));

                                borrowBookCall.enqueue(new Callback<BorrowBook>() {
                                    @Override
                                    public void onResponse(Call<BorrowBook> call, Response<BorrowBook> response) {
                                        if (response.code() == 200) {
                                            Toast.makeText(getActivity(),"Book lent", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(),"Something went wrong in trying lend book", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BorrowBook> call, Throwable t) {
                                        Toast.makeText(getActivity(),"Failure", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
//                        Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void verifyPermissions() {
        int permissionCheck =
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            setUpCodeScanner();
        } else {
            String[] permissions = {Manifest.permission.CAMERA};
            requestPermissions(permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        mView = null;
        super.onPause();
    }
}