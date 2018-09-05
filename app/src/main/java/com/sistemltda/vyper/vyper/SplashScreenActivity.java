package com.sistemltda.vyper.vyper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Models.DAO.InfoDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Info;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;
import com.sistemltda.vyper.vyper.VyperDatabase.VyperDatabase;
import com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints.DataServiceEndpoints;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.CredentialsVyper;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormRegistro;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.Authenticate;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.RegistroRetorno;
import com.sistemltda.vyper.vyper.VyperServerAPI.RetrofitClientInstance;
import com.varvet.barcodereadersample.barcode.BarcodeCaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashScreenActivity extends AppCompatActivity {

    private final int BARCODE_CAPTURED= 254;
    private static Retrofit retrofit;
    private DataServiceEndpoints servicosVyper;
    private VyperApp vyperApplication;
    private AlertDialog.Builder mAlertDialog;
    private TextView edtLogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        edtLogView = findViewById(R.id.edtLogView);
        vyperApplication = (VyperApp) getApplication();

        Info info = vyperApplication.getVyperDatabase().daoAcess().selectInfo();

              retrofit = RetrofitClientInstance.getRetrofitInstance();
        servicosVyper = retrofit.create(DataServiceEndpoints.class);

        if(info == null ){
            CredentialsVyper credentialsVyper = new CredentialsVyper();
            credentialsVyper.client_id  = "2";
            credentialsVyper.grant_type  = "password";
            credentialsVyper.client_secret  = "PdKlsHiPQGS6x3rtZiaaIy2YXHPufjoIH5HsQAhW";
            credentialsVyper.username  = "kaleb.borda@gmail.com";
            credentialsVyper.password  = "qwe123";
            credentialsVyper.scope  = "";

            Call<Authenticate> accessToken = servicosVyper.getAccessToken(credentialsVyper);
            accessToken.enqueue(new Callback<Authenticate>() {
                @Override
                public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                    Authenticate auth = response.body();
                    if(auth==null){
                        mostrarBarcodeDectector();
                        return;
                    }
                    Info inf = new Info();
                    if(auth.getAccess_token()==null && auth.getRefresh_token()==null)return ;

                    inf.setBearerToken(auth.getAccess_token());
                    inf.setRefreshToken(auth.getRefresh_token());

                    vyperApplication.getVyperDatabase().daoAcess().insertInfo(inf);

                    mostrarBarcodeDectector();
                }

                @Override
                public void onFailure(Call<Authenticate> call, Throwable t) {
                    enviarParaConcerto();
                }
            });
        }else if(info.getRegistroVyper()==null  ) mostrarBarcodeDectector();
        else abreComandaActivity();
    }

    private void mostrarBarcodeDectector() {
        Intent it = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(it,BARCODE_CAPTURED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(this.BARCODE_CAPTURED== requestCode){
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode  = data.getParcelableExtra("Barcode");
                    String abc = barcode.displayValue;

                    registraDispositivo(abc);

                } else{
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registraDispositivo(String codigos) {
        String[] cod = codigos.split(":");

        String registratinoId = getRegistrationIdFCM();
        registraDispositivoBackend(cod[0],cod[1],registratinoId);
    }

    private void registraDispositivoBackend(final String user,final  String code,final String registrationId) {

         mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setTitle("Titulo do dispositivo!");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        mAlertDialog.setView(input);
        mAlertDialog.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                edtLogView.setText("Registrando Dispositivo nos servidores!!");
                String descricao = input.getText().toString();
                FormRegistro form  = new FormRegistro();

                form.descricao      = descricao;
                form.registrationId = registrationId;
                form.cod_id         = code;
                InfoDao dao =  vyperApplication.getVyperDatabase().daoAcess();
                Info inf  = dao.selectInfo();
                Call<RegistroRetorno> retorno = servicosVyper.registraDispositivo("Bearer "+inf.getBearerToken(),user,form);
                retorno.enqueue(new Callback<RegistroRetorno>() {
                    @Override
                    public void onResponse(Call<RegistroRetorno> call, Response<RegistroRetorno> response) {
                        edtLogView.setText(R.string.sincronizando);
                        RegistroRetorno retorno = response.body();
                        if(retorno==null)enviarParaConcerto();
                        Info inf = vyperApplication.getVyperDatabase().daoAcess().selectInfo();
                        inf.setRegistroVyper(retorno.id);

                        vyperApplication.getVyperDatabase().daoAcess().updateInfo(inf);
                        int size = retorno.item.size();
                        int count = 0;
                        edtLogView.setText("Salvando items  0/"+size+"!!");
                        for ( Item it:retorno.item) {
                            count++;
                            edtLogView.setText("Salvando items  "+count+"/"+size+"!!");
                            vyperApplication.getVyperDatabase().daoItem().insert(it);
                        }
                        abreComandaActivity();
                    }

                    @Override
                    public void onFailure(Call<RegistroRetorno> call, Throwable t) {
//                        RegistroRetorno ret = call.
                        enviarParaConcerto();
                    }
                });


            }
        });
        mAlertDialog.show();

    }

    private void abreComandaActivity() {
        Intent it = new Intent(this,ScrollingActivity.class);
        startActivity(it);
        finish();
    }

    private void enviarParaConcerto() {
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setTitle(R.string.erro_app);
        mAlertDialog.setMessage(R.string.erro_msg_24_horas);
        mAlertDialog.setPositiveButton(R.string.btn_entendi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        mAlertDialog.show();
    }

    private String getRegistrationIdFCM() {
        edtLogView.setText(R.string.gerando_chave_acesso);
        InfoDao dao =  vyperApplication.getVyperDatabase().daoAcess();
        Info inf  = dao.selectInfo();

        if(inf!=null && inf.getRegistrationId()!=null)return inf.getRegistrationId();

        else return FirebaseInstanceId.getInstance().getToken();
    }
}
