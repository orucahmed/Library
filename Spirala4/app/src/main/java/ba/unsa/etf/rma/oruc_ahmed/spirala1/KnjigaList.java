package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by oruc_ on 27.03.2018..
 */

public class KnjigaList implements Serializable {
    private ArrayList<Knjiga> knjige;

    public int Velicina(){
        return knjige.size();
    }

    public KnjigaList(){
        this.knjige = new ArrayList<>();
    }

    public KnjigaList(ArrayList<Knjiga> knjige) {
        this.knjige = knjige;
    }

    public ArrayList<Knjiga> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<Knjiga> knjige) {
        this.knjige = knjige;
    }

    public void obrisiKnjige(){
        knjige.clear();
    }

    public void dodajKnjigu(Knjiga k){
        knjige.add(k);
    }

    public Knjiga dajZadnju(){
        return knjige.get(knjige.size()-1);
    }

    public void brisiZadnju(){
        knjige.remove(knjige.size()-1);
    }
}
