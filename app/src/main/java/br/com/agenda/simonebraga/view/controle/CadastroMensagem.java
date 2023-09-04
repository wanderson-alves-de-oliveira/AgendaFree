package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.modelo.Agenda;


/**
 * Created by wanderson on 24/08/19.
 */

public class CadastroMensagem extends AppCompatDialog {
    private AlertDialog alerta;
    private Button bt;
    private EditText calendario;
    private EditText edMensagem,edHorario, edHorasAviso;
    private String nome,mensagem,hora;
    private LayoutInflater li;
    private LinearLayout l1;
    private View view;
    private Button btFechar;
    private RadioButton radio_dia;
    private RadioButton radio_semana;
    private RadioButton radio_mes;
    private RadioButton radio_ano;
    private RadioButton radio_null;
    private RadioButton radio_dia_s_n;
    private Switch switchSonOnOff;
    private ImageView sonico;


    private Button  btcor1 ;
    private Button btcor2 ;
    private Button  btcor3 ;
    private Button  btcor4 ;
    private Button  btcor5 ;
    private Button  btcor6 ;
    private Button  btcor7 ;
    private Button  btcor8 ;
    private Button  btcor9 ;
    private Button  btcor10 ;
    private Button  btcor11 ;
    private Button  btcor12 ;
    private Button  btcor13 ;
    private Button  btcor14 ;
    private Button  btcor15 ;
    private Button  btcor16 ;
    private TextView tvrepetir;
    private TextView bgcolor;
    private TextView avisar;
    private TextView minutosAntes;

    public CadastroMensagem(Context context) {
        super(context);






        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.activity_cadastrar_mensagem, null );


        tvrepetir = view.findViewById( R.id.repetir );
        bgcolor = view.findViewById( R.id.bgcolor );
        avisar = view.findViewById( R.id.avisar );
        minutosAntes = view.findViewById( R.id.minutosAntes );

        String local=  Locale.getDefault().getDisplayLanguage();



        l1=view.findViewById( R.id.l1 );
        bt = view.findViewById( R.id.btOk );
        edMensagem = view.findViewById( R.id.edMensagem );
        edHorario = view.findViewById( R.id.edHorario );
        edHorasAviso = view.findViewById( R.id.horasAviso);
        btFechar = view.findViewById( R.id.btFechar );
        radio_semana = view.findViewById( R.id.radio_semana );
        radio_ano= view.findViewById( R.id.radio_ano );
        radio_mes = view.findViewById( R.id.radio_mes );
        radio_null = view.findViewById( R.id.radio_null );
        radio_dia_s_n= view.findViewById( R.id.radio_dia_s_n );
        switchSonOnOff= view.findViewById( R.id.switchSonOnOff );
        calendario =view.findViewById( R.id.calendariopb );
        calendario.setVisibility(View.INVISIBLE);
        calendario.setEnabled(false);
        btcor1 = view.findViewById( R.id.btcor1 );
        btcor2 = view.findViewById( R.id.btcor2 );
        btcor3 = view.findViewById( R.id.btcor3 );
        btcor4 = view.findViewById( R.id.btcor4 );
        btcor5 = view.findViewById( R.id.btcor5 );
        btcor6 = view.findViewById( R.id.btcor6 );
        btcor7 = view.findViewById( R.id.btcor7 );
        btcor8 = view.findViewById( R.id.btcor8 );
        btcor9 = view.findViewById( R.id.btcor9 );
        btcor10 = view.findViewById( R.id.btcor10 );
        btcor11 = view.findViewById( R.id.btcor11 );
        btcor12 = view.findViewById( R.id.btcor12 );
        btcor13 = view.findViewById( R.id.btcor13 );
        btcor14 = view.findViewById( R.id.btcor14 );
        btcor15 = view.findViewById( R.id.btcor15 );
        btcor16 = view.findViewById( R.id.btcor16 );
        sonico=view.findViewById (R.id.imgAltofalante);

        radio_dia = view.findViewById( R.id.radio_dia );
        edHorario.addTextChangedListener(MaskEditUtil.mask(edHorario, MaskEditUtil.FORMAT_HOUR));
        traduzir (local);

    }





    public CadastroMensagem(Context context, Agenda agenda) {
        super(context);





        li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //inflamos o layout alerta.xml na view
        view = li.inflate( R.layout.activity_cadastrar_mensagem, null );



        tvrepetir = view.findViewById( R.id.repetir );
        bgcolor = view.findViewById( R.id.bgcolor );
        avisar = view.findViewById( R.id.avisar );
        minutosAntes = view.findViewById( R.id.minutosAntes );

        String local=  Locale.getDefault().getDisplayLanguage();

        bt = view.findViewById( R.id.btOk );
        l1=view.findViewById( R.id.l1 );

        edMensagem = view.findViewById( R.id.edMensagem );
        edHorario = view.findViewById( R.id.edHorario );
        edHorasAviso = view.findViewById( R.id.horasAviso);
        radio_semana = view.findViewById( R.id.radio_semana );
        radio_ano= view.findViewById( R.id.radio_ano );
        radio_mes = view.findViewById( R.id.radio_mes );
        radio_null = view.findViewById( R.id.radio_null );
        radio_dia_s_n= view.findViewById( R.id.radio_dia_s_n );
        radio_dia = view.findViewById( R.id.radio_dia );
        calendario =view.findViewById( R.id.calendariopb );
        this.nome=agenda.getNomeP();
        edHorario.setText(agenda.getHorario());
        btFechar = view.findViewById( R.id.btFechar );
        edMensagem.setText(agenda.getMensagem());
        edHorario.addTextChangedListener(MaskEditUtil.mask(edHorario, MaskEditUtil.FORMAT_HOUR));

        final java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String dataFormatada = formato.format(agenda.getData());
        btcor1 = view.findViewById( R.id.btcor1 );
        btcor1 = view.findViewById( R.id.btcor1 );
        btcor2 = view.findViewById( R.id.btcor2 );
        btcor3 = view.findViewById( R.id.btcor3 );
        btcor4 = view.findViewById( R.id.btcor4 );
        btcor5 = view.findViewById( R.id.btcor5 );
        btcor6 = view.findViewById( R.id.btcor6 );
        btcor7 = view.findViewById( R.id.btcor7 );
        btcor8 = view.findViewById( R.id.btcor8 );
        btcor9 = view.findViewById( R.id.btcor9 );
        btcor10 = view.findViewById( R.id.btcor10 );
        btcor11 = view.findViewById( R.id.btcor11 );
        btcor12 = view.findViewById( R.id.btcor12 );
        btcor13 = view.findViewById( R.id.btcor13 );
        btcor14 = view.findViewById( R.id.btcor14 );
        btcor15 = view.findViewById( R.id.btcor15 );
        btcor16 = view.findViewById( R.id.btcor16 );
        switchSonOnOff= view.findViewById( R.id.switchSonOnOff );
        sonico=view.findViewById (R.id.imgAltofalante);

        calendario.setText(dataFormatada);
        traduzir (local);

    }

    public AlertDialog enviarAlerta() {

        //-pegamos nossa instancia da classe



        AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );

        builder.setView( view );
        alerta = builder.create();

        return alerta;
    }

    public Button getBtcor1 () {
        return btcor1;
    }

    public LinearLayout getL1 () {
        return l1;
    }

    public Button getBtcor2 () {
        return btcor2;
    }

    public Button getBtcor3 () {
        return btcor3;
    }

    public Button getBtcor4 () {
        return btcor4;
    }

    public Button getBtcor5 () {
        return btcor5;
    }

    public Button getBtcor6 () {
        return btcor6;
    }

    public Button getBtcor7 () {
        return btcor7;
    }

    public Button getBtcor8 () {
        return btcor8;
    }

    public Button getBtcor9 () {
        return btcor9;
    }

    public Button getBtcor10 () {
        return btcor10;
    }

    public Button getBtcor11 () {
        return btcor11;
    }

    public Button getBtcor12 () {
        return btcor12;
    }

    public Button getBtcor13 () {
        return btcor13;
    }

    public Button getBtcor14 () {
        return btcor14;
    }

    public Button getBtcor15 () {
        return btcor15;
    }

    public Button getBtcor16 () {
        return btcor16;
    }

    public EditText getCalendario() {
        return calendario;
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

    public RadioButton getRadio_dia () {
        return radio_dia;
    }

    public Switch getSwitchSonOnOff () {
        return switchSonOnOff;
    }

    public void setSwitchSonOnOff (Switch switchSonOnOff) {
        this.switchSonOnOff = switchSonOnOff;
    }

    public RadioButton getRadio_semana () {
        return radio_semana;
    }

    public RadioButton getRadio_mes () {
        return radio_mes;
    }

    public RadioButton getRadio_ano () {
        return radio_ano;
    }

    public RadioButton getRadio_null () {
        return radio_null;
    }


    public String getNome() {
        return nome;
    }

    public void sonOnOff(){
        Resources res = getContext ().getResources ();
        Drawable drawable=null;
        if(switchSonOnOff.isChecked ()) {
            drawable = ResourcesCompat.getDrawable (res, R.drawable.ic_sons, null);
        }else {
            drawable = ResourcesCompat.getDrawable (res, R.drawable.ic_sonsx, null);


        }
        sonico.setImageDrawable (drawable);

    }

    public RadioButton getRadio_dia_s_n () {
        return radio_dia_s_n;
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


    public void popularDados() {

        this.mensagem=edMensagem.getText().toString().trim();
        this.hora=edHorario.getText().toString().trim();
        if(edHorario.getText().toString().trim().equals("")){
            this.hora="0:0";

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
                edHorario.setHint (getContext ().getString (R.string.pt_horario));
                edMensagem.setHint (getContext ().getString (R.string.pt_mensagem));
                radio_null.setText (getContext ().getString (R.string.pt_nunca));
                radio_dia_s_n.setText (getContext ().getString (R.string.pt_diaSimDiaNao));
                radio_semana.setText (getContext ().getString (R.string.pt_todaSemana));
                radio_mes.setText (getContext ().getString (R.string.pt_todoMes));
                radio_ano.setText (getContext ().getString (R.string.pt_todoAno));
                radio_dia.setText (getContext ().getString (R.string.pt_todoDia));
                bgcolor.setText(getContext ().getString (R.string.pt_cores));
                avisar.setText(getContext ().getString (R.string.pt_avisar));
                minutosAntes.setText(getContext ().getString (R.string.pt_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.pt_repetir));
                bt.setText(getContext ().getString (R.string.pt_salvar));
                btFechar.setText(getContext ().getString (R.string.pt_sair));

                break;
            case "español":
                edHorario.setHint (getContext ().getString (R.string.es_horario));
                edMensagem.setHint (getContext ().getString (R.string.es_mensagem));
                radio_null.setText (getContext ().getString (R.string.es_nunca));
                radio_dia_s_n.setText (getContext ().getString (R.string.es_diaSimDiaNao));
                radio_semana.setText (getContext ().getString (R.string.es_todaSemana));
                radio_mes.setText (getContext ().getString (R.string.es_todoMes));
                radio_ano.setText (getContext ().getString (R.string.es_todoAno));
                radio_dia.setText (getContext ().getString (R.string.es_todoDia));
                bgcolor.setText(getContext ().getString (R.string.es_cores));
                avisar.setText(getContext ().getString (R.string.es_avisar));
                minutosAntes.setText(getContext ().getString (R.string.es_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.es_repetir));
                bt.setText(getContext ().getString (R.string.es_salvar));
                btFechar.setText(getContext ().getString (R.string.es_sair));

                break;
            case "italiano":
                edHorario.setHint (getContext ().getString (R.string.it_horario));
                edMensagem.setHint (getContext ().getString (R.string.it_mensagem));
                radio_null.setText (getContext ().getString (R.string.it_nunca));
                radio_dia_s_n.setText (getContext ().getString (R.string.it_diaSimDiaNao));
                radio_semana.setText (getContext ().getString (R.string.it_todaSemana));
                radio_mes.setText (getContext ().getString (R.string.it_todoMes));
                radio_ano.setText (getContext ().getString (R.string.it_todoAno));
                radio_dia.setText (getContext ().getString (R.string.it_todoDia));
                bgcolor.setText(getContext ().getString (R.string.it_cores));
                avisar.setText(getContext ().getString (R.string.it_avisar));
                minutosAntes.setText(getContext ().getString (R.string.it_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.it_repetir));
                bt.setText(getContext ().getString (R.string.it_salvar));
                btFechar.setText(getContext ().getString (R.string.it_sair));

                break;
            default:
                edHorario.setHint (getContext ().getString (R.string.eg_horario));
                edMensagem.setHint (getContext ().getString (R.string.eg_mensagem));
                radio_null.setText (getContext ().getString (R.string.eg_nunca));
                radio_dia_s_n.setText (getContext ().getString (R.string.eg_diaSimDiaNao));
                radio_semana.setText (getContext ().getString (R.string.eg_todaSemana));
                radio_mes.setText (getContext ().getString (R.string.eg_todoMes));
                radio_ano.setText (getContext ().getString (R.string.eg_todoAno));
                radio_dia.setText (getContext ().getString (R.string.eg_todoDia));
                bgcolor.setText(getContext ().getString (R.string.eg_cores));
                avisar.setText(getContext ().getString (R.string.eg_avisar));
                minutosAntes.setText(getContext ().getString (R.string.eg_minutoAntes));
                tvrepetir.setText(getContext ().getString (R.string.eg_repetir));
                bt.setText(getContext ().getString (R.string.eg_salvar));
                btFechar.setText(getContext ().getString (R.string.eg_sair));

                break;

        }

    }




}
