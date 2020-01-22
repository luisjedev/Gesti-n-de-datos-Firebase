package com.example.herodex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditarHeroe extends AppCompatActivity {

    private EditText editar_nombre;
    private ImageView editar_heroe;
    private Spinner editar_rol;
    private RatingBar editar_dificultad;
    private String id;
    private Uri foto_url;
    private int posicion_rol;
    private String valor_rol;
    private DatabaseReference ref;
    private StorageReference sto;
    private final static int SELECCIONAR_HEROE=1;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_heroe);

        foto_url=null;
        editar_nombre = (EditText) findViewById(R.id.editar_nombre);
        editar_rol = (Spinner) findViewById(R.id.editar_rol);
        editar_dificultad = (RatingBar) findViewById(R.id.editar_dificultad);
        editar_heroe = (ImageView) findViewById(R.id.editar_heroe);

        mp = MediaPlayer.create(this, R.raw.click);

        LayerDrawable stars = (LayerDrawable) editar_dificultad.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.oscuro), PorterDuff.Mode.SRC_ATOP);

        final String[] opciones = {"Luchador", "Tanque", "Mago", "Asesino", "Tirador", "Soporte"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        editar_rol.setAdapter(adapter);

        Intent intent=getIntent();
        id=intent.getStringExtra("id_heroe");


        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        ref.child("campeones")
                .child("datos")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Heroe resultado=dataSnapshot.getValue(Heroe.class);


                        valor_rol=resultado.getRol();

                        for (int i=0; i<opciones.length;i++){
                            if (opciones[i].equals(valor_rol)){
                                posicion_rol=i;
                            }
                        }

                        editar_nombre.setText(resultado.getNombre());
                        editar_rol.setSelection(posicion_rol);
                        editar_dificultad.setProgress(resultado.getDificultad());


                        sto.child("campeones").child("imagen").child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext()).load(uri).into(editar_heroe);
                                foto_url=uri;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void modificarHeroe(View v){

        final String valor_nombre=editar_nombre.getText().toString();
        final String valor_rol=editar_rol.getSelectedItem().toString();
        final int valor_dificultad=editar_dificultad.getProgress();
        final Handler mWaitHandler = new Handler();

        mp = MediaPlayer.create(this,R.raw.modificar);

        if (editar_nombre.getText().toString().equals("") || valor_rol.equals("") || valor_dificultad == 0) {
            Toast.makeText(this, "Completa los campos necesarios", Toast.LENGTH_LONG).show();
        } else {



            ref.child("campeones")
                    .child("datos")
                    .orderByChild("nombre")
                    .equalTo(valor_nombre)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            DataSnapshot hijo=dataSnapshot.getChildren().iterator().next();

                            if (dataSnapshot.hasChildren() && hijo.getKey().equals(id)) {
                                if (foto_url != null) {

                                    Heroe nuevo_heroe = new Heroe(valor_nombre, valor_rol, valor_dificultad);

                                    ref.child("campeones").child("datos").child(id).setValue(nuevo_heroe);
                                    sto.child("campeones").child("imagen").child(id).putFile(foto_url);
                                    mp.start();
                                    Toast.makeText(getApplicationContext(), "Heroe editado con exito", Toast.LENGTH_LONG).show();

                                    mWaitHandler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {

                                            try {
                                                Intent intent = new Intent(getApplicationContext(), VerHeroes.class);
                                                startActivity(intent);
                                            } catch (Exception ignored) {
                                                ignored.printStackTrace();
                                            }
                                        }
                                    }, 1000);  // Give a 5 seconds delay.




                                } else {
                                    Toast.makeText(EditarHeroe.this, "No se ha seleccionado una imagen", Toast.LENGTH_LONG).show();
                                }


                            } else {

                        Toast.makeText(getApplicationContext(), "El Heroe existe", Toast.LENGTH_LONG).show();
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }

    public void eliminarHeroe(View v){
        ref.child("campeones").child("datos").child(id).removeValue();

        mp = MediaPlayer.create(this,R.raw.borrar);
        mp.start();
        Toast.makeText(getApplicationContext(),"Heroe borrado con exito",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getApplicationContext(),VerHeroes.class);
        startActivity(intent);
    }

    public void seleccionarHeroe(View v){
        mp.start();
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,SELECCIONAR_HEROE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECCIONAR_HEROE && resultCode==RESULT_OK){
            foto_url=data.getData();
            editar_heroe.setImageURI(foto_url);
            Toast.makeText(getApplicationContext(),"Imagen seleccionada",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
    }
}
