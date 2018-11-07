package com.example.blanquita.cm_wallets;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
     * {@link PagosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PagosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagosFragment extends Fragment {

    private FloatingActionButton Buscar_Cliente;
    private EditText TxtDocumento;
    private EditText TxtNombre;
    private EditText TxtValorPagar;

    private View FocusView=null;
    private Web_Services Servidor;
    private JSon_Server JSON;

    private String Usuario;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PagosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagosFragment newInstance(String param1, String param2) {
        PagosFragment fragment = new PagosFragment();
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

        Servidor = new Web_Services();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagos, container, false);
        Button b = view.findViewById(R.id.button2);

        Usuario = getArguments().getString("usuario");

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String Documento = TxtDocumento.getText().toString();
                double Valor_Pagar = Double.valueOf(TxtValorPagar.getText().toString().trim());

                Peticion_Buscar_Aplicar_Pago(Documento, Usuario, Valor_Pagar);

            }
        });

        Buscar_Cliente = (FloatingActionButton) view.findViewById(R.id.Buscar);
        Buscar_Cliente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getContext()," Flotante ", Toast.LENGTH_SHORT).show();
                String Documento = TxtDocumento.getText().toString();
                FocusView = TxtDocumento;
                if(Documento.trim().equals("")){
                    TxtDocumento.setError("Error Documento");
                    FocusView.requestFocus();
                }else{
                    Peticion_Buscar_Nombre(Documento);
                }


            }
        });
        TxtDocumento =(EditText) view.findViewById(R.id.TextDocumento);
        TxtNombre =(EditText) view.findViewById(R.id.TextNombre);
        TxtValorPagar = (EditText)view.findViewById(R.id.TextPago);
        return view;
    }

    public void Peticion_Buscar_Aplicar_Pago(final String Documento, String IdCobrador, double ValorPagado){

        //https://www.youtube.com/watch?v=M_TFzlOFeCg

        final ProgressDialog loading = ProgressDialog.show(getContext(),"Por espere....","Consultando Datos",false,false);
        String URL_PETICION ="http://"+Servidor.getIp_Server()+":"+Servidor.getPuerto()+"/api/pagos/?pdocumento="+Documento+"&pidcobrador="+IdCobrador+"&pvalor="+ValorPagado;
        System.out.println();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PETICION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response es un JSON
                        loading.dismiss();
                        Toast.makeText(getContext() , response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        System.out.println("Error al logear "+error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<String , String>();
                params.put("pcorreo",Usuario);
                params.put("pclave",Clave);

                return params;
            }*/
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public void Peticion_Buscar_Nombre(final String Documento){

        //https://www.youtube.com/watch?v=M_TFzlOFeCg

        final ProgressDialog loading = ProgressDialog.show(getContext(),"Por espere....","Consultando Datos",false,false);
        String URL_PETICION ="http://"+Servidor.getIp_Server()+":"+Servidor.getPuerto()+"/api/cliente/?pdocumento="+Documento.trim();
        System.out.println();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PETICION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response es un JSON
                        loading.dismiss();
                        //Toast.makeText(getContext() , response, Toast.LENGTH_SHORT).show();
                        JSON = new  JSon_Server(response.toString());
                        String Nombre = JSON.getJSON_Nombre( "clientes");
                        TxtNombre.setText(Nombre);
                        TxtNombre.setTextColor(TxtNombre.getContext().getResources().getColor(R.color.check_ok));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        System.out.println("Error al logear "+error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<String , String>();
                params.put("pcorreo",Usuario);
                params.put("pclave",Clave);

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
