package com.sistemltda.vyper.vyper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints.DataServiceEndpoints;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormCliente;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ClienteComanda;
import com.sistemltda.vyper.vyper.VyperServerAPI.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgFinger;
    private VyperApp vyperApp;
    private Retrofit retrofit;
    private DataServiceEndpoints servicosVyper;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        vyperApp = (VyperApp)getApplication();
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        servicosVyper = retrofit.create(DataServiceEndpoints.class);

        imgFinger= (ImageView) findViewById(R.id.imgFinger);
        imgFinger.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.imgFinger){
            progressBar.setVisibility(View.VISIBLE);
           final FormCliente form = new FormCliente();
            form.fingercode = "kalebkalebkalebkalebkalebkalebkalebkalebka";
            form.registroVyper = vyperApp.getVyperRegister();
            Call<ClienteComanda> retorno = servicosVyper.validaCliente("Bearer "+vyperApp.getBearerToken(),form);
            retorno.enqueue(new Callback<ClienteComanda>() {
                @Override
                public void onResponse(Call<ClienteComanda> call, Response<ClienteComanda> response) {
                    progressBar.setVisibility(View.GONE);
                    ClienteComanda cli = response.body();
                    if( cli.error){
                        if(cli.code == ClienteComanda.ERROR_CLIENTE_NOT_FOUND){
                            cadastreaCliente(  form.fingercode);
                            return;
                        }else
                            Toast.makeText(getApplicationContext(), cli.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    apresentaComanda(cli);
                }

                @Override
                public void onFailure(Call<ClienteComanda> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),  R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void apresentaComanda(ClienteComanda cli) {
        vyperApp.comandaAberta(cli);
        Intent it = new Intent(this,ComandaActivity.class);
        startActivity(it);
        Toast.makeText(this, "Abrindo a comanda do cliente", Toast.LENGTH_SHORT).show();
    }

    private void cadastreaCliente(String finger) {
        Intent it = new Intent(this,CadastroClienteActivity.class);
        vyperApp.setFingerCadas(finger);
        startActivity(it);
        Toast.makeText(this, "Criando novo cadastro de cliente", Toast.LENGTH_SHORT).show();
    }
}
