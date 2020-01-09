package com.example.projetoencomendacorridatm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.projetoencomendacorridatm.R;
import com.example.projetoencomendacorridatm.entidades.Percursos;
import com.example.projetoencomendacorridatm.helper.Permissoes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class tela_central extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    }; //Vetor do tipo string que será responsável por armazenar as permissões que precisamos. No caso, apenas a da localização
    //aproximada do celular

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private double latitudeInicial;
    private double latitudeFinal;
    private double longitudeInicial;
    private double longitudeFinal;
    private LatLng localInicio;
    private LatLng localFinal;
    private Button btnCorrida;
    private Chronometer chronometer;
    private LatLng meuLocal;
    List<LatLng> decodedPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_central);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Permissoes.validarPermissoes(permissoes, this, 1); //Classe responsável por requerir as permissões ao usuário;
        btnCorrida = findViewById(R.id.btnIniciar);
        chronometer = findViewById(R.id.cronometroCorrida);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //Método chamado quando o mapa termina de carregar
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        recuperarLocalizacao();

    }

    //Método que chama o alert da permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissaoNegada();
            }
        }
    }

    private void recuperarLocalizacao() { //Método responsável por recuperar a posição atual do celular;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                mMap.clear();
                meuLocal = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions()
                        .position(meuLocal)
                        .title("EU")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.homemmapa2)));


                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(meuLocal, 20)
                );
                decodedPath.add(meuLocal);
            }

            //Métodos abaixo que verificam alguns casos quanto a localização, mas que não utilizaremos
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //Atualiza a localização do usuário, verificando se ele permitiu capturar a posição atual
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        locationListener


                );
                return;
            }
        }
    }
    public void alertaPermissaoNegada(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas!");
        builder.setMessage("Para utilizar o aplicativo é necessário permitir a busca de sua localização!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    public void onClickIniciar(View v) {
        if (btnCorrida.getText().equals("INICIAR")) {
            mMap.clear();
            localInicio = null;
            localFinal = null;
            decodedPath = new ArrayList<>();
            recuperarLocalizacao();
            btnCorrida.setText("Terminar");
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            latitudeInicial = latitude;
            longitudeInicial = longitude;
            localInicio = new LatLng(latitudeInicial, longitudeInicial);
        } else {
            btnCorrida.setText("INICIAR");
            String tempo = chronometer.getText().toString();
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            latitudeFinal = latitude;
            longitudeFinal = longitude;
            localFinal = new LatLng(latitudeFinal, longitudeFinal);
            mMap.addMarker(new MarkerOptions()
                    .position(localInicio)
                    .title("INICIO")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bandeiramapa)));



            float distancia = calcularDistancia(localInicio, localFinal);
            String distanciaFormatada = formatarDistancia(distancia);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("RESULTADO");
            dialog.setMessage("Distância: " + distanciaFormatada  + "\nTempo: " + tempo);
            dialog.setNegativeButton("OK", null);
            dialog.show();
            Percursos percurso = new Percursos();
            percurso.salvarPercurso(distanciaFormatada, tempo, String.valueOf(latitudeInicial), String.valueOf(longitudeInicial), String.valueOf(latitudeFinal), String.valueOf(longitudeFinal));
            mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.GRAY));
        }
    }



    public static float calcularDistancia(LatLng latLngInicial, LatLng latLngFinal){

        Location localInicial = new Location("Local inicial");
        localInicial.setLatitude( latLngInicial.latitude );
        localInicial.setLongitude( latLngInicial.longitude );

        Location localFinal = new Location("Local final");
        localFinal.setLatitude( latLngFinal.latitude );
        localFinal.setLongitude( latLngFinal.longitude );

        //Calcula distancia - Resultado em Metros
        // dividir por 1000 para converter em KM
        float distancia = localInicial.distanceTo(localFinal) / 1000;

        return distancia;

    }

    public static String formatarDistancia(float distancia){

        String distanciaFormatada;
        if( distancia < 1 ){
            distancia = distancia * 1000;//em Metros
            distanciaFormatada = Math.round( distancia ) + " M ";
        }else {
            DecimalFormat decimal = new DecimalFormat("0.0");
            distanciaFormatada = decimal.format(distancia) + " KM ";
        }

        return distanciaFormatada;

    }

    public void onClickHistorico(View v){
        Intent i = new Intent(getApplicationContext(), tela_historico.class);
        startActivity(i);
        finish();
    }
}
