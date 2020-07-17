package proyecto.com.pe.hiperbodega;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;

import proyecto.com.pe.hiperbodega.datos.ConexionSQLite;
import proyecto.com.pe.hiperbodega.logica.Sesion;
import proyecto.com.pe.hiperbodega.ui.m_delivery.MapaRuta;
import proyecto.com.pe.hiperbodega.util.Funciones;
import proyecto.com.pe.hiperbodega.util.ServiciosWeb;
import proyecto.com.pe.hiperbodega.util.SharedPreferences;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab_android;
    EditText txtNroDoc, txtClave;
    TextView btnIniciarSesion;
    CheckBox chkRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtNroDoc = findViewById(R.id.c_txtDni);
        txtClave = findViewById(R.id.c_txtPassword);
        chkRecordar = findViewById(R.id.login_chkRecordar);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        btnIniciarSesion.setOnClickListener(this);


        /*Leer los datos guardados en el SharedPreferences*/
        SharedPreferences preferences = SharedPreferences.getInstance(getApplicationContext());
        boolean login_recordar = preferences.getBoolean("login_recordar", false);
        if (login_recordar){
            chkRecordar.setChecked(true);
            txtNroDoc.setText(preferences.getString("login_nro_doc",""));
            txtClave.setText(preferences.getString("login_clave",""));
            new SesionTask(txtNroDoc.getText().toString(), txtClave.getText().toString()).execute();
        }
        /*Leer los datos guardados en el SharedPreferences*/
    }


    @Override
    public void onClick(View v) {
        new SesionTask(txtNroDoc.getText().toString(), txtClave.getText().toString()).execute();
    }

    private class SesionTask extends AsyncTask<Void, Void, String> {
        //Esta clase nos permitirá acceder al servicio web denominado: "sesion.validar.php"
        ProgressDialog pDialog;
        private String nroDoc;
        private String clave;

        public SesionTask(String nroDoc, String clave) {
            this.nroDoc = nroDoc;
            this.clave = clave;
        }

        @Override
        protected void onPreExecute() {
            //Se utiliza para realizar las configuraciones iniciales, antes de ejecutrar el servicio web
            super.onPreExecute();

            pDialog = new ProgressDialog(ActivityLogin.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Validando credenciales");
            pDialog.setCancelable(false); //Significa que una vez que inicia la ejecución de la tarea, no es posible cancelarla
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            //Permite ejecutar/llamar al servicio web: "sesion.validar.php"
            String resultadoWS="";

            try{
                String URLServicioWeb = ServiciosWeb.URL_WS + "sesion.validar.php";
                HashMap parametros = new HashMap<String,String>();
                parametros.put("dniCliente", this.nroDoc);
                parametros.put("clave", Funciones.hashMD5(this.clave));
                resultadoWS = new ServiciosWeb().openWebServiceFormData(URLServicioWeb, parametros);
            }catch (Exception e){
                e.printStackTrace();
            }
            return resultadoWS;
        }

        @Override
        protected void onPostExecute(String resultadoWS) {
            //Recibe el resultado del método doInBackground
            super.onPostExecute(resultadoWS);

            Log.e("Resultado", resultadoWS);

            if ( ! resultadoWS.isEmpty()){ //Si el resultado del servicio web no esta vacio
                try{
                    JSONObject json = new JSONObject(resultadoWS);
                    String mensaje = json.getString("mensaje");
                    Integer estado = json.getInt("estado");

                    if (estado != 200){
                        Toast.makeText(ActivityLogin.this, mensaje, Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject jsonDatos = json.getJSONObject("datos");
                        Sesion.DNI = jsonDatos.getString("dniCliente");
                        Sesion.NOMBRES = jsonDatos.getString("nombres");
                        Sesion.TOKEN = jsonDatos.getString("token");
                        Toast.makeText(ActivityLogin.this, mensaje + " " + Sesion.NOMBRES, Toast.LENGTH_SHORT).show();

                        /*Guardar los datos de la sesión. En el caso de que se active el Check: Recordar datos*/
                        SharedPreferences preferences = SharedPreferences.getInstance(getApplicationContext());

                        if (chkRecordar.isChecked()){
                            preferences.saveBoolean("login_recordar", true);
                            preferences.saveString("login_nro_doc", this.nroDoc);
                            preferences.saveString("login_clave", this.clave);
                        }else{
                            preferences.clearAll();
                        }
                        /*Guardar los datos de la sesión. En el caso de que se active el Check: Recordar datos*/


                        ActivityLogin.this.finish();
                        Intent intent = new Intent(ActivityLogin.this, MenuPrincipal.class);
                        startActivity(intent);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    pDialog.dismiss();
                }
            }
        }
    }
}