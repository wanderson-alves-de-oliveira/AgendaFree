package br.com.agenda.simonebraga.view.controle;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.adapter.AdapterRecycleAgenda;
import br.com.agenda.simonebraga.view.dao.*;
import br.com.agenda.simonebraga.view.modelo.Agenda;
import br.com.agenda.simonebraga.view.view.RecycleViewOnclickListenerHack;


/**
 * Created by wanderson on 24/08/19.
 */

public class AvisoAgenda extends AppCompatDialog implements RecycleViewOnclickListenerHack {
    private AlertDialog alerta;
    private Context context;
    private final Long HORA_AVISO = 60000L;
    private long repetir =0;
    private boolean sonLigado=true;
    private LayoutInflater li;
    private View view;
    private RecyclerView mRecyclerView;
    private List<Agenda> listaR;
    private List<Agenda> listax1;
    private int position = 0;
    private String dataString;
    private String dataStringAtual;

    private Button btFechar;
    private Button btAgendar;

    private int cor = R.drawable.estilo9;

    public AvisoAgenda(Context context,String data,String data2) {
        super(context);
        this.context = context;
        this.dataString=data;
        this.dataStringAtual=data2;



        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflamos o layout alerta.xml na view
        view = li.inflate(R.layout.aviso_agenda, null);


        btFechar = view.findViewById( R.id.btFechar );

        btAgendar = view.findViewById( R.id.btaGENDAR );
        traduzir (Locale.getDefault().getDisplayLanguage());

        mRecyclerView = view.findViewById(R.id.recicleview);
        mRecyclerView.setHasFixedSize(true);
        listaR = carregar(position);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                if (listaR.size() == lm.findLastCompletelyVisibleItemPosition() + 1) {
                    position += 100;
                    AdapterRecycleAgenda adapterRecycle = (AdapterRecycleAgenda) mRecyclerView.getAdapter();
                    listax1 = new ArrayList<>();
                    listax1 = carregar(position);
                    for (int i = 0; i < listax1.size(); i++) {
                        adapterRecycle.addLista(listax1.get(i), listaR.size());

                    }
                }
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        AdapterRecycleAgenda adapterRecycle = new AdapterRecycleAgenda (context, listaR);
        adapterRecycle.setRecycleViewOnclickListenerHack(this);
        mRecyclerView.setAdapter(adapterRecycle);
        // Inflate the layout for this fragment



    }

    public AlertDialog enviarAlerta() {


        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setView(view);
        // builder.setCustomTitle(texto);
        alerta = builder.create();

        return alerta;
    }

    public Button getBtAgendar () {
        return btAgendar;
    }

    public void setBtAgendar (Button btAgendar) {
        this.btAgendar = btAgendar;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public void fechar(){

        alerta.dismiss();
    }

    public List<Agenda> getListaR () {
        return listaR;
    }

    private List<Agenda> carregar(int position) {
        BDAgenda dbP = new BDAgenda(context);
        List<Agenda> listaaux;

        listaaux = dbP.listarDia(dataStringAtual,dataString,position);


        return listaaux;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClickListener(final View view, final int position) {


        if (view.getId() == R.id.imgLixeira) {

            final Alerta  a = new Alerta(context);


            double w=0;
            double h=0;


            a.enviarAlerta("Deseja deletar \n" +listaR.get(position).getNomeP(), "Deletar", true, false, false).show();
            a.getBtSim().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, " Deletado com sucesso!", Toast.LENGTH_LONG).show();
                    BDAgenda bdAgenda = new BDAgenda(context);

                    NotificationManager note = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                    excluirAviso(position);


                    note.cancel((int) listaR.get(position).getId());
                    bdAgenda.delete(listaR.get(position));

                    AdapterRecycleAgenda adapterRecycle = (AdapterRecycleAgenda) mRecyclerView.getAdapter();
                    adapterRecycle.remove(position);


                    a.fechar();

                }
            });
            a.getBtNao().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.fechar();

                }
            });

        }else    if (view.getId() == R.id.imgLapis) {


            final CadastroMensagem cadastroMensagem = new CadastroMensagem(context,listaR.get(position));
            cadastroMensagem.enviarAlerta().show();




            try {
                cor= new BDAgenda(context).getCor ((int) listaR.get(position).getId ());

            }catch (Exception e){
                cor= R.drawable.estilo9;
            }


            cadastroMensagem.getL1 ().setBackgroundResource (cor);

            cadastroMensagem.getBtcor1 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo2;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });

            cadastroMensagem.getBtcor2 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.hoje;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor3 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo5;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor4 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo6;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getSwitchSonOnOff ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    if(cadastroMensagem.getSwitchSonOnOff ().isChecked ()){
                        sonLigado=true;
                        cadastroMensagem.sonOnOff();
                    }else {
                        sonLigado=false;
                        cadastroMensagem.sonOnOff();

                    }
                }
            });

            cadastroMensagem.getBtcor5 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo44;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor6 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo4;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor7 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo3;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor8 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo33;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });


            cadastroMensagem.getBtcor9 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo88;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor10 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo8;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor11 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo889;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor12 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo884;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });

            cadastroMensagem.getBtcor13 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo9;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor14 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo91;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor15 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo92;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor16 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo93;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });







            cadastroMensagem.getCalendario().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance();
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    int mes = calendar.get( Calendar.MONTH );
                    int ano = calendar.get( Calendar.YEAR );

                    DatePickerDialog datePickerDialog = new DatePickerDialog( view.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            cadastroMensagem.getCalendario().setText( dayOfMonth+"-"+(month+1)+"-"+year );
                        }
                    },ano,mes,dia );

                    datePickerDialog.show();



                }
            });


            cadastroMensagem.getBtFechar().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    cadastroMensagem.fechar();
                }
            });
            try {
                repetir=new BDAgenda(context).getRepetir ((int) listaR.get(position).getId());

            }catch (Exception e){

            }

            cadastroMensagem.getBtFechar().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    cadastroMensagem.fechar();
                }
            });

            if(repetir==0){
                cadastroMensagem.getRadio_null ().setChecked (true);
            }else if(repetir==86400000*2){
                cadastroMensagem.getRadio_dia_s_n ().setChecked (true);
            }else if(repetir==86400000){
                cadastroMensagem.getRadio_dia ().setChecked (true);
            }else if(repetir==604800000){
                cadastroMensagem.getRadio_semana ().setChecked (true);
            }else if((int)repetir== (int)2628000000l){
                cadastroMensagem.getRadio_mes ().setChecked (true);
            }else if((int)repetir==(int)31536000000l){
                cadastroMensagem.getRadio_ano ().setChecked (true);
            }




            cadastroMensagem.getRadio_dia_s_n ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=86400000*2;
                }
            });


            cadastroMensagem.getRadio_dia().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=86400000;
                }
            });
            cadastroMensagem.getRadio_semana ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=604800000;
                }
            });
            cadastroMensagem.getRadio_mes ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=2628000000l;
                }
            });
            cadastroMensagem.getRadio_ano ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=31536000000l;
                }
            });
            cadastroMensagem.getRadio_null ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=0;
                }
            });


            cadastroMensagem.getBt().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cadastroMensagem.popularDados();
                    final BDAgenda bdAgenda = new BDAgenda(context);


                    Agenda p = new Agenda();
                    p.setId(listaR.get(position).getId());
                    p.setIdCliente(listaR.get(position).getIdCliente());
                    p.setNomeP(cadastroMensagem.getNome());
                    final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    Date dateForm = null;
                    try {
                        dateForm=formato.parse(cadastroMensagem.getCalendario().getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                    Calendar  dataaux = Calendar.getInstance();
                    dataaux.set(Calendar.DAY_OF_MONTH, 2);

                    Date dateForm2 = dateForm;
                    dateForm2.setTime (dateForm.getTime ());





                    dataaux.setTime(dateForm2);


                    dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
                    dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
                    dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
                    dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
                    dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));

                    int diax = dataaux.get(Calendar.DAY_OF_MONTH);
                    int mesx = dataaux.get(Calendar.MONTH);
                    int anox = dataaux.get(Calendar.YEAR);
                    String[] horarioMarcado = cadastroMensagem.getHora().split(":");
                    int horax=0;
                    try {
                        horax = Integer.valueOf (horarioMarcado[0]);
                    }catch (Exception e){

                    }
                    int minutox=0;
                    try {
                        minutox = Integer.valueOf (horarioMarcado[1]);
                    }catch (Exception e){

                    }
                    int mh=3600000*horax;
                    int mm=60000*minutox;
                    int thm=mh+mm;
                    if(horax>23){
                        horax=0;
                    }
                    String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

                    String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;

                    BDAgenda bd = new BDAgenda (context);


                    p.setNomeP (dataMarcada);

                    p.setData (dateForm);
                    p.setFeriado ("normal");
                    p.setDatacheck (dataMarcadaf);



                    p.setMensagem(cadastroMensagem.getMensagem());
                    p.setHorario(cadastroMensagem.getHora());

                    p.setDatacheck(cadastroMensagem.getCalendario().getText().toString());
                    p.setAno (anox);
                    p.setMes (mesx+1);
                    p.setDia (diax);
                    excluirAviso(position);




                    int minutosAviso=0;
                    try {
                        minutosAviso = Integer.parseInt(cadastroMensagem.getEdHorasAviso().getText().toString());
                    } catch (Exception e) {
                        minutosAviso=0;
                    }

                    Date aux = new Date ();
                    aux.setTime (dataaux.getTimeInMillis ()+thm);


                    long data5 = aux.getTime() - (HORA_AVISO * minutosAviso);
                    Date dataa = aux;
                    dataa.setTime(dataaux.getTimeInMillis ()+thm- (HORA_AVISO * minutosAviso));


                    GerarAviso aviso = new GerarAviso (context);
                    aviso.AgendarNotificacao (dataa, dataMarcada, cadastroMensagem.getMensagem (), (int) p.getId (), repetir,sonLigado);


//                   AgendarNotificacao(dataa, dataMarcada, cadastroMensagem.getMensagem(), (int) p.getId(),repetir);

                    cadastroMensagem.fechar();


                    bdAgenda.atualizar(p);
                    bdAgenda.atualizarCor ((int) p.getId (),cor,repetir);

                    fechar();
                    Toast.makeText(context, " Alterado com sucesso!", Toast.LENGTH_LONG).show();

                    AdapterRecycleAgenda adapterRecycle = (AdapterRecycleAgenda) mRecyclerView.getAdapter();
                    adapterRecycle.remove(position );
                    adapterRecycle.addLista(p,position);

                }


            });



            ///////////////////////////////////////////////////////////////////////////////

        }

    }

    private void excluirAviso(int position){
        SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
        long id =0l;
        try {
            String dataMarcada= listaR.get(position).getNomeP();
            Calendar dataaux;
            dataaux = Calendar.getInstance();
            dataaux.setTime(formatar.parse(dataMarcada));
            id=listaR.get(position).getId ();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, CriarNotificacao.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT |  PendingIntent.FLAG_MUTABLE);
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);

    }



    private void AgendarNotificacao(Date data, String titulo, String msg, int id,long repetir) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        // Obtém um alarm manager
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        // Prepara o intent que deverá ser lançado na data definida
        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("repetir", repetir);

        // Obtém o pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Regista o alerta no sistema.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }








    private void traduzir(String local){

        switch(local){

            case "português":
                btAgendar.setText(getContext ().getString (R.string.pt_novoAgendamento));

                break;
            case "español":
                btAgendar.setText(getContext ().getString (R.string.es_novoAgendamento));

                break;
            case "italiano":
                btAgendar.setText(getContext ().getString (R.string.it_novoAgendamento));

                break;
            default:
                btAgendar.setText(getContext ().getString (R.string.eg_novoAgendamento));

                break;

        }

    }





}