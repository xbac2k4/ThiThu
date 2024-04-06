package com.example.thithu.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thithu.Adapter.AdapterXeMay;
import com.example.thithu.Model.Response;
import com.example.thithu.Model.XeMay;
import com.example.thithu.R;
import com.example.thithu.Service.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    ArrayList<XeMay> list = new ArrayList<>();
    RecyclerView recyclerView;
    Toolbar toolbar;
    HttpRequest httpRequest = new HttpRequest();
    AdapterXeMay adapter;
    ArrayList<XeMay> listSeacrch = new ArrayList<>();
    SearchView searchView;
    EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcv);
//        searchView = findViewById(R.id.search_view);
        edt_search = findViewById(R.id.edt_search);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setSubtitle("Quản lý xe máy");

        handleCallData();

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ( i == EditorInfo.IME_ACTION_SEARCH) {
                    String key = edt_search.getText().toString().trim();
                    filterList(key);
                    return true;
                }
                return false;
            }
        });
    }

    private void filterList(String text) {
        if (!text.equals("")) {
            listSeacrch.clear();
            httpRequest.callAPI().searchXeMayByName(text).enqueue(new Callback<Response<ArrayList<XeMay>>>() {
                @Override
                public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            listSeacrch = response.body().getData();
                            getData(listSeacrch);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {

                }
            });
        } else {
            handleCallData();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            Intent intent = new Intent(MainActivity.this, AddXeMay.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void handleCallData() {
        httpRequest.callAPI().getXeMay().enqueue(new Callback<Response<ArrayList<XeMay>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        list = response.body().getData();
                        getData(list);
                        Log.d(TAG, "onResponse: " + list);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {

            }
        });
    }
    private void getData(ArrayList<XeMay> listXeMay) {
        adapter = new AdapterXeMay(MainActivity.this, listXeMay);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        handleCallData();
        super.onResume();
    }
}