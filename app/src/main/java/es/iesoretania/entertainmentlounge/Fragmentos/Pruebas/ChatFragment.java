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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Chat;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class ChatFragment extends Fragment {
    String keyUser;
    EditText etMensajeUsuario;
    TextView tvEnviarMensajeA;
    Button btnEnviarMensajeUsuario;
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
        tvEnviarMensajeA = view.findViewById(R.id.tvEnviarMensajeA);
        if (getArguments() != null) {
            ChatFragmentArgs chatFragmentArgs = ChatFragmentArgs.fromBundle(getArguments());
            keyUser = chatFragmentArgs.getKeyUser();
        }
        db.collection("usuarios").document(keyUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Usuario usuario = task.getResult().toObject(Usuario.class);
                tvEnviarMensajeA.setText("Enviar mensaje a " + usuario.getNickname());
            }
        });

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").whereEqualTo("idReceptor", keyUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        Chat chat = new Chat();
                        chat.setIdReceptor(keyUser);
                        db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").document().set(chat);
                    }
                }
            }
        });

        btnEnviarMensajeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").whereEqualTo("idReceptor", keyUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot dn = task.getResult().getDocuments().get(0);
                            Chat chat = dn.toObject(Chat.class);
                            Mensaje mensaje = new Mensaje();
                            mensaje.setIdReceptor(keyUser);
                            mensaje.setIdEmisor(UserData.ID_USER_DB);
                            mensaje.setMensaje(etMensajeUsuario.getText().toString());
                            mensaje.setIdMensaje("s"); // TODO: MODIFICAR ESTO PARA QUE COJA LA ID PARA UN DOCUMENTO
                            mensaje.setFecha(Timestamp.now());
                            chat.getListaMensajes().add(mensaje);
                            db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").document(dn.getId()).set(chat);
                        }
                    }
                });
            }
        });
    }


}