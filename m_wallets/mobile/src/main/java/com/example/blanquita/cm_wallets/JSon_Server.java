package com.example.blanquita.cm_wallets;


import android.content.ContentResolver;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class JSon_Server {
    private DecimalFormat formateador = new DecimalFormat("###,###.##");
    private String jsonStr=null ;

    public  JSon_Server(String JSON){
        try {
            this.jsonStr =JSON;
        }catch (Exception e){
            System.out.println("ERROR JSON (CONSTRUCTOR) "+e.getMessage());
        }
    }

    //http://javainutil.blogspot.com.co/2013/03/java-leer-un-json.html

    public String getJSON_Nombre(String Name_tag){
        String Nombre_Completo="";
        try{

            if (this.jsonStr != null) {
                JSONObject jsonObj = new JSONObject(this.jsonStr);
                JSONArray contacts = jsonObj.getJSONArray( Name_tag);
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String id = c.getString("id");
                    String nombres = c.getString("nombres");
                    String apellidos = c.getString("apellidos");
                    Nombre_Completo = nombres+" "+apellidos;
                }
            }

        }catch (Exception e){
            System.out.println("ERROR JSON "+e.getMessage());
        }

        return Nombre_Completo;
    }

    public boolean getJSON_CONTROL_ACCESO(String Name_tag){
        boolean  Control=false;
        try{
            if (this.jsonStr != null) {
                JSONObject jsonObj = new JSONObject(this.jsonStr);
                JSONArray contacts = jsonObj.getJSONArray(Name_tag);
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    Control = true;
                }
            }

        }catch (Exception e){
            System.out.println("ERROR JSON (getJSON_CONTROL_ACCESO) "+e.getMessage());
        }
        return  Control;
    }

    public ArrayList<String> getMis_Pagos(String Name_tag , ArrayList<String> Lista_Array){
        try{
            double Total = 0;
            if (this.jsonStr != null) {
                JSONObject jsonObj = new JSONObject(this.jsonStr);
                JSONArray contacts = jsonObj.getJSONArray(Name_tag);
                for (int i = 0; i < contacts.length(); i++) {
                    /*
                        Agregar la lista
                        https://www.youtube.com/watch?v=v7fULw96rC4
                     */
                    JSONObject c = contacts.getJSONObject(i);
                    Total = Total + c.getDouble("valor_recaudado");
                    Lista_Array.add("   #:"+c.getString("idcredito")+"      Valor: $ "+formateador.format (c.getDouble("valor_recaudado")));
                }
                if(Total!=0){
                    Lista_Array.add(" Total Recaudado ------  Valor: $ "+formateador.format (Total));
                }
            }
        }catch (Exception e){
            System.out.println("ERROR JSON getMis_Pagos(String Name_tag) "+e.getMessage());
        }

         return  Lista_Array;
    }


}
