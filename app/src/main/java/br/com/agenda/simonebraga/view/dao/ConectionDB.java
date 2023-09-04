package br.com.agenda.simonebraga.view.dao;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import java.io.File;
import br.com.agenda.simonebraga.R;


/**
 * Created by wanderson on 08/08/19.
 */

public class ConectionDB extends SQLiteOpenHelper {
    private static final String nomeDB = "BD_V1";
    private static final int vers = 1;
    private Context context;


    public ConectionDB(Context context) {
        super(context, nomeDB, null, vers);
        this.context = context;
    }
    @Override
    public void onOpen(SQLiteDatabase database) {
        super.onOpen(database);
        if(Build.VERSION.SDK_INT >= 28)
        {
            File dbshm = new File(database.getPath() + "-shm");
            File dbwal = new File(database.getPath()+ "-wal");
            if (dbshm.exists()) {
                dbshm.delete();
            }
            if (dbwal.exists()) {
                dbwal.delete();
            }


            database.disableWriteAheadLogging();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table if not exists agenda(_id integer," +
                "_idCliente integer ," +
                "nome text ," +
                "dia integer," +
                "mes integer ," +
                "ano integer," +
                "horario text," +
                "data text," +
                "feriado text," +
                "mensagem text," +
                "datacheck text);");


        db.execSQL("create table if not exists tema(_id integer," +
                "tema text);");




        ContentValues valores = new ContentValues(  );
        valores.put( "_id", 1);

        valores.put( "tema", "3" );

        if(getTema (db)==null) {
            db.insert ("tema", null, valores);
        }


        ///////////////////////////////
        db.execSQL("create table if not exists version(_id integer," +
                "num integer);");

        valores = new ContentValues(  );
        valores.put( "_id", 1);

        valores.put( "num", 1 );



        if(getVersion (db)==0) {
            db.insert ("version", null, valores);
        }
        /////////////////////////////////////



        db.execSQL ("create table if not exists cores(_id integer," +
                "cor integer," +
                "repetir integer);");


        valores = new ContentValues ();
        valores.put ("_id", 1);

        valores.put ("cor", R.drawable.estilo2);
        valores.put ("repetir", 0);

        if(getCor (db,1)==0) {
            db.insert ("cores", null, valores);
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL( "drop table materiaPrima;drop table receita;drop table vendas" );

        //  onCreate( db );




    }



    public String getTema(SQLiteDatabase db) {
        String tema=null;
        Cursor cursor = db.rawQuery( "SELECT tema FROM tema " +
                "WHERE _id='1' ", null );
        cursor.moveToFirst();
        if(cursor.getCount ()==0) {
            tema=null;
        }else {
            tema=cursor.getString(0);
        }
        cursor.close();
        return tema;
    }


    public int getVersion(SQLiteDatabase db) {
        int version=0;
        Cursor cursor = db.rawQuery( "SELECT num FROM version " +
                "WHERE _id='1' ", null );
        cursor.moveToFirst();
        if(cursor.getCount ()==0) {
            version=0;

        }else {
            version = cursor.getInt (0);

        }
        cursor.close();
        return version;
    }

    public int getCor(SQLiteDatabase db,int id) {
        int cor=0;
        Cursor cursor = db.rawQuery( "SELECT cor FROM cores " +
                "WHERE _id='"+id+"' ", null );

        cursor.moveToFirst();
        if(cursor.getCount ()==0) {
            cor=0;

        }else {
            cor = cursor.getInt (0);

        }
        cursor.close();

        return cor;
    }

}
