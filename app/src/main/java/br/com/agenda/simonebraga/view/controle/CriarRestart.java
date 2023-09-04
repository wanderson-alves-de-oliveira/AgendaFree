package br.com.agenda.simonebraga.view.controle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import androidx.annotation.RequiresApi;

import java.util.Date;

import br.com.agenda.simonebraga.view.dao.BDAgenda;

public class CriarRestart extends BroadcastReceiver{

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        new BDAgenda (context).restauraAlarme ();
        new BDAgenda (context).restaurarAgenda (false);
        new BDAgenda (context).restaurarAgenda (true);
        Date nova = new Date();
        nova.setTime (nova.getTime ());
        new RefreshAviso ().agendar (nova,context);


    }
}