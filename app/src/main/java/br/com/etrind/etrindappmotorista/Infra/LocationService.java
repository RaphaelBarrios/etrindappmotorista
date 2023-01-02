package br.com.etrind.etrindappmotorista.Infra;


import static br.com.etrind.etrindappmotorista.Infra.Constantes.LOG_TIPO_LOCALIZACAO;
import static br.com.etrind.etrindappmotorista.Infra.Constantes.LOG_TIPO_TEMPO_ESTIMADO_VIAGEM;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import br.com.etrind.etrindappmotorista.Business.AtivacaoBusiness;
import br.com.etrind.etrindappmotorista.Business.DistanceMatrixBusiness;
import br.com.etrind.etrindappmotorista.Business.LocalizacaoBusiness;
import br.com.etrind.etrindappmotorista.Business.LogBusiness;
import br.com.etrind.etrindappmotorista.Business.TempoEtaBusiness;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.LocalizacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.DistMatrix.DistanceMatrixResult;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.R;


@SuppressWarnings("ALL")
public class LocationService extends Service {
    private static final String CHANNEL_ID = "appmotorista_notification_channel_ls";
    private LocalizacaoBusiness localizacaoBusiness;
    private DistanceMatrixBusiness distanceMatrixBusiness;
    private LogBusiness logBusiness;
    private String idMotorista;
    private String authToken;
    private AtivacaoEntity ativacaoAtualEntity;
    private AtivacaoBusiness ativacaoBusiness;
    private TempoEtaBusiness tempoEtaBusiness;

    private UserInfo userInfo;

    private LocationCallback locationCallback = new LocationCallback() {


        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {

                LocalizacaoEntity localizacaoEntity = new LocalizacaoEntity();
                localizacaoEntity.IdMotorista = idMotorista;
                localizacaoEntity.Latitude = locationResult.getLastLocation().getLatitude();
                localizacaoEntity.Longitude = locationResult.getLastLocation().getLongitude();
                localizacaoEntity.Velocidade = (int)locationResult.getLastLocation().getSpeed();
                localizacaoEntity.DataHora = Helper.GetCurrentDateTime();
                EnviarLocalizacao(localizacaoEntity);
                EnviarTempoEstimado(localizacaoEntity, ativacaoAtualEntity);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void startLocationService() {

        localizacaoBusiness = new LocalizacaoBusiness(this.authToken);
        distanceMatrixBusiness = new DistanceMatrixBusiness();
        logBusiness = new LogBusiness(getApplicationContext());
        tempoEtaBusiness = new TempoEtaBusiness(this.authToken);
        ativacaoBusiness = new AtivacaoBusiness(this.authToken);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                CHANNEL_ID
        );
        builder.setSmallIcon(R.drawable.ic_dlh_notification_location);
        builder.setContentTitle(getString(R.string.notification_location_title));
        builder.setContentText(getString(R.string.notification_location_body));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "ACCESS_FINE_LOCATION > Permissão Negada", Toast.LENGTH_LONG).show();
            return;
        }

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constantes.LOCATION_SERVICE_ID, builder.build());

        this.ativacaoAtualEntity = ativacaoBusiness.ObterAtivacaoAtual(this.idMotorista);
        this.userInfo = new UserInfo(getApplicationContext());

    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(Constantes.ACTION_START_LOCATION_SERVICE)){
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        this.idMotorista = extras.getString("idMotorista");
                        this.authToken = extras.getString("authToken");
                    }
                    startLocationService();
                }else if(action.equals(Constantes.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void EnviarLocalizacao(LocalizacaoEntity localizacaoEntity){
        try{

            GenericResult result = localizacaoBusiness.Enviar(localizacaoEntity);
            String logDescricao = "lat: " + localizacaoEntity.Latitude + " long: " + localizacaoEntity.Longitude + " veloc: " + localizacaoEntity.Velocidade + " result: " + result.Message;
            logBusiness.Insert(LOG_TIPO_LOCALIZACAO, Helper.GetCurrentDateTime(), logDescricao);
            Log.d(LOG_TIPO_LOCALIZACAO, logDescricao);

        }catch (Exception ex){
            //Toast.makeText(context, location_string, Toast.LENGTH_SHORT);
            Log.e("LocationService", "Erro: " + ex.getMessage());
        }
    }

    private void EnviarTempoEstimado(LocalizacaoEntity localizacaoEntity, AtivacaoEntity ativacaoAtualEntity){
        try{
            String dataUltimoEnvio = userInfo.etaUltimoEnvioGet();
            if(!dataUltimoEnvio.equals("")){
                Integer tempoAtualizacaoMinutos = userInfo.tempoAttEtaGet();
                if(tempoAtualizacaoMinutos == 0) {
                    Log.d(LOG_TIPO_TEMPO_ESTIMADO_VIAGEM, "TempoAttEta igual a zero, atualização não será enviada");
                    return;
                }
                Date horaAtual = new Date();
                Date horaUltimoEnvio = Helper.ConvertStringToDate(dataUltimoEnvio);
                long diffMiliSec = horaAtual.getTime() - horaUltimoEnvio.getTime();
                long diffMinutes = (diffMiliSec / 1000) / 60;
                if(diffMinutes < tempoAtualizacaoMinutos){
                    Log.d(LOG_TIPO_TEMPO_ESTIMADO_VIAGEM, "Estimativa não enviada > Último envio " + diffMinutes + " min. Tempo configurado: " + tempoAtualizacaoMinutos + " min");
                    return;
                }
            }

            String logDescricao;
            if(this.ativacaoAtualEntity == null){
                logDescricao = "Tempo estimado não calculado, motorista sem ativação em andamento";
            }else{
                GenericResult resultDistanceMatrix = distanceMatrixBusiness.Obter(localizacaoEntity, ativacaoAtualEntity);
                if(!resultDistanceMatrix.ResultOK){
                    logDescricao = "Erro ao obter informação da Api Google Distance Matrix: " + resultDistanceMatrix.Message;
                }else{
                    DistanceMatrixResult distanceMatrixResult = (DistanceMatrixResult)resultDistanceMatrix.ResultData;
                    int distanciaTotalKm = distanceMatrixResult.ObterDistanciaTotalKm();
                    GenericResult resultEnviar = tempoEtaBusiness.Enviar(this.ativacaoAtualEntity.Numero, distanciaTotalKm);

                    logDescricao = "Origem (Localização Atual) > lat: " + localizacaoEntity.Latitude + " long: " + localizacaoEntity.Longitude;
                    logDescricao += " Destino (Ativação Nº "+ ativacaoAtualEntity.Numero +") > lat: " + ativacaoAtualEntity.LatitudeDestino + " long: " + ativacaoAtualEntity.LongitudeDestino;
                    logDescricao += " > Distância Total > " + distanciaTotalKm+ " Km. Resultado Atualização: " + resultEnviar.Message;

                }
            }

            logBusiness.Insert(LOG_TIPO_TEMPO_ESTIMADO_VIAGEM, Helper.GetCurrentDateTime(), logDescricao);
            Log.d(LOG_TIPO_TEMPO_ESTIMADO_VIAGEM, logDescricao);

            userInfo.etaUltimoEnvioSet(Helper.GetCurrentDateTime());

        }catch (Exception ex){
            //Toast.makeText(context, location_string, Toast.LENGTH_SHORT);
            Log.e("LocationService", "Erro: " + ex.getMessage());
        }
    }





}