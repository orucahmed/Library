package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by oruc_ on 26.03.2018..
 */

public class KnjigaListAdapter extends BaseAdapter{

    private Context kontekst;
    private ArrayList<Knjiga> lista;
    private ArrayList<String> kategorije;
    private Autori autori;
    private Activity a;
    private PreporuciKnjiguKlik pkk;

    public KnjigaListAdapter(Context kontekst, ArrayList<Knjiga> lista, ArrayList<String> kategorije, Autori autori, Activity a) {
        this.kontekst = kontekst;
        this.lista = lista;
        this.kategorije = kategorije;
        this.autori = autori;
        this.a = a;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public interface PreporuciKnjiguKlik{
        public void onClick(int broj, Knjiga temp);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {




        View v = View.inflate(kontekst, R.layout.knjige_list, null);
        ImageView slika = (ImageView) v.findViewById(R.id.eNaslovna);
        TextView autor = (TextView) v.findViewById(R.id.eAutor);
        TextView naziv = (TextView) v.findViewById(R.id.eNaziv);
        TextView datum = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
        TextView opis = (TextView) v.findViewById(R.id.eOpis);
        TextView brojStranica = (TextView) v.findViewById(R.id.eBrojStranica);
        Button preporuka = (Button) v.findViewById(R.id.dPreporuci);

        LinearLayout linearl = (LinearLayout) v.findViewById(R.id.linearl);
        LinearLayout linearl1 = (LinearLayout) v.findViewById(R.id.linearl1);

        Bitmap b = null;
        URL url = lista.get(i).getSlika();

        if(url==null){
            try {
                b = MediaStore.Images.Media.getBitmap(kontekst.getContentResolver(), lista.get(i).getSlikaUri());
                slika.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
                new URLBitmapTask(slika).execute(url);
        }


        autor.setText(lista.get(i).getAutori().get(0).getImeiPrezime());
        naziv.setText(lista.get(i).getNaziv());
        datum.setText("Datum objavljivanja: "+lista.get(i).getDatumObjavljivanja());
        opis.setText("Opis: "+lista.get(i).getOpis());
        brojStranica.setText("Broj stranica: "+ Integer.toString(lista.get(i).getBrojStrinica()));
        if(lista.get(i).isBoja()) linearl.setBackgroundColor(R.color.bojenje);
        if(lista.get(i).isBoja()) linearl1.setBackgroundColor(R.color.bojenje);


        pkk=(PreporuciKnjiguKlik) a;


        preporuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pkk.onClick(2, lista.get(i));

            }
        });

        return v;



    }
}
