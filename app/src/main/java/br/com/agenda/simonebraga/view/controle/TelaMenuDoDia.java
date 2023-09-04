package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
 import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 24/08/19.
 */

public class TelaMenuDoDia extends AppCompatDialog {
    private AlertDialog alerta;
     private Button btAdd;
    private Button btVer;
    private Button btFechar;

    private String data;
    private LayoutInflater li;
    private View view;
     public TelaMenuDoDia(Context context,String data) {
        super(context);

          li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.item_mensagem_menu, null );
         btAdd = view.findViewById( R.id.btAdd );
         btVer = view.findViewById( R.id.btver );
         btFechar = view.findViewById( R.id.btFechar );

         this.data=data;

     }




    public AlertDialog enviarAlerta() {

        //-pegamos nossa instancia da classe




        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );

        alerta = builder.create();

        return alerta;
    }

    public Button getBtAdd() {
        return btAdd;
    }

    public Button getBtVer() {
        return btVer;
    }

    public String getData() {
        return data;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public void fechar(){

        alerta.dismiss();
    }



}
