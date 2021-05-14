package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    // ---------------- //
    SaveSerie saveSerieGlobal;
    DocumentSnapshot dn;
    // ---------------- //
    FloatingActionButton fabGuardarCambios;
    private int sw = 0;
    ClickListener clickListener;
    FirebaseFirestore db;

    public RecyclerCapitulos(List<Capitulo> listaCapitulos, Context context, String idSerie, int nTemporada, FloatingActionButton fabGuardarCambios) {
        this.listaCapitulos = listaCapitulos;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.idSerie = idSerie;
        this.nTemporada = nTemporada;
        this.fabGuardarCambios = fabGuardarCambios;
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
                            dn = task.getResult().getDocuments().get(0);
                            saveSerieGlobal = dn.toObject(SaveSerie.class);
                            if (saveSerieGlobal.getTemporadas().get(nTemporada).getCapitulos_vistos().get(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString())) == 1) {
                                recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_true);
                            }
                        }
                    }
                }
            });

            recyclerbtnMarcarComoVisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sw == 0) {
                        sw = 1;
                        fabGuardarCambios.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }

                    for (int i = 0; i < saveSerieGlobal.getTemporadas().get(nTemporada).getCapitulos_vistos().size(); i++) {
                        if (Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()) == i) {
                            switch (saveSerieGlobal.getTemporadas().get(nTemporada).getCapitulos_vistos().get(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()))) {
                                case 1: {
                                    recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_false);
                                    saveSerieGlobal.getTemporadas().get(nTemporada).getCapitulos_vistos().set(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()), 0);
                                }
                                break;
                                case 0: {
                                    recyclerbtnMarcarComoVisto.setImageResource(R.drawable.ic_visto_true);
                                    saveSerieGlobal.getTemporadas().get(nTemporada).getCapitulos_vistos().set(Integer.parseInt(recyclerbtnMarcarComoVisto.getTag().toString()), 1);
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
            });

            fabGuardarCambios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sw = 0;
                    db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").document(dn.getId()).set(saveSerieGlobal);
                    fabGuardarCambios.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
                    Snackbar.make(v, "Cambios guardados correctamente", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.DKGRAY).show();
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
