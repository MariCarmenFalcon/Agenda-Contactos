package com.example.listviewcontacto;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_MODIFICAR = 1;
    ListView ListViewContactos;
    public static List<Contactos> lst;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListViewContactos = findViewById(R.id.listView);

        lst = GetData();
        //adapter = new CustomAdapter(this, lst = new ArrayList<>());
        adapter = new CustomAdapter(this, lst);
        cargarContactosDesdeSharedPrefereces();
        ListViewContactos.setAdapter(adapter);

        //CONCEDER PERMISOS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }

        //MODIFICAR CONTACTO
        ListViewContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                abrirModificarContacto(position);
            }
        });
    }

    //METODO PARA RECIBIR DATOS DEL MAIN_ACTIVITY2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MODIFICAR && resultCode == RESULT_OK && data != null) {
            int posicionModificada = data.getIntExtra("posicionModificada", -1);

            if (posicionModificada != -1) {
                // Modificar el contacto existente en la lista
                lst.get(posicionModificada).setNombre(data.getStringExtra("nombre"));
                lst.get(posicionModificada).setNum(data.getStringExtra("telefono"));
                lst.get(posicionModificada).setImagenUri(Uri.parse(data.getStringExtra("imagenUri")));

                // Actualizar el adaptador
                adapter.notifyDataSetChanged();
            } else {
                // Agregar nuevo contacto a la lista
                String nombre = data.getStringExtra("nombre");
                String telefono = data.getStringExtra("telefono");
                Uri imagenUri = Uri.parse(data.getStringExtra("imagenUri"));

                lst.add(new Contactos(lst.size() + 1, imagenUri, nombre, telefono));
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void crearCarpeta() {
        String nombreCarpetaPrincipal = "ContactosApp";
        String nombreSubcarpeta = "Images";

        File directorioPrincipal = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), nombreCarpetaPrincipal);
        File subdirectorio = new File(directorioPrincipal, nombreSubcarpeta);

        if (!directorioPrincipal.exists()) {
            directorioPrincipal.mkdir();
            if (!subdirectorio.exists()) {
                subdirectorio.mkdir();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Ambos permisos concedidos, puedes realizar la acción que requiere el permiso.
                crearCarpeta();
            } else {
                // Al menos uno de los permisos fue denegado, puedes mostrar un mensaje al usuario y cerrar la aplicación.
                Toast.makeText(this, "Permisos denegados. La aplicación se cerrará.", Toast.LENGTH_SHORT * 3).show();
                finish(); // Cierra la actividad
            }
        }
    }

    private List<Contactos> GetData() {
        lst = new ArrayList<>();
        return lst;
    }

    public void goToActivity2(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //METODO PARA MODIFICAR CONTACTO
    //Abre el MainActivity2 con la opción de modificar
    //También he implementado una opcion de dialogo para confirmar si quiere modificar.
    private void abrirModificarContacto(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones de contacto");
        builder.setItems(new CharSequence[]{"Modificar", "Eliminar"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Modifico el contacto
                        Contactos contactoAModificar = lst.get(position);
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        intent.putExtra("nombre", contactoAModificar.getNombre());
                        intent.putExtra("telefono", contactoAModificar.getNum());
                        intent.putExtra("imagenUri", contactoAModificar.getImagenUri().toString());
                        intent.putExtra("posicionModificada", position);
                        intent.putExtra("modificarContacto", true);
                        startActivityForResult(intent, REQUEST_CODE_MODIFICAR);
                        break;
                    case 1:
                        //Elimino el contacto
                        confirmarEliminarContacto(position);
                        break;
                }
            }
        });

        builder.show();
    }

    //METODO PARA ELIMINAR CONTACTO
    private void confirmarEliminarContacto(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Quieres eliminar este contacto?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Si el usuario selecciona "Sí", eliminar el contacto y actualizar la lista
                lst.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Si el usuario selecciona "No", no hacer nada
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Manejar el caso en el que el diálogo se cierra sin seleccionar ninguna opción
            }
        });

        builder.show();
    }

    private void cargarContactosDesdeSharedPrefereces() {
        adapter = new CustomAdapter(this, lst = ContactPreferences.getContactList(this));
        adapter.notifyDataSetChanged();
    }
}
