package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.os.AsyncTask;
import android.util.JsonReader;

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

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiNajnovijeDone{
        public void onDone(ArrayList<Knjiga> rez);
    }

    ArrayList<Knjiga> knjigelista = new ArrayList<>();

    private IDohvatiNajnovijeDone pozivatelj;
    public DohvatiNajnovije(IDohvatiNajnovijeDone p) {pozivatelj=p;};



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

    @Override
    protected Void doInBackground(String... strings) {
        String query = null;
        try {
            query= URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "https://www.googleapis.com/books/v1/volumes?q=inauthor:"+query+"&maxResults=5&orderBy=newest";
        try{
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            URLConnection connection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("items");
            int velicina = items.length();
            for(int i=0; i<items.length(); i++){
                JSONObject knjige = items.getJSONObject(i);
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
                    for (int j = 0; j < autoriJO.length(); j++)
                        k.dodajAutora(autoriJO.getString(j));
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDone(knjigelista);
    }


}
