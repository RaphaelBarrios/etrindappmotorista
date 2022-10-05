package br.com.etrind.etrindappmotorista.Main;

import static br.com.etrind.etrindappmotorista.Infra.Constantes.LOG_TIPO_LOCALIZACAO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Business.AtivacaoBusiness;
import br.com.etrind.etrindappmotorista.Business.LogBusiness;
import br.com.etrind.etrindappmotorista.Business.MotoristaBusiness;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoFiltroEntity;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.Entity.MotoristaEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.LocationService;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Context ctx;
    UserInfo userInfo;
    AtivacaoBusiness ativacoesBusiness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
        ctx = this;
        userInfo = new UserInfo(ctx);

        //startLocationService();

        //Nesta tela usuário deve estar logado com serviço rodando sempre
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(ctx, InicialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

        ativacoesBusiness = new AtivacaoBusiness(userInfo.authTokenGet());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, ObterFragmentHome()).commit();

        bottomNavigationView.setSelectedItemId(R.id.my_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menuitem_home:

                        fragment = ObterFragmentHome();

                        break;
                    case R.id.menuitem_config:

                        fragment = ObterFragmentConfig();
                        break;
                    case R.id.menuitem_logout:

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setCancelable(true);
                        builder.setTitle("Confirmação");
                        builder.setMessage("Deseja realmente sair? Sua localização deixará de ser compartilhada.");
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopLocatonService();
                                userInfo.userActiveSet(false);

                                MotoristaEntity motoristaEntity = new MotoristaEntity();
                                motoristaEntity.IdMotorista = userInfo.idMotoristaGet();
                                MotoristaBusiness motoristaBusiness = new MotoristaBusiness();
                                motoristaBusiness.Sair(motoristaEntity);

                                finish();
                            }
                        });
                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                }

                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                    return true;
                }

                return true;
            }
        });
    }

    private Fragment ObterFragmentConfig(){
        return new ConfiguracoesFragment();
    }

    private Fragment ObterFragmentHome(){

        AtivacaoFiltroEntity ativacaoFiltroEntity = new AtivacaoFiltroEntity(userInfo.idMotoristaGet());
        GenericResult result = ativacoesBusiness.Listar(ativacaoFiltroEntity);
        ArrayList<AtivacaoEntity> ativacaoEntities = new ArrayList<>();
        if(result.ResultOK){
            ativacaoEntities = (ArrayList<AtivacaoEntity>)result.ResultData;
        }else{
            Toast.makeText(getApplicationContext(), result.Message, Toast.LENGTH_LONG).show();
        }

        return new AtivacoesFragment(ativacaoEntities, userInfo.cnhGet());
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

    private void stopLocatonService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constantes.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            //Toast.makeText(this, getString(R.string.notification_service_stopped), Toast.LENGTH_SHORT).show();
        }
    }



}