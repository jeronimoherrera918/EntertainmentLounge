package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerSeries extends RecyclerView.Adapter<RecyclerSeries.ViewHolder> {
    private List<Serie> listaSeries;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;

    public RecyclerSeries(List<Serie> listaSeries, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listaSeries = listaSeries;
    }

    @Override
    public int getItemCount() {
        return listaSeries.size();
    }

    @Override
    public RecyclerSeries.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_series, null);
        return new RecyclerSeries.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerSeries.ViewHolder holder, final int position) {
        holder.bindData(listaSeries.get(position));
    }

    public void setItems(List<Serie> listaSeries) {
        this.listaSeries = listaSeries;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerNombreSerie, recyclerGeneroSerie;

        public ViewHolder(View v) {
            super(v);
            recyclerNombreSerie = v.findViewById(R.id.recyclerNombreSerie);
            recyclerGeneroSerie = v.findViewById(R.id.recyclerGeneroSerie);
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

        public void bindData(final Serie serie) {
            recyclerNombreSerie.setText(serie.getNombre());
            recyclerGeneroSerie.setText(serie.getGenero());
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
