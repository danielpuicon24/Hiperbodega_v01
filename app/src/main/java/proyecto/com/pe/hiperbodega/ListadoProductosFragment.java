package proyecto.com.pe.hiperbodega;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import proyecto.com.pe.hiperbodega.adaptador.ListadoProductoAdaptador;
import proyecto.com.pe.hiperbodega.logica.Producto;
import proyecto.com.pe.hiperbodega.logica.Sesion;
import proyecto.com.pe.hiperbodega.util.ServiciosWeb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListadoProductosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListadoProductosFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerView ProductoRecyclerView;
    Button ProductoRegistrar;
    EditText txtCodigo, txtNombre;
    public SwipeRefreshLayout swipeListaProducto;
    public ArrayList<Producto> listaEvento;
    public ListadoProductoAdaptador adaptadorProducto;
    public String idBodega, nombreBodega;
    public ListadoProductosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListadoProductosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListadoProductosFragment newInstance(String param1, String param2) {
        ListadoProductosFragment fragment = new ListadoProductosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listado_productos, container, false);

        //Agregar un titulo al fragment
        this.getActivity().setTitle("Listado de Eventos Disponibles");

        //Enalazar los controles
        ProductoRegistrar = view.findViewById(R.id.productoBtnPagar);
        ProductoRegistrar.setOnClickListener(this);

        ProductoRecyclerView=view.findViewById(R.id.productoRecyclerView);
        ProductoRecyclerView.setHasFixedSize(true);
        ProductoRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        swipeListaProducto = view.findViewById(R.id.swipeListaProductos);
        swipeListaProducto.setColorSchemeColors(getResources().getColor(R.color.colorAccent)); //cambiar al color del tema
        swipeListaProducto.setOnRefreshListener(this);

        //Enlazar el fragment con el adaptador
        adaptadorProducto = new ListadoProductoAdaptador(this.getContext());

        //indicarle al recyclerView el adaptador que mostrará el listado
        ProductoRecyclerView.setAdapter(adaptadorProducto);

        txtNombre = view.findViewById(R.id.c_txtNombreBodega);
        txtCodigo = view.findViewById(R.id.c_txtCodigoBodega);


        Bundle arguments = getArguments();
        if(arguments!=null) {
            int codigo = arguments.getInt("idBodega");
            String nombre = arguments.getString("nombreBodega");
            txtCodigo.setText(codigo);
            txtNombre.setText(nombre);
            //Llmar al método listar()
            //listar();
        }


        //String description = getIntent().getStringExtra("description");

        return view;
    }

    private void listar(){
        //int idBodega = Sesion.CLIENTE_ID;
        new ListaProductoTask(ListadoProductosFragment.this.getContext(),txtCodigo.getText().toString()).execute();
    }

    @Override
    public void onClick(View v) {

    }

    public interface EventListener {
        void onEventName(String nombre);
    }

    @Override
    public void onRefresh() {
        listar();
        swipeListaProducto.setRefreshing(false);
    }

    private class ListaProductoTask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        private Context context;
        private String idBodega;

        public ListaProductoTask(Context context, String idBodega) {
            this.context = context;
            this.idBodega = idBodega;
        }

        @Override
        protected void onPreExecute() {
            //Pre-configuración de la tarea
            super.onPreExecute();
            dialog = new ProgressDialog(ListadoProductosFragment.this.getContext());
            dialog.setTitle("Obteniendo lista de Productos");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            //Llaada al servicio web
            String resultadoWS="";
            try {
                String URLServicioWeb = ServiciosWeb.URL_WS + "producto.listar.php";
                HashMap parametros = new HashMap<String,String>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("idBodega", this.idBodega);
                resultadoWS = new ServiciosWeb().openWebServiceFormData(URLServicioWeb, parametros);

            }catch (Exception e){
                e.printStackTrace();
            }
            return resultadoWS;
        }

        @Override
        protected void onPostExecute(String resultadoWS) {
            super.onPostExecute(resultadoWS);

            if (! resultadoWS.isEmpty()){
                try {
                    JSONObject json = new JSONObject(resultadoWS);
                    int estado = json.getInt("estado");
                    String mensaje = json.getString("mensaje");
                    String datos = json.getString("datos");
                    if(estado==500){
                        Toast.makeText(ListadoProductosFragment.this.getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }else { //Devuelve 200 (ok)
                        JSONArray jsonArray = new JSONArray(datos);

                        Producto.ListaProducto.clear(); //Limpiar los elementos de la lista

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonCuota = jsonArray.getJSONObject(i);
                            //Instanciar a la clase producto
                            Producto obj = new Producto();
                            obj.setIdProducto(jsonCuota.getInt("idProducto"));
                            obj.setIdBodega(jsonCuota.getInt("idBodega"));
                            obj.setIdCategoria(jsonCuota.getInt("idCategoria"));
                            obj.setDenominacion(jsonCuota.getString("denominacion"));
                            obj.setPresentacion(jsonCuota.getString("presentacion"));
                            obj.setPrecioVenta(jsonCuota.getDouble("precioVenta"));
                            obj.setRazonSocial(jsonCuota.getString("razonSocial"));
                            obj.setEscogerProducto(false);

                            Producto.ListaProducto.add(obj);
                        }
                        adaptadorProducto.cargarListaEventoAuxiliar(Producto.ListaProducto);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    dialog.dismiss(); //Cerrar el dialog (Obteniendo lista de cuotas)
                }
            }
        }
    }
}