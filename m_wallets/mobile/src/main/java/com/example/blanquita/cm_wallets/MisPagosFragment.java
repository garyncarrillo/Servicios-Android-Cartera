package com.example.blanquita.cm_wallets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MisPagosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MisPagosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisPagosFragment extends Fragment implements View.OnClickListener {

    //Mis preferencias
    private String PREFS_KEY = "mispreferencias";

    private FloatingActionButton boton;
    private Web_Services Servidor;
    private JSon_Server JSON;

    private ListView Lista;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MisPagosFragment() {
        // Required empty public constructor

    }
    public String getNombreActivityPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString("nombreActivity", "");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisPagosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisPagosFragment newInstance(String param1, String param2) {
        MisPagosFragment fragment = new MisPagosFragment();
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

        Servidor = new Web_Services();

        View view = inflater.inflate(R.layout.fragment_mis_pagos, container, false);
        boton = (FloatingActionButton) view.findViewById(R.id.button);
        this.Lista = (ListView) view.findViewById(R.id.ALista);
        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,arrayList);
        Lista.setAdapter(arrayAdapter);
        boton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext()," Mis pagos ", Toast.LENGTH_SHORT).show();
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyyMMdd").format(cDate);
                String nombreUltimaActivity = getNombreActivityPref(getContext());
                Peticion(nombreUltimaActivity, fDate);
                /*Consumir servicios haciendo uso volley.jar
                    https://www.youtube.com/watch?v=M_TFzlOFeCg
                */

            }
        });


            /*
               Evento click de los Fragmentos
               https://www.youtube.com/watch?v=dmCDDw-9suo
            */
        return view;
    }

    public void Peticion(String IdCobrador, String Fecha){

        //https://www.youtube.com/watch?v=M_TFzlOFeCg

        final ProgressDialog loading = ProgressDialog.show(getContext(),"Por espere....","Consultando Datos",false,false);
        final String URL_PETICION ="http://"+Servidor.getIp_Server()+":"+Servidor.getPuerto()+"/api/my_pagos/?pidcobrador="+IdCobrador+"&pfecha="+Fecha;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PETICION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response es un JSON
                        loading.dismiss();
                        JSON = new  JSon_Server(response.toString());

                        arrayList = JSON.getMis_Pagos( "Cobrador", arrayList);
                        arrayAdapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<String , String>();
                params.put("","");
                return params;
            }*/
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
