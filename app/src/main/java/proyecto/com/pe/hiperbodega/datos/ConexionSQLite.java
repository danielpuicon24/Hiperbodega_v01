package proyecto.com.pe.hiperbodega.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLite extends SQLiteOpenHelper {
    private static String nombreBD = "bd_hiperbodega";
    private static int versionBD = 1;
    public static Context context;

    public ConexionSQLite(){
        super(context, nombreBD, null, versionBD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TablaDatos.tablaLogin);
        for(String insert : TablaDatos.tablaLoginDatos){
            db.execSQL(insert);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TablaDatos.eliminarTablaLogin);
    }
}
