package br.com.agenda.simonebraga.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.modelo.Agenda;
import br.com.agenda.simonebraga.view.view.RecycleViewOnclickListenerHack;

/**
 * Created by wanderson on 15/09/19.
 */

public class AdapterRecycleAlarme extends RecyclerView.Adapter<AdapterRecycleAlarme.ViewHolder> {
private List<Agenda> lista;
    private RecycleViewOnclickListenerHack recycleViewOnclickListenerHack;
    private LayoutInflater inflater;


    public AdapterRecycleAlarme (Context context, List<Agenda> l ){

    this.lista=l;
    this.inflater=(LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
}

    public void setRecycleViewOnclickListenerHack(RecycleViewOnclickListenerHack r) {
        this.recycleViewOnclickListenerHack = r;
    }


    public void remove(int position){
        lista.remove( position );
        notifyItemRemoved( position );
    }


    public void addLista(Agenda p , int position){
    lista.add( p );
    notifyItemInserted( position );

}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate( R.layout.item_mensagem_alarme,parent,false );
        ViewHolder vh = new ViewHolder(v );

         return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int pos=position;
        
         holder.tvNome.setText(lista.get( pos ).getHorario ()+"\n"+lista.get( pos ).getMensagem ()+"\n"+
                 lista.get( pos ).getFeriado ());

        Calendar    dataDeHoje= Calendar.getInstance();
        Calendar    dataDaLista= Calendar.getInstance();

        dataDeHoje.setTime(new Date());
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date data= formato.parse(lista.get( pos ).getDatacheck());
            dataDaLista.setTime(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

         //   holder.bgLista.setBackgroundResource(R.drawable.estilo9);


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         public TextView tvNome ;
        public ImageView lixeira ;
        public LinearLayout bgLista ;
        public ImageView imgLapis ;


          public ViewHolder(View itemView) {
            super( itemView );
              tvNome = itemView.findViewById( R.id.tvNomeAssunto );

              lixeira = itemView.findViewById( R.id.imgLixeira );
              bgLista = itemView.findViewById( R.id.bgLista );
              imgLapis = itemView.findViewById( R.id.imgLapis );


              lixeira.setOnClickListener(this);
              imgLapis.setOnClickListener(this);

          }

        @Override
        public void onClick(View v) {

            if(recycleViewOnclickListenerHack!=null){
                recycleViewOnclickListenerHack.onClickListener(v,getPosition());
            }

        }
    }

}
