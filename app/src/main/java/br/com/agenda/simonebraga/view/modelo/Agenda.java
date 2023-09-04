package br.com.agenda.simonebraga.view.modelo;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by wanderson on 04/09/19.
 */

@SuppressLint("ParcelCreator")
public class Agenda implements Parcelable{



    private long id,idCliente;
    private Date data;
    private int dia,mes,ano;
    private String nomeP,horario,mensagem,datacheck,feriado;

    public Agenda() {
    }

    protected Agenda(Parcel in) {
        id = in.readLong();
        idCliente = in.readLong();
        dia = in.readInt();
        mes = in.readInt();
        ano = in.readInt();
        nomeP = in.readString();
        horario = in.readString();
        mensagem = in.readString();
        datacheck = in.readString();
        feriado = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(idCliente);
        dest.writeInt(dia);
        dest.writeInt(mes);
        dest.writeInt(ano);
        dest.writeString(nomeP);
        dest.writeString(horario);
        dest.writeString(mensagem);
        dest.writeString(datacheck);
        dest.writeString(feriado);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Agenda> CREATOR = new Creator<Agenda>() {
        @Override
        public Agenda createFromParcel(Parcel in) {
            return new Agenda(in);
        }

        @Override
        public Agenda[] newArray(int size) {
            return new Agenda[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getNomeP() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        this.nomeP = nomeP;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDatacheck() {
        return datacheck;
    }

    public void setDatacheck(String datacheck) {
        this.datacheck = datacheck;
    }

    public String getFeriado() {
        return feriado;
    }

    public void setFeriado(String feriado) {
        this.feriado = feriado;
    }
}
