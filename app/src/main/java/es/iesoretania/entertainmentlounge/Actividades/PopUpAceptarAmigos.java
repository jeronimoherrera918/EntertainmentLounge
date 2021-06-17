package es.iesoretania.entertainmentlounge.Actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.AgregarAmigosAdapter;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class PopUpAceptarAmigos extends AppCompatActivity {
    private ListView listaPendientes;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_aceptar_amigos);

        listaPendientes = findViewById(R.id.listaPendientes);
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Usuario usuario = task.getResult().toObject(Usuario.class);
                    List<String> solicitudes = usuario.getAmigosPendientes();
                    AgregarAmigosAdapter agregarAmigosAdapter = new AgregarAmigosAdapter(listaPendientes.getContext(), R.layout.adapter_aceptar_amigos, solicitudes);
                    listaPendientes.setAdapter(agregarAmigosAdapter);
                    listaPendientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            usuario.getListaAmigos().add(solicitudes.get(position));
                            db.collection("usuarios").document(UserData.ID_USER_DB).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        db.collection("usuarios").document(solicitudes.get(position)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Usuario user2 = task.getResult().toObject(Usuario.class);
                                                    user2.getListaAmigos().add(UserData.ID_USER_DB);
                                                    db.collection("usuarios").document(task.getResult().getId()).set(user2);
                                                    usuario.getAmigosPendientes().remove(solicitudes.get(position));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
