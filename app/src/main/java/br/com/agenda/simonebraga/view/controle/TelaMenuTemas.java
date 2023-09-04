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

public class TelaMenuTemas extends AppCompatDialog {
    private AlertDialog alerta;
    private Button tema1;
    private Button tema2;
    private Button tema3;
    private Button tema4;
    private Button tema5;
    private Button tema6;
    private Button tema7;
    private Button tema8;
    private Button tema9;
    private Button tema10;

    private Button btFechar;

    private LayoutInflater li;
    private View view;
    public TelaMenuTemas(Context context) {
        super(context);

        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.item_temas_menu, null );
        tema1 = view.findViewById( R.id.tema1 );
        tema2 = view.findViewById( R.id.tema2 );
        tema3 = view.findViewById( R.id.tema3 );
        tema4 = view.findViewById( R.id.tema4 );
        tema5 = view.findViewById( R.id.tema5 );
        tema6 = view.findViewById( R.id.tema6 );
        tema7 = view.findViewById( R.id.tema7 );
        tema8 = view.findViewById( R.id.tema8 );
        tema9 = view.findViewById( R.id.tema9 );
        tema10 = view.findViewById( R.id.tema10);

        btFechar = view.findViewById( R.id.btFechar );


    }




    public AlertDialog enviarAlerta() {

        //-pegamos nossa instancia da classe




        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );

        alerta = builder.create();

        return alerta;
    }

    public Button getTema1() {
        return tema1;
    }

    public Button getTema2() {
        return tema2;
    }

    public Button getTema3() {
        return tema3;
    }

    public Button getTema4() {
        return tema4;
    }

    public Button getTema6 () {
        return tema6;
    }

    public Button getTema7 () {
        return tema7;
    }

    public Button getTema8 () {
        return tema8;
    }

    public Button getTema9 () {
        return tema9;
    }

    public Button getTema10 () {
        return tema10;
    }

    public Button getTema5() {
        return tema5;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public void fechar(){

        alerta.dismiss();
    }



}
