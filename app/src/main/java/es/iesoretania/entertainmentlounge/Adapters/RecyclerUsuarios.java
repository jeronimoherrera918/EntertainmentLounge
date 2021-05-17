package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerUsuarios extends RecyclerView.Adapter<RecyclerUsuarios.ViewHolder> {
    private List<Usuario> listaUsuarios;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;

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
        View view = layoutInflater.inflate(R.layout.adapter_usuario, null);
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
        TextView recyclerNombreUsuario, recyclerCorreoUsuario;

        public ViewHolder(View v) {
            super(v);
            recyclerNombreUsuario = v.findViewById(R.id.recyclerNombreUsuario);
            recyclerCorreoUsuario = v.findViewById(R.id.recyclerCorreoUsuario);
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
            recyclerNombreUsuario.setText(usuario.getNickname());
            recyclerCorreoUsuario.setText(usuario.getEmail());
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(RecyclerUsuarios.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
