package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements  ListeFragment.OnItemClick, ListeFragment.DodajKnjiguKlik, ListeFragment.DodajKnjiguOnlineKlik, KnjigaListAdapter.PreporuciKnjiguKlik {

    private ArrayList<String> unosiKategorije;
    private ArrayList<Knjiga> knjige;
    private KnjigaList knjigalista;
    Boolean siriL;
    private boolean dozvola;
    private Autori autorilista;
    private BazaOpenHelper baza;

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("value","Permission is granted");
                return true;
            } else {

                Log.v("value","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("value","Permission is granted");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baza = new BazaOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);
        dozvola=isStoragePermissionGranted();
        if(!dozvola) ActivityCompat.requestPermissions(KategorijeAkt.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        knjige=new ArrayList<>();
        unosiKategorije=new ArrayList<>();

        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey("kategorije")) {
                unosiKategorije = savedInstanceState.getStringArrayList("kategorije");
            }

            if (savedInstanceState.containsKey("knjige")) {
                knjigalista = (KnjigaList) savedInstanceState.getSerializable("knjige");
                knjige=knjigalista.getKnjige();
            }

            if (savedInstanceState.containsKey("autori")) {
                autorilista = (Autori) savedInstanceState.getSerializable("autori");
            }
        }

        else {

            //baza.ocistiTabele();

            Knjiga k = new Knjiga();
            k.setId("y1-aDgAAQBAJ");
            k.setZanr("Drama");
            URL url = null;
            try {
                url = new URL ("http://books.google.com/books/content?id=y1-aDgAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            k.setSlika(url);
            k.setBrojStrinica(124);
            k.setOpis("Tit Makcije Plaut (lat. Titus Maccius Plautus, oko 254. pr. Kr. ― 184. pr. Kr.) je najveći i najcjenjeniji autor komedija u starorimskoj književnosti. Uspjeh kod rimske publike polučio je ponajviše poradi slobodne i žive obrade grčkih originala, koje je po volji mijenjao, pojednostavljivao i kombinirao tako da je od grčkog predloška stvarao latinsku adaptaciju. U antici mu se pripisivalo čak oko 130 komedija, međutim čuveni rimski historičar Marko Terencije Varon u 1. stoljeću pr. Kr. kao autentične je izdvojio 21 komediju, od kojih je u cijelosti sačuvano 20. Plautove su komedije bile veoma cijenjene u doba kasne Rimske Republike i ranog Carstva, ali su se kasnije malo čitale jer im je jezik bio arhaičan. Ponovo je otkriven i rado čitan u doba renesanse, pa je tako poslužio kao uzor i izvor velikim piscima komedija Shakespeareu i Molièreu, a Marin Držić je komediju \\\"Skup\\\" napisao po uzoru na Plautovu \\\"Aululariju\\\". Jedna od najpoznatijih Plautovih komedija je \\\"Aulularia\\\" – naslov joj se doslovno prevodi kao \\\"Ćup sa zlatom\\\", a na hrvatski je prevedena pod naslovima \\\"Tvrdica\\\", \\\"Škrtac\\\" i \\\"Ćup\\\". Nije poznato koja joj je točno grčka komedija uzor, ili ona nije sačuvana. Priča prati starog škrtca Eukliona koji pronalazi ćup sa zlatom i neprestano bdije nad njime. Motiv se prepliće s motivom ljubavi Euklionove kćerke i mladog Likonida. Pored škrtosti, kroz lik starog Megadora koji također želi oženiti Euklionovu kćer, ismijava se staračka požuda. Preveo: Koloman Rac. Lektira za 1. razred srednje škole.");
            k.setNaziv("Tvrdica");
            Autor autor = new Autor("Plaut", "y1-aDgAAQBAJ");
            ArrayList<Autor> nesto = new ArrayList<>();
            nesto.add(autor);
            k.setAutori(nesto);
            k.setDatumObjavljivanja("2013-08-02");

            try {
                knjige = baza.dajKnjige();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            baza.dodajKategoriju("Drama");
            try {
                baza.dodajKnjigu(k);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            ArrayList<String> temp = baza.dajKategorije();


            autorilista = new Autori();

            autorilista = baza.dajAutore();
            for(int p=0; p<temp.size(); p++) unosiKategorije.add(temp.get(p));




        }


        siriL = false;
        FragmentManager fm = getFragmentManager();
        FrameLayout ldetalji = (FrameLayout) findViewById(R.id.mjestoF2);

        if(ldetalji!=null){
            siriL=true;
            KnjigeFragment kf;
            kf = (KnjigeFragment) fm.findFragmentById(R.id.mjestoF2);
            if(kf==null){
                Bundle argumenti = new Bundle();
                kf=new KnjigeFragment();
                knjigalista=new KnjigaList(knjige);
                argumenti.putSerializable("knjige",knjigalista);
                argumenti.putString("dodato", "");
                argumenti.putSerializable("autori",autorilista);
                argumenti.putStringArrayList("kategorije",unosiKategorije);
                kf.setArguments(argumenti);
                fm.beginTransaction().replace(R.id.mjestoF2,kf).commit();
            }
        }
        ListeFragment lf = (ListeFragment) fm.findFragmentByTag("Lista");
        if(lf==null){
            lf=new ListeFragment();
            Bundle argumenti = new Bundle();
            argumenti.putStringArrayList("kategorije",unosiKategorije);
            knjigalista=new KnjigaList(knjige);
            argumenti.putSerializable("knjige",knjigalista);
            argumenti.putSerializable("autori",autorilista);
            lf.setArguments(argumenti);

            fm.beginTransaction().replace(R.id.mjestoF1,lf,"Lista").commit();
        }
        else fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }



    @Override
    public void onSaveInstanceState(Bundle onState) {
        super.onSaveInstanceState(onState);
        onState.putStringArrayList("kategorije",unosiKategorije);
        knjigalista=new KnjigaList(knjige);
        onState.putSerializable("knjige",knjigalista);
        onState.putSerializable("autori",autorilista);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("value","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onItemClicked(int pos, boolean istina) {
        Bundle arguments = new Bundle();

        if(istina==true) arguments.putString("dodato", unosiKategorije.get(pos));
        else arguments.putString("dodato", autorilista.getAutori().get(pos));
        knjigalista=new KnjigaList(knjige);
        arguments.putStringArrayList("kategorije",unosiKategorije);
        arguments.putSerializable("knjige", knjigalista);
        arguments.putSerializable("autori",autorilista);
        KnjigeFragment kf = new KnjigeFragment();
        kf.setArguments(arguments);
        if(siriL) getFragmentManager().beginTransaction().replace(R.id.mjestoF2,kf).commit();
        else getFragmentManager().beginTransaction().replace(R.id.mjestoF1,kf).commit();
    }

    @Override
    public void onClick(int broj) {
        if(broj==0){
            Bundle arguments = new Bundle ();
            arguments.putStringArrayList("kategorije",unosiKategorije);
            knjigalista=new KnjigaList(knjige);
            arguments.putSerializable("knjige",knjigalista);
            arguments.putSerializable("autori",autorilista);
            DodavanjeKnjigeFragment dkf = new DodavanjeKnjigeFragment();
            dkf.setArguments(arguments);
            if(siriL) getFragmentManager().beginTransaction().replace(R.id.mjestoF3,dkf).addToBackStack(null).commit();
            else getFragmentManager().beginTransaction().replace(R.id.mjestoF1,dkf).addToBackStack(null).commit();
        }
        else if(broj==1){
            Bundle arguments = new Bundle ();
            arguments.putStringArrayList("kategorije",unosiKategorije);
            knjigalista=new KnjigaList(knjige);
            arguments.putSerializable("knjige",knjigalista);
            arguments.putSerializable("autori",autorilista);
            FragmentOnline dkf = new FragmentOnline();
            dkf.setArguments(arguments);
            if(siriL) getFragmentManager().beginTransaction().replace(R.id.mjestoF3,dkf).addToBackStack(null).commit();
            else getFragmentManager().beginTransaction().replace(R.id.mjestoF1,dkf).addToBackStack(null).commit();
        }
    }

    @Override
    public void onClick(int broj, Knjiga temp) {
        Bundle arguments = new Bundle ();
        arguments.putStringArrayList("kategorije",unosiKategorije);
        knjigalista=new KnjigaList(knjige);
        knjigalista.dodajKnjigu(temp);
        arguments.putSerializable("knjige",knjigalista);
        arguments.putSerializable("autori",autorilista);
        FragmentPreporuci dkf = new FragmentPreporuci();
        dkf.setArguments(arguments);
        if(siriL) getFragmentManager().beginTransaction().replace(R.id.mjestoF3,dkf).addToBackStack(null).commit();
        else getFragmentManager().beginTransaction().replace(R.id.mjestoF1,dkf).addToBackStack(null).commit();
    }
}


