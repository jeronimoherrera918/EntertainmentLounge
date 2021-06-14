package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerUsuarios extends RecyclerView.Adapter<RecyclerUsuarios.ViewHolder> {
    private List<Usuario> listaUsuarios;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    public RecyclerUsuarios(List<Usuario> listaUsuarios, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    @Override
    public RecyclerUsuarios.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_usuario_final, null);
        return new RecyclerUsuarios.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerUsuarios.ViewHolder holder, final int position) {
        holder.bindData(listaUsuarios.get(position));
    }

    public void setItems(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUserChat;
        ImageView imgChatPerfilUser;

        public ViewHolder(View v) {
            super(v);
            tvUserChat = v.findViewById(R.id.tvUserChat);
            imgChatPerfilUser = v.findViewById(R.id.imgChatPerfilUser);
            db = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();

            if (clickListener != null) {
                v.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
        }

        void bindData(final Usuario usuario) {
            tvUserChat.setText(usuario.getNickname());
            StorageReference storageReference = firebaseStorage.getReference().child(usuario.getFotoPerfil());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(imgChatPerfilUser.getContext()).load(uri).into(imgChatPerfilUser);
                }
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(RecyclerUsuarios.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
