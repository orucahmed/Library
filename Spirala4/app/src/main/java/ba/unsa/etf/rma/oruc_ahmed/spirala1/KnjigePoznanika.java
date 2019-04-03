package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService {


    public static final int STATUS_START = 0;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_ERROR = 2;

    private String rezultat;
    ArrayList<Knjiga> knjigelista = new ArrayList<>();


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null ;
        try {
            while ((line = reader.readLine()) != null ) {
                sb.append(line + " \n " );
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    public KnjigePoznanika(){
        super(null);
    }


    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        rezultat = intent.getStringExtra("string");

        try {
            rezultat = URLEncoder.encode(rezultat, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Bundle b = new Bundle();

        receiver.send(STATUS_START, Bundle.EMPTY);

        String query = rezultat;

        String url1="https://www.googleapis.com/books/v1/users/"+query+"/bookshelves";

        try{
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            URLConnection connection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("items");
            for(int j=0; j<items.length(); j++){
                JSONObject temp = items.getJSONObject(j);

                String idKnjige=temp.getString("id");
                try {
                    idKnjige = URLEncoder.encode(idKnjige, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String url2="https://www.googleapis.com/books/v1/users/"+query+"/bookshelves"+"/"+idKnjige+"/"+"volumes";
                url = new URL(url2);
                urlConnection = (HttpURLConnection) url.openConnection();
                connection = url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                rezultat = convertStreamToString(in);
                jo = new JSONObject(rezultat);
                JSONArray itemsknnjige = jo.getJSONArray("items");
                int velicina = itemsknnjige.length();
                for(int i=0; i<itemsknnjige.length(); i++){
                    JSONObject knjige = itemsknnjige.getJSONObject(i);
                    Knjiga k = new Knjiga();
                    String id = null;
                    try{
                        id = knjige.getString("id");
                    }
                    catch (JSONException e){
                        id = "No value for id";
                    }
                    k.setId(id);
                    JSONObject volumeInfo = knjige.getJSONObject("volumeInfo");
                    String title=null;
                    try{
                        title=volumeInfo.getString("title");
                    }
                    catch(JSONException e){
                        title="No value for title";
                    }
                    k.setNaziv(title);

                    JSONArray autoriJO = null;
                    try {
                        autoriJO = volumeInfo.getJSONArray("authors");
                        for (int l = 0; l < autoriJO.length(); l++)
                            k.dodajAutora(autoriJO.getString(l));
                    }
                    catch (JSONException e){
                        k.dodajAutora("No value for autors");
                    }
                    String description = null;
                    try{
                        description = volumeInfo.getString("description");
                    }
                    catch (JSONException e){
                        description = "No value for description";
                    }
                    k.setOpis(description);
                    String datum = null;
                    try{
                        datum = volumeInfo.getString("publishedDate");
                    }
                    catch (JSONException e){
                        datum = "No value for date";
                    }
                    k.setDatumObjavljivanja(datum);
                    int stranice = 0;
                    try{
                        stranice = volumeInfo.getInt("pageCount");
                    }
                    catch (JSONException e){
                        stranice = -1;
                    }
                    k.setBrojStrinica(stranice);

                    String slika = null;
                    try{
                        JSONObject slicica = volumeInfo.getJSONObject("imageLinks");
                        slika = slicica.getString("thumbnail");
                    }
                    catch (JSONException e){
                        slika = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9VuQlXJLAZkOgl-71cO0MnFvaVHhWAoQVeODGcWSu7W_rKlmovA";
                    }
                    URL urlslika = new URL (slika);
                    k.setSlika(urlslika);
                    knjigelista.add(k);
                }
            }
            b.putSerializable("knjige", knjigelista);
            receiver.send(STATUS_FINISH, b);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            b.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, b);
        } catch (IOException e) {
            e.printStackTrace();
            b.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, b);
        } catch (JSONException e) {
            e.printStackTrace();
            b.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, b);
        }

    }
}
