package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerUsuarios;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class ChatsFragment extends Fragment {
    EditText etMensajesChat;
    Button btnEnviarMensaje;
    RecyclerView listRecyclerUsuarios;
    List<Usuario> listaUsuarios;
    List<String> listaUsuariosKeys;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etMensajesChat = view.findViewById(R.id.etMensajeChat);
        btnEnviarMensaje = view.findViewById(R.id.btnEnviarMensaje);
        listRecyclerUsuarios = view.findViewById(R.id.listRecyclerUsuarios);
        db = FirebaseFirestore.getInstance();

        db.collection("usuarios").whereNotEqualTo("email", UserData.USER_EMAIL).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listaUsuarios = new ArrayList<>();
                    listaUsuariosKeys = new ArrayList<>();
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        Usuario usuario = dn.toObject(Usuario.class);
                        listaUsuarios.add(usuario);
                        listaUsuariosKeys.add(dn.getId());
                    }
                    RecyclerUsuarios recyclerUsuarios = new RecyclerUsuarios(listaUsuarios, listRecyclerUsuarios.getContext());
                    recyclerUsuarios.setOnItemClickListener(new RecyclerUsuarios.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Navigation.findNavController(v).navigate(ChatsFragmentDirections.actionNavChatsToNavChat(listaUsuariosKeys.get(position)));
                        }
                    });
                    listRecyclerUsuarios.setHasFixedSize(true);
                    listRecyclerUsuarios.setLayoutManager(new LinearLayoutManager(listRecyclerUsuarios.getContext()));
                    listRecyclerUsuarios.setAdapter(recyclerUsuarios);
                }
            }
        });
    }
}