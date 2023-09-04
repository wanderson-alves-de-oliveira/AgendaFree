package br.com.agenda.simonebraga.view.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable {

    private String nome,telefone;

    protected Contato(Parcel in) {
        nome = in.readString();
        telefone = in.readString();
    }

    public static final Creator<Contato> CREATOR = new Creator<Contato>() {
        @Override
        public Contato createFromParcel(Parcel in) {
            return new Contato(in);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(telefone);
    }
}
