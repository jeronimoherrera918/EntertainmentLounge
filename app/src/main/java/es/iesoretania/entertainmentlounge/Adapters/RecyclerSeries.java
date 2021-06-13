package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class RecyclerSeries extends RecyclerView.Adapter<RecyclerSeries.ViewHolder> {
    private List<Serie> listaSeries;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;

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
        View view = layoutInflater.inflate(R.layout.adapter_series_final, null);
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
        TextView nombreSerieCard;
        ImageView imagenSerieCard;
        ImageView imgLoadingSerie;

        public ViewHolder(View v) {
            super(v);

            db = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();

            nombreSerieCard = v.findViewById(R.id.nombreSerieCard);
            imagenSerieCard = v.findViewById(R.id.imagenSerieCard);
            imgLoadingSerie = v.findViewById(R.id.imgLoadingSerie);

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
            nombreSerieCard.setText(serie.getNombre().toUpperCase());
            StorageReference sr = firebaseStorage.getReference().child("series/" + serie.getId_serie() + ".jpg");
            sr.getDownloadUrl().addOnSuccessListener(uri -> {
                imgLoadingSerie.setVisibility(View.GONE);
                Glide.with(imagenSerieCard.getContext()).load(uri).into(imagenSerieCard);
                imagenSerieCard.setVisibility(View.VISIBLE);
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
