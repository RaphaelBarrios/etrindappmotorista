package br.com.etrind.etrindappmotorista.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.etrind.etrindappmotorista.BuildConfig;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings("ALL")
public class InicialActivity extends AppCompatActivity {
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        ctx = this;

        Button btnEntrar = findViewById(R.id.btnEntrar);
        TextView tvVersao = findViewById(R.id.tvVersao);

        tvVersao.setText(String.format(getResources().getString(R.string.texto_versao_label), BuildConfig.VERSION_NAME));

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, EntrarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }

}