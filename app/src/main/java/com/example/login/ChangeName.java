package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeName extends AppCompatActivity {
    EditText  txt_username, txt_firstName, txt_secondName;
    String credentials;
    Login  parameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        Bundle data =getIntent().getExtras();
        credentials = data.getString("variable");

        parameters = new Gson().fromJson(credentials, Login.class);
        System.out.println(parameters.getUser());

        setTitle(parameters.getUser().get("email").getAsString());

        txt_username = findViewById(R.id.txt_username);
        txt_firstName = findViewById(R.id.txt_firstName);
        txt_secondName = findViewById(R.id.txt_secondName);

    }

    public void EditUser(){
        if(txt_username.getText().toString().equals("") || txt_firstName.getText().toString().equals("") || txt_secondName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "MISSING FIELDS", Toast.LENGTH_LONG).show();
        }else{
            JsonObject paramObject = new JsonObject();
            try {
                paramObject.addProperty("username", txt_username.getText().toString());
                paramObject.addProperty("first_name", txt_firstName.getText().toString());
                paramObject.addProperty("last_name", txt_secondName.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(paramObject.toString());
            patchUser(paramObject);
        }
    }

    private void patchUser(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().patchUser(parameters.getToken(),body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Intent intent = getIntent();
                    intent.putExtra("variable", credentials);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }else{
                    System.out.println("SOMETHING IS WRONG");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.change_name_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnCommit:
                EditUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}