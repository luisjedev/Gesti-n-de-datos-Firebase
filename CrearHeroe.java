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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CrearHeroe extends AppCompatActivity {

    private EditText nombre;
    private Spinner rol;
    private RatingBar dificultad;
    private ImageView heroe;
    private Uri heroe_url;
    private final static int SELECCIONAR_HEROE = 1;
    private MediaPlayer mp;
    private DatabaseReference ref;
    private StorageReference sto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_heroe);

        mp = MediaPlayer.create(this, R.raw.click);

        nombre = (EditText) findViewById(R.id.nombre);
        rol = (Spinner) findViewById(R.id.rol);
        dificultad = (RatingBar) findViewById(R.id.dificultad);
        heroe = (ImageView) findViewById(R.id.heroe);

        LayerDrawable stars = (LayerDrawable) dificultad.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.oscuro), PorterDuff.Mode.SRC_ATOP);



        ref = FirebaseDatabase.getInstance().getReference();
        sto = FirebaseStorage.getInstance().getReference();
        heroe_url = null;

        String[] opciones = {"Luchador", "Tanque", "Mago", "Asesino", "Tirador", "Soporte"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        rol.setAdapter(adapter);


    }



    public void crearHeroe(View v) {
        final String valor_nombre = nombre.getText().toString();
        final String valor_posicion = rol.getSelectedItem().toString();
        final int valor_dificultad = dificultad.getProgress();

        if (nombre.getText().toString().equals("") || valor_posicion.equals("") || valor_dificultad == 0) {
            Toast.makeText(this, "Completa los campos necesarios", Toast.LENGTH_LONG).show();
        } else {


            ref.child("campeones")
                    .child("datos")
                    .orderByChild("nombre")
                    .equalTo(valor_nombre)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                Toast.makeText(getApplicationContext(), "El Heroe existe", Toast.LENGTH_LONG).show();
                            } else {
                                if (heroe_url != null) {
                                    Heroe nuevo_heroe = new Heroe(valor_nombre, valor_posicion, valor_dificultad);

                                    String clave = ref.child("campeones").child("datos").push().getKey();
                                    ref.child("campeones").child("datos").child(clave).setValue(nuevo_heroe);
                                    sto.child("campeones").child("imagen").child(clave).putFile(heroe_url);
                                    mp.start();
                                    Toast.makeText(CrearHeroe.this, "Heroe creado con exito", Toast.LENGTH_LONG).show();
                                    nombre.setText("");
                                    rol.setId(0);
                                    dificultad.setProgress(0);

                                    heroe.setImageResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
                                } else {
                                    Toast.makeText(CrearHeroe.this, "No se ha seleccionado una imagen", Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

        public void seleccionarHeroe (View v){
            mp.start();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECCIONAR_HEROE);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SELECCIONAR_HEROE && resultCode == RESULT_OK) {
                heroe_url = data.getData();
                heroe.setImageURI(heroe_url);
                Toast.makeText(getApplicationContext(), "Imagen seleccionada", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }


