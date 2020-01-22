package com.example.herodex;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HeroeAdapter extends ArrayAdapter {


    private Context context;
    private int resource;
    private ArrayList<Heroe> items;
    private RatingBar dificultad;
    private TextView nombre;
    private TextView rol;
    private ImageView foto;

    public HeroeAdapter(Context context, int resource, ArrayList<Heroe> items){
        super(context,resource,items);

        this.context=context;
        this.resource=resource;
        this.items=items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vista=convertView;

        if(vista==null){
            vista= LayoutInflater.from(context).inflate(R.layout.elemento_lista,null);
        }

        nombre=vista.findViewById(R.id.nombre_campeon);
        rol=vista.findViewById(R.id.rol_campeon);
        foto=vista.findViewById(R.id.foto_campeon);
        dificultad=vista.findViewById(R.id.dificultad_campeon);

        Heroe pojo_heroe=items.get(position);
        nombre.setText(pojo_heroe.getNombre());
        rol.setText(pojo_heroe.getRol());
        dificultad.setProgress(pojo_heroe.getDificultad());

        Glide.with(context).load(pojo_heroe.getUrl_heroe()).into(foto);

        return vista;
    }
}




