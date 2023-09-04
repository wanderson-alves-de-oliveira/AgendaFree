package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import java.util.Locale;

import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 24/08/19.
 */

public class AlertaMostrarNotificacao extends AppCompatDialog {
    private AlertDialog alerta;
     private Button btSim;
    private Button btNao;
    private Button bt;
    private  final TextView mensagem ;

    private LayoutInflater li;
    private View view;
    public AlertaMostrarNotificacao (Context context) {
        super(context);

          li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.caixa_nova, null );
        btSim = view.findViewById( R.id.btPositivo );
        btNao = view.findViewById( R.id.btNegativo );
        bt = view.findViewById( R.id.btOk );
        mensagem = view.findViewById( R.id.tvMensagem );

    }



    public AlertDialog enviarAlerta(String msg, String titulo, boolean btSimNao, boolean btOK, boolean edT) {

        //-pegamos nossa instancia da classe
        final TextView texto = new TextView( view.getContext() );
        texto.setText( " " );
        texto.setBackgroundColor( Color.argb( 255, 255, 255, 255 ) );
        texto.setTextSize( 25 );

         traduzir (Locale.getDefault().getDisplayLanguage());


        if (!btOK) {
            bt.setVisibility( View.INVISIBLE );
            bt.setEnabled( false );
        }

        if (!btSimNao) {
            btSim.setEnabled( true );
            btNao.setEnabled( true );
            btNao.setVisibility( View.INVISIBLE );
            btSim.setVisibility( View.INVISIBLE );
        }


        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );
        builder.setCustomTitle( texto );
        alerta = builder.create();

        return alerta;
    }

    public void fechar(){

        alerta.dismiss();
    }
    public Button getBtSim() {
        return this.btSim;
    }


    public Button getBtNao() {
        return this.btNao;
    }




    private void traduzir(String local){

        switch(local){

            case "português":
                btNao.setText(getContext ().getString (R.string.pt_nao));
                btSim.setText(getContext ().getString (R.string.pt_sim));
                mensagem.setText(getContext ().getString (R.string.pt_mostrar_notfic));

                break;
            case "español":
                btNao.setText(getContext ().getString (R.string.es_nao));
                btSim.setText(getContext ().getString (R.string.es_sim));
                mensagem.setText(getContext ().getString (R.string.es_mostrar_notfic));

                break;
            case "italiano":
                btNao.setText(getContext ().getString (R.string.it_nao));
                btSim.setText(getContext ().getString (R.string.it_sim));
                mensagem.setText(getContext ().getString (R.string.it_mostrar_notfic));

                break;
            default:
                btNao.setText(getContext ().getString (R.string.eg_nao));
                btSim.setText(getContext ().getString (R.string.eg_sim));
                mensagem.setText(getContext ().getString (R.string.eg_mostrar_notfic));

                break;

        }

    }


 }