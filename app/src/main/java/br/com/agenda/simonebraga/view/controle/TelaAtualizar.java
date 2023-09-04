package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 24/08/19.
 */

public class TelaAtualizar extends AppCompatDialog {
    private AlertDialog alerta;
    private TextView atualizar;


    private LayoutInflater li;
    private View view;
    public TelaAtualizar (Context context) {
        super(context);

        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.item_atualizar, null );
        atualizar = view.findViewById( R.id.tvAviso );


    }




    public AlertDialog enviarAlerta() {

        //-pegamos nossa instancia da classe




        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );

        alerta = builder.create();

        return alerta;
    }

    public TextView getAtualizar () {
        return atualizar;
    }

    public void fechar(){

        alerta.dismiss();
    }



}
