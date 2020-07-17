package proyecto.com.pe.hiperbodega;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import proyecto.com.pe.hiperbodega.adaptador.ListadoBodegaAdaptador;
import proyecto.com.pe.hiperbodega.adaptador.ListadoProductoAdaptador;
import proyecto.com.pe.hiperbodega.datos.ConexionSQLite;
import proyecto.com.pe.hiperbodega.logica.Bodega;
import proyecto.com.pe.hiperbodega.logica.Sesion;
import proyecto.com.pe.hiperbodega.util.ServiciosWeb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListadoBodegasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListadoBodegasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<Bodega> listaBodegas;
    public ArrayList<Bodega> listaFiltrada = new ArrayList<Bodega>();
    public ListadoBodegaAdaptador adaptador;
    RecyclerView recyclerViewBodega;
    public Button btnFiltrar;
    public Spinner spDistritoBusqueda;
    public ProgressDialog dialog;

    boolean usuarioTocaPantalla;
    private Context context;

    //Declarar el control SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    public ListadoBodegasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListadoBodegasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListadoBodegasFragment newInstance(String param1, String param2) {
        ListadoBodegasFragment fragment = new ListadoBodegasFragment();
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
        View view = inflater.inflate(R.layout.fragment_listado_bodegas, container, false);

        //enlazar los controles xml con los java
        recyclerViewBodega = view.findViewById(R.id.bodegaRecyclerView);
        recyclerViewBodega.setHasFixedSize(true);
        recyclerViewBodega.setLayoutManager(new LinearLayoutManager(this.getContext()));


        //Agregar un titulo al fragment
        this.getActivity().setTitle("Listado de Bodegas");

        btnFiltrar = view.findViewById(R.id.btnFiltrar);
        spDistritoBusqueda = view.findViewById(R.id.spDsitritoBusqueda);

        //Enlazar el control swipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.srlBodega);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        //Enlazar el fragment con el adaptador
        adaptador = new ListadoBodegaAdaptador(this.getContext());

        //indicarle al recyclerView el adaptador que mostrará el listado
        recyclerViewBodega.setAdapter(adaptador);


        //Llamar al método listar()
        listar();

        //cargar datos al spProductoFiltro
        //this.cargarSpinnerBusqueda();

        // on item list clicked
        adaptador.setOnItemClickListener(new ListadoBodegaAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Bodega obj, int position) {
                Fragment fragment = new ListadoProductosFragment();
                Bundle bundle = new Bundle();
                //int codigo = listaBodegas.get(position).getIdBodega();
                String nombre = Bodega.ListaBodega.get(adaptador.posicionItemSeleccionado).getRazonSocial();
                /*Toast.makeText(ListadoBodegasFragment.this.getContext(), "ID:" + codigo, Toast.LENGTH_SHORT).show();*/
                Toast.makeText(ListadoBodegasFragment.this.getActivity(), "Nombre:" + nombre, Toast.LENGTH_SHORT).show();
                Log.e("bodega",nombre );
                /*
                bundle.putInt("idBodega", codigo);
                bundle.putString("nombreBodega", nombre);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });






        return view;
    }

    private void listar(){
        //Llamar a la clase ListarClienteTask para ejecutar la descarga de datos desde el servicio web
        new ListadoBodegaTask().execute();
    }

    private class ListadoBodegaTask extends AsyncTask<Void, Void, String>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //Pre-configuración de la tarea
            super.onPreExecute();
            dialog = new ProgressDialog(ListadoBodegasFragment.this.getContext());
            dialog.setTitle("Obteniendo lista de bodegas");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            //Llaada al servicio web
            String resultadoWS="";
            try {
                String URLServicioWeb = ServiciosWeb.URL_WS + "bodega.listar.php";
                HashMap parametros = new HashMap<String,String>();
                parametros.put("token", Sesion.TOKEN);
                //parametros.put("cliente_id", Sesion.CLIENTE_ID);
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
                        Toast.makeText(ListadoBodegasFragment.this.getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }else { //Devuelve 200 (ok)
                        JSONArray jsonArray = new JSONArray(datos);

                        Bodega.ListaBodega.clear(); //Limpiar los elementos de la lista

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonCuota = jsonArray.getJSONObject(i);
                            //Instanciar a la clase producto
                            Bodega obj = new Bodega();

                            obj.setIdBodega(jsonCuota.getInt("idBodega"));
                            obj.setRuc(jsonCuota.getString("ruc"));
                            obj.setRazonSocial(jsonCuota.getString("razonSocial"));
                            obj.setDireccion(jsonCuota.getString("direccion"));
                            obj.setEstado(jsonCuota.getString("estado"));
                            obj.setFoto(jsonCuota.getString("foto_bodega"));
                            obj.setNombreDistrito(jsonCuota.getString("descripcion"));
                            Bodega.ListaBodega.add(obj);

                        }
                        adaptador.cargarDatosArrayList(Bodega.ListaBodega);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    dialog.dismiss(); //Cerrar el dialog (Obteniendo lista de cuotas)
                }
            }
        }
    }



    @Override
    public void onRefresh() {
        listar();
        swipeRefreshLayout.setRefreshing(false);
    }
}