package com.sistemltda.vyper.vyper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sistemltda.vyper.vyper.Adapters.ItemsAdapter;
import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Dialogs.PagamentoDialog;
import com.sistemltda.vyper.vyper.Listeners.SendItem;
import com.sistemltda.vyper.vyper.Models.DAO.InfoDao;
import com.sistemltda.vyper.vyper.Models.SQLite.Info;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;
import com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints.DataServiceEndpoints;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.AddItemComanda;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ResultadoAddItem;
import com.sistemltda.vyper.vyper.VyperServerAPI.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComandaActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog pDialog;
    private Button btnComanda;
    private ItemsAdapter iAdapter;
    private RecyclerView itemsComanda;
    private VyperApp app;
    private Button enviar;
    private RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView navigation;
    private Retrofit retrofit;
    private DataServiceEndpoints servicosVyper;
    private boolean enviaIsVisible= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda);
        app = (VyperApp)getApplication();
        setViews();
        mostraBotalEnviar();

        retrofit = RetrofitClientInstance.getRetrofitInstance();
        servicosVyper = retrofit.create(DataServiceEndpoints.class);
        app.setListenerItem(new SendItem() {
            @Override
            public void addItemComanda(Item it) {
                if( app.getCli().items==null) app.getCli().items= new ArrayList<>();

                it.isNew=true;
                app.getCli().items.add(it);
                iAdapter.notifyDataSetChanged();

            }
        });

        setList();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processando !");

    }

    private void setList() {
        layoutManager = new LinearLayoutManager(this);

        ArrayList<Item> lit = getListaItem();
        //chamada do adapter
        iAdapter = new ItemsAdapter( this,lit);
        itemsComanda.setAdapter(iAdapter);
        itemsComanda.setHasFixedSize(true);
        itemsComanda.setLayoutManager(layoutManager);
    }

    private void setViews() {
        itemsComanda= (RecyclerView) findViewById(R.id.itemsComanda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enviar = (Button)findViewById(R.id.enviar);
        enviar.setOnClickListener(this);

        navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app.getCli().cliente.nome);
        getSupportActionBar().setSubtitle("R$ "+app.getCli().calculaValor());
    }

    private void mostraBotalEnviar() {
        boolean visible= false;
        if(app.getCli().items==null) return;
        for( Item it : app.getCli().items ){
                if(it.isNew){
                    visible= true;
                    break;
                }
        }
        if(visible){
            if(enviar.getVisibility()!= View.VISIBLE)
            enviar.setVisibility(View.VISIBLE);

        }else{
            if(enviar.getVisibility()!= View.GONE)
            enviar.setVisibility(View.GONE);
//            navigation.setVisibility(View.VISIBLE);
        }

    }

    private ArrayList<Item> getListaItem() {
        return app.getCli().items;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comanda_toolbar, menu);
        SearchView search = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        search.setQueryHint(getResources().getString(R.string.hint_searchable));
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.enviar){
            enviarItemsComanda();
        }
    }

    private void enviarItemsComanda() {
        pDialog.show();
        InfoDao dao =  app.getVyperDatabase().daoAcess();
        Info inf  = dao.selectInfo();

        AddItemComanda itForm = new AddItemComanda();
        itForm.cliente = app.getCli().cliente.id;
        itForm.id_comanda = app.getCli().id+"";

        ArrayList<Integer> arr = new ArrayList<>();
        for (Item it:  app.getCli().items){
            if(it.isNew){
                arr.add(Integer.parseInt(it.getId_vyper())) ;
            }
        }
        itForm.item = arr;
        itForm.registroVyper = inf.getRegistroVyper();

        Call<ResultadoAddItem> request =  servicosVyper.adicionaItensComanda("Bearer "+inf.getBearerToken(),itForm);
        request.enqueue(new Callback<ResultadoAddItem>() {
            @Override
            public void onResponse(Call<ResultadoAddItem> call, Response<ResultadoAddItem> response) {
                if(response.code()!=200){
                    pDialog.hide();
                    Toast.makeText(ComandaActivity.this, "Ocorreu algum erro interno codigo:"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ResultadoAddItem result = response.body();
                pDialog.hide();
                if(!result.error){
                    for (int i=0; i<=app.getCli().items.size()-1;i++  ) {
                        if (app.getCli().items.get(i).isNew) {
                            app.getCli().items.get(i).isNew=false;
                        }
                    }
                    iAdapter.notifyDataSetChanged();
                    mostraBotalEnviar();
                }else{
                    Toast.makeText(getBaseContext(), "Tente novamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultadoAddItem> call, Throwable t) {
                pDialog.hide();
                Toast.makeText(getBaseContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_item){
            Intent it = new Intent(this,SearchableActivity.class);
            startActivity(it);
        }else if(item.getItemId()==R.id.finish){
            finalizaComanda();
        }
        return false;
    }

    private void finalizaComanda() {
        //instancia do dialog de pagamento
        PagamentoDialog pagamentoDialog = new PagamentoDialog();
        //abre a instancia do dialog com o gerenciador de fragmentos
        //define uma tag para o fragmento aberto
        pagamentoDialog.show(getSupportFragmentManager(),"outro");
    }
}
