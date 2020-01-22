package com.example.herodex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VerHeroes extends AppCompatActivity {

    private EditText nombre;
    private ListView lista;
    private DatabaseReference ref;
    private ArrayList<Heroe> items;
    private HeroeAdapter adaptador;
    private StorageReference sto;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_heroes);

        nombre= (EditText) findViewById(R.id.nombre_buscado);
        nombre.setSelected(false);
        lista=findViewById(R.id.lista);
        items=new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        mp = MediaPlayer.create(this, R.raw.click);


        ref.child("campeones").child("datos").orderByChild("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot hijo:dataSnapshot.getChildren()) {
                    final Heroe heroe = hijo.getValue(Heroe.class);
                    heroe.setId(hijo.getKey());
                    items.add(heroe);
                }

                for(final Heroe heroe:items){
                    sto.child("campeones")
                            .child("imagen")
                            .child(heroe.getId())
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    heroe.setUrl_heroe(uri);
                                    adaptador.notifyDataSetChanged();
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adaptador=new HeroeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Heroe pojo_club=(Heroe) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(getApplicationContext(),EditarHeroe.class);
                intent.putExtra("id_heroe",pojo_club.getId());
                mp.start();
                startActivity(intent);
            }
        });
    }

    public void buscarHeroe(View v){
        final String busqueda=nombre.getText().toString();
        mp.start();
        ref.child("campeones").child("datos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                items.clear();
                for(DataSnapshot hijo:dataSnapshot.getChildren()){
                    Heroe pojo=hijo.getValue(Heroe.class);
                    if(pojo.getNombre().contains(busqueda)){
                        pojo.setId(hijo.getKey());
                        items.add(pojo);
                    }
                }

                for(final Heroe c:items){
                    sto.child("campeones")
                            .child("imagen")
                            .child(c.getId())
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    c.setUrl_heroe(uri);
                                    adaptador.notifyDataSetChanged();
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
