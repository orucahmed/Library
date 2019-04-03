package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mojaBaza.db";

    public static final int DATABASE_VERSION = 1 ;

    public static final String DATABASE_TABLE_KATEGORIJA = "Kategorija";
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    public static final String DATABASE_TABLE_KNJIGA = "Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJ_STRANICA = "brojStranica";
    public static final String KNJIGA_ID_WEB_SERVIS = "idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE = "idkategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";

    public static final String DATABASE_TABLE_AUTOR = "Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";

    public static final String DATABASE_TABLE_AUTORSTVO = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_ID_AUTORA = "idautora";
    public static final String AUTORSTVO_ID_KNJIGE = "idknjige";

    private static final String CREATE_TABLE_KATEGORIJA = "create table " + DATABASE_TABLE_KATEGORIJA + " (" + KATEGORIJA_ID + " integer primary key autoincrement, " + KATEGORIJA_NAZIV + " text not null);";
    private static final String CREATE_TABLE_KNJIGA = "create table " + DATABASE_TABLE_KNJIGA + " (" + KNJIGA_ID + " integer primary key autoincrement, " + KNJIGA_NAZIV + " text not null, " + KNJIGA_OPIS + " text not null, " + KNJIGA_DATUM_OBJAVLJIVANJA + " text not null, " + KNJIGA_BROJ_STRANICA + " integer not null, " + KNJIGA_ID_WEB_SERVIS + " text not null, " + KNJIGA_ID_KATEGORIJE + " integer not null, " + KNJIGA_SLIKA + " text not null, " + KNJIGA_PREGLEDANA + " integer not null);";
    private static final String CREATE_TABLE_AUTOR = "create table " + DATABASE_TABLE_AUTOR + " (" + AUTOR_ID + " integer primary key autoincrement, " + AUTOR_IME + " text not null);";
    private static final String CREATE_TABLE_AUTORSTVO = "create table " + DATABASE_TABLE_AUTORSTVO + " (" + AUTORSTVO_ID + " integer primary key autoincrement, " + AUTORSTVO_ID_AUTORA + " integer not null, " + AUTORSTVO_ID_KNJIGE + " integer not null);";

    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_KATEGORIJA);
        sqLiteDatabase.execSQL(CREATE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL(CREATE_TABLE_AUTOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTORSTVO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KATEGORIJA);

        onCreate(sqLiteDatabase);
    }


    public  Autori dajAutore(){
        Autori a = new Autori();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultati = new String[] {AUTOR_ID,AUTOR_IME };
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,rezultati,null,null,null,null, null);
        while(cursor.moveToNext()){
            a.dodajAutora(cursor.getString(cursor.getColumnIndex(AUTOR_IME)));
        }
        return a;
    }

    public long dodajKategoriju(String naziv){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues novi = new ContentValues();
        String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)).equals(naziv)) return -1;
            }
        }
        cursor.close();
        novi.put(KATEGORIJA_NAZIV,naziv);
        db.insert(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,null,novi);
        int idKategorija=-1;
        cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)).equals(naziv)) idKategorija=cursor.getInt(cursor.getColumnIndex(KATEGORIJA_ID));
            }
        }
        cursor.close();
        db.close();
        return idKategorija;
    }

    public long dajIdIzImena(String ime){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        long broj=0;
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)).equals(ime)) broj=cursor.getLong(cursor.getColumnIndex(KATEGORIJA_ID));
        }
        return broj;
    }

    public long dajIdIzImenaAutora(String ime){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultati = new String[] {AUTOR_ID, AUTOR_IME};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,rezultati,null,null,null,null, null);
        long broj=0;
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(AUTOR_IME)).equals(ime)) broj=cursor.getLong(cursor.getColumnIndex(AUTOR_ID));
        }
        return broj;
    }

    public ArrayList<String> dajKategorije(){
        ArrayList<String> kategorije = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                kategorije.add(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)));
            }
        }
        cursor.close();
        db.close();
        return kategorije;
    }

    public long dodajKnjigu(Knjiga knjiga) throws MalformedURLException {
        int idKnjige=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)).equals(knjiga.getNaziv())) return -1;
        }


        ContentValues novi = new ContentValues();
        novi.put(KNJIGA_NAZIV, knjiga.getNaziv());
        novi.put(KNJIGA_OPIS, knjiga.getOpis());
        novi.put(KNJIGA_BROJ_STRANICA, knjiga.getBrojStrinica());
        novi.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
        novi.put(KNJIGA_ID_WEB_SERVIS, knjiga.getId());
        int idKategorije=0;
        String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV)).equals(knjiga.getZanr())) idKategorije=cursor.getInt(cursor.getColumnIndex(KATEGORIJA_ID));
            }
        }
        cursor.close();
        novi.put(KNJIGA_ID_KATEGORIJE, idKategorije);
        if(knjiga.getSlika()==null){
            URL url = new URL ("https://thumbs.dreamstime.com/t/no-value-rubber-stamp-no-value-rubber-stamp-grunge-design-dust-scratches-effects-can-be-easily-removed-clean-crisp-look-98988518.jpg");
            novi.put(KNJIGA_SLIKA,url.toString());
        }
        else novi.put(KNJIGA_SLIKA, knjiga.getSlika().toString());

        if(knjiga.isBoja()==true){
            novi.put(KNJIGA_PREGLEDANA, 1);
        }
        else novi.put(KNJIGA_PREGLEDANA, 0);

        db.insert(BazaOpenHelper.DATABASE_TABLE_KNJIGA,null, novi);

        String[] rezuktatiKnjiga = new String[] {KNJIGA_ID, KNJIGA_NAZIV};
        cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezuktatiKnjiga, null,null,null,null,null);
        while (cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)).equals(knjiga.getNaziv())) idKnjige=cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));
        }
        ArrayList<Autor> autori = knjiga.getAutori();
        String[] rezultatiAutori = new String[] {AUTOR_ID, AUTOR_IME};
        for(int i=0; i<autori.size(); i++){
            cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null, null, null, null, null);
            boolean istina = false;
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(autori.get(i).getImeiPrezime().equals(cursor.getString(cursor.getColumnIndex(AUTOR_IME)))){
                        istina=true;
                        int indexAutora = cursor.getInt(cursor.getColumnIndex(AUTOR_ID));
                        ContentValues noviAutorstvo = new ContentValues();
                        noviAutorstvo.put(AUTORSTVO_ID_AUTORA, indexAutora);
                        noviAutorstvo.put(AUTORSTVO_ID_KNJIGE, idKnjige);
                        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, null, noviAutorstvo);

                    }
                }
            }
            if(istina==false){
                ContentValues noviAutor = new ContentValues();
                noviAutor.put(AUTOR_IME, autori.get(i).getImeiPrezime());
                db.insert(DATABASE_TABLE_AUTOR, null, noviAutor);
                cursor = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null,null,null,null,null);
                while(cursor.moveToNext()){
                    if(autori.get(i).getImeiPrezime().equals(cursor.getString(cursor.getColumnIndex(AUTOR_IME)))){
                        int indexAutora = cursor.getInt(cursor.getColumnIndex(AUTOR_ID));
                        ContentValues noviAutorstvo = new ContentValues();
                        noviAutorstvo.put(AUTORSTVO_ID_AUTORA, indexAutora);
                        noviAutorstvo.put(AUTORSTVO_ID_KNJIGE, idKnjige);
                        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, null, noviAutorstvo);
                    }
                }
            }
        }
        cursor.close();
        db.close();
        return idKnjige;
    }

    public ArrayList<Knjiga> dajKnjige() throws MalformedURLException {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        ArrayList<String> kategorije = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Knjiga k = new Knjiga();
                k.setId(cursor.getString(cursor.getColumnIndex(KNJIGA_ID_WEB_SERVIS)));
                URL url = new URL(cursor.getString(cursor.getColumnIndex(KNJIGA_SLIKA)));
                k.setSlika(url);
                k.setDatumObjavljivanja(cursor.getString(cursor.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA)));
                k.setNaziv(cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)));
                k.setOpis(cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS)));
                int boja = cursor.getInt(cursor.getColumnIndex(KNJIGA_PREGLEDANA));
                if(boja==1) k.setBoja(true);
                else k.setBoja(false);
                k.setBrojStrinica(cursor.getInt(cursor.getColumnIndex(KNJIGA_BROJ_STRANICA)));

                String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
                Cursor cursor1 = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA, rezultati, null, null, null, null, null);
                while(cursor1.moveToNext()){
                    if(cursor1.getInt(cursor1.getColumnIndex(KATEGORIJA_ID))==cursor.getInt(cursor.getColumnIndex(KNJIGA_ID_KATEGORIJE))) k.setZanr(cursor1.getString(cursor1.getColumnIndex(KATEGORIJA_NAZIV)));
                }
                cursor1.close();
                String[] rezultatiAutori = new String[] {AUTOR_ID, AUTOR_IME};
                cursor1 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null,null,null,null,null );
                String[] rezultatiAutorstvo = new String[] {AUTORSTVO_ID_AUTORA, AUTORSTVO_ID_KNJIGE};
                Cursor cursor2 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, rezultatiAutorstvo, null,null,null,null,null );



                while(cursor2.moveToNext()){
                    if(cursor.getInt(cursor.getColumnIndex(KNJIGA_ID))==cursor2.getInt(cursor2.getColumnIndex(AUTORSTVO_ID_KNJIGE))){
                        while(cursor1.moveToNext()){
                            if(cursor1.getInt(cursor1.getColumnIndex(AUTOR_ID))==cursor2.getInt(cursor2.getColumnIndex(AUTORSTVO_ID_AUTORA))) k.dodajAutora(cursor1.getString(cursor1.getColumnIndex(AUTOR_IME)));
                        }
                        cursor1.moveToFirst();
                    }
                }
                cursor1.close();
                cursor2.close();
                knjige.add(k);
            }
        }

        return knjige;
    }

    public void ocistiTabele(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DATABASE_TABLE_KATEGORIJA);
        db.execSQL("delete from "+ DATABASE_TABLE_KNJIGA);
        db.execSQL("delete from "+ DATABASE_TABLE_AUTOR);
        db.execSQL("delete from "+ DATABASE_TABLE_AUTORSTVO);
    }

    ArrayList<Knjiga> knjigeKategorije(long idKategorije) throws MalformedURLException {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        try {
            ArrayList<Knjiga> temp = this.dajKnjige();
            for(int i=0; i<temp.size(); i++){
                String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor1 = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA, rezultati, null, null, null, null, null);
                String kategorija = "";
                while (cursor1.moveToNext()){
                    if(cursor1.getInt(cursor1.getColumnIndex(KATEGORIJA_ID)) == (int) idKategorije) kategorija=cursor1.getString(cursor1.getColumnIndex(KATEGORIJA_NAZIV));
                }
                if(!kategorija.equals("")){
                    if(temp.get(i).getZanr().equals(kategorija)) knjige.add(temp.get(i));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return knjige;
    }

    ArrayList<Knjiga> knjigeAutora(long idAutora) throws MalformedURLException {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        ArrayList<Knjiga> temp = this.dajKnjige();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        String[] rezultatiAutori = new String[] {AUTOR_ID, AUTOR_IME};
        Cursor cursor1 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null,null,null,null,null );
        String[] rezultatiAutorstvo = new String[] {AUTORSTVO_ID_AUTORA, AUTORSTVO_ID_KNJIGE};
        Cursor cursor2 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, rezultatiAutorstvo, null,null,null,null,null );
        while(cursor2.moveToNext()){
            if(cursor2.getInt(cursor2.getColumnIndex(AUTORSTVO_ID_AUTORA))==idAutora ){
                while(cursor.moveToNext()){
                    if(cursor2.getInt(cursor2.getColumnIndex(AUTORSTVO_ID_KNJIGE))==cursor.getInt(cursor.getColumnIndex(KNJIGA_ID))){
                        for(int i=0; i<temp.size(); i++) if(temp.get(i).getNaziv().equals(cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)))) knjige.add(temp.get(i));
                    }
                }
                cursor.moveToFirst();
            }
        }
        return knjige;
    }

    public void obojiKnjigu(String naziv){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor cursor = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)).equals(naziv)){
                ContentValues novi = new ContentValues();
                novi.put(KNJIGA_NAZIV, cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV)));
                novi.put(KNJIGA_OPIS, cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS)));
                novi.put(KNJIGA_DATUM_OBJAVLJIVANJA, cursor.getString(cursor.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA)));
                novi.put(KNJIGA_BROJ_STRANICA, cursor.getInt(cursor.getColumnIndex(KNJIGA_BROJ_STRANICA)));
                novi.put(KNJIGA_ID_WEB_SERVIS, cursor.getString(cursor.getColumnIndex(KNJIGA_ID_WEB_SERVIS)));
                novi.put(KNJIGA_ID_KATEGORIJE, cursor.getInt(cursor.getColumnIndex(KNJIGA_ID_KATEGORIJE)));
                novi.put(KNJIGA_SLIKA, cursor.getString(cursor.getColumnIndex(KNJIGA_SLIKA)));
                novi.put(KNJIGA_PREGLEDANA, 1);
                int id = cursor.getInt(cursor.getColumnIndex(KNJIGA_ID));
                db.update(BazaOpenHelper.DATABASE_TABLE_KNJIGA,novi,"_id="+id, null);
            }
        }
    }

}