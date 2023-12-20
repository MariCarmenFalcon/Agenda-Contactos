package com.example.listviewcontacto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Contactos> ls;

    public CustomAdapter(Context context, List<Contactos> ls) {
        this.context = context;
        this.ls = ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageViewContactos;
        TextView textViewNombre;
        TextView textViewNum;

        Contactos c = ls.get(i);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_contacto, null);
        }

        imageViewContactos = view.findViewById(R.id.imageViewContactos);
        textViewNombre = view.findViewById(R.id.textViewNombre);
        textViewNum = view.findViewById(R.id.textViewNum);

        //Uso setImageURI en lugar de setImageResource
        imageViewContactos.setImageURI(c.getImagenUri());
        textViewNombre.setText(c.getNombre());
        textViewNum.setText(c.getNum());

        return view;
    }
}

