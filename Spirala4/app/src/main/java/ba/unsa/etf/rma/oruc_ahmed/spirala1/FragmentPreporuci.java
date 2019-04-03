package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPreporuci.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPreporuci#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPreporuci extends Fragment {


    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    private KnjigaList knjigalista;
    private Autori autorilista;
    private ArrayList<String> kategorije;

    private ArrayList<String> kontakti;
    private ArrayAdapter<String> kontaktiAdapter;
    private ArrayList<String> mailovi;

    private Spinner sKontakti;
    private TextView eKontakti;
    private TextView datum;
    private TextView brojStranica;
    private TextView autori;
    private TextView knjiga;
    private ImageView slika;
    private TextView opis;
    private Button slanje;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentPreporuci() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPreporuci.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPreporuci newInstance(String param1, String param2) {
        FragmentPreporuci fragment = new FragmentPreporuci();
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
        return inflater.inflate(R.layout.fragment_fragment_preporuci, container, false);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loadContacts(){
        getPermissionToReadUserContacts();

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (cursor1.moveToNext()){
                    String ime = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String email = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        kontakti.add(ime);
                        mailovi.add(email);
                    }
                }
                cursor1.close();
            }
        }
        cursor.close();
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        kategorije = new ArrayList<String>();
        knjigalista=new KnjigaList();
        autorilista=new Autori();
        kontakti=new ArrayList<>();
        mailovi=new ArrayList<>();

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


        sKontakti = (Spinner) getActivity().findViewById(R.id.sKontakti);
        eKontakti = (TextView) getActivity().findViewById(R.id.kontakti);
        datum = (TextView) getActivity().findViewById(R.id.e1DatumObjavljivanja);
        brojStranica = (TextView) getActivity().findViewById(R.id.e1BrojStranica);
        autori = (TextView) getActivity().findViewById(R.id.e1Autor);
        knjiga = (TextView) getActivity().findViewById(R.id.e1Naziv);
        slika = (ImageView) getActivity().findViewById(R.id.e1Naslovna);
        opis = (TextView) getActivity().findViewById(R.id.e1Opis);
        slanje = (Button) getActivity().findViewById(R.id.dPosalji);
        opis.setMovementMethod(new ScrollingMovementMethod());

        loadContacts();

        kontaktiAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, kontakti);
        kontaktiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKontakti.setAdapter(kontaktiAdapter);

        final Knjiga temp = knjigalista.dajZadnju();
        knjigalista.brisiZadnju();
        datum.setText("Datum objavljivanja: " + temp.getDatumObjavljivanja());
        brojStranica.setText("Broj stranica: " + Integer.toString(temp.getBrojStrinica()));
        autori.setText(temp.getAutori().get(0).getImeiPrezime());
        knjiga.setText(temp.getNaziv());
        opis.setText("Opis: " + temp.getOpis());

        Bitmap b = null;
        URL url = temp.getSlika();

        if(url==null){
            try {
                b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), temp.getSlikaUri());
                slika.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            new URLBitmapTask(slika).execute(url);
        }


        slanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Send email", "");
                String[] TO = {mailovi.get(sKontakti.getSelectedItemPosition())};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka za knjigu");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " + sKontakti.getSelectedItem().toString() + ",\nPročitaj knjigu " + temp.getNaziv() + " od " + autori.getText().toString() + "!"  );
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                    Bundle argumenti = new Bundle();
                    argumenti.putSerializable("knjige", knjigalista);
                    argumenti.putStringArrayList("kategorije", kategorije);
                    argumenti.putSerializable("autori",autorilista);
                    ListeFragment lf = new ListeFragment();
                    lf.setArguments(argumenti);
                    if(getFragmentManager().findFragmentById(R.id.mjestoF3)!=null) getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF1, lf).commit();

                    Log.i("Finished sending email", "");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity().getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });

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



    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {

            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) { Toast.makeText(getActivity().getApplicationContext(), "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else { Toast.makeText(getActivity().getApplicationContext(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
