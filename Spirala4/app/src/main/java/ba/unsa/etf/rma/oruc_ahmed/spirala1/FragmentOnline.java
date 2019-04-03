package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentOnline.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentOnline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {


    private ArrayList<String> kategorije;
    private ArrayAdapter<String> kategorijeAdapter;
    private ArrayAdapter<String> rezultatiAdapter;
    private ArrayList<Knjiga> knjigeOnline;
    private MojResultReceiver mReceiver;
    private BazaOpenHelper baza;

    private Button run;
    private Button add;
    private Spinner kategorijeSpinner;
    private Spinner rezultati;
    private EditText pretraga;
    private Button nazad;

    private KnjigaList knjigalista;
    private Autori autorilista;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentOnline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOnline.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOnline newInstance(String param1, String param2) {
        FragmentOnline fragment = new FragmentOnline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baza= new BazaOpenHelper(getActivity());

        kategorije = new ArrayList<String>();
        knjigalista=new KnjigaList();
        autorilista=new Autori();
        knjigeOnline = new ArrayList<>();

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

        run = (Button) getView().findViewById(R.id.dRun);
        add = (Button) getView().findViewById(R.id.dAdd);
        kategorijeSpinner = (Spinner) getView().findViewById(R.id.sKategorije);
        rezultati = (Spinner) getView().findViewById(R.id.sRezultat);
        pretraga = (EditText) getView().findViewById(R.id.tekstUpit);
        nazad = (Button) getView().findViewById(R.id.dPovratak);

        kategorijeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        kategorijeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorijeSpinner.setAdapter(kategorijeAdapter);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                knjigeOnline=new ArrayList<>();
                String s = pretraga.getText().toString();

                if(s.length()==0){
                    pretraga.requestFocus();
                }

                if(s.contains(";")){
                    String[] dijelovi = s.split(";");
                    for(int i=0; i<dijelovi.length; i++) new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(dijelovi[i]);
                }

                else if(s.contains("autor:")){
                    String[] dijelovi = s.split(":");
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(dijelovi[1]);
                }
                else if(s.contains("korisnik:")){
                    String[] dijelovi = s.split(":");
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    mReceiver = new MojResultReceiver(new Handler());
                    mReceiver.setmReceiver(FragmentOnline.this);
                    intent.putExtra("string", dijelovi[1]);
                    intent.putExtra("receiver", mReceiver);
                    getActivity().startService(intent);


                }
                else{
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(s);
                }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(knjigeOnline.size()==0) return;

                String dodajKnjigu = rezultati.getSelectedItem().toString();
                for(int i=0; i<knjigeOnline.size(); i++) if(dodajKnjigu.equals(knjigeOnline.get(i).getNaziv())){
                    Knjiga temp = knjigeOnline.get(i);
                    temp.setZanr(kategorijeSpinner.getSelectedItem().toString());
                    try {
                        if(baza.dodajKnjigu(temp)!=-1) Toast.makeText(getActivity().getApplicationContext(), "Uspjesno upisana knjiga", Toast.LENGTH_SHORT).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Autor> pom = temp.getAutori();
                    for(int j=0; j<pom.size();j++) autorilista.dodajAutora(pom.get(j).getImeiPrezime());
                    knjigalista.dodajKnjigu(temp);
                    break;
                }


            }
        });

        nazad.setOnClickListener(new View.OnClickListener() {
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
        return inflater.inflate(R.layout.fragment_fragment_online, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDone(ArrayList<Knjiga> rez) {
        for(int i=0; i<rez.size(); i++) knjigeOnline.add(rez.get(i));

        ArrayList<String> listaRezultataString = new ArrayList<String>();
        for(int i=0; i<knjigeOnline.size(); i++) listaRezultataString.add(knjigeOnline.get(i).getNaziv());

        rezultatiAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listaRezultataString);
        rezultatiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rezultati.setAdapter(rezultatiAdapter);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case KnjigePoznanika.STATUS_START:
                Toast.makeText(getActivity().getApplicationContext(), "Zapocinjem servis", Toast.LENGTH_LONG).show();
                break;

            case KnjigePoznanika.STATUS_FINISH:
                knjigeOnline= (ArrayList<Knjiga>) resultData.getSerializable("knjige");
                ArrayList<String> listaRezultataString = new ArrayList<String>();
                for(int i=0; i<knjigeOnline.size(); i++) listaRezultataString.add(knjigeOnline.get(i).getNaziv());

                rezultatiAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listaRezultataString);
                rezultatiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rezultati.setAdapter(rezultatiAdapter);
                break;
            case KnjigePoznanika.STATUS_ERROR:
                String error = resultData . getString ( Intent . EXTRA_TEXT );
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }
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
