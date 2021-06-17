package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class AgregarAmigosAdapter extends ArrayAdapter {
    private Context ctx;
    private int layoutTemplate;
    private List<String> amigosPendientes;
    private TextView tvInfoAgregarAmigo;
    private FirebaseFirestore db;

    public AgregarAmigosAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        amigosPendientes = objects;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);
        String idUsuario = amigosPendientes.get(position);
        tvInfoAgregarAmigo = v.findViewById(R.id.tvInfoAgregarAmigo);
        db.collection("usuarios").document(idUsuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Usuario usuario = task.getResult().toObject(Usuario.class);
                    String info = "Nombre de usuario: " +  usuario.getNickname() + " - Pulsa aquí para aceptar";
                    tvInfoAgregarAmigo.setText(info);
                }
            }
        });
        return v;
    }
}
