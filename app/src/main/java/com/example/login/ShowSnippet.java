package com.example.login;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSnippet extends AppCompatActivity {
    Snippet snippet;
    ActivityResultLauncher<Intent> activityEditResultLauncher;
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_snippet);
        myWebView = (WebView) findViewById(R.id.webview);

        iniciaMostrar(getIntent().getExtras());

        activityEditResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println(result);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            iniciaMostrar(result.getData().getExtras());
                        }else{
                            System.out.println("ELSE");
                        }
                    }
                });
    }

    public void iniciaMostrar(Bundle datos){
        snippet = new Gson().fromJson(datos.getString("snippet"), Snippet.class);
        myWebView.loadUrl(snippet.getHighlight());
        setTitle(snippet.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                pulsarEdit();
                return true;
            case R.id.delete:
                deleteSnippets( Integer.parseInt(snippet.getId()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteSnippets(int num){
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().deleteSnippets(sharedPref.getString("token", " "),num);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "SOMETHING IS WRONG", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "AN ERROR HAS OCCURED", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void pulsarEdit(){
        Intent intent = new Intent(getApplicationContext(), EditSnippets.class);
        intent.putExtra("snippet", snippet.toString());
        activityEditResultLauncher.launch(intent);

    }
}