package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import java.util.Locale;

import br.com.agenda.simonebraga.view.modelo.Agenda;
import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 24/08/19.
 */

public class CadastroAlarme extends AppCompatDialog {
    private AlertDialog alerta;
     private Button bt;
    private EditText edMensagem,edHorario, edHorasAviso;
    private String nome,mensagem,hora;
    private LayoutInflater li;
    private View view;
    private Button btFechar;
    private TextView avisar;
    private TextView minutosAntes;
    private Button     btDO ;
    private Button btSE ;
    private Button  btTE ;
    private Button  btQA ;
    private Button  btQI ;
    private Button  btSX ;
    private Button  btSA ;
    private TextView tvrepetir;

    public CadastroAlarme (Context context) {
        super(context);

          li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.activity_cadastrar_alarme, null );
         bt = view.findViewById( R.id.btOk );
         edMensagem = view.findViewById( R.id.edMensagem );
        edHorario = view.findViewById( R.id.edHorario );
         edHorasAviso = view.findViewById( R.id.horasAviso);
        btFechar = view.findViewById( R.id.btFechar );

        avisar = view.findViewById( R.id.avisar );
        minutosAntes = view.findViewById( R.id.minutosAntes );
        tvrepetir = view.findViewById( R.id.repetir );

        btDO = view.findViewById( R.id.btDO );
        btSE = view.findViewById( R.id.btSE );
        btTE = view.findViewById( R.id.btTE );
        btQA = view.findViewById( R.id.btQA );
        btQI = view.findViewById( R.id.btQI );
        btSX = view.findViewById( R.id.btSX );
        btSA = view.findViewById( R.id.btSA );
        edHorario.addTextChangedListener(MaskEditUtil.mask(edHorario, MaskEditUtil.FORMAT_HOUR));
        traduzir (Locale.getDefault().getDisplayLanguage());

     }





    public CadastroAlarme (Context context, Agenda agenda) {
        super(context);

        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.activity_cadastrar_alarme, null );
        bt = view.findViewById( R.id.btOk );
         edMensagem = view.findViewById( R.id.edMensagem );
        edHorario = view.findViewById( R.id.edHorario );
        edHorasAviso = view.findViewById( R.id.horasAviso);

        this.nome=agenda.getNomeP();
        edHorario.setText(agenda.getHorario());
        btFechar = view.findViewById( R.id.btFechar );
        edMensagem.setText(agenda.getMensagem());
        edHorario.addTextChangedListener(MaskEditUtil.mask(edHorario, MaskEditUtil.FORMAT_HOUR));

        final java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String dataFormatada = formato.format(agenda.getData());


        avisar = view.findViewById( R.id.avisar );
        minutosAntes = view.findViewById( R.id.minutosAntes );
        tvrepetir = view.findViewById( R.id.repetir );

        btDO = view.findViewById( R.id.btDO );
        btSE = view.findViewById( R.id.btSE );
        btTE = view.findViewById( R.id.btTE );
        btQA = view.findViewById( R.id.btQA );
        btQI = view.findViewById( R.id.btQI );
        btSX = view.findViewById( R.id.btSX );
        btSA = view.findViewById( R.id.btSA );
        traduzir (Locale.getDefault().getDisplayLanguage());

    }

    public AlertDialog enviarAlerta() {

        //-pegamos nossa instancia da classe



        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );
        alerta = builder.create();

        return alerta;
    }

    public Button getBtDO () {
        return btDO;
    }

    public Button getBtSE () {
        return btSE;
    }

    public Button getBtTE () {
        return btTE;
    }

    public Button getBtQA () {
        return btQA;
    }

    public Button getBtQI () {
        return btQI;
    }

    public Button getBtSX () {
        return btSX;
    }

    public Button getBtSA () {
        return btSA;
    }


    public EditText getEdHorario() {
        return edHorario;
    }

    public EditText getEdMensagem () {
        return edMensagem;
    }

    public Button getBt() {
        return this.bt;
    }



    public String getNome() {
        return nome;
    }


    public String getMensagem() {
        return mensagem;
    }


    public String getHora() {
        return hora;
    }


    public EditText getEdHorasAviso() {
        return edHorasAviso;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void popularDados() {
        String[]  smg= edMensagem.getText().toString().split (":");

 this.mensagem=smg[0].trim();


            this.hora = edHorario.getText ().toString ().trim ();
            if (edHorario.getText ().toString ().trim ().equals ("")) {
                this.hora = "0:0";

            }

    }

    public Button getBtFechar() {

        return btFechar;
    }

    public void fechar(){

        alerta.dismiss();
    }


    private void traduzir(String local){

        switch(local){

            case "português":

                btDO.setText (getContext ().getString (R.string.pt_D));
                btSE.setText (getContext ().getString (R.string.pt_S));
                btTE.setText (getContext ().getString (R.string.pt_T));
                btQA.setText (getContext ().getString (R.string.pt_Q));
                btQI.setText (getContext ().getString (R.string.pt_QI));
                btSX.setText (getContext ().getString (R.string.pt_SE));
                btSA.setText (getContext ().getString (R.string.pt_SA));
                edHorario.setHint (getContext ().getString (R.string.pt_horario));
                edMensagem.setHint (getContext ().getString (R.string.pt_titulo));
                avisar.setText(getContext ().getString (R.string.pt_avisar));
                minutosAntes.setText(getContext ().getString (R.string.pt_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.pt_repetir));
                bt.setText(getContext ().getString (R.string.pt_salvar));
                btFechar.setText(getContext ().getString (R.string.pt_sair));

                break;

            case "español":

                btDO.setText (getContext ().getString (R.string.es_D));
                btSE.setText (getContext ().getString (R.string.es_S));
                btTE.setText (getContext ().getString (R.string.es_T));
                btQA.setText (getContext ().getString (R.string.es_Q));
                btQI.setText (getContext ().getString (R.string.es_QI));
                btSX.setText (getContext ().getString (R.string.es_SE));
                btSA.setText (getContext ().getString (R.string.es_SA));
                edHorario.setHint (getContext ().getString (R.string.es_horario));
                edMensagem.setHint (getContext ().getString (R.string.es_titulo));
                avisar.setText(getContext ().getString (R.string.es_avisar));
                minutosAntes.setText(getContext ().getString (R.string.es_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.es_repetir));
                bt.setText(getContext ().getString (R.string.es_salvar));
                btFechar.setText(getContext ().getString (R.string.es_sair));

                break;

            case "italiano":

                btDO.setText (getContext ().getString (R.string.it_D));
                btSE.setText (getContext ().getString (R.string.it_S));
                btTE.setText (getContext ().getString (R.string.it_T));
                btQA.setText (getContext ().getString (R.string.it_Q));
                btQI.setText (getContext ().getString (R.string.it_QI));
                btSX.setText (getContext ().getString (R.string.it_SE));
                btSA.setText (getContext ().getString (R.string.it_SA));
                edHorario.setHint (getContext ().getString (R.string.it_horario));
                edMensagem.setHint (getContext ().getString (R.string.it_titulo));
                avisar.setText(getContext ().getString (R.string.it_avisar));
                minutosAntes.setText(getContext ().getString (R.string.it_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.it_repetir));
                bt.setText(getContext ().getString (R.string.it_salvar));
                btFechar.setText(getContext ().getString (R.string.it_sair));

                break;

            default:
                btDO.setText (getContext ().getString (R.string.eg_D));
                btSE.setText (getContext ().getString (R.string.eg_S));
                btTE.setText (getContext ().getString (R.string.eg_T));
                btQA.setText (getContext ().getString (R.string.eg_Q));
                btQI.setText (getContext ().getString (R.string.eg_QI));
                btSX.setText (getContext ().getString (R.string.eg_SE));
                btSA.setText (getContext ().getString (R.string.eg_SA));
                edHorario.setHint (getContext ().getString (R.string.eg_horario));
                edMensagem.setHint (getContext ().getString (R.string.eg_titulo));
                avisar.setText(getContext ().getString (R.string.eg_avisar));
                minutosAntes.setText(getContext ().getString (R.string.eg_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.eg_repetir));
                bt.setText(getContext ().getString (R.string.eg_salvar));
                btFechar.setText(getContext ().getString (R.string.eg_sair));

                break;

        }

    }
}
