package proyecto.com.pe.hiperbodega.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import proyecto.com.pe.hiperbodega.R;
import proyecto.com.pe.hiperbodega.logica.Producto;

public class ListadoProductoAdaptador extends RecyclerView.Adapter<ListadoProductoAdaptador.ViewHolder>{
    public static ArrayList<Producto> listaProductoAuxiliar;
    private Context context;
    private int posicionItemSeleccionado;

    public ListadoProductoAdaptador(Context context) {
        this.context = context;
        this.listaProductoAuxiliar = new ArrayList<Producto>();
    }

    public void cargarListaEventoAuxiliar(ArrayList<Producto> lista){
        listaProductoAuxiliar = lista;
        notifyDataSetChanged(); //refrescar la lista
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Permite enlazar el archivo que contiene el cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Mostrar los datos que esta en la lista, en cada control del cardview
        //txtNombre, txtFechaReal, txtFechaMax, txtPrecioIns, txtPrecioCe, txtVacantes, txtTipo;
        Producto producto = listaProductoAuxiliar.get(position);

        holder.txtNombre.setText(producto.getDenominacion());
        holder.txtCodig.setText(String.valueOf(producto.getIdProducto()));
        holder.txtDescripcion.setText("1 " + producto.getPresentacion() + " = " + "S/ " + String.valueOf(producto.getPrecioVenta()));
        holder.txtSubtotal.setText("1 " + String.valueOf(producto.getSubtotal()));
        //holder.txtCodig.setText(String.valueOf(evento.getCod_evento()));


        if (producto.getEscogerProducto()){
            holder.btnAgregar.setEnabled(false);
            holder.btnQuitar.setEnabled(true);
        }else{
            holder.btnAgregar.setEnabled(true);
            holder.btnAgregar.setText("Agregar al ");
            holder.btnQuitar.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        //Permite obtener la cantidad total de elementos que tienen almacenado el ArrayList
        return listaProductoAuxiliar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNombre, txtCodig, txtDescripcion, txtSubtotal;
        Button btnAgregar, btnQuitar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Relacionar o enlazar los controles declarados en Java con los controles que estan en el cardview
            txtNombre           = itemView.findViewById(R.id.c_txtNombre);
            txtCodig        = itemView.findViewById(R.id.c_txtCodigo);
            txtDescripcion        = itemView.findViewById(R.id.c_txtDescripccion);
            txtSubtotal             = itemView.findViewById(R.id.c_txtSubtotalProducto);
            btnAgregar               = itemView.findViewById(R.id.c_btnAgregar);
            btnQuitar               = itemView.findViewById(R.id.c_btnQuitar);

            btnAgregar.setOnClickListener(this);
            btnQuitar.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
