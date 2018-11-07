package com.example.blanquita.cm_wallets;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements
        RutaFragment.OnFragmentInteractionListener, PagosFragment.OnFragmentInteractionListener,
        MisPagosFragment.OnFragmentInteractionListener, OtrasConsultasFragment.OnFragmentInteractionListener,
        SOSFragment.OnFragmentInteractionListener, LLamadaFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {


    private String PREFS_KEY = "mispreferencias";
    private TextView Caja_mail;
    private String Mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Mail = getIntent().getExtras().getString("correo_electronico");

        //Web_Services Server = (Web_Services) getIntent().getExtras().getParcelable("Server");


        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.textView_mail);
        nav_user.setText(Mail);



    }

    public void saveNombreActivityPref(Context context, String nombreActivity) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString("nombreActivity", nombreActivity);
        editor.commit();
    }

    public String getNombreActivityPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString("nombreActivity", "");
    }
    public void mensaje(String cadena){
        Toast.makeText(this, cadena, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            saveNombreActivityPref(getApplicationContext(), "") ;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Cada elemento de navegacion o ventana interna debe ser un fragmento
        //Video para crearlo
        //https://www.youtube.com/watch?v=-s39b_Ki1C8

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            RutaFragment frag = new RutaFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();

        } else if (id == R.id.nav_gallery) {

            Bundle bundle = new Bundle();
            bundle.putString("usuario", Mail);

            PagosFragment frag = new PagosFragment();
            frag.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();

        } else if (id == R.id.nav_slideshow) {
            MisPagosFragment frag = new MisPagosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();

        } else if (id == R.id.nav_manage) {
            OtrasConsultasFragment frag = new OtrasConsultasFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();
        } else if (id == R.id.nav_share) {
            SOSFragment frag = new SOSFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();

        } else if (id == R.id.nav_send) {
            LLamadaFragment frag = new LLamadaFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_fragmentos, frag).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
