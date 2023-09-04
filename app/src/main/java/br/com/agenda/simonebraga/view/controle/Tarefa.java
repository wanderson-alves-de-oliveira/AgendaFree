package br.com.agenda.simonebraga.view.controle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.com.agenda.simonebraga.view.view.TarefaInterface;

public class Tarefa extends AsyncTask<Uri, String, String> {
    private final ProgressDialog progressDialog;
    private Context context;
    private static final String nomeDB = "ABNz";
    private String msg;
    private TarefaInterface tarefaInterface;
private   boolean erro = false;

    public Tarefa(Context context, TarefaInterface tarefaInterface) {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
        this.tarefaInterface = tarefaInterface;
    }


    @Override
    protected void onPreExecute() {

        progressDialog.setMessage("inviando...");
        progressDialog.show();


    }

    @Override
    protected String doInBackground(Uri... uri) {

        Uri urix = uri[0];
        String currentDBPath = "/data/" + "br.com.docesebolos.simonebraga"
                + "/databases/" + nomeDB;
        // String  backupDBPath ="simone.bd";

        String[] urlPura = urix.toString().split(".");

        final File file = new File(urix.getPath());
        String backupDBPath = "/" + file.getAbsoluteFile().getName();

        try {
            int i = Integer.parseInt(backupDBPath);
            String path = urlPura[0];
            String idStr = path.substring(path.lastIndexOf('/') + 1);

            File fx = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

            File fxx = new File(fx, idStr);

            msg = " Erro!! tente entrar diretamente no diretório da memoria interna ou no cartão SD";

        } catch (Exception e) {

            try {

                String destPath = file.getAbsoluteFile().getAbsolutePath();
                String auxz = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
                String[] urif = auxz.toString().split("0");
                String[] urif2 = destPath.toString().split(":");
                File fe = null;
                try {
                    fe = new File(urif[0] + "0//" + urif2[1]);
                } catch (Exception ee) {
                    erro = true;
                    fe = new File(auxz + backupDBPath);
                }
                File fx = new File(fe.getParent());
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {

                    publishProgress("carregando...");

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(fx, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    publishProgress("sucesso...");

                }

            } catch (FileNotFoundException ee) {
                e.printStackTrace();

                msg = "backup invalido";
            } catch (IOException ee) {
                e.printStackTrace();

                msg = "erro!";
            }

        }
        return msg;
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onProgressUpdate(String... strings) {
        progressDialog.setMessage(strings[0]);

    }

    @Override
    protected void onPostExecute(String strings) {
        if (erro) {
            tarefaInterface.depoisDeBaixado(" Erro!!  tente entrar diretamente no diretório da memoria interna ou no cartão SD");

        } else {
            tarefaInterface.depoisDeBaixado(" base de dados restaurada com sucesso!");
        }
         progressDialog.dismiss();
    }


}
