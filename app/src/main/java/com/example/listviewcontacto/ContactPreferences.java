package com.example.listviewcontacto;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ContactPreferences {

    private static final String PREF_NAME = "ContactPreferences";
    private static final String KEY_CONTACT_COUNT = "contactCount";
    private static final String KEY_CONTACT_ID = "contactId_";
    private static final String KEY_CONTACT_IMAGE = "contactImage_";
    private static final String KEY_CONTACT_NAME = "contactName_";
    private static final String KEY_CONTACT_NUMBER = "contactNumber_";

    public static void saveContactList(Context context, List<Contactos> contactosList){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_CONTACT_COUNT, contactosList.size());

        for(int i = 0; i < contactosList.size(); i++){
            Contactos contacto = contactosList.get(i);
            editor.putInt(KEY_CONTACT_ID + i, contacto.getId());
            if(contacto.getImagenUri() != null){
                editor.putString(KEY_CONTACT_IMAGE + i, contacto.getImagenUri().getPath());
            } else {
                editor.putString(KEY_CONTACT_IMAGE + i, null);
            }
            editor.putString(KEY_CONTACT_NAME + i, contacto.getNombre());
            editor.putString(KEY_CONTACT_NUMBER + i, contacto.getNum());
        }
        editor.apply();
    }

    public static List<Contactos> getContactList(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int contacCount = sharedPreferences.getInt(KEY_CONTACT_COUNT, 0);

        List<Contactos> contactosList = new ArrayList<>();

        for (int i = 0; i < contacCount; i++){
            int id = sharedPreferences.getInt(KEY_CONTACT_ID + i, 0);
            String imagePath = sharedPreferences.getString(KEY_CONTACT_IMAGE + i, null);
            Uri imageUri;
            if (imagePath != null){
                imageUri = Uri.parse(imagePath);
            } else {
                imageUri = null;
            }
            String name = sharedPreferences.getString(KEY_CONTACT_NAME + i, "Sin nombre");
            String number = sharedPreferences.getString(KEY_CONTACT_NUMBER + i, "Sin telefono");

            Contactos contacto = new Contactos(id, imageUri, name, number);
            contactosList.add(contacto);
        }
        return contactosList;
    }
}
