package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import java.io.Serializable;
import java.util.ArrayList;

public class Autori implements Serializable {

    private ArrayList<String> autori;
    private ArrayList<Integer> autoriknjige;

    public int Velicina(){
        return autori.size();
    }


    public Autori() {
        this.autori = new ArrayList<>();
        this.autoriknjige = new ArrayList<>();
    }

    public ArrayList<String> getAutori() {
        ArrayList<String> pom = new ArrayList<>();
        for(int i=0; i<autori.size();i++) pom.add(autori.get(i) + " " + autoriknjige.get(i).toString());
        return pom;
    }

    public void dodajAutora(String autor){
        if(autori.size()==0){
            autori.add(autor);
            autoriknjige.add(1);
            return;
        }
        for(int i=0; i<autori.size(); i++){
            Integer a=autoriknjige.get(i);
            if(autori.get(i).equals(autor)){
                a=a+1;
                autoriknjige.set(i,a);
                return;
            }
        }
        autori.add(autor);
        autoriknjige.add(1);
    }
}
