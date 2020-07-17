package proyecto.com.pe.hiperbodega.logica;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import proyecto.com.pe.hiperbodega.datos.ConexionSQLite;

public class Login extends ConexionSQLite {
    private String dni, clave, estado, fechahoraRegistro;

    public Login(String dni, String clave, String estado, String fechahoraRegistro) {
        this.dni = dni;
        this.clave = clave;
        this.estado = estado;
        this.fechahoraRegistro = fechahoraRegistro;
    }

    public Login() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechahoraRegistro() {
        return fechahoraRegistro;
    }

    public void setFechahoraRegistro(String fechahoraRegistro) {
        this.fechahoraRegistro = fechahoraRegistro;
    }

    public long agregarLoginSQLite(String dni, String clave, String estado, String fechahoraRegistro){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DNI", dni);
        values.put("CLAVE", clave);
        values.put("ESTADO", estado);
        values.put("FECHAHORAREGISTRO", fechahoraRegistro);
        long resultado = db.insert("LOGIN", null, values);
        db.close();
        return resultado;
    }
}
