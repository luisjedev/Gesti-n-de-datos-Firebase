package com.example.herodex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class MenuHeroes extends AppCompatActivity {
    ImageView imagen;
    MediaPlayer mp;
    Button creacion,ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_heroes);

        creacion = (Button) findViewById(R.id.crearHeroe);
        ver = (Button) findViewById(R.id.verHeroes);

        mp = MediaPlayer.create(this, R.raw.click);

        creacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                irCreacion();
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                verHeroes();
            }
        });


        imagen = (ImageView) findViewById(R.id.animacion);
        Glide.with(this).asGif().load(R.drawable.poro).into(imagen);

    }


    public void irCreacion(){
        Intent i = new Intent(this, CrearHeroe.class);
        startActivity(i);
    }

    public void verHeroes(){
        Intent i = new Intent(this,VerHeroes.class);
        startActivity(i);

    }


}

