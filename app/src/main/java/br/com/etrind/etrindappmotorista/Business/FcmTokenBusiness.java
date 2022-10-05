package br.com.etrind.etrindappmotorista.Business;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.MotoristaEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.HttpPost;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;

@SuppressWarnings("TryWithIdenticalCatches")
public class FcmTokenBusiness {
    private final UserInfo userInfo;

    public FcmTokenBusiness(Context context){
        userInfo = new UserInfo(context);
    }

    public GenericResult Atualizar(MotoristaEntity motoristaEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_ATUALIZAR_TOKEN;
        HttpPost httpPost = new HttpPost();

        JSONObject obj = ConverterParaJSON(motoristaEntity);
        try {
            String strResponse = httpPost.execute(new String[]{url, obj.toString(), userInfo.authTokenGet()}).get();

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

    private JSONObject ConverterParaJSON(MotoristaEntity motoristaEntity)  {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("IdMotorista", motoristaEntity.IdMotorista);
            jsonParam.put("Token", motoristaEntity.Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam;
    }

    private GenericResult ConverterParaGenericResult(String strResponse){

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


}
