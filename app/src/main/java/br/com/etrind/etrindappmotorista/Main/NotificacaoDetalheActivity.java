package br.com.etrind.etrindappmotorista.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings("Convert2Lambda")
public class NotificacaoDetalheActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_detalhe);

        TextView tvNotifDetalheTitulo = findViewById(R.id.tvNotifDetalheTitulo);
        TextView tvNotifDetalheMensagem = findViewById(R.id.tvNotifDetalheMensagem);
        Button btnNotificDetalheFechar = findViewById(R.id.btnNotificDetalheFechar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvNotifDetalheTitulo.setText(extras.getString("Titulo"));
            tvNotifDetalheMensagem.setText(extras.getString("Mensagem"));
        }

        btnNotificDetalheFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}