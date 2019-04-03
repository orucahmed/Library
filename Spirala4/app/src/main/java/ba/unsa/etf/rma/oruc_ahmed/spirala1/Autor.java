package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import java.io.Serializable;
import java.util.ArrayList;

public class Autor implements Serializable {
    private String imeiPrezime;
    private ArrayList<String> knjige = new ArrayList<String>();


    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        this.knjige.add(id);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id){
        knjige.add(id);
    }
}
