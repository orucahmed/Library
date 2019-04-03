package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.BitSet;

public class URLBitmapTask extends AsyncTask<URL, String, Bitmap> {

    private final static  String TAG="Greska";
    private ImageView slika;

    public URLBitmapTask(ImageView slika){
        this.slika=slika;
    }


    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap b = null;
        try{
            URL url = urls[0];
            b=BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        slika.setImageBitmap(bitmap);
    }
}
