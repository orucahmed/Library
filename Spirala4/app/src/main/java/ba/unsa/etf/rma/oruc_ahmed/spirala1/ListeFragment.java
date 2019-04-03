package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeFragment extends Fragment {


    private DodajKnjiguOnlineKlik dkok;
    private OnItemClick oic;
    private DodajKnjiguKlik dc;
    private ArrayList<String> unosiKategorije;
    private ArrayAdapter<String> unosiAdapter;
    private ArrayList<Knjiga> knjige;
    private KnjigaList knjigalista;
    private Button kategorije ;
    private Button autori;
    private Button pretraga;
    private Button dodajKategoriju;
    private Button dodajKnjigu;
    private EditText tekstPretraga;
    private ListView lista;
    private boolean istina;
    private Autori autorilista;
    private Button dodajKnjiguOnline;
    private int broj;
    private BazaOpenHelper baza;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListeFragment newInstance(String param1, String param2) {
        ListeFragment fragment = new ListeFragment();
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
        return inflater.inflate(R.layout.fragment_liste, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface DodajKnjiguOnlineKlik{
        public void onClick(int broj);
    }

    public interface DodajKnjiguKlik{
        public void onClick(int broj);
    }


    public interface OnItemClick {
        public void onItemClicked ( int pos, boolean istina );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        baza=null;
        unosiKategorije=new ArrayList<>();

        if(getArguments().containsKey("kategorije")){
            unosiKategorije=getArguments().getStringArrayList("kategorije");
        }
        if(getArguments().containsKey("knjige")){
            Bundle b = getArguments();
            knjigalista=(KnjigaList) b.getSerializable("knjige");
            knjige=knjigalista.getKnjige();
        }

        if(getArguments().containsKey("autori")){
            Bundle b = getArguments();
            autorilista=(Autori) b.getSerializable("autori");
        }

        try{
            oic = (OnItemClick)getActivity();
        }
        catch(ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "Treba implementirati OnItemClick");
        }



        lista=(ListView) getView().findViewById(R.id.listaKategorija);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                oic.onItemClicked(i,istina);
            }
        });


        dodajKnjiguOnline = (Button) getView().findViewById(R.id.dDodajOnline);
        kategorije = (Button) getView().findViewById(R.id.dKategorije);
        autori = (Button) getView().findViewById(R.id.dAutori);
        pretraga = (Button) getView().findViewById(R.id.dPretraga);
        dodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        dodajKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        tekstPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);
        dodajKategoriju.setEnabled(false);
        baza= new BazaOpenHelper(getActivity());

        kategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unosiAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, unosiKategorije);
                lista.setAdapter(unosiAdapter);
                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);
                istina=true;
            }
        });

        autori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autorilista!=null) {
                    pretraga.setVisibility(View.GONE);
                    dodajKategoriju.setVisibility(View.GONE);
                    tekstPretraga.setVisibility(View.GONE);
                    ArrayList<String> neki = autorilista.getAutori();
                    unosiAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, neki);
                    lista.setAdapter(unosiAdapter);
                    unosiAdapter.notifyDataSetChanged();
                    istina = false;
                }
            }
        });


        dc=(DodajKnjiguKlik)getActivity();
        dkok=(DodajKnjiguOnlineKlik) getActivity();




        dodajKnjiguOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broj=1;
                dkok.onClick(broj);
            }
        });

        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broj=0;
                dc.onClick(broj);

            }
        });

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(istina) {
                    unosiAdapter.getFilter().filter(tekstPretraga.getText().toString(), new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int i) {
                            if (i == 0) dodajKategoriju.setEnabled(true);
                            else dodajKategoriju.setEnabled(false);
                        }
                    });
                }
            }
        });

        dodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(baza.dodajKategoriju(tekstPretraga.getText().toString())!=-1) {
                    unosiKategorije.add(tekstPretraga.getText().toString());
                    unosiAdapter.add(tekstPretraga.getText().toString());
                    unosiAdapter.notifyDataSetChanged();
                    tekstPretraga.setText("");
                    unosiAdapter.getFilter().filter("");
                    dodajKategoriju.setEnabled(false);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Zeljena kategorija je vec u bazi", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

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
