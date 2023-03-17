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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnippetList extends AppCompatActivity {
    String credentials;
    Login  parameters;
    Button btn_next, btn_previous;
    TextView paginas;
    int page;
    Options options;
    SharedPreferences sharedPref;
    Snippets allSnippets;
    ListView mLeadsList;
    ListAdapter mLeadsAdapter;

    ActivityResultLauncher<Intent> activityAddResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippet_list);

        Bundle datos = getIntent().getExtras();
        credentials = datos.getString("variable");

        parameters= new Gson().fromJson(credentials, Login.class);
        setTitle(parameters.getUser().get("email").getAsString());


        sharedPref= getDefaultSharedPreferences(
                getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", parameters.getToken());
        editor.apply();

        mLeadsList = findViewById(R.id.superListView);
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Snippet r = mLeadsAdapter.getItem(position);
                pulsarMostrar(r);

            }
        });

        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        paginas = findViewById(R.id.PAGINA);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSnippets(page+=1);
                paginas.setText(""+page);
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSnippets(page-=1);
                paginas.setText(""+page);
            }
        });


        activityAddResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println(result);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            loadFirstPage();
                            System.out.println("result");
                        }else{
                            System.out.println("ELSE");
                            getSnippets(page);
                        }
                    }
                });



        getOptions();
        loadFirstPage();
    }
    public void loadFirstPage (){
        page=1;
        paginas.setText(""+page);
        getSnippets(page);
    }

    public void setButtons(String next, String previous){
        System.out.println("Pagina: "+page);
        if (previous == null){
            btn_previous.setEnabled(false);

        } else{
            btn_previous.setEnabled(true);
        }
        if (next == null){
            btn_next.setEnabled(false);
        }else {
            btn_next.setEnabled(true);
        }

    }
    public void setAdapter(){
        //Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new ListAdapter(this, allSnippets.getSnippetList());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

    }
    public  void getSnippets(int page){
        Call<JsonElement> call;
        if(page>1){
            call = RetrofitClient.getInstance().getMyApi().getSnippets(page);
        }else{
            call = RetrofitClient.getInstance().getMyApi().getSnippets();
        }
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement body = response.body();
                    System.out.println(body);


                    allSnippets = new Gson().fromJson(body, Snippets.class);
                    setButtons(allSnippets.getNext(),allSnippets.getPrevious());
                    setAdapter();

                }else{
                    System.out.println("FALLA");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t);
                System.out.println("An error has occurred");
            }

        });
    }

    public void postLogout(){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().postLogout();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println(response.body());
                finish();
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
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                pulsarAdd();
                return true;
            case R.id.changeName:
                pulsarPerfil();
                return true;
            case R.id.logout:
                postLogout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getOptions(){
        Call<JsonElement> call = RetrofitClient.getInstance().getMyApi().getOptions();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    options= new Gson().fromJson(response.body(), Options.class);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("styles", options.getStyles().toString());
                    editor.putString("languages", options.getLanguages().toString());
                    editor.apply();

                }else{
                    Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void pulsarMostrar(Snippet snippet){
        Intent intent = new Intent(getApplicationContext(), ShowSnippet.class);
        intent.putExtra("snippet", snippet.toString());
        activityAddResultLauncher.launch(intent);

    }

    public void pulsarAdd(){
        Intent intent = new Intent(getApplicationContext(), AddSnippets.class);
        intent.putExtra("credenciales", credentials);
        activityAddResultLauncher.launch(intent);
    }

    public void pulsarPerfil(){
        Intent intent = new Intent(getApplicationContext(), ChangeName.class);
        intent.putExtra("variable", credentials);
        activityAddResultLauncher.launch(intent);
    }

}