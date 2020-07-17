package proyecto.com.pe.hiperbodega.datos;

public class TablaDatos {
    /*! ============================ INICIO DDL ========================= CREATE, ALTER, DROP, TRUNCATE*/
    public static String tablaLogin = "CREATE TABLE IF NOT EXISTS LOGIN(DNI CHAR(8), CLAVE VARCHAR(50), ESTADO CHAR(1), FECHAHORAREGISTRO DATETIME)";


    /*============================ FIN DML ========================= CREATE, ALTER, DROP, TRUNCATE !*/


    /*!============================ INICIO DML ========================= SELECT, INSERT, UPDATE, DELETE*/

    public static String tablaLoginDatos[]={
            "insert into LOGIN values ('00000000', '-', '1', DATETIME(now));"
    };

    public static String eliminarTablaLogin ="DROP TABLE IF EXISTS LOGIN";

    /*============================ FIN DML ========================= SELECT, INSERT, UPDATE, DELETE !*/
}
