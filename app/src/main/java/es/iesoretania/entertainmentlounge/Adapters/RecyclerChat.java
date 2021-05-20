package es.iesoretania.entertainmentlounge.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Mensaje;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class RecyclerChat extends RecyclerView.Adapter<RecyclerChat.ViewHolder> {
    private List<Mensaje> listaMensajes;
    private LayoutInflater layoutInflater;
    private Context context;
    ClickListener clickListener;

    public RecyclerChat(List<Mensaje> listaMensajes, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listaMensajes = listaMensajes;
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
        TextView recyclerMensajeChat, recyclerFechaChat;
        RelativeLayout rlMensaje;

        public ViewHolder(View v) {
            super(v);
            recyclerMensajeChat = v.findViewById(R.id.recyclerMensajeChat);
            recyclerFechaChat = v.findViewById(R.id.recyclerFechaChat);
            rlMensaje = v.findViewById(R.id.rlMensaje);
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
            recyclerMensajeChat.setText(mensaje.getMensaje());
            String formato = "dd-MM-yyyy - HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            recyclerFechaChat.setText(sdf.format(mensaje.getFecha().toDate()));
            if (!mensaje.getIdEmisor().equals(UserData.ID_USER_DB)) {
                rlMensaje.setBackgroundColor(Color.parseColor("#2887BE"));
                recyclerMensajeChat.setGravity(Gravity.LEFT);
                recyclerFechaChat.setGravity(Gravity.LEFT);
            } else {
                rlMensaje.setBackgroundColor(Color.parseColor("#1C587B"));
                recyclerMensajeChat.setGravity(Gravity.RIGHT);
                recyclerFechaChat.setGravity(Gravity.RIGHT);
            }
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
