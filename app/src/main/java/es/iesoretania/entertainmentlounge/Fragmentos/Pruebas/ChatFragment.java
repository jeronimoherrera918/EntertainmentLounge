package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class ChatFragment extends Fragment {
    String keyUser;
    EditText etMensajeUsuario;
    Button btnEnviarMensajeUsuario;
    // ----- //
    DocumentReference newRef;
    String newKey;
    // ----- //
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        etMensajeUsuario = view.findViewById(R.id.etMensajeUsuario);
        btnEnviarMensajeUsuario = view.findViewById(R.id.btnEnviarMensajeUsuario);

        if (getArguments() != null) {
            ChatFragmentArgs chatFragmentArgs = ChatFragmentArgs.fromBundle(getArguments());
            keyUser = chatFragmentArgs.getKeyUser();
        }

        btnEnviarMensajeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRef = db.collection("mensajes").document();
                newKey = newRef.getId();
                Mensaje mensaje = new Mensaje();
                mensaje.setFecha(Timestamp.now());
                mensaje.setMensaje(etMensajeUsuario.getText().toString());
                mensaje.setIdEmisor(UserData.ID_USER_DB);
                mensaje.setIdReceptor(keyUser);
                mensaje.setIdMensaje(newKey);
                newRef.set(mensaje);
            }
        });
    }


}