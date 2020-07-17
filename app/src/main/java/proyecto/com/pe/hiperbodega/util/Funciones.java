package proyecto.com.pe.hiperbodega.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Funciones {

    static String currentPhotoPath;
    public static double latitud;
    public static double longitud;

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ((Activity) context).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public static String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageDecode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageDecode;
    }


    public static Bitmap base64ToImage(String imageString){
        //encode image to base64 string
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static void selectedItemSpinner(Spinner spinner, String itemSelection){
        int position = 0;
        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(itemSelection)){
                position = i;
                break;
            }
        }
        spinner.setSelection(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String base64Encode(String cadena){
        byte[] data = cadena.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String base64Decode(String cadena){
        byte[] data = Base64.decode(cadena, Base64.DEFAULT);
        String text = new String(data, StandardCharsets.UTF_8);
        return text;
    }

    //agregados

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void localizacion(Activity act) {
        if (act.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && act.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(act,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION

            }, 1000);
        }

        Boolean b = checkIfLocationOpened(act);
        if(!b){
            Toast.makeText(act, "ERROR! ACTIVE SU GPS", Toast.LENGTH_SHORT).show();
        }

        System.out.println("está:"+b);

        LocationManager ubicacion = (LocationManager) act.getSystemService(act.getApplicationContext().LOCATION_SERVICE);

        Location loc = ubicacion.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if(loc !=null){
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            Toast.makeText(act, "latitud:"+loc.getLatitude()+"longitud:"+loc.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(act, "latitud: NLA; longitud: NLA", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean checkIfLocationOpened(Activity act) {
        String provider = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }

    public static String hashMD5(String md5)
    {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }



}
