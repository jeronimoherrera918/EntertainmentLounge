package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerComentarios extends RecyclerView.Adapter<RecyclerComentarios.ViewHolder> {
    private List<Comentario> listaComentarios;
    private LayoutInflater layoutInflater;
    private Context context;
    private FirebaseFirestore db;

    public RecyclerComentarios(List<Comentario> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    @Override
    public RecyclerComentarios.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_comentarios, null);
        return new RecyclerComentarios.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerComentarios.ViewHolder holder, final int position) {
        holder.bindData(listaComentarios.get(position));
    }

    public void setItems(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerComentarioEmisor, recyclerComentarioMensaje;

        public ViewHolder(View v) {
            super(v);
            recyclerComentarioEmisor = v.findViewById(R.id.recyclerComentarioEmisor);
            recyclerComentarioMensaje = v.findViewById(R.id.recyclerComentarioMensaje);
            db = FirebaseFirestore.getInstance();
        }

        @Override
        public void onClick(View v) {

        }

        public void bindData(final Comentario comentario) {
            db.collection("usuarios").document(comentario.getId_usuario()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Usuario usuario = task.getResult().toObject(Usuario.class);
                    recyclerComentarioEmisor.setText(usuario.getNickname());
                    recyclerComentarioMensaje.setText(comentario.getComentario());
                }
            });
        }
    }
}
