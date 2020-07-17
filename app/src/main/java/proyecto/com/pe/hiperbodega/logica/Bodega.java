package proyecto.com.pe.hiperbodega.logica;

import org.json.JSONObject;

import java.util.ArrayList;

public class Bodega {
    private int idBodega;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String estado;
    private double latitud;
    private double longitud;
    private int idDistrito;
    private String nombreDistrito;
    private String foto;

    public static ArrayList<Bodega> ListaBodega= new ArrayList<>();

    public int getIdBodega() { return idBodega; }

    public void setIdBodega(int idBodega) { this.idBodega = idBodega; }

    public String getRuc() { return ruc; }

    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getRazonSocial() { return razonSocial; }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public JSONObject generarJSONTotales(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("idBodega", this.getIdBodega());
            jsonObject.put("ruc", this.getRuc());
            jsonObject.put("razon_social", this.getRazonSocial());
            jsonObject.put("direccion", this.getDireccion());
            jsonObject.put("estado", this.getEstado());
            jsonObject.put("latitud", this.getLatitud());
            jsonObject.put("longitud", this.getLongitud());
            jsonObject.put("idDistrito", this.getIdDistrito());
            jsonObject.put("foto_bodega", this.getFoto());
            jsonObject.put("descripcion", this.getNombreDistrito());

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }


}
