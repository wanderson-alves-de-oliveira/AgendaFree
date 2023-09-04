package br.com.agenda.simonebraga.view.controle;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RefreshAviso {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void agendar(final Date date,Context context) {

        if (true) {

                      SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
                    Calendar dataaux;
                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);
                    dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
                    dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
                    dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
                    dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
                    dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));
                    dataaux.set(Calendar.SECOND, 0);

                    int diax = dataaux.get(Calendar.DAY_OF_MONTH);
                    int mesx = dataaux.get(Calendar.MONTH);
                    int anox = dataaux.get(Calendar.YEAR);
                     int horax = 1;
                    int minutox=16;
                    if(horax>23){
                        horax=0;
                    }
                    String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;
                    try {

                        dataaux.setTime(formatar.parse(dataMarcada));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(context, " Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);
                    dataaux.set(Calendar.DAY_OF_MONTH, 2);
                    try {
                        dataaux.setTime(formatar.parse(dataMarcada));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                     Date aux = dataaux.getTime();
                    long data5 = aux.getTime();
                    Date dataa = aux;
                    dataa.setTime(data5);
                     GerarAviso aviso = new GerarAviso (context);
                    aviso.AgendarNotificacao (dataa, dataMarcada, "Resetar alarmes", 1000000, 86400000,true);


        }

    }


    public void agendarVolume(final Date date,Context context,int id,int volume) {

        if (true) {

            SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
            Calendar dataaux;
            dataaux = Calendar.getInstance();
            dataaux.setTime(date);
            Date aux = dataaux.getTime();
            long data5 = aux.getTime();
            Date dataa = aux;
            dataa.setTime(data5);
            GerarAviso aviso = new GerarAviso (context);
           aviso.AgendarVolume (dataa, "dataMarcada", "Volume alarmes", id, volume);


        }

    }

    public void desligarAlarme(final Date date,Context context,int id,long idDoAlarme,boolean desligamentoAltomatico) {

        if (true) {

            SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
            Calendar dataaux;
            dataaux = Calendar.getInstance();
            dataaux.setTime(date);
            Date aux = dataaux.getTime();
            long data5 = aux.getTime();
            Date dataa = aux;
            dataa.setTime(data5);
            GerarAviso aviso = new GerarAviso (context);
            aviso.AgendarDesligamento (dataa, "dataMarcada", "Volume alarmes", id, idDoAlarme,desligamentoAltomatico);


        }

    }
}
