package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

import static android.app.Activity.RESULT_OK;

public class ModificarDatosFragment extends Fragment {
    EditText etModFecha, etModNickname, etModNombreCompleto;
    FloatingActionButton fabGuardarCambiosUsuario;
    ImageView imgModFotoPerfil;
    Button btnModSubirFoto;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    Usuario infoUsuario;
    static final int GALLERY_INTENT = 1;
    Bitmap bitmapImage;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modificar_datos, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etModNickname = view.findViewById(R.id.etModNickname);
        etModNombreCompleto = view.findViewById(R.id.etModNombreCompleto);
        etModFecha = view.findViewById(R.id.etModFecha);
        fabGuardarCambiosUsuario = view.findViewById(R.id.fabGuardarCambiosUsuario);
        imgModFotoPerfil = view.findViewById(R.id.imgModFotoPerfil);
        btnModSubirFoto = view.findViewById(R.id.btnModSubirFoto);
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        db.collection("usuarios").document(UserData.ID_USER_DB).get().addOnCompleteListener(recogerUsuario -> {
            if (recogerUsuario.isSuccessful()) {
                infoUsuario = recogerUsuario.getResult().toObject(Usuario.class);
                etModNickname.setText(infoUsuario.getNickname());
                etModNombreCompleto.setText(infoUsuario.getNombre_completo());
                etModFecha.setText(infoUsuario.getFechaNacimiento());

                StorageReference storageReference = firebaseStorage.getReference().child(infoUsuario.getFotoPerfil());
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getContext()).load(uri).into(imgModFotoPerfil));

                btnModSubirFoto.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_INTENT);
                });

                fabGuardarCambiosUsuario.setOnClickListener(v -> {
                    infoUsuario.setNickname(etModNickname.getText().toString());
                    infoUsuario.setNombre_completo(etModNombreCompleto.getText().toString());
                    infoUsuario.setFechaNacimiento(etModFecha.getText().toString());
                    db.collection("usuarios").document(UserData.ID_USER_DB).set(infoUsuario).addOnCompleteListener(guardarCambios -> {
                        if (guardarCambios.isSuccessful()) {
                            Snackbar.make(v, "Cambios guardados correctamente", Snackbar.LENGTH_SHORT).show();
                            UserData.USUARIO = infoUsuario;
                            StorageReference storageReference2 = firebaseStorage.getReference().child("usuarios/" + UserData.USUARIO.getEmail() + ".jpg");
                            storageReference2.putFile(uri).addOnSuccessListener(taskSnapshot -> System.out.println("Foto subida correctamente"));
                        }
                    });
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            bitmapImage = null;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imgModFotoPerfil.setImageBitmap(bitmapImage);
                infoUsuario.setFotoPerfil("usuarios/" + UserData.USUARIO.getEmail() + ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}