package com.example.login;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSnippets extends AppCompatActivity {
    Login parameters;
    EditText txt_title, txt_code;
    String credentials;
    String token;
    CheckBox lineas;
    SharedPreferences sharedPref;
    ArrayList<String> styles;
    ArrayList<String> languages;
    Spinner dropdownLanguages;
    Spinner dropdownStyles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_snippet);
        setTitle("Add Snippet");

        Bundle data = getIntent().getExtras();
        credentials = data.getString("credenciales");

        txt_title = findViewById(R.id.txt_addTitle);
        txt_code = findViewById(R.id.txt_addCode);
        lineas = findViewById(R.id.check_add);

        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());

        languages= new Gson().fromJson(sharedPref.getString("languages", " "), ArrayList.class);

        styles= new Gson().fromJson(sharedPref.getString("styles", " "), ArrayList.class);

        dropdownLanguages = findViewById(R.id.spinner_addLanguage);
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdownLanguages.setAdapter(adapterLanguage);

        dropdownStyles = findViewById(R.id.spinner_addStyle);
        ArrayAdapter<String> adapterStyle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, styles);
        dropdownStyles.setAdapter(adapterStyle);

        parameters= new Gson().fromJson(credentials, Login.class);
        token = parameters.getToken();
    }

    private void AddSnippet(){
        if(txt_title.getText().toString().equals("")||txt_code.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "FALTAN CAMPOS POR RELLENAR", Toast.LENGTH_LONG).show();
        }else{
            JsonObject paramObject = new JsonObject();
            try {
                paramObject.addProperty("title", txt_title.getText().toString());
                paramObject.addProperty("code",txt_code.getText().toString());
                paramObject.addProperty("linenos", lineas.isChecked());
                paramObject.addProperty("language",  String.valueOf(dropdownLanguages.getSelectedItem()));
                paramObject.addProperty("style", String.valueOf(dropdownStyles.getSelectedItem()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(paramObject.toString());
            postSnippets(paramObject);
        }
    }


    private void postSnippets(JsonObject body){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postSnippets(token,body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    System.out.println(response);
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
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
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnAdd:
                AddSnippet();
                System.out.println("LO hace");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}