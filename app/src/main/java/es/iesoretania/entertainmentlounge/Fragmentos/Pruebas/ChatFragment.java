package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerChat;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Chat;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class ChatFragment extends Fragment {
    String nameReceptor;
    String keyUser;
    EditText etMensajeUsuario;
    TextView tvEnviarMensajeA;
    Button btnEnviarMensajeUsuario;
    RecyclerView listRecyclerMensajes;
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
        listRecyclerMensajes = view.findViewById(R.id.listRecyclerMensajes);

        if (getArguments() != null) {
            ChatFragmentArgs chatFragmentArgs = ChatFragmentArgs.fromBundle(getArguments());
            keyUser = chatFragmentArgs.getKeyUser();
        }

        db.collection("usuarios").document(keyUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Usuario usuario = task.getResult().toObject(Usuario.class);
                nameReceptor = usuario.getNickname();
                tvEnviarMensajeA.setText("Enviar mensaje a " + nameReceptor);
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

        db.collection("usuarios").document(keyUser).collection("chats").whereEqualTo("idReceptor", UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        Chat chat = new Chat();
                        chat.setIdReceptor(UserData.ID_USER_DB);
                        db.collection("usuarios").document(keyUser).collection("chats").document().set(chat);
                    }
                }
            }
        });

        // TODO: ORDENAR POR FECHA LOS MENSAJES
        // TODO: PONER FECHA AL MOSTRAR EL MENSAJE

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").whereEqualTo("idReceptor", keyUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot dn = task.getResult().getDocuments().get(0);
                    Chat chat = dn.toObject(Chat.class);
                    RecyclerChat recyclerChat = new RecyclerChat(chat.getListaMensajes(), listRecyclerMensajes.getContext());
                    listRecyclerMensajes.setHasFixedSize(true);
                    listRecyclerMensajes.setLayoutManager(new LinearLayoutManager(listRecyclerMensajes.getContext()));
                    listRecyclerMensajes.setAdapter(recyclerChat);
                }
            }
        });

        btnEnviarMensajeUsuario.setOnClickListener(new View.OnClickListener() {
            String keyMensaje = generarID(12);

            @Override
            public void onClick(View v) {
                String mensaje = etMensajeUsuario.getText().toString();
                etMensajeUsuario.setText("");
                db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").whereEqualTo("idReceptor", keyUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot dn = task.getResult().getDocuments().get(0);
                            Chat chat = dn.toObject(Chat.class);
                            chat.getListaMensajes().add(crearMensaje(keyMensaje, UserData.ID_USER_DB, keyUser, mensaje));
                            db.collection("usuarios").document(UserData.ID_USER_DB).collection("chats").document(dn.getId()).set(chat);
                        }
                    }
                });

                db.collection("usuarios").document(keyUser).collection("chats").whereEqualTo("idReceptor", UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot dn = task.getResult().getDocuments().get(0);
                            Chat chat = dn.toObject(Chat.class);
                            chat.getListaMensajes().add(crearMensaje(keyMensaje, UserData.ID_USER_DB, keyUser, mensaje));
                            db.collection("usuarios").document(keyUser).collection("chats").document(dn.getId()).set(chat);
                        }
                    }
                });
            }
        });
    }

    private Mensaje crearMensaje(String keyMensaje, String idEmisor, String idReceptor, String tMensaje) {
        Mensaje mensaje = new Mensaje();
        mensaje.setIdMensaje(keyMensaje);
        mensaje.setIdEmisor(idEmisor);
        mensaje.setIdReceptor(idReceptor);
        mensaje.setMensaje(tMensaje);
        mensaje.setFecha(Timestamp.now());
        return mensaje;
    }

    private String generarID(int tam) {
        String key = "";
        Random random = new Random();
        for (int i = 0; i < tam; i++) {
            char randomizedCharacter = (char) (random.nextInt(26) + 'A');
            key += randomizedCharacter;
        }
        return key;
    }
}