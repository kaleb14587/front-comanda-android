package com.sistemltda.vyper.vyper.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemltda.vyper.vyper.Models.SQLite.Item;
import com.sistemltda.vyper.vyper.R;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity act;
 private ArrayList<Item> listaItem;

    //view Types
    private static final int VIEW_EMPTY= 3;
    public ItemsAdapter(Context ctx ,ArrayList<Item> itens) {
        this.context = ctx;
        this.listaItem = itens ;
        if(  this.listaItem==null )  this.listaItem= new ArrayList<>();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_EMPTY){
            return new EmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, parent, false));

        }

        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.items_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(listaItem.size()!= 0 ){
            ItemHolder itemHolder =(ItemHolder) holder;
            Item it = listaItem.get(position);
            if(it.isNew) {
                Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
                itemHolder.linearLayout.startAnimation(pulse);
            }
            itemHolder.txvNomeItem.setText(it.getNome());
            itemHolder.txvQtd.setText(  (it.getQtd()==0 ?"1x":it.getQtd())+""  );
            itemHolder.txvValorItem.setText(   (it.getValor_vigente()!=null ?it.getValor_vigente().getValor_final():"0,00")   );
            itemHolder.txvValorTotal.setText(getValorTotal(it));
        }
    }

    private String getValorTotal(Item it) {
        return (it.getTotal()+"").replace(".",",");
    }

    @Override
    public int getItemViewType(int position) {
        if(listaItem.size()== 0 ){
                return VIEW_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(listaItem.size()==0){
            return 1 ;
        }else return listaItem.size();
    }
    public class ItemHolder extends RecyclerView.ViewHolder{
         public TextView txvValorTotal;
         public TextView txvValorItem;
         public TextView txvNomeItem;
         public TextView txvQtd;
        public LinearLayout linearLayout;
        public ItemHolder(View itemView) {
            super(itemView);
            linearLayout    = (LinearLayout)itemView.findViewById(R.id.linearLayout);
            txvNomeItem     = (TextView)itemView.findViewById(R.id.txvNome);
            txvValorItem    = (TextView)itemView.findViewById(R.id.txvValorItem);
            txvValorTotal   = (TextView)itemView.findViewById(R.id.txvValorTotal);
            txvQtd          = (TextView)itemView.findViewById(R.id.txvQtd);
        }
    }
    public class EmptyView extends RecyclerView.ViewHolder{

        public EmptyView(View itemView) {
            super(itemView);
        }
    }

}
