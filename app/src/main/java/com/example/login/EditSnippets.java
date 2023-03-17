package com.example.login;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class EditSnippets extends AppCompatActivity {
    EditText txt_titleEdit,txt_codeEdit;
    TextView txt_url, txt_owner;
    CheckBox lineas;
    int id;
    SharedPreferences sharedPref;
    ArrayList<String> styles;
    ArrayList<String> languages;
    Spinner dropdownLanguages, dropdownStyles;
    Snippet snippet;
    String snipetString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_snippet);

        Bundle datos = getIntent().getExtras();
        snipetString = datos.getString("snippet");
        snippet= new Gson().fromJson(snipetString, Snippet.class);
        setTitle(snippet.getId());
        id = Integer.parseInt(snippet.getId());


        txt_titleEdit = findViewById(R.id.txt_editTitle);
        txt_codeEdit = findViewById(R.id.txt_editCode);
        txt_url = findViewById(R.id.txt_url);
        txt_owner = findViewById(R.id.txt_owner);
        lineas = findViewById(R.id.lineas);

        txt_titleEdit.setText(snippet.getTitle());
        txt_codeEdit.setText(snippet.getCode());
        txt_url.setText("URL: "+snippet.getUrl());
        txt_owner.setText("OWNER: "+snippet.getOwner());

        lineas.setChecked(snippet.getLinenos());

        sharedPref = getDefaultSharedPreferences(
                getApplicationContext());

        languages= new Gson().fromJson(sharedPref.getString("languages", " "), ArrayList.class);
        dropdownLanguages = findViewById(R.id.spinner_editLanguage);
        ArrayAdapter<String> adapterLenguaje = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdownLanguages.setAdapter(adapterLenguaje);
        dropdownLanguages.setSelection(adapterLenguaje.getPosition(snippet.getLanguage()));

        styles= new Gson().fromJson(sharedPref.getString("styles", " "), ArrayList.class);
        dropdownStyles = findViewById(R.id.spinner_editStyle);
        ArrayAdapter<String> adapterEstilo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, styles);
        dropdownStyles.setAdapter(adapterEstilo);
        dropdownStyles.setSelection(adapterEstilo.getPosition(snippet.getStyle()));

    }
    public void EditSnippet() {
        if (txt_titleEdit.getText().toString().equals("") || txt_codeEdit.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "MISSING FIELDS", Toast.LENGTH_LONG).show();
        } else {
            JsonObject paramObject = new JsonObject();
            try {
                paramObject.addProperty("title", txt_titleEdit.getText().toString());
                paramObject.addProperty("code", txt_codeEdit.getText().toString());
                paramObject.addProperty("linenos", lineas.isChecked());
                paramObject.addProperty("language", String.valueOf(dropdownLanguages.getSelectedItem()));
                paramObject.addProperty("style", String.valueOf(dropdownStyles.getSelectedItem()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(paramObject.toString());
            patchSnippets(paramObject, id);
            System.out.println(dropdownLanguages.getSelectedItem());
        }
    }


    private void patchSnippets(JsonObject body, int num){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().patchSnippets(sharedPref.getString("token", " "),body, num);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Intent i = getIntent();
                    i.putExtra("snippet", response.body().toString());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "SOMETHING IS WRONG", Toast.LENGTH_LONG).show();
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
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnEditar:
                EditSnippet();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}