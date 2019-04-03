package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaKnjigAkt extends AppCompatActivity {

    private KnjigaListAdapter adapter;
    private ArrayList<Knjiga> knjge;
    private KnjigaList knjigalista;
    private ArrayList<Knjiga> knjigekategorija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjig_akt);

        ListView lista = (ListView) findViewById(R.id.listaKnjiga);
        Button  povratak = (Button) findViewById(R.id.dPovratak);

        knjigalista=(KnjigaList) getIntent().getSerializableExtra("knjige");
        knjge=knjigalista.getKnjige();
        knjigekategorija=new ArrayList<>();
        String kategorija=getIntent().getStringExtra("kategorija");
        for(int j=0; j<knjge.size();j++){
            if(knjge.get(j).getZanr().equals(kategorija)) knjigekategorija.add(knjge.get(j));
        }

        //adapter=new KnjigaListAdapter(getApplicationContext(),knjigekategorija);
        lista.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent povratakNaPocetnuFormu=new Intent();
                knjigalista = new KnjigaList(knjge);
                povratakNaPocetnuFormu.putExtra("knjige",knjigalista);
                setResult(Activity.RESULT_OK,povratakNaPocetnuFormu);
                finish();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(knjigekategorija.get(i).isBoja()==false) knjigekategorija.get(i).setBoja(true);
                for(int j=0; j<knjge.size(); j++) if(knjge.get(j).getId()==knjigekategorija.get(i).getId()) { knjge.get(j).setBoja(true); break ;}
                adapter.notifyDataSetChanged();
            }

        });

    }
}
