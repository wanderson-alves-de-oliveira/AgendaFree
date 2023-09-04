package br.com.agenda.simonebraga.view.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.agenda.simonebraga.view.controle.GerarAviso;
import br.com.agenda.simonebraga.view.modelo.Agenda;


/**
 * Created by wanderson on 04/09/19.
 */

public class BDAgenda {

    private List<Agenda> listaR;
private    String[] diasDaSemanax=new String[7];

    private SQLiteDatabase db;
    private   Context context;
    public BDAgenda(Context context){
        ConectionDB aux = new ConectionDB( context );
        this.context=context;
        db = aux.getWritableDatabase();

    }

    public Long getId(){

        Cursor cursor = db.rawQuery("SELECT _id FROM agenda ORDER BY _id DESC LIMIT 1;", null);
        cursor.moveToNext();
        cursor.close();

        return cursor.getLong(0);
    }

    public void inserir(Agenda agenda){
        ContentValues valores = new ContentValues(  );
        valores.put( "_id", agenda.getId());
        valores.put( "_idCliente", agenda.getIdCliente());
        valores.put( "nome", agenda.getNomeP());
        valores.put( "dia", agenda.getDia());
        valores.put( "mes", agenda.getMes());
        valores.put( "ano", agenda.getAno());
        valores.put( "data", agenda.getData().getTime());
        valores.put( "mensagem", agenda.getMensagem());
        valores.put( "horario", agenda.getHorario());
        valores.put( "feriado", agenda.getFeriado());




        SimpleDateFormat formatar= new SimpleDateFormat( "dd-MM-yyyy" );


        Calendar dataFormatada;
        String t = "";
        dataFormatada = Calendar.getInstance();
        try {
            t = formatar.format( agenda.getData().getTime() );
            dataFormatada.setTime( formatar.parse( t ) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valores.put( "datacheck", t );


        db.insert( "agenda", null, valores );

        //  Toast.makeText(context,"oi "+t,Toast.LENGTH_SHORT).show();

    }


    public void atualizar(Agenda agenda){
        ContentValues valores = new ContentValues(  );
        valores.put( "_id", agenda.getId());
        valores.put( "_idCliente", agenda.getIdCliente());
        valores.put( "nome", agenda.getNomeP());
        valores.put( "dia", agenda.getDia());
        valores.put( "mes", agenda.getMes());
        valores.put( "ano", agenda.getAno());
        valores.put( "data", agenda.getData().getTime());
        valores.put( "mensagem", agenda.getMensagem());
        valores.put( "horario", agenda.getHorario());
        valores.put( "feriado", agenda.getFeriado());




        SimpleDateFormat formatar= new SimpleDateFormat( "dd-MM-yyyy" );


        Calendar dataFormatada;
        String t = "";
        dataFormatada = Calendar.getInstance();
        try {
            t = formatar.format( agenda.getData().getTime() );
            dataFormatada.setTime( formatar.parse( t ) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valores.put( "datacheck", t );



        db.update( "agenda", valores, "_id = ?", new String[]{"" + agenda.getId()} );

    }

    public void atualizarTema(int tema){
        ContentValues valores = new ContentValues(  );
        valores.put( "tema", ""+tema );
        db.update( "tema", valores, "_id = ?", new String[]{"" + 1} );

    }
    public void inserirSon(String tema,int num){
        ContentValues valores = new ContentValues(  );
        valores.put( "tema", ""+tema );
        valores.put( "_id", num);

        valores.put( "tema", tema );

        db.insert ("tema", null, valores);


    }
    public void atualizarSon(String tema,int num){
        ContentValues valores = new ContentValues(  );
        valores.put( "tema", tema );

        Cursor cursor = db.rawQuery( "SELECT tema FROM tema " +
                "WHERE _id='"+num+"' ", null );
        cursor.moveToFirst();
        if(cursor.getCount ()>0) {
            db.update( "tema", valores, "_id = ?", new String[]{"" + num} );

        }else {
            inserirSon (tema,num);

        }
        cursor.close();


    }
    public void atualizarVersion(int version){
        ContentValues valores = new ContentValues(  );
        valores.put( "num", ""+version );
        db.update( "version", valores, "_id = ?", new String[]{"" + 1} );

    }
    public void delete(Agenda agenda) {
        db.execSQL("DELETE  FROM agenda WHERE _id ="+agenda.getId()  );

    }


    public void deleteFeriados() {

        db.execSQL("DELETE  FROM agenda WHERE feriado='feriado'"  );
    }

    public List<Agenda> listar(long inicio, long fim) {

        List<Agenda> lista = new ArrayList< >();


        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,feriado FROM agenda " +
                "WHERE feriado ='normal' AND data BETWEEN " + inicio + " AND " + fim + " ORDER BY data ASC", null );



        final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Calendar pegarAnoF = Calendar.getInstance();
        Date data = new Date ();
        data.setTime (fim);
        pegarAnoF.setTime(data);

        Calendar pegarAnoI = Calendar.getInstance();
        Date data0 = new Date ();
        data0.setTime (inicio);
        pegarAnoI.setTime(data0);

        Calendar hoje = Calendar.getInstance();
        Date datah = new Date ();
        hoje.setTime(datah);



        long primeiroDiaDoMes=0;
        long ultimoDiaDoMes=0;

        try {
            primeiroDiaDoMes=formato.parse ("01-01-"+pegarAnoI.get (Calendar.YEAR)).getTime ();
            ultimoDiaDoMes=formato.parse ("31-12-"+pegarAnoF.get (Calendar.YEAR)).getTime ();

        } catch (ParseException e) {
            e.printStackTrace ();
        }


        Cursor cursor2= db.rawQuery( "SELECT  agenda._id, agenda._idCliente, agenda.nome, agenda.dia, agenda.mes, agenda.ano, agenda.data, agenda.mensagem, agenda.horario, agenda.feriado FROM agenda " +
                "INNER JOIN cores ON  agenda._id==cores._id AND cores.repetir==2628000000 ORDER BY agenda.data ASC", null );


        if (cursor2.getCount () > 0) {

            cursor2.moveToFirst ();

            do {

                //   if (getRepetir ((int) cursor2.getLong (0)) == 2628000000l) {
                Agenda p = new Agenda ();
                p.setId (cursor2.getLong (0));
                p.setIdCliente (cursor2.getLong (1));
                p.setNomeP (cursor2.getString (2));
                p.setDia (cursor2.getInt (3));
                p.setMes (cursor2.getInt (4));
                p.setAno (cursor2.getInt (5));

                Date date = new Date ();
                date.setTime (Long.valueOf (cursor2.getString (6)));


                p.setData (date);
                p.setMensagem (cursor2.getString (7));
                p.setHorario (cursor2.getString (8));
                p.setFeriado (cursor2.getString (9));



                Calendar hojex = Calendar.getInstance();
                hojex.setTime(p.getData ());
                if(hojex.get (Calendar.DAY_OF_MONTH)>pegarAnoF.getActualMaximum(pegarAnoF.DAY_OF_MONTH)){
                    try {
                        p.setData (formato.parse(pegarAnoF.getActualMaximum(pegarAnoF.DAY_OF_MONTH)+"-"+pegarAnoF.get (Calendar.MONTH)+ "-" + pegarAnoF.get (Calendar.YEAR)));
                    } catch (ParseException e) {
                        e.printStackTrace ();
                    }


                }

                lista.add (p);
                // }


            } while (cursor2.moveToNext ());

        }


        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                Agenda p = new Agenda ();
                p.setId (cursor.getLong (0));
                p.setIdCliente (cursor.getLong (1));
                p.setNomeP (cursor.getString (2));
                p.setDia (cursor.getInt (3));
                p.setMes (cursor.getInt (4));
                p.setAno (cursor.getInt (5));

                Date date = new Date ();
                date.setTime (Long.valueOf (cursor.getString (6)));
                p.setData (date);
                p.setMensagem (cursor.getString (7));
                p.setHorario (cursor.getString (8));
                p.setFeriado (cursor.getString (9));

                lista.add (p);

            } while (cursor.moveToNext());

        }
        cursor.close();

        return lista;
    }








    public List<Agenda> listarFeriados(long inicio, long fim) {

        List<Agenda> lista = new ArrayList< >();


        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,feriado FROM agenda " +
                "WHERE feriado ='feriado' AND data BETWEEN " + inicio + " AND " + fim + " ORDER BY data ASC", null );



        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );

                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setFeriado( cursor.getString( 9) );

                lista.add( p );
            } while (cursor.moveToNext());

        }
        cursor.close();

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return lista;
    }






    public List<Agenda> listarDiasAgenda() {

        List<Agenda> lista = new ArrayList< >();

        String n ="normal";
        String f ="feriado";

        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,datacheck,feriado FROM agenda " +
                "WHERE feriado='"+n+"' ORDER BY horario ASC ", null );



        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setDatacheck(  cursor.getString( 9 ) );
                p.setFeriado( cursor.getString( 10) );

                lista.add( p );
            } while (cursor.moveToNext());

        }
        cursor.close();

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return lista;
    }






    public List<Agenda> listarDiasAgendaMensalAnterior() {

        List<Agenda> lista = new ArrayList< >();

        String n ="normal";
        SimpleDateFormat formatoz = new SimpleDateFormat ("dd-MM-yyyy");
        Date datAtual = new Date ();
        String data = formatoz.format (datAtual);
        Cursor cursor= db.rawQuery( "SELECT  DISTINCT agenda._id, agenda._idCliente, agenda.nome, agenda.dia, agenda.mes, agenda.ano," +
                " agenda.data, agenda.mensagem, agenda.horario,agenda.datacheck, agenda.feriado FROM agenda " +
                "INNER JOIN cores ON  agenda._id=cores._id " +
                "WHERE cores.repetir=2628000000 AND agenda.datacheck<'"+data+"'   AND agenda.feriado='"+n+"'" +
                " ORDER BY agenda.horario ASC ", null );


        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setDatacheck(  cursor.getString( 9 ) );
                p.setFeriado( cursor.getString( 10) );

                lista.add( p );
            } while (cursor.moveToNext());

        }
        cursor.close();

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return lista;
    }





    public List<Agenda> listarDia(String datAtual,String dat,int position) {

        List<Agenda> lista = new ArrayList< >();

        SimpleDateFormat formatoz = new SimpleDateFormat ("dd-MM-yyyy");

        String n ="normal";
        String f ="feriado";

        Calendar hojexc= Calendar.getInstance();
        try {
            hojexc.setTime(formatoz.parse (dat));
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        int diaSelecionado=hojexc.get (Calendar.DAY_OF_MONTH);

        Cursor cursor= db.rawQuery( "SELECT  DISTINCT agenda._id, agenda._idCliente, agenda.nome, agenda.dia, agenda.mes, agenda.ano," +
                " agenda.data, agenda.mensagem, agenda.horario,agenda.datacheck, agenda.feriado FROM agenda " +
                "INNER JOIN cores ON  agenda._id=cores._id " +
                "WHERE cores.repetir=2628000000 AND agenda.dia='"+diaSelecionado+"'   AND agenda.feriado='"+n+"' OR " +
                " cores.repetir=2628000000 AND agenda.dia='"+diaSelecionado+"'    AND agenda.feriado='"+f+"'ORDER BY agenda.horario ASC LIMIT "+position+" , 70 ", null );



        Cursor cursor2= db.rawQuery( "SELECT DISTINCT agenda._id, agenda._idCliente, agenda.nome, agenda.dia, agenda.mes, agenda.ano, " +
                "agenda.data, agenda.mensagem, agenda.horario,agenda.datacheck, agenda.feriado FROM agenda " +
                "INNER JOIN cores ON  agenda._id=cores._id " +
                "WHERE cores.repetir<>2628000000 AND agenda.datacheck='"+datAtual+"' AND agenda.feriado='"+n+"' OR " +
                "cores.repetir<>2628000000 AND agenda.datacheck='"+datAtual+"' AND agenda.feriado='"+f+"'ORDER BY agenda.horario ASC LIMIT "+position+" , 70 ", null );

        Calendar hojex = Calendar.getInstance();
        try {
            hojex.setTime(formatoz.parse (datAtual));
        } catch (ParseException e) {
            e.printStackTrace ();
        }

        int ultimoDiaDoMes=hojex.getActualMaximum(Calendar.DAY_OF_MONTH);

        Cursor cursor3= null;
        if(hojex.get (Calendar.DAY_OF_MONTH)>=ultimoDiaDoMes) {
            cursor3 = db.rawQuery ("SELECT  DISTINCT agenda._id, agenda._idCliente, agenda.nome, agenda.dia, agenda.mes, agenda.ano, agenda.data, agenda.mensagem, agenda.horario,agenda.datacheck, agenda.feriado FROM agenda " +
                    "INNER JOIN cores ON  agenda._id=cores._id " +
                    "WHERE cores.repetir=2628000000 AND agenda.dia>'" + ultimoDiaDoMes + "'   AND agenda.feriado='" + n + "' OR " +
                    " cores.repetir=2628000000 AND agenda.dia>'" + ultimoDiaDoMes + "'    AND agenda.feriado='" + f + "'ORDER BY agenda.horario ASC LIMIT " + position + " , 70 ", null);

        }


// TODO: 15/10/2021  modificar listagem do ultimo dia do mes gravado

        if (cursor2.getCount() > 0) {

            cursor2.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor2.getLong( 0 ) );
                p.setIdCliente( cursor2.getLong( 1) );
                p.setNomeP( cursor2.getString( 2) );
                p.setDia( cursor2.getInt(3 ) );
                p.setMes( cursor2.getInt(4 ) );
                p.setAno( cursor2.getInt(5 ) );
                Date datec = new Date();
                datec.setTime(Long.valueOf(cursor2.getString(6) ));
                p.setData(datec );
                p.setMensagem( cursor2.getString( 7) );
                p.setHorario( cursor2.getString( 8) );
                p.setDatacheck(  cursor2.getString( 9 ) );
                p.setFeriado( cursor2.getString( 10) );
                lista.add (p);

            } while (cursor2.moveToNext());

        }
        cursor2.close();



        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setDatacheck(  cursor.getString( 9 ) );
                p.setFeriado( cursor.getString( 10) );

                lista.add (p);

            } while (cursor.moveToNext());

        }
        cursor.close();




        if (cursor3!=null ) {

            if (cursor3.getCount () > 0) {
                cursor3.moveToFirst ();


                do {
                    Agenda p = new Agenda ();
                    p.setId (cursor3.getLong (0));
                    p.setIdCliente (cursor3.getLong (1));
                    p.setNomeP (cursor3.getString (2));
                    p.setDia (cursor3.getInt (3));
                    p.setMes (cursor3.getInt (4));
                    p.setAno (cursor3.getInt (5));
                    Date date = new Date ();


                    Calendar hojexz = Calendar.getInstance ();
                    String dataxz = ultimoDiaDoMes + "-" + hojex.get (Calendar.MONTH + 1) + "-" + hojex.get (Calendar.YEAR);
                    try {
                        hojexz.setTime (formatoz.parse (dataxz));
                    } catch (ParseException e) {
                        e.printStackTrace ();
                    }


                    date.setTime (Long.valueOf (cursor3.getString (6)));
                    p.setData (hojexz.getTime ());
                    p.setMensagem (cursor3.getString (7));
                    p.setHorario (cursor3.getString (8));
                    p.setDatacheck (cursor3.getString (9));
                    p.setFeriado (cursor3.getString (10));

                    lista.add (p);

                } while (cursor3.moveToNext ());

            }
            cursor3.close ();


        }
        Collections.sort (lista, new Comparator () {
            public int compare(Object o1, Object o2) {
                Agenda c1 = (Agenda) o1;
                Agenda c2 = (Agenda) o2;
                return c1.getHorario ().compareToIgnoreCase(c2.getHorario ());
            }
        });
        return lista;
    }




    public boolean listarDiaTem(String dat) {
        boolean tem=false;

        String n ="normal";
        String f ="feriado";

        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,datacheck,feriado FROM agenda " +
                "WHERE datacheck='"+dat+"' AND feriado='"+n+"' OR datacheck='"+dat+"' AND feriado='"+f+"' ORDER BY horario ASC LIMIT "+100+" , 70 ", null );



        if (cursor.getCount() > 0) {
            tem=true;

        }

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return tem;
    }


    public boolean listarPorData(String dat) {
        boolean conhecide=false;
        Cursor cursor = db.rawQuery( "SELECT datacheck FROM agenda " +
                "WHERE datacheck='"+dat+"' AND feriado='feriado'", null );


        if (cursor.getCount() > 0 ) {
            conhecide=true;

            cursor.close();

        }


        return conhecide;
    }

    public String getTema() {
        String tema=""+1;
        Cursor cursor = db.rawQuery( "SELECT tema FROM tema " +
                "WHERE _id='1' ", null );
        cursor.moveToFirst();
        tema=cursor.getString(0);
        cursor.close();
        return tema;
    }
    public String getSon(int num) {
        String tema="";
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri (context.getApplicationContext (),RingtoneManager.TYPE_ALARM);
        tema=uri.getPath ();
        Cursor cursor = db.rawQuery( "SELECT tema FROM tema " +
                "WHERE _id='"+num+"' ", null );
        cursor.moveToFirst();
        if(cursor.getCount ()>0) {
            tema = cursor.getString (0);
        }else {

            inserirSon (tema,num);
        }
        cursor.close();
        return tema;
    }

    public int getVersion() {
        int version=0;
        Cursor cursor = db.rawQuery( "SELECT num FROM version " +
                "WHERE _id='1' ", null );
        cursor.moveToFirst();
        version=cursor.getInt (0);
        cursor.close();
        return version;
    }






    public int getCor(int id) {
        int cor=0;
        Cursor cursor = db.rawQuery( "SELECT cor FROM cores " +
                "WHERE _id='"+id+"' ", null );

        cursor.moveToFirst();

        cor=cursor.getInt (0);
        cursor.close();

        return cor;
    }
    public long getRepetir(long id) {
        long rep=0;
        Cursor cursor = db.rawQuery( "SELECT repetir FROM cores " +
                "WHERE _id='"+id+"' ", null );

        cursor.moveToFirst();

        rep=cursor.getLong (0);
        cursor.close();

        return rep;
    }

    public void insertCor(int id,int cor,long repetir) {
        ContentValues valores = new ContentValues(  );
        valores = new ContentValues(  );
        valores.put( "_id", id);
        valores.put( "cor", cor);
        valores.put( "repetir", repetir);

        db.execSQL ("create table if not exists cores(_id integer," +
                "cor integer," +
                "repetir integer);");



        db.insert( "cores", null, valores );


    }

    public void atualizarCor(int id,int cor,long repetir){

        ContentValues valores = new ContentValues(  );
        valores.put( "cor", cor);
        valores.put( "repetir", repetir);

        try{
            getCor( id);
            db.update ("cores", valores, "_id = ?", new String[]{"" + id});
        }catch (Exception e){
            valores = new ContentValues(  );
            valores.put( "_id", id);
            valores.put( "cor", cor);
            valores.put( "repetir", repetir);
            db.insert( "cores", null, valores );

        }
    }
    public int getVerTabelaCor() {
        int rep=0;
        Cursor cursor = db.rawQuery( "SELECT count(*) FROM cores ", null );

        cursor.moveToFirst();

        rep=cursor.getInt (0);
        cursor.close();

        return rep;
    }
    public Agenda agendamento(long id) {

        Agenda p = new Agenda();

        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,feriado FROM agenda " +
                "WHERE _id= "+id+" ORDER BY data ASC", null );



        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );

                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setFeriado( cursor.getString( 9) );

            } while (cursor.moveToNext());

        }
        cursor.close();

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return p;
    }

    public List<Agenda> listarDiaAlarme() {

        List<Agenda> lista = new ArrayList< >();


        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,datacheck,feriado FROM agenda " +
                "WHERE  feriado!='normal' AND feriado!='feriado' ORDER BY horario ASC ", null );



        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setDatacheck(  cursor.getString( 9 ) );
                p.setFeriado( cursor.getString( 10) );

                lista.add( p );
            } while (cursor.moveToNext());
            cursor.close();

        }

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return lista;
    }


    public List<Agenda> listarDiaAlarme(String dat,int position) {

        List<Agenda> lista = new ArrayList< >();


        Cursor cursor = db.rawQuery( "SELECT _id,_idCliente,nome,dia,mes,ano,data,mensagem,horario,datacheck,feriado FROM agenda " +
                "WHERE  feriado!='normal' AND feriado!='feriado' ORDER BY horario ASC LIMIT "+position+" , 70 ", null );



        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Agenda p = new Agenda();
                p.setId( cursor.getLong( 0 ) );
                p.setIdCliente( cursor.getLong( 1) );
                p.setNomeP( cursor.getString( 2) );
                p.setDia( cursor.getInt(3 ) );
                p.setMes( cursor.getInt(4 ) );
                p.setAno( cursor.getInt(5 ) );
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(6) ));
                p.setData(date );
                p.setMensagem( cursor.getString( 7) );
                p.setHorario( cursor.getString( 8) );
                p.setDatacheck(  cursor.getString( 9 ) );
                p.setFeriado( cursor.getString( 10) );

                lista.add( p );
            } while (cursor.moveToNext());
            cursor.close();

        }

//        Toast.makeText(context," "+lista.size(),Toast.LENGTH_SHORT).show();

        return lista;
    }


    public void restauraAlarme() {
        listaR=listarDiaAlarme();
        long repetir =0;

        int[] diasDaSemana=new int[7];

        diasDaSemana[0]= -1;
        diasDaSemana[1]= -1;
        diasDaSemana[2]= -1;
        diasDaSemana[3]= -1;
        diasDaSemana[4]= -1;
        diasDaSemana[5]= -1;
        diasDaSemana[6]= -1;


        traduzir ( Locale.getDefault().getDisplayLanguage());





        for(int position=0;position<listaR.size ();position++) {



            String[] diasAlarme = new BDAgenda (context).agendamento ((int) listaR.get(position).getId()).getFeriado ().split (" ");
            Resources res = context.getResources();

            if(diasAlarme[0].equals (diasDaSemanax[0].trim ())){
                diasDaSemana[0]=1;
                repetir=86400000;

            }

            if(diasAlarme[1].equals (diasDaSemanax[1].trim ())){
                diasDaSemana[1]=2;
                repetir=86400000;

            }

            if(diasAlarme[2].equals (diasDaSemanax[2].trim ())){
                diasDaSemana[2]=3;
                repetir=86400000;

            }

            if(diasAlarme[3].equals (diasDaSemanax[3].trim ())){
                diasDaSemana[3]=4;
                repetir=86400000;

            }


            if(diasAlarme[4].equals (diasDaSemanax[4].trim ())){
                diasDaSemana[4]=5;
                repetir=86400000;

            }



            if(diasAlarme[5].equals (diasDaSemanax[5].trim ())){
                diasDaSemana[5]=6;
                repetir=86400000;

            }


            if(diasAlarme[6].equals (diasDaSemanax[6].trim ())){
                diasDaSemana[6]=7;
                repetir=86400000;

            }








            Agenda p = new Agenda ();
            p.setId (listaR.get (position).getId ());
            p.setIdCliente (listaR.get (position).getIdCliente ());
            p.setNomeP (listaR.get (position).getNomeP ());
            final SimpleDateFormat formato = new SimpleDateFormat ("dd-MM-yyyy");
            Date dateForm = new Date ();

            SimpleDateFormat formatar = new SimpleDateFormat ("dd-MM-yyyy_HH:mm");


            Calendar dataaux = Calendar.getInstance ();
            dataaux.set (Calendar.DAY_OF_MONTH, 2);

            Date dateForm2 = new Date ();


            dataaux.setTime (dateForm2);

            dataaux.set (Calendar.YEAR, dataaux.get (Calendar.YEAR));
            dataaux.set (Calendar.MONTH, dataaux.get (Calendar.MONTH));
            dataaux.set (Calendar.DAY_OF_MONTH, dataaux.get (Calendar.DAY_OF_MONTH));
            dataaux.set (Calendar.HOUR_OF_DAY, dataaux.get (Calendar.HOUR_OF_DAY));
            dataaux.set (Calendar.MINUTE, dataaux.get (Calendar.MINUTE));
            dataaux.set (Calendar.SECOND, 0);

            int diax = dataaux.get (Calendar.DAY_OF_MONTH);
            int mesx = dataaux.get (Calendar.MONTH);
            int anox = dataaux.get (Calendar.YEAR);
            String[] horarioMarcado;
            int horax =0;
            int minutox =0;
            int mh = 0;
            int mm = 0;
            int thm =0;
            try {
                horarioMarcado = listaR.get (position).getHorario ().split (":");
                horax = Integer.valueOf (horarioMarcado[0]);
                try {
                    minutox = Integer.valueOf (horarioMarcado[1]);
                }catch (Exception e){
                    minutox=0;
                }                mh = 3600000 * horax;
                mm = 60000 * minutox;
                thm = mh + mm;
                if (horax > 23) {
                    horax = 0;
                }
            }catch (Exception e){
                Log.e("Erro em",e.toString ());
            }
            String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

            String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;


            p.setNomeP (dataMarcada);

            p.setData (dateForm);






            String alarme = "";
            for (int i = 0; i < diasDaSemana.length; i++) {
                if (diasDaSemana[i] > -1) {
                    alarme += diasDaSemanax[i];

                } else {
                    alarme += "- ";

                }

            }
            p.setFeriado (alarme);
            p.setDatacheck (dataMarcadaf);


            p.setMensagem (listaR.get (position).getMensagem ());
            p.setHorario (listaR.get (position).getHorario ());

            p.setDatacheck (dataMarcadaf);

            int minutosAviso = 0;


            Date aux = null;
            try {
                aux = formatar.parse (dataMarcada);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            long data5 = aux.getTime () - (60000L * minutosAviso);
            Date dataa = aux;
            dataa.setTime (data5);

            GerarAviso aviso = new GerarAviso (context);
            aviso.AgendarAlarme (dataa, listaR.get (position).getMensagem (), listaR.get (position).getMensagem (), (int) p.getId (), repetir, diasDaSemana);
        }
    }


    // TODO: 19/10/2021  
    public void restaurarAgenda(boolean mensal) {

        if(mensal){
            listaR = listarDiasAgendaMensalAnterior ();

        }else {
            listaR = listarDiasAgenda ();
        }
        for (int position = 0; position < listaR.size (); position++) {
            long repetir = getRepetir((int) listaR.get (position).getId ());

            Agenda p = new Agenda ();
            p.setId (listaR.get (position).getId ());
            p.setIdCliente (listaR.get (position).getIdCliente ());
            p.setNomeP (listaR.get (position).getNomeP ());
            final SimpleDateFormat formato = new SimpleDateFormat ("dd-MM-yyyy");
            Date dateForm = null;
            try {
                dateForm = formato.parse (listaR.get (position).getDatacheck ());
            } catch (ParseException e) {
                e.printStackTrace ();
            }


            Calendar dataaux = Calendar.getInstance ();
            dataaux.set (Calendar.DAY_OF_MONTH, 2);

            Date dateForm2 = dateForm;
            dateForm2.setTime (dateForm.getTime ());


            dataaux.setTime (dateForm2);


            dataaux.set (Calendar.YEAR, dataaux.get (Calendar.YEAR));
            dataaux.set (Calendar.MONTH, dataaux.get (Calendar.MONTH));
            dataaux.set (Calendar.DAY_OF_MONTH, dataaux.get (Calendar.DAY_OF_MONTH));
            dataaux.set (Calendar.HOUR_OF_DAY, dataaux.get (Calendar.HOUR_OF_DAY));
            dataaux.set (Calendar.MINUTE, dataaux.get (Calendar.MINUTE));

            int diax = dataaux.get (Calendar.DAY_OF_MONTH);
            if(mensal){
                Calendar dath = Calendar.getInstance ();
                Date dh =new Date ();
                if(dh.getTime ()>dataaux.getTimeInMillis ()){
                    dataaux.set (Calendar.MONTH, dataaux.get (Calendar.MONTH)+1);

                }
            }
            int mesx = dataaux.get (Calendar.MONTH);
            int anox = dataaux.get (Calendar.YEAR);

            String[] horarioMarcado;
            int horax =0;
            int minutox =0;
            int mh = 0;
            int mm = 0;
            int thm =0;
            try {
                 horarioMarcado = listaR.get (position).getHorario ().split (":");
                  horax = Integer.valueOf (horarioMarcado[0]);
                  try {
                      minutox = Integer.valueOf (horarioMarcado[1]);
                  }catch (Exception e){
                      minutox=0;
                  }
                  mh = 3600000 * horax;
                  mm = 60000 * minutox;
                  thm = mh + mm;
                if (horax > 23) {
                    horax = 0;
                }
            }catch (Exception e){
             Log.e("Erro em",e.toString ());
            }
            String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;

            String dataMarcadaf = diax + "-" + (mesx + 1) + "-" + anox;

            BDAgenda bd = new BDAgenda (context);
            p.setAno (anox);
            p.setMes (mesx + 1);
            p.setDia (diax);

            p.setNomeP (dataMarcada);

            p.setData (dateForm);
            p.setFeriado ("normal");
            p.setDatacheck (dataMarcadaf);


            p.setMensagem (listaR.get (position).getMensagem ());
            p.setHorario (listaR.get (position).getHorario ());

            p.setDatacheck (listaR.get (position).getDatacheck ());

            int minutosAviso = 0;


            Date aux = new Date ();
            aux.setTime (dataaux.getTimeInMillis () + thm);


            long data5 = aux.getTime () - (60000L * minutosAviso);
            Date dataa = aux;
            dataa.setTime (dataaux.getTimeInMillis () + thm - (60000L * minutosAviso));
            if(mensal){

                GerarAviso aviso = new GerarAviso (context);
                aviso.AgendarNotificacao (dataa, dataMarcada, listaR.get (position).getMensagem (), (int) p.getId (), repetir,true);

            }else {
                if (dataa.getTime () > new Date ().getTime ()) {
                    GerarAviso aviso = new GerarAviso (context);
                    aviso.AgendarNotificacao (dataa, dataMarcada, listaR.get (position).getMensagem (), (int) p.getId (), repetir,true);

                }
            }
        }





    }





    private void traduzir(String local){

        switch(local){

            case "Português":


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

