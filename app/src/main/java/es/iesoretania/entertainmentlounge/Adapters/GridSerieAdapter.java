package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class GridSerieAdapter extends ArrayAdapter {
    Context ctx;
    int layoutTemplate;
    List<Serie> listaSeries;

    TextView tvAdapterGridSerieNombre, tvAdapterGridSerieGenero, tvAdapterGridSerieDescripcion;

    public GridSerieAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        listaSeries = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);

        Serie serie = listaSeries.get(position);
        tvAdapterGridSerieNombre = v.findViewById(R.id.tvAdapterGridSerieNombre);
        tvAdapterGridSerieGenero = v.findViewById(R.id.tvAdapterGridSerieGenero);
        tvAdapterGridSerieDescripcion = v.findViewById(R.id.tvAdapterGridSerieDescripcion);

        tvAdapterGridSerieNombre.setText(serie.getNombre());
        tvAdapterGridSerieGenero.setText(serie.getGenero());
        tvAdapterGridSerieDescripcion.setText(serie.getDescripcion());

        return v;
    }
}
