package br.com.etrind.etrindappmotorista.Business;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import br.com.etrind.etrindappmotorista.Entity.LocalizacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Helper;
import br.com.etrind.etrindappmotorista.Infra.HttpPost;
import br.com.etrind.etrindappmotorista.Infra.Constantes;

public class LocalizacaoBusiness {
    private final String authToken;

    public LocalizacaoBusiness(String authToken){
        this.authToken = authToken;
    }

    public GenericResult Enviar(LocalizacaoEntity localizacaoEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_LOCALIZACAO_ENVIAR;
        HttpPost httpPost = new HttpPost();

        JSONObject obj = ConverterParaJSON(localizacaoEntity);
        try {
            String strResponse = httpPost.execute(new String[]{url, obj.toString(), authToken}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResult(strResponse);
            }
        } catch (ExecutionException | InterruptedException e) {
            result.ResultOK = false;
            result.Message = "Erro: " + e.getMessage();
            e.printStackTrace();
        }
        return  result;

    }

    private JSONObject ConverterParaJSON(LocalizacaoEntity obj)  {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("IdMotorista", obj.IdMotorista);
            jsonParam.put("Latitude", obj.Latitude);
            jsonParam.put("Longitude", obj.Longitude);
            jsonParam.put("Velocidade", obj.Velocidade);
            jsonParam.put("DataHora", Helper.GetCurrentDateTime());
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
