package br.com.etrind.etrindappmotorista.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Business.AtivacaoBusiness;
import br.com.etrind.etrindappmotorista.Business.NotificacaoBusiness;
import br.com.etrind.etrindappmotorista.Business.NotificacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoFiltroEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemLogAdapter;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemNotificacaoAdapter;
import br.com.etrind.etrindappmotorista.R;

public class NotificacoesActivity extends AppCompatActivity {
    private Context ctx;
    private ListView lvNotificacoes;
    private ArrayList<NotificacaoEntity> notificacoes;
    UserInfo userInfo;
    NotificacaoBusiness notificacaoBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        ctx = this;
        userInfo = new UserInfo(ctx);
        lvNotificacoes =  findViewById(R.id.lvNotificacoes);

        notificacaoBusiness = new NotificacaoBusiness(ctx, userInfo.authTokenGet());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer ativacaoNumero = extras.getInt("AtivacaoNumero");
            if(ativacaoNumero != null && ativacaoNumero > 0){
                GenericResult result = notificacaoBusiness.Listar(ativacaoNumero);

                if(result.ResultOK) {
                    notificacoes = (ArrayList<NotificacaoEntity>)result.ResultData;
                    CarregarNotificacoes();
                }else{
                    Toast.makeText(ctx, result.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    protected  void CarregarNotificacoes(){
        if(this.notificacoes != null && this.notificacoes.size() > 0){
            DesignItemNotificacaoAdapter designItemNotificacaoAdapter = new DesignItemNotificacaoAdapter(ctx, this.notificacoes);
            lvNotificacoes.setAdapter(designItemNotificacaoAdapter);
        }
    }
}