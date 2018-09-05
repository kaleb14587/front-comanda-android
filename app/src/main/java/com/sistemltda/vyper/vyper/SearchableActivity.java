package com.sistemltda.vyper.vyper;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sistemltda.vyper.vyper.AppVyper.VyperApp;
import com.sistemltda.vyper.vyper.Models.SQLite.Item;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private List<Item> listaItem;
    private VyperApp application;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        listaItem = new ArrayList<>();
        application= (VyperApp)getApplication();
        listView = (ListView)findViewById(R.id.listaItems);

        // pega o que foi digitado na busca da intent
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {//se nao tiver nada abre a lista
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        listaItem = application.getVyperDatabase().daoItem().likeItem("%"+query+"%" );
        if(listaItem.size()==0){
            Item it = new Item();
            it.setNome("nenhum item encontrado");
            listaItem.add(it);
        }
        adapter  = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                application.getItemPComanda().addItemComanda(listaItem.get(i));

            }
        });
    }



    @Override
    public void onClick(View view) {

    }
}
