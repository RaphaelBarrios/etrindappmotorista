package br.com.etrind.etrindappmotorista.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.etrind.etrindappmotorista.Business.FcmTokenBusiness;
import br.com.etrind.etrindappmotorista.Business.LogBusiness;
import br.com.etrind.etrindappmotorista.Business.MotoristaBusiness;
import br.com.etrind.etrindappmotorista.Entity.MotoristaEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.LocationService;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings("Convert2Lambda")
public class EntrarActivity extends AppCompatActivity {
    private Context ctx;
    private EditText txtEntrarCnh;
    private TextView tvPoliticaPrivacidade;
    private UserInfo userInfo;
    private MotoristaBusiness motoristaBusiness;
    private FcmTokenBusiness fcmTokenBusiness;
    private LogBusiness logBusiness;
    private boolean permissaoOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        ctx = this;
        txtEntrarCnh = findViewById(R.id.txtEntrarCnh);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        tvPoliticaPrivacidade = findViewById(R.id.tvPoliticaPrivacidade);
        tvPoliticaPrivacidade.setMovementMethod(LinkMovementMethod.getInstance());

        userInfo = new UserInfo(ctx);
        motoristaBusiness = new MotoristaBusiness();
        fcmTokenBusiness = new FcmTokenBusiness(ctx);
        logBusiness = new LogBusiness(ctx);

        if(!userInfo.cnhGet().equals("")){
            txtEntrarCnh.setText(userInfo.cnhGet());
        }

        AppPossuiPermissaoLocalizacaoRequest();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickEntrar();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean autoLogin = extras.getBoolean("autoLogin");
            if(autoLogin){
                OnClickEntrar();
            }
        }
    }

    private void OnClickEntrar(){
        if(permissaoOk) {
            if (Entrar()) {
                Intent intent = new Intent(EntrarActivity.this, MainActivity.class);
                userInfo.userActiveSet(true);
                userInfo.cnhSet(txtEntrarCnh.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }else{
            AppPossuiPermissaoLocalizacaoRequest();
        }
    }

    protected  boolean Entrar(){
        MotoristaEntity motoristaLogin = new MotoristaEntity(txtEntrarCnh.getText().toString());
        GenericResult result = motoristaBusiness.Entrar(motoristaLogin);

        userInfo.cnhSet(txtEntrarCnh.getText().toString());

        if(!result.ResultOK){
            Toast.makeText(ctx, "Problema ao logar: " + result.Message, Toast.LENGTH_SHORT).show();
            return false;
        }

        MotoristaEntity motoristaResult = (MotoristaEntity) result.ResultData;
        userInfo.userNameSet(motoristaResult.Nome);
        userInfo.idMotoristaSet(motoristaResult.IdMotorista);
        userInfo.authTokenSet(motoristaResult.TokenApi);
        userInfo.cnhSet(motoristaResult.Cnh);

        motoristaLogin.Token = userInfo.fbTokenGet();
        motoristaLogin.IdMotorista = motoristaResult.IdMotorista;

        if(!motoristaResult.MotoristaAutenticado){
            Intent intent = new Intent(ctx, RegistrarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return false;
        }

        result = fcmTokenBusiness.Atualizar(motoristaLogin);

        if(!result.ResultOK){
            Toast.makeText(ctx, "Problema ao atualizar token: " + result.Message, Toast.LENGTH_SHORT).show();
            return false;
        }

        startLocationService();

        //logBusiness.LimparLog();

        return true;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.putExtra("idMotorista", userInfo.idMotoristaGet());
            intent.putExtra("authToken", userInfo.authTokenGet());

            intent.setAction(Constantes.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            //Toast.makeText(this, getString(R.string.notification_service_started), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }


    private void AppPossuiPermissaoLocalizacaoRequest() {
        boolean permissaolocalizacaoCoarse = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permissaolocalizacaoFine = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permissaolocalizacaoFine || !permissaolocalizacaoCoarse) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.entrar_permissao_localizacao_titulo));
            builder.setMessage(getResources().getString(R.string.entrar_permissao_localizacao_desc));
            builder.setNegativeButton(getResources().getString(R.string.entrar_permissao_localizacao_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    AlertPermissaoNegadaPreConfirmacao();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.entrar_permissao_localizacao_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    ActivityCompat.requestPermissions(
                            EntrarActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            Constantes.REQUEST_CODE_LOCATION_PERMISSION_COARSE_FINE
                    );
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            this.permissaoOk = false;
        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            boolean permissaolocalizacaoBackground = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if(!permissaolocalizacaoBackground){
                ActivityCompat.requestPermissions(
                        EntrarActivity.this,
                        new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        Constantes.REQUEST_CODE_LOCATION_PERMISSION_BACKGROUND
                );
            }else{
                this.permissaoOk = true;
            }
        }else{
            this.permissaoOk = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constantes.REQUEST_CODE_LOCATION_PERMISSION_COARSE_FINE){
            if(grantResults.length > 1){
                boolean permissaolocalizacaoCoarse = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissaolocalizacaoFine = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    //Android 9 (Api level 28 = Build.VERSION_CODES.P) não possuem permissão de  ACCESS_BACKGROUND_LOCATION
                    if(permissaolocalizacaoCoarse && permissaolocalizacaoFine){
                        this.permissaoOk = true;
                    }else{
                        AlertPermissaoNegadaRequestPermissionsResult();
                        this.permissaoOk = false;
                    }
                }else{
                    if(permissaolocalizacaoCoarse && permissaolocalizacaoFine){
                        boolean permissaolocalizacaoBackground = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
                        if(!permissaolocalizacaoBackground){
                            ActivityCompat.requestPermissions(
                                    EntrarActivity.this,
                                    new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                    Constantes.REQUEST_CODE_LOCATION_PERMISSION_BACKGROUND
                            );
                        }else{
                            this.permissaoOk = true;
                        }
                    }
                }
            }else{
                AlertPermissaoNegadaRequestPermissionsResult();
                this.permissaoOk = false;
            }
        }else if(requestCode == Constantes.REQUEST_CODE_LOCATION_PERMISSION_BACKGROUND){
            if(grantResults.length > 0){
                @SuppressLint("InlinedApi") boolean permissaolocalizacaoBackground = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
                if(permissaolocalizacaoBackground){
                    this.permissaoOk = true;
                }else{
                    AlertPermissaoNegadaRequestPermissionsResult();
                    this.permissaoOk = false;
                }
            }else{
                AlertPermissaoNegadaRequestPermissionsResult();
                this.permissaoOk = false;
            }
        }
    }

    private void AlertPermissaoNegadaRequestPermissionsResult(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.entrar_permissao_localizacao_negada_titulo));

        String message = getResources().getString(R.string.entrar_permissao_localizacao_negada_desc1);

        message += getResources().getString(R.string.entrar_permissao_localizacao_negada_desc2);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            message += getResources().getString(R.string.entrar_permissao_localizacao_negada_desc_detalhe_android_maior_9);
        }else{
            message += getResources().getString(R.string.entrar_permissao_localizacao_negada_desc_detalhe_android_menor_9);
        }

        builder.setMessage(message);
        builder.setNegativeButton(getResources().getString(R.string.entrar_permissao_localizacao_negada_fechar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void AlertPermissaoNegadaPreConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.entrar_permissao_localizacao_negada_titulo));

        String message = getResources().getString(R.string.entrar_permissao_localizacao_negada_desc1);

        builder.setMessage(message);
        builder.setNegativeButton(getResources().getString(R.string.entrar_permissao_localizacao_negada_fechar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}