package br.com.etrind.etrindappmotorista.Business;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.DanfeEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;
import br.com.etrind.etrindappmotorista.Infra.HttpPost;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;

@SuppressWarnings("TryWithIdenticalCatches")
public class DanfeBusiness {
    private final UserInfo userInfo;

    public DanfeBusiness(Context context){
        userInfo = new UserInfo(context);
    }

    public GenericResult Enviar(DanfeEntity danfeEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_ATUALIZAR_DANFE;
        HttpPost httpPost = new HttpPost();

        JSONObject obj = ConverterParaJSON(danfeEntity);
        try {
            String strResponse = httpPost.execute(new String[]{url, obj.toString(), userInfo.authTokenGet()}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterPostParaGenericResult(strResponse);
            }
        } catch (ExecutionException e) {
            result.ResultOK = false;
            result.Message = "Erro: " + e.getMessage();
            e.printStackTrace();
        }  catch (InterruptedException e) {
            result.ResultOK = false;
            result.Message = "Erro: " + e.getMessage();
            e.printStackTrace();
        }
        return  result;

    }

    public GenericResult Listar(DanfeEntity danfeEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_LOCALIZACAO_LISTAR + "?AtivacaoNumero=" + danfeEntity.AtivacaoNumero;
        HttpGet httpGet = new HttpGet();

        try {
            String strResponse = httpGet.execute(new String[]{url, userInfo.authTokenGet()}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterGetParaGenericResult(strResponse);
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

    private JSONObject ConverterParaJSON(DanfeEntity danfeEntity)  {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("AtivacaoNumero", danfeEntity.AtivacaoNumero);
            JSONArray jsArray = new JSONArray(danfeEntity.ChavesNfe);
            jsonParam.put("ChavesNfe", jsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam;
    }

    private GenericResult ConverterPostParaGenericResult(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");
            if(reader.optJSONObject("ResultData") != null)
                result.ResultData  = reader.getJSONObject("ResultData");

        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }

    private GenericResult ConverterGetParaGenericResult(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");
            if(reader.optJSONObject("ResultData") != null) {
                JSONObject jsonObject = reader.getJSONObject("ResultData");

                DanfeEntity danfeEntity = new DanfeEntity();
                danfeEntity.AtivacaoNumero = jsonObject.getInt("AtivacaoNumero");
                JSONArray jsonArray = jsonObject.getJSONArray("ChavesNfe");
                for (int i = 0; i < jsonArray.length(); i++) {
                    danfeEntity.ChavesNfe.add(jsonArray.getString(i));
                }
                result.ResultData = danfeEntity;
            }
        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }




}
