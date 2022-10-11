package br.com.etrind.etrindappmotorista.Business;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;

public class TempoEtaBusiness {
    private final String authToken;

    public TempoEtaBusiness(String authToken){
        this.authToken = authToken;
    }

    public GenericResult Enviar(int numeroAtivacao, int distanciaTotalKm){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_TEMPO_ETA_ENVIAR + "?NumeroViagem=" + numeroAtivacao + "&DistanciaKm=" + distanciaTotalKm;
        HttpGet httpGet = new HttpGet();

        try {
            String strResponse = httpGet.execute(new String[]{url, authToken}).get();

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
