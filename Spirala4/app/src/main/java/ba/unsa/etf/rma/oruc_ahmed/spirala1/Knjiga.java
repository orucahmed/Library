package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by oruc_ on 26.03.2018..
 */

public class Knjiga implements Serializable {

    private String id;
    private String naziv;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStrinica;


    //
    //

    private String zanr;

    public Uri getSlikaUri() {
        return slikaUri;
    }

    public void setSlikaUri(Uri slikaUri) {
        this.slikaUri = slikaUri;
    }

    private Uri slikaUri;



    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    //
    //


    public Knjiga() {
        this.autori = new ArrayList<>();
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStrinica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStrinica = brojStrinica;
    }

    public void dodajAutora(String ime){
        Autor a = new Autor(ime, id);
        autori.add(a);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStrinica() {
        return brojStrinica;
    }

    public void setBrojStrinica(int brojStrinica) {
        this.brojStrinica = brojStrinica;
    }

    boolean boja=false;

    public boolean isBoja() {
        return boja;
    }

    public void setBoja(boolean boja) {
        this.boja = boja;
    }
}
