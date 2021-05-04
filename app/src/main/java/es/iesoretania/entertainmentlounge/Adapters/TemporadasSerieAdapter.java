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

import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.R;

public class TemporadasSerieAdapter extends ArrayAdapter {
    Context ctx;
    int layoutTemplate;
    List<Temporada> listaTemporadas;
    TextView tvAdapterTemporada;

    public TemporadasSerieAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        listaTemporadas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);
        Temporada temporada = listaTemporadas.get(position);
        tvAdapterTemporada = v.findViewById(R.id.tvAdapterTemporada);
        tvAdapterTemporada.setText("Temporada " + (position + 1) + "\n");
        return v;
    }
}
