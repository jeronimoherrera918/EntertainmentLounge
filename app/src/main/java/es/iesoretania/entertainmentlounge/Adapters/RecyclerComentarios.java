package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerComentarios extends RecyclerView.Adapter<RecyclerComentarios.ViewHolder> {
    private List<Comentario> listaComentarios;
    private LayoutInflater layoutInflater;
    private Context context;

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
        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void onClick(View v) {

        }

        public void bindData(final Comentario comentario) {

        }
    }
}
