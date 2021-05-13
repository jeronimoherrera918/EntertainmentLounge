package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveTemporadaSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerCapitulos extends RecyclerView.Adapter<RecyclerCapitulos.ViewHolder> {
    private List<Capitulo> listaCapitulos;
    private LayoutInflater layoutInflater;
    private Context context;
    private String idSerie;
    private int nTemporada;
    ClickListener clickListener;
    FirebaseFirestore db;

    public RecyclerCapitulos(List<Capitulo> listaCapitulos, Context context, String idSerie, int nTemporada) {
        this.listaCapitulos = listaCapitulos;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.idSerie = idSerie;
        this.nTemporada = nTemporada;
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
        holder.recyclerbtnMarcarComoVisto.setTag(position);
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
            db = FirebaseFirestore.getInstance();

            if (clickListener != null) {
                v.setOnClickListener(this);
            }

            db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", idSerie).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot dn = task.getResult().getDocuments().get(0);
                            SaveSerie saveSerie = dn.toObject(SaveSerie.class);
                            if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString())) == 1) {
                                recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_true);
                            }
                            recyclerbtnMarcarComoVisto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().size(); i++) {
                                        if (Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()) == i) {
                                            System.out.println(recyclerbtnMarcarComoVisto.getTag().toString());
                                            switch (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()))) {
                                                case 1: {
                                                    recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_false);
                                                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()), 0);
                                                }
                                                break;
                                                case 0: {
                                                    recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_true);
                                                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()), 1);
                                                }
                                                break;
                                            }
                                            db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").document(dn.getId()).set(saveSerie);
                                            break;
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
        }

        public void bindData(final Capitulo capitulo) {
            recyclerNombreCapitulo.setText(capitulo.getNombre());
            recyclerPuntuacionMediaCapitulo.setText("Puntuación media: " + capitulo.getPuntuacion().toString());
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(RecyclerCapitulos.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
