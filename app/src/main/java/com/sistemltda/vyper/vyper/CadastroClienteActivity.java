package com.sistemltda.vyper.vyper;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Extras.Validacoes;
import com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints.DataServiceEndpoints;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormCliente;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ClienteComanda;
import com.sistemltda.vyper.vyper.VyperServerAPI.RetrofitClientInstance;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroClienteActivity extends AppCompatActivity {
    private VyperApp vyperApp;
    private Retrofit retrofit;
    private DataServiceEndpoints servicosVyper;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutCpf;
    private TextInputLayout textInputLayoutNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        vyperApp = (VyperApp)getApplication();
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        servicosVyper = retrofit.create(DataServiceEndpoints.class);

        setViews();
    }

    private void setViews() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.txlEmail);
        textInputLayoutCpf = (TextInputLayout) findViewById(R.id.txvCpf);
        textInputLayoutNome = (TextInputLayout) findViewById(R.id.txvNome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.salvar){
            onSalvar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSalvar() {
       if( formIsValid()){
           enviaFormulario();
       }else{

       }
    }

    private void enviaFormulario() {
        FormCliente form = new FormCliente();
        String nome  = textInputLayoutNome.getEditText().getText().toString();
        String email  = textInputLayoutEmail.getEditText().getText().toString();
        String cpf  = textInputLayoutCpf.getEditText().getText().toString();
        form.registroVyper = vyperApp.getVyperRegister();
        form.fingercode = vyperApp.getFingerCadas();
        form.cpf = cpf;
        form.email = email;
        form.nome = nome;
       Call<ClienteComanda> ret =  servicosVyper.registracliente("Bearer "+vyperApp.getBearerToken(),form);
        ret.enqueue(new Callback<ClienteComanda>() {
            @Override
            public void onResponse(Call<ClienteComanda> call, Response<ClienteComanda> response) {
                ClienteComanda com = response.body();
                if(com!=null && com.error){
                    Toast.makeText(getApplicationContext(), com.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(getApplicationContext(), "Salvo com sucesso !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ClienteComanda> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreram erros internos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean formIsValid() {
        String nome  = textInputLayoutNome.getEditText().getText().toString();
        String email  = textInputLayoutEmail.getEditText().getText().toString();
        String cpf  = textInputLayoutCpf.getEditText().getText().toString();
        boolean retorno = true;
        if(nome.equals("")){
            textInputLayoutNome.setError("Campo obrigatorio");
            retorno= false;
        }
        if(cpf.equals("")){
            textInputLayoutCpf.setError("Campo obrigatorio");
            retorno= false;
        }else if(!Validacoes.isCPF(cpf)){
            textInputLayoutCpf.setError("Cpf invalido");
            retorno= false;
        }

        return retorno;
    }
}
