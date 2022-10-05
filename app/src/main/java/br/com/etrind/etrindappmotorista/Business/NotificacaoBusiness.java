package br.com.etrind.etrindappmotorista.Business;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Dal.LogDal;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoItinerarioEntity;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;

public class NotificacaoBusiness {
    Context ctx;
    LogDal logDal;
    String authToken;

    public NotificacaoBusiness(Context context, String authToken){
        this.ctx = context;
        this.authToken = authToken;
        logDal = new LogDal(context);
    }

    public GenericResult Listar(Integer ativacaoNumero){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_NOTIFICACAO_LISTAR + "?AtivacaoNumero=" + ativacaoNumero;
        HttpGet httpGet = new HttpGet();

        try {
            String strResponse = httpGet.execute(new String[]{url, authToken}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResult(strResponse);
            }
        } catch (ExecutionException e) {
            result.ResultOK = false;
            result.Message = "Erro: " + e.getMessage();
            e.printStackTrace();
        } catch (InterruptedException e) {
            result.ResultOK = false;
            result.Message = "Erro: " + e.getMessage();
            e.printStackTrace();
        }
        return  result;
    }

    private GenericResult ConverterParaGenericResult(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");
            JSONArray jsonArray;

            try{
                jsonArray = reader.getJSONArray("ResultData");
            }catch (Exception ex){
                result.ResultOK  = false;
                result.Message  = "Dados não encontrados. Erro ao ler resultData";
                return result;
            }

            ArrayList<NotificacaoEntity> notificacaoEntities = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mJsonObjectProperty = jsonArray.getJSONObject(i);

                NotificacaoEntity notificacaoEntity = new NotificacaoEntity();
                notificacaoEntity.IdNotificacao = Long.parseLong(String.valueOf(i));
                notificacaoEntity.GuidNotificacao = mJsonObjectProperty.getString("IdNotificacao");
                notificacaoEntity.Notificacao = mJsonObjectProperty.getString("Notificacao");
                notificacaoEntity.Data =  mJsonObjectProperty.getString("Data");

                notificacaoEntities.add(notificacaoEntity);
            }
            result.ResultData = notificacaoEntities;


        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;


    }

}
