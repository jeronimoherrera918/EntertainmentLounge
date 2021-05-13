package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerChat extends RecyclerView.Adapter<RecyclerChat.ViewHolder> {
    private List<Mensaje> listaMensajes;
    private LayoutInflater layoutInflater;
    private Context context;
    RecyclerChat.ClickListener clickListener;

    public RecyclerChat(List<Mensaje> listaMensajes, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listaMensajes = listaMensajes;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    @Override
    public RecyclerChat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_chat, null);
        return new RecyclerChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerChat.ViewHolder holder, final int position) {
        holder.bindData(listaMensajes.get(position));
    }

    public void setItems(List<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recyclerMensajeChat, recyclerEmisorChat, recyclerReceptorChat;

        public ViewHolder(View v) {
            super(v);
            recyclerMensajeChat = v.findViewById(R.id.recyclerMensajeChat);
            recyclerEmisorChat = v.findViewById(R.id.recyclerEmisorChat);
            recyclerReceptorChat = v.findViewById(R.id.recyclerReceptorChat);
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

        public void bindData(final Mensaje mensaje) {
            // TODO: ORDENAR POR FECHA LOS MENSAJES
            // TODO: PONER FECHA AL MOSTRAR EL MENSAJE

            recyclerMensajeChat.setText(mensaje.getMensaje());
            recyclerEmisorChat.setText("EMISOR: " + mensaje.getIdEmisor());
            recyclerReceptorChat.setText("RECEPTOR: " + mensaje.getIdReceptor());
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
