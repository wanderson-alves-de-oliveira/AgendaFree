package br.com.agenda.simonebraga.view.controle;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.dao.BDAgenda;
import br.com.agenda.simonebraga.view.modelo.Agenda;

public class InserirFeriado {
  private   BDAgenda bdAgenda ;
private   String[] diaDe ;
private Context context;
    public InserirFeriado(Context context,int ano) {
        bdAgenda = new BDAgenda(context);
        int anox=ano;
        this.context=context;
        String[] datas = new String[]{
                "01-01-"+anox,
                 "25-12-"+anox};

        traduzir (Locale.getDefault().getDisplayLanguage());


for(int i =0;i<datas.length;i++) {
    gerarFeriado(datas[i],diaDe[i]);

 }
    }



    private void gerarFeriado(String data, String msg) {

        Agenda p = new Agenda();
        SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = formatar.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar dataaux;
        dataaux = Calendar.getInstance();
        dataaux.setTime(date);

        dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
        dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH) + 1);
        dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
        dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
        dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));

        int diax = dataaux.get(Calendar.DAY_OF_MONTH);
        int mesx = dataaux.get(Calendar.MONTH);
        int anox = dataaux.get(Calendar.YEAR);
        int horax = 0;
        int minutox = 0;

        String dataMarcada = diax + "-" + mesx + "-" + anox + "_" + horax + ":" + minutox;
        p.setId((int) date.getTime() / 1000);
        p.setNomeP(dataMarcada);
        p.setIdCliente(0);
        p.setData(date);
        p.setFeriado("feriado");
        p.setMensagem(msg);
        p.setHorario("00:00");
        bdAgenda.inserir(p);
        bdAgenda.insertCor ((int) p.getId (), R.drawable.estilo9,0);

    }


    private void traduzir(String local){

        switch(local){

            case "português":
                diaDe = new String[]{
                       context.getString (R.string.pt_anoNovo),
                        context.getString (R.string.pt_natal)
                };
                break;
            case "español":
                diaDe = new String[]{
                        context.getString (R.string.es_anoNovo),
                        context.getString (R.string.es_natal)
                };
                break;
            case "italiano":
                diaDe = new String[]{
                        context.getString (R.string.it_anoNovo),
                        context.getString (R.string.it_natal)
                };
                break;
            default:
                diaDe = new String[]{
                        context.getString (R.string.eg_anoNovo),
                        context.getString (R.string.eg_natal)
                };
                break;

        }

    }

}
