package br.com.etrind.etrindappmotorista.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Business.DanfeBusiness;
import br.com.etrind.etrindappmotorista.Entity.DanfeEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Main.Capture.DanfeCaptureAct;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemDanfeAdapter;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings({"Convert2Lambda", "unchecked"})
public class DanfeActivity extends AppCompatActivity implements View.OnClickListener {
    private Context ctx;
    private DanfeEntity danfeEntity;
    private DanfeBusiness danfeBusiness;
    private ListView lvDanfe;
    private static final String STATE_ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danfe);

        ctx = this;
        TextView tvDanfeAtivacaoNumero = findViewById(R.id.tvDanfeAtivacaoNumero);
        Button btnScanDanfe = findViewById(R.id.btnScanDanfe);
        btnScanDanfe.setOnClickListener(this);
        danfeEntity = new DanfeEntity();
        Button btnEnviarDanfes = findViewById(R.id.btnEnviarDanfes);
        lvDanfe = findViewById(R.id.lvDanfe);
        danfeBusiness = new DanfeBusiness(ctx);

        btnEnviarDanfes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDanfes();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int ativacaoNumero = extras.getInt("AtivacaoNumero");
            tvDanfeAtivacaoNumero.setText(String.format(ctx.getString(R.string.DanfeActivity_tvDanfeAtivacaoNumero), ativacaoNumero));
            danfeEntity.AtivacaoNumero = ativacaoNumero;

            GenericResult result = danfeBusiness.Listar(danfeEntity);
            if(result.ResultOK) {
                danfeEntity = (DanfeEntity)result.ResultData;
            }else{
                Toast.makeText(ctx, result.Message, Toast.LENGTH_SHORT).show();
            }
        }

        if (savedInstanceState != null) {
            danfeEntity.ChavesNfe = (ArrayList<String>) savedInstanceState.getSerializable(STATE_ITEMS);
        }

        CarregarDanfes();
    }

    protected  void CarregarDanfes(){
        if(danfeEntity.ChavesNfe != null && danfeEntity.ChavesNfe.size() > 0){
            DesignItemDanfeAdapter designItemDanfeAdapter = new DesignItemDanfeAdapter(ctx, danfeEntity.ChavesNfe);
            lvDanfe.setAdapter(designItemDanfeAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, danfeEntity.ChavesNfe);
    }

    @Override
    public void onClick(View view){
        ScanDanfe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result  = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                String content = result.getContents();

                danfeEntity.ChavesNfe.add(content);
                CarregarDanfes();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(content);
                builder.setTitle("CÓDIGO DE BARRAS DANFE");
                builder.setPositiveButton("LER OUTRA DANFE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScanDanfe();
                    }
                }).setNegativeButton("FINALIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*finish();*/
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(this, "Não foi possível ler o código de barras", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected  void ScanDanfe(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(DanfeCaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt("LENDO CÓDIGO DE BARRAS DANFE");
        intentIntegrator.initiateScan();

    }

    public void EnviarDanfes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ctx.getString(R.string.enviar_danfe_confirmacao_mensagem));
        builder.setTitle(ctx.getString(R.string.enviar_danfe_confirmacao_titulo));
        builder.setPositiveButton(ctx.getString(R.string.enviar_danfe_confirmacao_texto_positivo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final ProgressDialog progressDialog = ProgressDialog.show(ctx, null, ctx.getString(R.string.enviar_danfe_progressdialog_mensagem));

                progressDialog.show();

                Runnable runnable = new Runnable(){
                    public void run() {
                        GenericResult result = danfeBusiness.Enviar(danfeEntity);
                        if(result.ResultOK){
                            Toast.makeText(ctx, ctx.getString(R.string.enviar_danfe_mensagem_sucesso), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ctx, ctx.getString(R.string.enviar_danfe_mensagem_erro) + "\n" + result.Message, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                };
                runnable.run();

            }
        }).setNegativeButton(ctx.getString(R.string.enviar_danfe_confirmacao_texto_negativo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*finish();*/
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}