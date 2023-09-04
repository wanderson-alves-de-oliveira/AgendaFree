package br.com.agenda.simonebraga.view.controle;


import static br.com.agenda.simonebraga.view.view.Index.active;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.dao.BDAgenda;
import br.com.agenda.simonebraga.view.modelo.Agenda;
import br.com.agenda.simonebraga.view.view.Index;
import br.com.agenda.simonebraga.view.view.MinhaAtividade;


public class CriarNotificacao extends BroadcastReceiver{

    private    Uri uri ;
    public static Ringtone ringtone ;
    public static    Notification notification;
    public static    int id;
    public static    String informacao;
    private String aviso;
    private Context context;
    private int[] diasDaSemana;
    private static boolean sonLigado;
    public static boolean desligamentoAltomatico;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent paramIntent) {
        this.context= context;

        try {
            int id = paramIntent.getExtras().getInt("id");
            long repetir = paramIntent.getExtras().getLong("repetir");
            sonLigado = paramIntent.getExtras().getBoolean("sonLigado");
            if (id != 1000000) {

                if (id < 7) {
                    if (ringtone != null) {


                        if (ringtone.isPlaying()) {
                            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                            //audio.setStreamMute (AudioManager.STREAM_NOTIFICATION,true);

                            int mais = paramIntent.getExtras().getInt("mais");
                            audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume, 0);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                                ringtone.setVolume(Float.valueOf(mais) * 0.01f);


                            }

                            //Toast.makeText (context, "" + mais, Toast.LENGTH_SHORT).show ();
                        } else {

                        }

                    }

                } else if (id == 7) {
                    son(paramIntent);


                    Date mais = new Date();
                    mais.setTime(mais.getTime() + 5000);
                    new RefreshAviso().agendarVolume(mais, context, 6, 12);
                    mais.setTime(mais.getTime() + 10000);
                    new RefreshAviso().agendarVolume(mais, context, 5, 24);
                    mais.setTime(mais.getTime() + 15000);
                    new RefreshAviso().agendarVolume(mais, context, 4, 36);
                    mais.setTime(mais.getTime() + 20000);
                    new RefreshAviso().agendarVolume(mais, context, 3, 48);
                    mais.setTime(mais.getTime() + 25000);
                    new RefreshAviso().agendarVolume(mais, context, 2, 60);
                    mais.setTime(mais.getTime() + 30000);
                    new RefreshAviso().agendarVolume(mais, context, 1, 74);
                    mais.setTime(mais.getTime() + 35000);
                    new RefreshAviso().agendarVolume(mais, context, 0, 100);

                    AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                    //audio.setStreamMute (AudioManager.STREAM_NOTIFICATION,true);
                    audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume, 0);

                    //audio.setStreamVolume (AudioManager.STREAM_NOTIFICATION, 1, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ringtone.setVolume(0.05f);
                    }
                    ringtone.play();

                } else if (id == 8) {

                    if (ringtone != null) {
                        Intent intent = new Intent(context, Index.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        if (ringtone.isPlaying()) {
                            if (active) {
                            } else {
                                System.exit(0);

                            }
                        }
                    } else {
                        if (active) {
                        } else {
                            System.exit(0);

                        }
                    }
                } else {


                    son(paramIntent);


                    this.id = id;
                    BDAgenda bd = new BDAgenda(context);
                    Agenda p = bd.agendamento(id);
                    //horror do calendario
                    if (p.getFeriado().equals("feriado") || p.getFeriado().equals("normal")) {

                        try {
                            GerarAviso avisox = new GerarAviso(context);

                            if (repetir > 0) {
                                avisox.AgendarNotificacaoRepetir(repetir, paramIntent.getExtras().getString("titulo"),
                                        aviso, id, sonLigado);


                            }


                            Date mais = new Date();
                            mais.setTime(mais.getTime() + 5000);
                            new RefreshAviso().agendarVolume(mais, context, 6, 12);
                            mais.setTime(mais.getTime() + 10000);
                            new RefreshAviso().agendarVolume(mais, context, 5, 24);
                            mais.setTime(mais.getTime() + 15000);
                            new RefreshAviso().agendarVolume(mais, context, 4, 36);
                            mais.setTime(mais.getTime() + 20000);
                            new RefreshAviso().agendarVolume(mais, context, 3, 48);
                            mais.setTime(mais.getTime() + 25000);
                            new RefreshAviso().agendarVolume(mais, context, 2, 60);
                            mais.setTime(mais.getTime() + 30000);
                            new RefreshAviso().agendarVolume(mais, context, 1, 74);
                            mais.setTime(mais.getTime() + 35000);
                            new RefreshAviso().agendarVolume(mais, context, 0, 100);

                            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                            //audio.setStreamMute (AudioManager.STREAM_NOTIFICATION,true);
                            audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume, 0);

                            //audio.setStreamVolume (AudioManager.STREAM_NOTIFICATION, 1, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ringtone.setVolume(0.05f);
                            }
                            if (sonLigado) {
                                ringtone.play();
                            }
                            if (repetir > 0) {
                                gravarAgenda();
                            }
                        } catch (Exception e) {
                        }
                    }
                    //horario do alarme
                    else {
                        son(paramIntent);

                        try {
                            GerarAviso avisox = new GerarAviso(context);

                            avisox.AgendarAlarmeRepetir(repetir, paramIntent.getExtras().getString("titulo"),
                                    aviso, id, diasDaSemana);

                            Date mais = new Date();
                            mais.setTime(mais.getTime() + 5000);
                            new RefreshAviso().agendarVolume(mais, context, 6, 12);
                            mais.setTime(mais.getTime() + 10000);
                            new RefreshAviso().agendarVolume(mais, context, 5, 24);
                            mais.setTime(mais.getTime() + 15000);
                            new RefreshAviso().agendarVolume(mais, context, 4, 36);
                            mais.setTime(mais.getTime() + 20000);
                            new RefreshAviso().agendarVolume(mais, context, 3, 48);
                            mais.setTime(mais.getTime() + 25000);
                            new RefreshAviso().agendarVolume(mais, context, 2, 60);
                            mais.setTime(mais.getTime() + 30000);
                            new RefreshAviso().agendarVolume(mais, context, 1, 74);
                            mais.setTime(mais.getTime() + 35000);
                            new RefreshAviso().agendarVolume(mais, context, 0, 100);

                            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                            //audio.setStreamMute (AudioManager.STREAM_NOTIFICATION,true);
                            audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume, 0);

                            //audio.setStreamVolume (AudioManager.STREAM_NOTIFICATION, 1, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ringtone.setVolume(0.05f);
                            }
                            ringtone.play();


                            // rodar=true;
                            //   run ();
                        } catch (Exception e) {
                            Log.e("TAG", e + "");

                        }


                    }
                }
            } else {

                new BDAgenda (context).restauraAlarme();
                new BDAgenda(context).restaurarAgenda(false);
                new BDAgenda(context).restaurarAgenda(true);

                final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

                Date dataR = new Date();
                //
                dataR.setTime(dataR.getTime() + (86400000));
                new RefreshAviso().agendar(dataR, context);


            }
        }catch (Exception e){}
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void son(Intent paramIntent){

        Intent fullScreenIntent = new Intent(context, MinhaAtividade.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_MUTABLE);
        informacao= paramIntent.getExtras ().getString ("aviso");



        NotificationManager note = (NotificationManager) context.getSystemService (context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity (context, 0, new Intent (context,
                Index.class), PendingIntent.FLAG_MUTABLE | 0);

        String tema = new BDAgenda(context).getSon (2);
        uri = Uri.parse (tema);


        try {
            RingtoneManager.setActualDefaultRingtoneUri (context.getApplicationContext (), RingtoneManager.TYPE_ALARM, uri);

            uri = RingtoneManager.getActualDefaultRingtoneUri (context.getApplicationContext (), RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone (context.getApplicationContext (), uri);

            tema = new BDAgenda (context).getSon (3);
            uri = Uri.parse (tema);
            RingtoneManager.setActualDefaultRingtoneUri (context.getApplicationContext (), RingtoneManager.TYPE_ALARM, uri);

        } catch (Exception e) {

            uri = RingtoneManager.getActualDefaultRingtoneUri (context.getApplicationContext (), RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone (context.getApplicationContext (), uri);

        }


        // ringtone = RingtoneManager.getRingtone(context,som);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping (true);
        }

        NotificationCompat.InboxStyle estilo = new NotificationCompat.InboxStyle ();
        aviso = paramIntent.getExtras ().getString ("aviso");
        diasDaSemana = paramIntent.getExtras ().getIntArray ("diasDaSemana");

        String[] descri;
        try {
            descri = new String[(aviso.length () / 40) + 1];
        } catch (Exception e) {

            descri = new String[0];


        }
        String aux = "";
        int j = 0;
        for (int i = 0; i < aviso.length (); i++) {
            aux += aviso.charAt (i);
            if (aux.length () == 40) {
                descri[j] = aux;
                estilo.addLine (descri[j]);
                aux = "";
                j++;
            }

        }
        descri[j] = aux;
        estilo.addLine (descri[j]);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService (Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        int importance = NotificationManager.IMPORTANCE_MAX;
        NotificationChannel notificationChannel = new NotificationChannel ("ID", "Aviso", importance);

        notificationManager.createNotificationChannel (notificationChannel);


        builder = new NotificationCompat.Builder (context, notificationChannel.getId ());

        builder.setTicker ("Aviso")
                .setContentTitle (paramIntent.getExtras ().getString ("titulo"))
                .setStyle (estilo)
                .setSmallIcon (R.drawable.lapis)
                //.setStyle( new NotificationCompat.BigPictureStyle().bigPicture( bitmap ) )
                .setVisibility (Notification.VISIBILITY_PUBLIC)
                .setContentIntent (pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notification = builder.build ();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;





        Date mais = new Date ();
        mais.setTime (mais.getTime () +120000);
        new RefreshAviso ().desligarAlarme (mais, context, 8, new Date ().getTime (),true);





        note.notify (id, notification);
    }


    public void gravarAgenda() {


        Agenda p = new Agenda();


        SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

        Calendar dataaux;
        dataaux = Calendar.getInstance();
        dataaux.setTime(new Date ());

        dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
        dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
        dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
        dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
        dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));
        dataaux.set(Calendar.SECOND, 0);

        int diax = dataaux.get(Calendar.DAY_OF_MONTH);
        int mesx = dataaux.get(Calendar.MONTH);
        int anox = dataaux.get(Calendar.YEAR);
        String arrayHora =dataaux.get (Calendar.HOUR_OF_DAY)+":"+dataaux.get (Calendar.MINUTE);
        String[] horarioMarcado = arrayHora.split(":");
        int horax =  Integer.valueOf(horarioMarcado[0]);
        int minutox=0;
        try {
            minutox = Integer.valueOf (horarioMarcado[1]);
        }catch (Exception e){

        }

        if(horax>23){
            horax=0;
        }
        String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;
        try {

            dataaux.setTime(formatar.parse(dataMarcada));

            p.setId((dataaux.getTimeInMillis()-604800001)/1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        p.setNomeP(dataMarcada);
        p.setIdCliente(0);

        p.setData(new Date ());
        p.setMensagem(aviso);
        p.setHorario(arrayHora);
        p.setFeriado("normal");
        p.setDia (diax);
        p.setMes (mesx+1);
        p.setAno (anox);
        int cor = R.drawable.estilo3;

        new BDAgenda (context).inserir(p);
        new BDAgenda (context).insertCor ((int) p.getId (),cor,0);

    }







}