package br.com.agenda.simonebraga.view.controle;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.res.ResourcesCompat;
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
import br.com.agenda.simonebraga.view.adapter.AdapterRecycleAlarme;
import br.com.agenda.simonebraga.view.dao.BDAgenda;
import br.com.agenda.simonebraga.view.modelo.Agenda;
import br.com.agenda.simonebraga.view.view.RecycleViewOnclickListenerHack;


/**
 * Created by wanderson on 24/08/19.
 */

public class AvisoAlarme extends AppCompatDialog implements RecycleViewOnclickListenerHack {
    private AlertDialog alerta;
    private Context context;
    private final Long HORA_AVISO = 60000L;
    private long repetir =0;
    boolean tocarHoje=false;

    private LayoutInflater li;
    private View view;
    private RecyclerView mRecyclerView;
    private List<Agenda> listaR;
    private List<Agenda> listax1;
    private int position = 0;
    private String dataString;
    private Button btFechar;
    private Button btMais;
    private TextView tvnulo;
    private String[] diasDaSemanax=new String[7];

    public AvisoAlarme (Context context, String data) {
        super(context);
        this.context = context;
        this.dataString=data;



        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflamos o layout alerta.xml na view
        view = li.inflate(R.layout.aviso_lista_alarme, null);


        btFechar = view.findViewById( R.id.btFechar );
        btMais = view.findViewById( R.id.btmais );
        tvnulo = view.findViewById( R.id.tvnulo );


        mRecyclerView = view.findViewById(R.id.recicleview);
        mRecyclerView.setHasFixedSize(true);

        traduzir ( Locale.getDefault().getDisplayLanguage());

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
                    AdapterRecycleAlarme adapterRecycle = (AdapterRecycleAlarme) mRecyclerView.getAdapter();
                    listax1 = new ArrayList<>();
                    listax1 = carregar(position);
                    for (int i = 0; i < listax1.size(); i++) {
                        adapterRecycle.addLista(listax1.get(i), listaR.size());
                    }
                }
            }
        });
        listaR = carregar(position);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        AdapterRecycleAlarme adapterRecycle = new AdapterRecycleAlarme(context, listaR);
        adapterRecycle.setRecycleViewOnclickListenerHack(this);
        mRecyclerView.setAdapter(adapterRecycle);
        // Inflate the layout for this fragment


        if(listaR.size ()>0){

            tvnulo.setText ("");

        }

    }

    public AlertDialog enviarAlerta() {


        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setView(view);
        // builder.setCustomTitle(texto);
        alerta = builder.create();

        return alerta;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public Button getBtMais () {
        return btMais;
    }

    public void fechar(){

        alerta.dismiss();
    }

    public TextView getTvnulo () {
        return tvnulo;
    }

    private List<Agenda> carregar(int position) {
        BDAgenda dbP = new BDAgenda(context);
        List<Agenda> listaaux;

        listaaux = dbP.listarDiaAlarme(dataString,position);


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

                    AdapterRecycleAlarme adapterRecycle = (AdapterRecycleAlarme) mRecyclerView.getAdapter();
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


            final CadastroAlarme cadastroMensagem = new CadastroAlarme(context,listaR.get(position));
            cadastroMensagem.enviarAlerta().show();



            int[] diasDaSemana=new int[7];

            diasDaSemana[0]= -1;
            diasDaSemana[1]= -1;
            diasDaSemana[2]= -1;
            diasDaSemana[3]= -1;
            diasDaSemana[4]= -1;
            diasDaSemana[5]= -1;
            diasDaSemana[6]= -1;


            Calendar calendar = Calendar.getInstance();

            final int hora = calendar.get(Calendar.HOUR_OF_DAY);
            final int minuto = calendar.get(Calendar.MINUTE);


            cadastroMensagem.getBtDO ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[0]== -1){
                        diasDaSemana[0]=1;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroMensagem.getBtDO ().setBackground (dw);
                    }else {
                        diasDaSemana[0]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtDO ().setBackground (dw);
                    }

                }
            });




            cadastroMensagem.getBtSE ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[1]== -1){
                        diasDaSemana[1]=2;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroMensagem.getBtSE ().setBackground (dw);
                    }else {
                        diasDaSemana[1]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtSE ().setBackground (dw);
                    }

                }
            });


            cadastroMensagem.getBtTE ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[2]== -1){
                        diasDaSemana[2]=3;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroMensagem.getBtTE ().setBackground (dw);
                    }else {
                        diasDaSemana[2]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtTE ().setBackground (dw);
                    }

                }
            });


            cadastroMensagem.getBtQA ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[3]== -1){
                        diasDaSemana[3]=4;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroMensagem.getBtQA ().setBackground (dw);
                    }else {
                        diasDaSemana[3]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtQA ().setBackground (dw);
                    }

                }
            });

            cadastroMensagem.getBtQI ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[4]== -1){
                        diasDaSemana[4]=5;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroMensagem.getBtQI ().setBackground (dw);
                    }else {
                        diasDaSemana[4]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtQI ().setBackground (dw);
                    }

                }
            });


            cadastroMensagem.getBtSX ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[5]== -1){
                        diasDaSemana[5]=6;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroMensagem.getBtSX ().setBackground (dw);
                    }else {
                        diasDaSemana[5]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroMensagem.getBtSX ().setBackground (dw);
                    }

                }
            });



            cadastroMensagem.getBtSA ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = context.getResources();

                    if(diasDaSemana[6]== -1){
                        diasDaSemana[6]=7;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroMensagem.getBtSA ().setBackground (dw);
                    }else {
                        diasDaSemana[6]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);
                        cadastroMensagem.getBtSA ().setBackground (dw);
                    }

                }
            });



            String[] diasAlarme = new BDAgenda (context).agendamento ((int) listaR.get(position).getId()).getFeriado ().split (" ");
            Resources res = context.getResources();

            if(diasAlarme[0].equals (diasDaSemanax[0].trim ())){
                diasDaSemana[0]=1;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtDO ().setBackground (dw);
            }

            if(diasAlarme[1].equals (diasDaSemanax[1].trim ())){
                diasDaSemana[1]=2;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtSE ().setBackground (dw);
            }

            if(diasAlarme[2].equals (diasDaSemanax[2].trim ())){
                diasDaSemana[2]=3;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtTE ().setBackground (dw);
            }

            if(diasAlarme[3].equals (diasDaSemanax[3].trim ())){
                diasDaSemana[3]=4;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtQA ().setBackground (dw);
            }


            if(diasAlarme[4].equals (diasDaSemanax[4].trim ())){
                diasDaSemana[4]=5;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtQI ().setBackground (dw);
            }



            if(diasAlarme[5].equals (diasDaSemanax[5].trim ())){
                diasDaSemana[5]=6;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtSX ().setBackground (dw);
            }


            if(diasAlarme[6].equals (diasDaSemanax[6].trim ())){
                diasDaSemana[6]=7;
                repetir=86400000;

                Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                cadastroMensagem.getBtSA ().setBackground (dw);
            }




            cadastroMensagem.getBtFechar().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    cadastroMensagem.fechar();
                }            });


            cadastroMensagem.getBtFechar().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    cadastroMensagem.fechar();
                }
            });
//            cadastroMensagem.getRadio_dia().setOnClickListener (new View.OnClickListener () {
//                @Override
//                public void onClick (View view) {
//                    repetir=86400000;
//                }
//            });


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
                    Date dateForm = new Date ();

                    SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");


                    Calendar  dataaux = Calendar.getInstance();
                    dataaux.set(Calendar.DAY_OF_MONTH, 2);

                    Date dateForm2 = new Date ();


                    dataaux.setTime(dateForm2);

                    dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
                    dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
                    dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
                    dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
                    dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));
                    dataaux.set(Calendar.SECOND, 0);

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

                    if(horax>23){
                        horax=0;
                    }
                    String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

                    String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;



                    p.setNomeP (dataMarcada);

                    p.setData (dateForm);
                    String alarme = "";
                    for(int i=0;i<diasDaSemana.length;i++){
                        if(diasDaSemana[i]>-1) {
                            alarme += diasDaSemanax[i];

                        }else {
                            alarme += "- ";

                        }

                    }
                    p.setFeriado (alarme);
                    p.setDatacheck (dataMarcadaf);



                    p.setMensagem(cadastroMensagem.getMensagem());
                    p.setHorario(cadastroMensagem.getHora());

                    p.setDatacheck(dataMarcadaf);

                    excluirAviso(position);




                    int minutosAviso=0;
                    try {
                        minutosAviso = Integer.parseInt(cadastroMensagem.getEdHorasAviso().getText().toString());
                    } catch (Exception e) {
                        minutosAviso=0;
                    }

                    Date aux = null;
                    try {
                        aux = formatar.parse (dataMarcada);
                    } catch (ParseException e) {
                        e.printStackTrace ();
                    }
                    long data5 = aux.getTime() - (HORA_AVISO * minutosAviso);
                    Date dataa = aux;
                    dataa.setTime(data5);

                    bdAgenda.atualizar(p);

                    GerarAviso aviso = new GerarAviso (context);
                    aviso.AgendarAlarme (dataa, cadastroMensagem.getMensagem (), cadastroMensagem.getMensagem (), (int) p.getId (), repetir, diasDaSemana);


                    // AgendarNotificacao(dataa, dataMarcada, cadastroMensagem.getMensagem(), (int) p.getId(),repetir,diasDaSemana);

                    cadastroMensagem.fechar();


                    fechar();
                    Toast.makeText(context, " Alterado com sucesso!", Toast.LENGTH_LONG).show();

                    AdapterRecycleAlarme adapterRecycle = (AdapterRecycleAlarme) mRecyclerView.getAdapter();
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
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);

    }




    public void AgendarNotificacao(Date data, String titulo, String msg, int id, long repetir, int[] diasDaSemana) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.MILLISECOND,0);


        int diaDasemanaDehoje = calendar.get (Calendar.DAY_OF_WEEK);
        Date d=new Date ();
        int soma=0;
        for(int k=0;k<diasDaSemana.length;k++){
            if (diasDaSemana[k] > -1) {
                soma++;
                if(d.getTime ()<data.getTime ()){
                    tocarHoje=true;
                }
            }
        }
        if(tocarHoje==false) {
            if (soma > 0) {

                int periodo = 1;
                boolean dispertar = false;
                int i = 0;
                int ii = 0;
                while (dispertar == false) {
                    if (diaDasemanaDehoje - 1 == i) {
                        ii++;
                        if (ii == 7) {
                            ii = 0;
                        }
                        if (diasDaSemana[ii] > -1) {
                            dispertar = true;
                        } else {
                            periodo++;
                        }

                    } else {
                        i++;
                        ii++;
                        if (i == 7) {
                            i = 0;
                            ii = 0;
                        }
                    }

                }
                Date proximoAlarme2;
                proximoAlarme2 = data;
                proximoAlarme2.setTime (proximoAlarme2.getTime () + (repetir * periodo));

                calendar.setTime (proximoAlarme2);

            }
        }else {

        }
        // Prepara o intent que deverá ser lançado na data definida
        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("repetir", repetir);
        intent.putExtra("diasDaSemana", diasDaSemana);

        // Obtém o pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Regista o alerta no sistema.
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        AlarmManagerCompat alarmManagerCompat = null;
        alarmManagerCompat.setAlarmClock(alarmManager,  calendar.getTimeInMillis(), pendingIntent,pendingIntent);

    }





    private void traduzir(String local){

        switch(local){

            case "português":



                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "S ";
                diasDaSemanax[2]= "T ";
                diasDaSemanax[3]= "Q ";
                diasDaSemanax[4]= "Q ";
                diasDaSemanax[5]= "S ";
                diasDaSemanax[6]= "S ";
                break;
            case "español":


                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "L ";
                diasDaSemanax[2]= "M ";
                diasDaSemanax[3]= "M ";
                diasDaSemanax[4]= "J ";
                diasDaSemanax[5]= "V ";
                diasDaSemanax[6]= "S ";
                break;
            case "italiano":


                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "L ";
                diasDaSemanax[2]= "M ";
                diasDaSemanax[3]= "M ";
                diasDaSemanax[4]= "G ";
                diasDaSemanax[5]= "V ";
                diasDaSemanax[6]= "S ";
                break;
            default:


                diasDaSemanax[0]= "S ";
                diasDaSemanax[1]= "M ";
                diasDaSemanax[2]= "T ";
                diasDaSemanax[3]= "W ";
                diasDaSemanax[4]= "T ";
                diasDaSemanax[5]= "F ";
                diasDaSemanax[6]= "S ";

                break;

        }

    }

}