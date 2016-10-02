package com.dm.usa.edu.co.geofence;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonDetectar;
    Button buttonRadio;
    Button buttonCheck;
    TextView textFijo;
    TextView textUbicacion;
    EditText editTextRadio;
    TextView textPosiGuar;
    TextView textPosiAct;
    TextView textGuardado;
    TextView textActual;
    TextView textDentroFuera;

    float radio = 0;
    float distancia=0;

    private boolean cambiarGuardada = true;
    private Location guardada;
    private Location actual;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "mensaje: inicio");
        //------------------------------------------------------------------------------------------
        buttonDetectar = (Button) findViewById(R.id.buttonDetectar);
        buttonRadio = (Button) findViewById(R.id.buttonRadio);
        textFijo = (TextView) findViewById(R.id.textFijo);
        textUbicacion = (TextView) findViewById(R.id.textUbicacion); //Es el texto de Distancia
        editTextRadio = (EditText) findViewById(R.id.editTextRadio);
        textPosiAct = (TextView) findViewById(R.id.textPosAc); //Son los textos Fijos como los titulos
        textPosiGuar = (TextView) findViewById(R.id.textPosiGuardada); //Son los textos Fijos como los titulos
        textGuardado = (TextView) findViewById(R.id.textGuardada); //Son los textos variables
        textActual = (TextView) findViewById(R.id.textActual); //Son los textos variables
        buttonCheck= (Button) findViewById(R.id.buttonCheck);
        textDentroFuera= (TextView) findViewById(R.id.textViewDentroFuera);

        //ASIGNACION DE RADIO-----------------------------------------------------------------------

        buttonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRadio.getText().toString().isEmpty()==false){
                    Log.d(TAG, "mensaje: Guardando radio");
                    radio = Integer.valueOf(editTextRadio.getText().toString());
                    Log.d(TAG, "mensaje: se guardo radio");
                    mensajeToast("Se establecio el radio a "+radio);
                    textFijo.setText("r: "+radio+" - Distancia: ");
                }
            }
        });

        //-----------------------------------------------------------------------------------------

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "mensaje: onLocationChanged");
                textGuardado.setText(location.getLatitude()+", "+location.getLongitude());
                Log.d(TAG, "mensaje: "+location.getLatitude()+", "+location.getLongitude());
                if(cambiarGuardada){
                    Log.d(TAG, "mensaje: guardando locacion --->");
                    guardada = location;
                    textActual.setText(location.getLatitude()+", "+location.getLongitude());
                    cambiarGuardada=false;
                    Log.d(TAG, "mensaje: guardada la locacion--->");
                    mensajeToast("Posicion Guardada");
                }else{
                    actual=location;
                    mensajeToast("Posicion Actual actualizada");
                }

                distancia = guardada.distanceTo(location);
                Log.d(TAG, "mensaje: Distancia: "+distancia+"m");
                textUbicacion.setText(distancia+" m");

                if(radio>0){
                    if(distancia>radio){
                        Log.d(TAG, "Distancia > que el radio");
                        //mensajeToast("Se paso la cerca");
                    }
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "mensaje: onStatusChanged------------------------------------------------> "+status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "mensaje: onProviderEnable-----------------------------------------------> "+provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "mensaje: onProviderDisable----------------------------------------------> "+provider);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        Log.d(TAG, "mensaje: empieza configurebutton");
        configureButton();
        checkPosition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "mensaje: Switch");
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
        }
    }

    private void configureButton() {
        Log.d(TAG, "mensaje: entre conf buton");


        //DETECCIÓN DE DISTANCIA-----------------------------------------------------------------------
        buttonDetectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mensaje: If 0");



                textFijo.setText("r: "+radio+" - Distancia: ");
                textPosiAct.setText("Posición Guardada:");
                textPosiGuar.setText("Posición Actual:");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "mensaje: If 1");

                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "mensaje: If 2");
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                        return;
                    }
                }else{
                    Log.d(TAG, "mensaje: If 3");
                    //provider , minTime, minDistance, locationListener

                    mensajeToast("Detectando nueva posicion");
                    cambiarGuardada = true;
                    textGuardado.setText("");
                    textActual.setText("");

                    //NETWORK_PROVIDER
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    Log.d(TAG, "mensaje: sale ----------> ");
                }
            }
        });
        //------------------------------------------------------------------------------------------

    }

    public void checkPosition(){
        //REVISAR POSICIÓN-----------------------------------------------------------------------
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distancia>radio){
                    textDentroFuera.setText("Se Encuentra Fuera de la Cerca");
                    textDentroFuera.setBackgroundColor(Color.parseColor("#B20000"));
                }
                else{
                    textDentroFuera.setText("Se Encuentra Dentro de la Cerca");
                    textDentroFuera.setBackgroundColor(Color.parseColor("#183A05"));
                }
            }
        });
        //------------------------------------------------------------------------------------------
    }

    public void mensajeToast(String ms){
        Toast toast = Toast.makeText(this, ms, Toast.LENGTH_SHORT);
        toast.show();
    }
}
