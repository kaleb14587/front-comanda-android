package com.sistemltda.vyper.vyper.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.R;
import com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints.DataServiceEndpoints;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormPagamento;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.CompEmail;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ResultadoPagamento;
import com.sistemltda.vyper.vyper.VyperServerAPI.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PagamentoDialog extends DialogFragment implements View.OnClickListener {
    private ConstraintLayout pagamento;
    private ConstraintLayout nota;
    private ConstraintLayout carregando;
    private EditText edtValor;
    private Button btnCartao;
    private Button btnDinheiro;
    private Button btnEmail;
    private Button btnImprimir;
    private Button btnFechar;
    private VyperApp app;
    private Retrofit retrofit;
    private DataServiceEndpoints servicosVyper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pagamento_layout, container, false);
        app = (VyperApp)getActivity().getApplicationContext();
        setViews(v);
        retrofit = RetrofitClientInstance.getRetrofitInstance();
        servicosVyper = retrofit.create(DataServiceEndpoints.class);
        return v;
    }

    private void setViews( View v) {
        //set layouts
        pagamento = (ConstraintLayout)v.findViewById(R.id.pagamento);
        nota = (ConstraintLayout)v.findViewById(R.id.nota);
        carregando = (ConstraintLayout)v.findViewById(R.id.carregando);

        //set "pagamento" views
        edtValor = (EditText) v.findViewById(R.id.edtValor);
        btnCartao = (Button) v.findViewById(R.id.btnCartao);
        btnDinheiro = (Button) v.findViewById(R.id.btnDinheiro);

        edtValor.setText(app.getCli().calculaValor());
        //set "nota" views
        btnEmail = (Button)v.findViewById(R.id.btnEmail);
        btnImprimir = (Button)v.findViewById(R.id.btnImprimir);
        btnFechar  = (Button)v.findViewById(R.id.btnFinal);

        //set listener btns
        btnCartao.setOnClickListener(this);
        btnDinheiro.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnImprimir.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        //
    }

    private void pagamentoEfetuado(String tipo){
        this.setCancelable(false);
        carrega(true,pagamento);
        FormPagamento pg = new FormPagamento();
        pg.registroVyper=app.getVyperRegister();
        pg.forma_pagamento = tipo;
        pg.id_comanda= app.getCli().id+"";
        pg.valor_pago = Float.parseFloat(edtValor.getText().toString().replace(",","."));
        
        Call<ResultadoPagamento> pag = servicosVyper.pagaComanda("Bearer "+app.getBearerToken(),pg);
        pag.enqueue(new Callback<ResultadoPagamento>() {
            @Override
            public void onResponse(Call<ResultadoPagamento> call, Response<ResultadoPagamento> response) {
                ResultadoPagamento rpg = response.body();
                if(response.code()!=200){
                    dismiss();
                    Toast.makeText(getContext(), "Erro no servidor de destino codigo:"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!rpg.error){
                    carrega(false,nota);
                }else
                {
                    dismiss();
                    Toast.makeText(getContext(), "Ocorreu algum Erro interno!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultadoPagamento> call, Throwable t) {
                carrega(false,pagamento);
                Toast.makeText(getContext(),  R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carrega(boolean b,ConstraintLayout last) {
        carregando.setVisibility(b?  View.VISIBLE:View.GONE  );
        if(b){
            nota.setVisibility(View.GONE);
            pagamento.setVisibility(View.GONE);
        }
        else last.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCartao:
                pagamentoEfetuado(FormPagamento.FORMA_PAGA_CARTAO);
                break;
            case R.id.btnDinheiro:
                pagamentoEfetuado(FormPagamento.FORMA_PAGA_DINHEIRO);
                break;
            case R.id.btnEmail:
                carrega(true,nota);
                comprovanteEmail();
                break;
            case R.id.btnImprimir:
                break;
            case R.id.btnFinal:
                break;
        }
    }

    private void comprovanteEmail() {
        Call<CompEmail> request = servicosVyper.comprovantePEmail(
                "Bearer "+app.getBearerToken(),
                app.getCli().id+"",
                app.getVyperRegister());
        request.enqueue(new Callback<CompEmail>() {
            @Override
            public void onResponse(Call<CompEmail> call, Response<CompEmail> response) {


                if(response.code()!=200){
                    carrega(false,nota);
                    Toast.makeText(getContext(), "Erro ao enviar email", Toast.LENGTH_SHORT).show();
                    return;
                }
                CompEmail email = response.body();

                if(email.error) {
                    carrega(false,nota);
                    Toast.makeText(getContext(), "Erro ao enviar email", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!email.error) {
                    dismiss();
                    Toast.makeText(getContext(), email.message, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<CompEmail> call, Throwable t) {
                carrega(false,nota);
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
