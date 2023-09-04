package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import java.util.Locale;

import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 24/08/19.
 */

public class TelaMenuIndex extends AppCompatDialog {
    private AlertDialog alerta;
    private TextView tvDespertador;
    private TextView tvTemas;
    private Button btFechar;
    private TextView tvSons;


    private String data;
    private LayoutInflater li;
    private View view;
 private   FrameLayout frameLayout;
    public TelaMenuIndex (Context context) {
        super(context);

        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.item_menu_index, null );
        tvDespertador = view.findViewById( R.id.tvDespertador );
        tvTemas = view.findViewById( R.id.tvTemas );
        tvSons = view.findViewById( R.id.tvSons );
        btFechar = view.findViewById( R.id.btFechar );
         frameLayout = view.findViewById(R.id.fl_adplaceholder);

        this.data=data;
        String local=  Locale.getDefault().getDisplayLanguage();

        traduzir (local);

    }




    public AlertDialog enviarAlerta() {
        //-pegamos nossa instancia da classe
        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );
        builder.setView( view );
        alerta = builder.create();
        return alerta;
    }




    public FrameLayout getFrameLayout () {
        return frameLayout;
    }

    public void setFrameLayout (FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public TextView getTvDespertador () {
        return tvDespertador;
    }

    public TextView getTvTemas () {
        return tvTemas;
    }

    public String getData() {
        return data;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public TextView getTvSons () {
        return tvSons;
    }

    public void fechar(){

        alerta.dismiss();
    }


    private void traduzir(String local){

        switch(local){

            case "português":

                tvDespertador.setText(getContext ().getString (R.string.pt_despertador));
                tvTemas.setText(getContext ().getString (R.string.pt_papelDeParede));
                tvSons.setText(getContext ().getString (R.string.pt_somDoAlarme));

                break;
            case "español":

                tvDespertador.setText(getContext ().getString (R.string.es_despertador));
                tvTemas.setText(getContext ().getString (R.string.es_papelDeParede));
                tvSons.setText(getContext ().getString (R.string.es_somDoAlarme));

                break;
            case "italiano":

                tvDespertador.setText(getContext ().getString (R.string.it_despertador));
                tvTemas.setText(getContext ().getString (R.string.it_papelDeParede));
                tvSons.setText(getContext ().getString (R.string.it_somDoAlarme));

                break;
            default:

                tvDespertador.setText(getContext ().getString (R.string.eg_despertador));
                tvTemas.setText(getContext ().getString (R.string.eg_papelDeParede));
                tvSons.setText(getContext ().getString (R.string.eg_somDoAlarme));

                break;

        }

    }

}
