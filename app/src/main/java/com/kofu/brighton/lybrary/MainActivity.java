package com.kofu.brighton.lybrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kofu.brighton.lybrary.fragments.FirstFragmentDirections;
import com.kofu.brighton.lybrary.fragments.SecondFragmentDirections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityCallBacks {

    public static final String TOKEN_LABEL = "token";
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private FloatingActionButton mFab;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = getPreferences(Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navigateToNewBook();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToken(String token) {
        mEditor.putString(TOKEN_LABEL, token);
        mEditor.apply();
    }

    @Override
    public String getToken() {
        return mPreferences.getString(TOKEN_LABEL, "");
    }

    @Override
    public void hideFab() {
        if (mFab.getVisibility() == View.VISIBLE) {
            mFab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showFab() {
        if (mFab.getVisibility() == View.INVISIBLE) {
            mFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateToNewBook() {
        Navigation
                .findNavController(this.findViewById(R.id.nav_host_fragment))
                .navigate(SecondFragmentDirections.actionSecondFragmentToNewBookFragment());
    }

    @Override
    public void bookSelected(int id) {
        Navigation
                .findNavController(this.findViewById(R.id.nav_host_fragment))
                .navigate(SecondFragmentDirections.actionSecondFragmentToScanFragment(id));
    }
}