package proyecto.com.pe.hiperbodega.adaptador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import proyecto.com.pe.hiperbodega.ListadoBodegasFragment;
import proyecto.com.pe.hiperbodega.ListadoProductosFragment;
import proyecto.com.pe.hiperbodega.MenuPrincipal;
import proyecto.com.pe.hiperbodega.R;
import proyecto.com.pe.hiperbodega.logica.Bodega;
import proyecto.com.pe.hiperbodega.logica.Sesion;
import proyecto.com.pe.hiperbodega.util.ServiciosWeb;

public class ListadoBodegaAdaptador extends RecyclerView.Adapter<ListadoBodegaAdaptador.ViewHolder> {
    private Context context;
    public static ArrayList<Bodega> listaBodega;
    public ProgressDialog dialog;
    public int posicionItemSeleccionado;

    private OnItemClickListener mOnItemClickListener;

    public ListadoBodegaAdaptador(Context context) {
        this.context = context;
        this.listaBodega = new ArrayList<Bodega>();

    }

    public  int obtenerPosicionIemSeleccionado(){
        return this.posicionItemSeleccionado;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //enlaza el adaptador y el xml del cardview.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bodegas_cardview,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Mostrar los datos que esta en la lista, en cada control del cardview
        //txtNombre, txtFechaReal, txtFechaMax, txtPrecioIns, txtPrecioCe, txtVacantes, txtTipo;
        Bodega bodega = listaBodega.get(position);

        holder.txtRazonSocial.setText(bodega.getRazonSocial());
        holder.txtDistrito.setText(bodega.getNombreDistrito());
        holder.txtDireccion.setText(bodega.getDireccion());
        holder.txtCodigo.setText(String.valueOf(bodega.getIdBodega()));
        holder.imgBodegaLista.setTag(bodega.getFoto());

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Bodega obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return listaBodega.size();
    }

    public void cargarDatosArrayList (ArrayList<Bodega> lista){
        listaBodega = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //Declarar los controles del card view
        //ImageView imgCliente;
        TextView txtRazonSocial, txtDireccion, txtDistrito, txtCodigo ;
        ImageButton btnVerProductos;
        ImageView imgBodegaLista;
        CardView cardview;
        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Enlazar los controles
            txtRazonSocial = itemView.findViewById(R.id.c_txtRucRazonSocial);
            txtDireccion = itemView.findViewById(R.id.c_txtDireccion);
            txtDistrito = itemView.findViewById(R.id.c_txtDistrito);
            txtCodigo = itemView.findViewById(R.id.c_txtIDBodega);
            imgBodegaLista = itemView.findViewById(R.id.imgBodegaLista);
            cardview = (CardView)itemView.findViewById(R.layout.bodegas_cardview);


            btnVerProductos = itemView.findViewById(R.id.btnVerProductos);
            btnVerProductos.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
/*
            ListadoProductosFragment f1 = new ListadoProductosFragment();
            Bundle bundle = new Bundle();
            int codigo = listaBodega.get(posicionItemSeleccionado).getIdBodega();
            String nombre = listaBodega.get(posicionItemSeleccionado).getRazonSocial();
            bundle.putInt("idBodega", codigo);
            bundle.putString("nombreBodega", nombre);
            f1.setArguments(bundle);

            Intent i = new Intent (v.getContext(), ListadoProductosFragment.class);
            i.putExtra("idBodega", String.valueOf(listaBodega.get(posicionItemSeleccionado).getIdBodega()));
            i.putExtra("nombreBodega", listaBodega.get(posicionItemSeleccionado).getRazonSocial());
            v.getContext().startActivity(i);*/

            ListadoProductosFragment fragment = new ListadoProductosFragment();
            Bundle bundle = new Bundle();
            int codigo = listaBodega.get(posicionItemSeleccionado).getIdBodega();
            String nombre = listaBodega.get(posicionItemSeleccionado).getRazonSocial();
            bundle.putInt("idBodega", codigo);
            bundle.putString("nombreBodega", nombre);
            fragment.setArguments(bundle);

            androidx.fragment.app.FragmentTransaction transaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedor, fragment);
            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        @Override
        public boolean onLongClick(View v) {
            //Permite almacenar la posición del item seleccionado en el RecylcerView
            posicionItemSeleccionado = this.getAdapterPosition();
            return false;
        }
    }
}
