package br.com.etrind.etrindappmotorista.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.R;
import br.com.etrind.etrindappmotorista.Business.MotoristaBusiness;

@SuppressWarnings("Convert2Lambda")
public class RegistrarCodigoAtivity extends AppCompatActivity {
    private Context ctx;
    private EditText txtCodigoSeguranca;
    private String idAgenda;
    private MotoristaBusiness motoristaBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_codigo_ativity);

        ctx = this;
        txtCodigoSeguranca = findViewById(R.id.txtCodigoSeguranca);
        Button btnRegistrarCodigo = findViewById(R.id.btnRegistrarCodigo);

        motoristaBusiness = new MotoristaBusiness();

        btnRegistrarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCodigoSeguranca.getText().toString().equals("")){
                    Toast.makeText(ctx, getResources().getString(R.string.activity_registrar_codigosegurancao_naoinformado), Toast.LENGTH_LONG).show();
                    return;
                }else if(txtCodigoSeguranca.getText().toString().length() < 2){
                    Toast.makeText(ctx,getResources().getString(R.string.activity_registrar_codigosegurancao_invalido), Toast.LENGTH_LONG).show();
                    return;
                }
                EnviarCodigoSeguranca();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAgenda = extras.getString("idAgenda");
        }
    }

    protected  void EnviarCodigoSeguranca(){

        GenericResult result = motoristaBusiness.EnviarCodigoSeguranca(
                txtCodigoSeguranca.getText().toString(),
                idAgenda
        );

        if(result.ResultOK) {
            Intent intent = new Intent(ctx, EntrarActivity.class);
            intent.putExtra("autoLogin", true);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(ctx, result.Message, Toast.LENGTH_SHORT).show();
        }
    }
}