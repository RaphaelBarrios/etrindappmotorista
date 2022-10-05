package br.com.etrind.etrindappmotorista.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.etrind.etrindappmotorista.Entity.MotoristaEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;
import br.com.etrind.etrindappmotorista.R;
import br.com.etrind.etrindappmotorista.Business.MotoristaBusiness;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

@SuppressWarnings("Convert2Lambda")
public class RegistrarActivity extends AppCompatActivity {
    private EditText txtTelefone;
    private Context ctx;
    private MotoristaBusiness motoristaBusiness;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        ctx = this;
        userInfo = new UserInfo(ctx);
        txtTelefone = findViewById(R.id.txtTelefone);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        motoristaBusiness = new MotoristaBusiness();
        //userInfo.cnhGet();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTelefone.getText().toString().equals("")){
                    Toast.makeText(ctx, getResources().getString(R.string.activity_registrar_telefone_naoinformado), Toast.LENGTH_LONG).show();
                    return;
                }else if(txtTelefone.getText().toString().length() < 11){
                    Toast.makeText(ctx,getResources().getString(R.string.activity_registrar_telefone_invalido), Toast.LENGTH_LONG).show();
                    return;
                }
                EnviarRegistroMotorista();
            }
        });
    }

    protected  void EnviarRegistroMotorista(){
        MotoristaEntity motoristaEntity = new MotoristaEntity(
                userInfo.cnhGet(),
                txtTelefone.getText().toString()
        );

        GenericResult result = motoristaBusiness.Registar(motoristaEntity);

        if(result.ResultOK) {
            Intent intent = new Intent(ctx, RegistrarCodigoAtivity.class);
            intent.putExtra("idAgenda", (String)result.ResultData);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(ctx, result.Message, Toast.LENGTH_SHORT).show();
        }
    }

}