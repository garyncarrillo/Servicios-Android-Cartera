package com.example.blanquita.cm_wallets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

import static com.example.blanquita.cm_wallets.RutaFragment.DIALOG_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RutaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RutaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RutaFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner Spinner_Tipo_Credito;
    private Spinner Spinner_Intereses;
    private EditText TxtDocumento;
    private EditText textNombres;
    private View FocusView=null;
    private Web_Services Servidor;
    private JSon_Server JSON;

    private FloatingActionButton Buscar_Cliente;

    //DataPicker
    private EditText textFecha_Inicial;
    private EditText textFecha_Final;
    private int year_x , month_x, day_x;
    static final int DIALOG_ID=0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public View obtenerView(){
        return getView();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public RutaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RutaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RutaFragment newInstance(String param1, String param2) {
        RutaFragment fragment = new RutaFragment();
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

    private DatePickerDialog.OnDateSetListener dpListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month;
            day_x=dayOfMonth;
            String mes_y;
            if(month_x<9){
                mes_y="0"+month_x;
            }else{
                mes_y=""+month_x;
            }
            String dia_y;
            if(day_x<9){
                dia_y="0"+day_x;
            }else{
                dia_y=""+day_x;
            }

            textFecha_Inicial.setText(year_x+""+mes_y+ ""+dia_y);

        }
    };

    private DatePickerDialog.OnDateSetListener dpListener_Final = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month;
            day_x=dayOfMonth;
            String mes_y;
            if(month_x<9){
                mes_y="0"+month_x;
            }else{
                mes_y=""+month_x;
            }
            String dia_y;
            if(day_x<9){
                dia_y="0"+day_x;
            }else{
                dia_y=""+day_x;
            }

            textFecha_Final.setText(year_x+""+mes_y+ ""+dia_y);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Servidor = new Web_Services();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ruta, container, false);

        Spinner_Tipo_Credito = (Spinner) view.findViewById(R.id.spinner_Tipo_Credito);
        String[] creditos = {"Diario","Semanal","Mensual"};
        Spinner_Tipo_Credito.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, creditos));

        this.Spinner_Intereses = (Spinner) view.findViewById(R.id.spinner_Intereses);
        String[] intereses= {"10","20","25"};
        Spinner_Intereses.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, intereses));

        TxtDocumento = (EditText) view.findViewById(R.id.TextDocumento);
        textNombres = (EditText) view.findViewById(R.id.textNombres);

        textFecha_Inicial = (EditText) view.findViewById(R.id.textFecha_Inicial);
        textFecha_Inicial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //https://github.com/wdullaer/MaterialDateTimePicker
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(
                        getContext(), R.style.DialogTheme, dpListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show();

            }
        });

        textFecha_Final = (EditText) view.findViewById(R.id.textFecha_Final);
        textFecha_Final.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //https://github.com/wdullaer/MaterialDateTimePicker
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(
                        getContext(), R.style.DialogTheme, dpListener_Final,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show();

            }
        });


        Buscar_Cliente = (FloatingActionButton) view.findViewById(R.id.Flot_Buscar);
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


        return view;

    }

    public void Peticion_Buscar_Nombre(final String Documento){

        //https://www.youtube.com/watch?v=M_TFzlOFeCg

        final ProgressDialog loading = ProgressDialog.show(getContext(),"Por espere....","Consultando Datos",false,false);
        String URL_PETICION ="http://"+Servidor.getIp_Server()+":"+Servidor.getPuerto()+"/api/cliente/?pdocumento="+Documento.trim();
        //System.out.println("******************************************************* "+URL_PETICION);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PETICION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response es un JSON
                        loading.dismiss();
                        //Toast.makeText(getContext() , response, Toast.LENGTH_SHORT).show();
                        JSON = new  JSon_Server(response.toString());
                        String Nombre = JSON.getJSON_Nombre( "clientes");
                        textNombres.setText(Nombre);
                        textNombres.setTextColor(textNombres.getContext().getResources().getColor(R.color.check_ok));
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

}

