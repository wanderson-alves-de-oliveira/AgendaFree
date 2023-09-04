package br.com.agenda.simonebraga.view.controle;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.agenda.simonebraga.view.dao.BDAgenda;
import br.com.agenda.simonebraga.view.modelo.Agenda;

public class GerarAviso {
    private Context context;
    boolean tocarHoje=false;
   private String[] diasDaSemanax=new String[7];

    public GerarAviso (Context context) {
        this.context=context;
    }

    public void AgendarAlarme(Date data, String titulo, String msg, int id, long repetir, int[] diasDaSemana) {
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
                if(d.getTime ()<data.getTime ()&&diasDaSemana[k] ==diaDasemanaDehoje){
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
                proximoAlarme2.setTime (data.getTime () + (repetir * periodo));

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

        Alarmar( calendar ,  id, intent);

    }

    String alarme = "";

    public void AgendarAlarmeRepetir(long repetir, String titulo, String msg, int id, int[] diasDaSemana) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        Date proximoAlarme;
        proximoAlarme = new Date ();
        proximoAlarme.setTime (proximoAlarme.getTime ()+repetir);

        calendar.setTime(proximoAlarme);


        int soma=0;
        for(int k=0;k<7;k++){
            if (diasDaSemana[k] > -1) {
                soma++;
            }
        }

        if(soma>0) {
            final int  diaDasemanaDehoje = calendar.get (Calendar.DAY_OF_WEEK)-1;
            int periodo = 1;
            boolean dispertar = false;
            int i = 0;
            int ii = 0;

            while (dispertar==false) {
                if (diaDasemanaDehoje == i) {

                    if (ii == diasDaSemana.length) {
                        ii = 0;
                    }
                    if (diasDaSemana[ii] > -1) {
                        dispertar = true;
                    } else {
                        periodo++;
                    }
                    ii++;
                } else {
                    i++;
                    ii++;
                    if (i == diasDaSemana.length) {
                        i = 0;
                        ii = 0;
                    }
                }

            }
            proximoAlarme = new Date ();
            proximoAlarme.setTime (proximoAlarme.getTime ()+(repetir*periodo));

            calendar.setTime(proximoAlarme);


        }

        calendar.set(Calendar.MILLISECOND,0);

        // Prepara o intent que deverá ser lançado na data definida
        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("repetir", repetir);
        intent.putExtra("diasDaSemana", diasDaSemana);




        SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

        Calendar dataaux;
        dataaux = Calendar.getInstance();
        dataaux.setTime(proximoAlarme);

        dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
        dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
        dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
        dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
        dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));

        int diax = dataaux.get(Calendar.DAY_OF_MONTH);
        int mesx = dataaux.get(Calendar.MONTH);
        int anox = dataaux.get(Calendar.YEAR);
        int horax =   dataaux.get(Calendar.HOUR);
        int minutox =  dataaux.get(Calendar.MINUTE);

        BDAgenda bdv = new BDAgenda (context);
        Calendar dataauxv;
        dataauxv = Calendar.getInstance();
        dataauxv.setTime(bdv.agendamento (id).getData ());

        if(horax>23){
            horax=0;
        }

        if(diax!=dataauxv.get (Calendar.DAY_OF_MONTH)&&repetir==2628000000l||diax!=dataauxv.get (Calendar.DAY_OF_MONTH)&&repetir==31536000000l){
            diax=dataauxv.get (Calendar.DAY_OF_MONTH);
        }

        String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

        String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;

        BDAgenda bd = new BDAgenda (context);
        Agenda p = bd.agendamento (id);
        if(p!=null) {
            p.setNomeP (dataMarcada);

            final SimpleDateFormat formato = new SimpleDateFormat ("dd-MM-yyyy");
            Date dateForm = null;
            try {
                dateForm = formato.parse (dataMarcadaf);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            p.setData (dateForm);

            String local=  Locale.getDefault().getDisplayLanguage();

            traduzir (local);


            for(int i=0;i<diasDaSemana.length;i++){
                if(diasDaSemana[i]>-1) {
                    alarme += diasDaSemanax[i];

                }else {
                    alarme += "- ";

                }

            }
            p.setFeriado (alarme);
            p.setDatacheck (dataMarcadaf);
            bd.atualizar (p);
            calendar.setTime(dataaux.getTime ());


            Alarmar( calendar ,  id, intent);



        }
    }





    public void AgendarNotificacao(Date data, String titulo, String msg, int id, long repetir,boolean sonLigado) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.MILLISECOND,0);
        // Prepara o intent que deverá ser lançado na data definida


        titulo="";
        for(int k=0;k<msg.length ();k++){
            if (k<12) {
                titulo=titulo+msg.charAt (k);

            }


            if(k>=12||k>=msg.length ()){
                titulo=titulo+"...";
                break;
            }


        }


        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("repetir", repetir);
        intent.putExtra("sonLigado", sonLigado);

        Alarmar( calendar ,  id, intent);


    }


    public void AgendarNotificacaoRepetir(long repetir, String titulo, String msg, int id,boolean sonLigado) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance ();
        Date proximoAlarme;
        proximoAlarme = new Date ();
        proximoAlarme.setTime (proximoAlarme.getTime () + repetir);
        SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

        calendar.set(Calendar.MILLISECOND,0);



        Calendar calendarP = Calendar.getInstance ();
        Date proximoAlarmeP;
        proximoAlarmeP = new Date ();
        calendarP.setTime(proximoAlarmeP);
        calendarP.set(Calendar.MILLISECOND,0);

        int diaxP = 2;
        int mesxP = calendarP.get(Calendar.MONTH)+2;
        int anoxP = calendarP.get(calendarP.YEAR);
        String dataMarcadaP = diaxP + "-" + mesxP + "-" + anoxP + "_" + 01 + ":" + 00;

        try {
            calendarP.setTime (formatar.parse (dataMarcadaP));

            calendarP.set(Calendar.YEAR, calendarP.get(Calendar.YEAR));
            calendarP.set(Calendar.MONTH, calendarP.get(Calendar.MONTH));
            calendarP.set(Calendar.DAY_OF_MONTH, calendarP.get(Calendar.DAY_OF_MONTH));
            calendarP.set(Calendar.HOUR_OF_DAY, calendarP.get(Calendar.HOUR_OF_DAY));
            calendarP.set(Calendar.MINUTE, calendarP.get(Calendar.MINUTE));

        } catch (ParseException e) {
            e.printStackTrace ();
        }

        // Prepara o intent que deverá ser lançado na data definida

        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("repetir", repetir);
        intent.putExtra("sonLigado", sonLigado);





        Calendar dataaux;
        dataaux = Calendar.getInstance();
        dataaux.setTime(proximoAlarme);

        dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
        dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
        dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
        dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
        dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));

        BDAgenda bd = new BDAgenda (context);
        Agenda p = bd.agendamento (id);

        int diax = dataaux.get(Calendar.DAY_OF_MONTH);
        int mesx = dataaux.get(Calendar.MONTH);
        int anox = dataaux.get(Calendar.YEAR);
        String[] horarioMarcado = p.getHorario ().split(":");
        int horax =  Integer.valueOf(horarioMarcado[0]);
        int minutox = Integer.valueOf(horarioMarcado[1]);


        BDAgenda bdv = new BDAgenda (context);
        Calendar dataauxv;
        dataauxv = Calendar.getInstance();
        dataauxv.setTime(bdv.agendamento (id).getData ());

        if(horax>23){
            horax=0;
        }

        int ultimoDiaDoMes=calendarP.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(diax!=dataauxv.get (Calendar.DAY_OF_MONTH)&&repetir==2628000000l||diax!=dataauxv.get (Calendar.DAY_OF_MONTH)&&repetir==31536000000l){
            diax=p.getDia ();

        }

        String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

        String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;


        if(p!=null) {
            p.setNomeP (dataMarcada);

            final SimpleDateFormat formato = new SimpleDateFormat ("dd-MM-yyyy");
            Date dateForm = null;
            try {
                dateForm = formato.parse (dataMarcadaf);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            p.setData (dateForm);
            p.setFeriado ("normal");
            p.setDatacheck (dataMarcadaf);
            if(repetir!=2628000000l) {
                bd.atualizar (p);
                calendar.setTime (dataaux.getTime ());
            }else {

                Date dateFormDeMes = null;
                try {
                    if(diax>ultimoDiaDoMes){
                        diax=ultimoDiaDoMes;
                    }

                    if(mesxP>12){
                        mesxP=1;
                    }
                    dataMarcada = diax + "-" + mesxP+ "-" + anox + "_" + horax + ":" + minutox;
                    dateFormDeMes = formatar.parse (dataMarcada);

                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                calendar.setTime (dateFormDeMes);

            }
            Alarmar( calendar ,  id, intent);
        }

    }


    public void Alarmar( Calendar calendar , int id,Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast (context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Regista o alerta no sistema.
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService (ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC, calendar.getTimeInMillis (), pendingIntent);
        AlarmManagerCompat alarmManagerCompat = null;
        alarmManagerCompat.setAlarmClock (alarmManager, calendar.getTimeInMillis (), pendingIntent, pendingIntent);

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
    public void AgendarVolume(Date data, String titulo, String msg, int id, int volume) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("mais", volume);

        Alarmar( calendar , id, intent);


    }

    public void AgendarDesligamento(Date data, String titulo, String msg, int id, long idAlarme,boolean desligamentoAltomatico) {
        // Obtém um novo calendário e define a data para a data da notificação
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        Intent intent = new Intent(context, CriarNotificacao.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", msg);
        intent.putExtra("id", id);
        intent.putExtra("mais", idAlarme);
        intent.putExtra("desligamentoAltomatico", desligamentoAltomatico);

        Alarmar( calendar , id, intent);


    }

}
