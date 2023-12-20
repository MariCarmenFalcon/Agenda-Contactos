package com.example.listviewcontacto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public ImageView imagencontacto;
    public EditText editTextNombre;
    public EditText editTextPhone;
    private Uri imagenUriSeleccionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imagencontacto = findViewById(R.id.imagenContacto);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextPhone = findViewById(R.id.editTextPhone);

        imagencontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFileChosser();
            }
        });

        //Compruebo si hay datos extra indicando que estoy modificando un contacto
        if (getIntent().getBooleanExtra("modificarContacto", false)) {
            //Pongo un Toast para indicar que estoy modificando
            Toast.makeText(this, "Modo de modificación activado", Toast.LENGTH_SHORT * 3).show();
        }
    }

    public void oppenFileChosser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Compruebo si la solicitud es para seleccionar una imagen y si la operación fue exitosa
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            //Obtengo la URI de la imagen seleccionada desde los datos de la intención
            Uri uri = data.getData();
            //Escalo la imagen a un tamaño adecuado
            Bitmap bitmap;
            try {
                //Obtengo el mapa de bits de la imagen utilizando la URI
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                //Ancho deseado de la imagen después de escalar
                int targetWidth = 300;
                //Calculo la altura proporcionada para mantener la relación de aspecto original
                int targetHeight = (int) (targetWidth * ((float) bitmap.getHeight() / bitmap.getWidth()));

                //Configuro la matriz de transformación para escalar la imagen
                Matrix matrix = new Matrix();
                matrix.postScale((float) targetWidth / bitmap.getWidth(), (float) targetHeight / bitmap.getHeight());

                //Creo un nuevo mapa de bits escalado
                Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                //Configuro la vista de imagen para mostrar el mapa de bits escalado
                imagencontacto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imagencontacto.setAdjustViewBounds(true);
                imagencontacto.setImageBitmap(scaledBitmap);

                //Asigno la URI directamente a la variable de instancia
                imagenUriSeleccionada = uri;

               /* // Configurar el resultado y cerrar MainActivity2
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imagenUri", uri.toString());
                setResult(RESULT_OK, resultIntent);
                finish();*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void añadirContacto(View view) {
        /*// Obtengo datos del EditText y la imagen
        String nombre = editTextNombre.getText().toString();
        String telefono = editTextPhone.getText().toString();
        Uri imagenUri = obtenerUriImagenSeleccionada();

        // Comprubo si se está modificando un contacto existente
        int posicionModificada = getIntent().getIntExtra("posicionModificada", -1);

        // Creo un Intent para devolver los resultados
        Intent resultIntent = new Intent();

        if (posicionModificada != -1) {
            // Modifico el contacto existente y devuelvo la posición modificada
            resultIntent.putExtra("posicionModificada", posicionModificada);
        }

        // Devuelvo datos adicionales
        resultIntent.putExtra("nombre", nombre);
        resultIntent.putExtra("telefono", telefono);
        resultIntent.putExtra("imagenUri", imagenUri != null ? imagenUri.toString() : "");

        // Accedo a la lista de contactos
        List<Contactos> contactosList = MainActivity.lst;
        // Guardo la lista de contactos
        ContactPreferences.saveContactList(this, contactosList);

        // Configuro el resultado y cierro MainActivity2
        setResult(RESULT_OK, resultIntent);
        finish();*/

        // Obtengo datos del EditText y la imagen
        String nombre = editTextNombre.getText().toString().trim();
        String telefono = editTextPhone.getText().toString().trim();
        Uri imagenUri = obtenerUriImagenSeleccionada();

        // Validar que al menos el nombre o el teléfono estén presentes
        if (nombre.isEmpty() && telefono.isEmpty()) {
            Toast.makeText(this, "Ingrese al menos el nombre o el teléfono", Toast.LENGTH_SHORT * 3).show();
            return;
        }

        // Compruebo si se está modificando un contacto existente
        int posicionModificada = getIntent().getIntExtra("posicionModificada", -1);

        // Creo un Intent para devolver los resultados
        Intent resultIntent = new Intent();

        if (posicionModificada != -1) {
            // Modifico el contacto existente y devuelvo la posición modificada
            resultIntent.putExtra("posicionModificada", posicionModificada);
        }

        // Devuelvo datos adicionales
        resultIntent.putExtra("nombre", nombre);
        resultIntent.putExtra("telefono", telefono);
        resultIntent.putExtra("imagenUri", imagenUri != null ? imagenUri.toString() : "");

        // Accedo a la lista de contactos
        List<Contactos> contactosList = MainActivity.lst;
        // Guardo la lista de contactos
        ContactPreferences.saveContactList(this, contactosList);

        // Configuro el resultado y cierro MainActivity2
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Método para obtener la URI de la imagen seleccionada
    private Uri obtenerUriImagenSeleccionada() {
        // Devuelve la URI almacenada en la variable de instancia
        return imagenUriSeleccionada;
    }

}