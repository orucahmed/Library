package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DodavanjeKnjigeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DodavanjeKnjigeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DodavanjeKnjigeFragment extends Fragment {


    private ImageView slika;
    private KnjigaList knjigalista;
    private ArrayList<String> kategorije;
    private ArrayAdapter<String> kategorijeAdapter;
    private ArrayList<Autor> autori = new ArrayList<Autor>();
    private BazaOpenHelper baza;

    private EditText autor;
    private EditText naziv ;
    private Spinner spiner;
    private Button ponisti;
    private Button dodajSliku;
    private Button upisi;
    private Autori autorilista;
    private Button dodajAutora;
    private EditText brojStranica;
    private EditText datumObjave;
    private EditText id;
    private EditText opis;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baza= new BazaOpenHelper(getActivity());
        autorilista=new Autori();

        if(getArguments().containsKey("kategorije")){
            Bundle b = getArguments();
            kategorije=getArguments().getStringArrayList("kategorije");
        }

        if(getArguments().containsKey("knjige")){
            Bundle b = getArguments();
            knjigalista=(KnjigaList) b.getSerializable("knjige");
        }

        if(getArguments().containsKey("autori")){
            Bundle b = getArguments();
            autorilista=(Autori) b.getSerializable("autori");
        }

        slika = (ImageView) getView().findViewById(R.id.naslovnaStr);
        autor = (EditText) getView().findViewById(R.id.imeAutora);
        naziv = (EditText) getView().findViewById(R.id.nazivKnjige);
        spiner = (Spinner) getView().findViewById(R.id.sKategorijaKnjige);
        ponisti = (Button) getView().findViewById(R.id.dPonisti);
        dodajSliku = (Button) getView().findViewById(R.id.dNadjiSliku);
        upisi = (Button) getView().findViewById(R.id.dUpisiKnjigu);
        dodajAutora = (Button) getView().findViewById(R.id.dodajAutora);
        brojStranica = (EditText) getView().findViewById(R.id.brojStranica);
        datumObjave = (EditText) getView().findViewById(R.id.datumObjave);
        id = (EditText) getView().findViewById(R.id.id);
        opis = (EditText) getView().findViewById(R.id.id);


        dodajAutora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autori.add(new Autor(autor.getText().toString(), id.getText().toString()));
                if(autorilista!=null)autorilista.dodajAutora(autor.getText().toString());
                autor.setText("");
            }
        });

        kategorijeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        kategorijeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(kategorijeAdapter);

        autor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autor.getText().toString().equals("Ime Autora")) autor.setText("");
            }
        });

        naziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (naziv.getText().toString().equals("Naziv Knjige")) naziv.setText("");
            }
        });

        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dodajSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nesto = new Intent();
                nesto.setAction(Intent.ACTION_GET_CONTENT);
                nesto.setType("image/*");
                startActivityForResult(nesto, 2);
            }
        });


        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle argumenti = new Bundle();
                argumenti.putSerializable("knjige", knjigalista);
                argumenti.putStringArrayList("kategorije", kategorije);
                argumenti.putSerializable("autori",autorilista);
                ListeFragment lf = new ListeFragment();
                lf.setArguments(argumenti);
                if(getFragmentManager().findFragmentById(R.id.mjestoF3)!=null) getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.mjestoF1, lf).commit();
            }
        });



        upisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slika.buildDrawingCache();
                Bitmap b=slika.getDrawingCache();
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG,100,s);
                String put = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),b,"Naziv", null);
                Uri uri = Uri.parse(put);

                Knjiga k= new Knjiga(id.getText().toString(), naziv.getText().toString(), autori, opis.getText().toString(), datumObjave.getText().toString(),null, Integer.parseInt(brojStranica.getText().toString()) );
                k.setSlikaUri(uri);
                k.setZanr(spiner.getSelectedItem().toString());
                knjigalista.dodajKnjigu(k);
                try {
                    if(baza.dodajKnjigu(k)!=-1) Toast.makeText(getActivity().getApplicationContext(), "Uspjesno upisana knjiga", Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bundle argumenti = new Bundle();
                argumenti.putSerializable("knjige", knjigalista);
                argumenti.putStringArrayList("kategorije", kategorije);
                argumenti.putSerializable("autori",autorilista);
                ListeFragment lf = new ListeFragment();
                lf.setArguments(argumenti);
                if(getFragmentManager().findFragmentById(R.id.mjestoF3)!=null) getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.mjestoF1, lf).commit();

            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==2) {
            Uri imageUri = data.getData();
            InputStream s=null;
            try {
                s=getActivity().getContentResolver().openInputStream(imageUri);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(s);
            slika.setImageBitmap(bitmap);
        }
    }



    public DodavanjeKnjigeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DodavanjeKnjigeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DodavanjeKnjigeFragment newInstance(String param1, String param2) {
        DodavanjeKnjigeFragment fragment = new DodavanjeKnjigeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dodavanje_knjige, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
