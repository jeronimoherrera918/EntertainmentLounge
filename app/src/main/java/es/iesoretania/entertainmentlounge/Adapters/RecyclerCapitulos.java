package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerCapitulos extends RecyclerView.Adapter<RecyclerCapitulos.ViewHolder> {
    private List<Capitulo> listaCapitulos;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;

    public RecyclerCapitulos(List<Capitulo> listaCapitulos, Context context) {
        this.listaCapitulos = listaCapitulos;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listaCapitulos.size();
    }

    @Override
    public RecyclerCapitulos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_capitulos, null);
        return new RecyclerCapitulos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerCapitulos.ViewHolder holder, final int position) {
        holder.bindData(listaCapitulos.get(position));
    }

    public void setItems(List<Capitulo> listaCapitulos) {
        this.listaCapitulos = listaCapitulos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerNombreCapitulo, recyclerPuntuacionMediaCapitulo;
        ImageButton recyclerbtnMarcarComoVisto;

        public ViewHolder(View v) {
            super(v);
            recyclerNombreCapitulo = v.findViewById(R.id.recyclerNombreCapitulo);
            recyclerPuntuacionMediaCapitulo = v.findViewById(R.id.recyclerPuntuacionMediaCapitulo);
            recyclerbtnMarcarComoVisto = v.findViewById(R.id.recyclerbtnMarcarComoVisto);
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

        public void bindData(final Capitulo capitulo) {
            recyclerNombreCapitulo.setText(capitulo.getNombre());
            recyclerPuntuacionMediaCapitulo.setText(capitulo.getPuntuacion().toString());
            recyclerbtnMarcarComoVisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerbtnMarcarComoVisto.setImageResource(R.drawable.googleg_standard_color_18);
                    Toast.makeText(context, "Capítulo visto (PLACEHOLDER)", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(RecyclerCapitulos.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
