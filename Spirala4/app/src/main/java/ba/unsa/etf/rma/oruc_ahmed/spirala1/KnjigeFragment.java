package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KnjigeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KnjigeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KnjigeFragment extends Fragment {


    private KnjigaListAdapter adapter;
    private ArrayList<Knjiga> knjge;
    private KnjigaList knjigalista;
    private ArrayList<Knjiga> knjigekategorija;
    private ArrayList<String> kategorije;
    ListView lista;
    Button povratak;
    private Autori autorilista;
    private BazaOpenHelper baza;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public KnjigeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KnjigeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KnjigeFragment newInstance(String param1, String param2) {
        KnjigeFragment fragment = new KnjigeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baza= new BazaOpenHelper(getActivity());

        lista = (ListView) getView().findViewById(R.id.listaKnjiga);
        povratak = (Button) getView().findViewById(R.id.dPovratak);

        String parametar="";

        parametar=getArguments().getString("dodato");


        if(getArguments().containsKey("knjige")){
            Bundle b = getArguments();
            knjigalista=(KnjigaList) b.getSerializable("knjige");
            knjge=knjigalista.getKnjige();
        }
        if(getArguments().containsKey("kategorije")){
            kategorije=getArguments().getStringArrayList("kategorije");
        }

        if(getArguments().containsKey("autori")){
            Bundle b = getArguments();
            autorilista=(Autori) b.getSerializable("autori");
        }


        knjigekategorija=new ArrayList<>();
        if(parametar.length()==0){
            adapter=new KnjigaListAdapter(getActivity().getApplicationContext(),knjge, kategorije, autorilista, getActivity());
            knjigekategorija=knjge;
            lista.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else{
            if(autorilista!=null) {
                boolean autori = false;
                for (int i = 0; i < autorilista.getAutori().size(); i++)
                    if (autorilista.getAutori().get(i).equals(parametar)) autori = true;


                if (autori == false) {

                    try {
                        knjigekategorija = baza.knjigeKategorije(baza.dajIdIzImena(parametar));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    adapter = new KnjigaListAdapter(getActivity().getApplicationContext(), knjigekategorija,kategorije, autorilista,getActivity());
                    lista.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {

                    try {

                        knjigekategorija = baza.knjigeAutora(baza.dajIdIzImenaAutora(parametar.substring(0,parametar.length()-2)));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    adapter = new KnjigaListAdapter(getActivity().getApplicationContext(), knjigekategorija,kategorije, autorilista,getActivity());
                    lista.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(knjigekategorija.get(i).isBoja()==false) knjigekategorija.get(i).setBoja(true);
                baza.obojiKnjigu(knjigekategorija.get(i).getNaziv());
                for(int j=0; j<knjge.size(); j++) if(knjge.get(j).getId()==knjigekategorija.get(i).getId()) {
                    knjge.get(j).setBoja(true);


                    break ;
                }
                adapter.notifyDataSetChanged();
            }

        });

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle argumenti = new Bundle();
                knjigalista = new KnjigaList(knjge);
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
        return inflater.inflate(R.layout.fragment_knjige, container, false);
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
