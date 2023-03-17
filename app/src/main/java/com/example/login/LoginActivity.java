package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText txt_username, txt_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();
                JsonObject paramObject = new JsonObject();
                try {
                   paramObject.addProperty("username", username);
                   paramObject.addProperty("password", password);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getLogins(paramObject);
            }
        });
    }

    public void getLogins(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().getLogins(body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {

                    txt_username.getText().clear();

                    Intent intent = new Intent(getApplicationContext(), SnippetList.class);
                    intent.putExtra("variable",  response.body().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "INCORRET CREDENTIALS", Toast.LENGTH_LONG).show();
                }
                txt_password.getText().clear();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "AN ERROR HAS OCCURED", Toast.LENGTH_LONG).show();
            }

        });
    }
}
