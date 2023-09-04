package br.com.agenda.simonebraga.view.controle;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

public class ValidarMostrarNotificcoes {

private Context context;
    public ValidarMostrarNotificcoes (Context context) {
        this.context=context;
    }

    public boolean validarMostrarNotificcoes () {

        NotificationManager note = (NotificationManager) context.getSystemService (context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!note.areNotificationsEnabled ()) {
                final AlertaMostrarNotificacao a = new AlertaMostrarNotificacao (context);
                a.enviarAlerta ("", "", true, false, false).show ();
                a.getBtSim ().setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        context.startActivity (new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse ("package:" + context.getPackageName ())));
                        a.fechar ();
                    }
                });
                a.getBtNao ().setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        a.fechar ();
                        System.exit (0);
                    }
                });

            }
        }
        return note.areNotificationsEnabled ();
    }


}
