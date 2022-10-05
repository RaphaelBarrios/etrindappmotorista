package br.com.etrind.etrindappmotorista.Business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.LocalizacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.DistMatrix.DistanceMatrixElementItemResult;
import br.com.etrind.etrindappmotorista.Entity.Result.DistMatrix.DistanceMatrixElementResult;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Entity.Result.DistMatrix.DistanceMatrixResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;

public class DistanceMatrixBusiness {

    public GenericResult Obter(LocalizacaoEntity localizacaoEntity, AtivacaoEntity ativacaoAtual){
        GenericResult result = new GenericResult();

        String url = Constantes.REMOTE_URL_GOOGLE_DISTANCE_MATRIX + "/json?";
        url += String.format("destinations=%s,%s", ativacaoAtual.LatitudeDestino, ativacaoAtual.LongitudeDestino);
        url += String.format("&origins=%s,%s", localizacaoEntity.Latitude, localizacaoEntity.Longitude);
        url += String.format("&key=%s", Constantes.REMOTE_URL_GOOGLE_DISTANCE_MATRIX_KEY);
        HttpGet httpGet = new HttpGet();

        try {
            String strResponse = httpGet.execute(new String[]{url}).get();

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
        DistanceMatrixResult distanceMatrixResult = new DistanceMatrixResult();
        try {
            reader = new JSONObject(strResponse);

            //Origens
            JSONArray jsonArrayDestinationAddresses = reader.getJSONArray("destination_addresses");
            for (int i = 0; i < jsonArrayDestinationAddresses.length(); i++) {
                distanceMatrixResult.DestinationAddresses.add(jsonArrayDestinationAddresses.getString(i));
            }

            //Destinos
            JSONArray jsonArrayOriginAddresses = reader.getJSONArray("origin_addresses");
            for (int i = 0; i < jsonArrayOriginAddresses.length(); i++) {
                distanceMatrixResult.OriginAddresses.add(jsonArrayOriginAddresses.getString(i));
            }

            //Status
            distanceMatrixResult.Status = reader.getString("status");

            //Elements
            JSONArray jsonArrayRows = reader.getJSONArray("rows");

            for (int i = 0; i < jsonArrayRows.length(); i++) {
                DistanceMatrixElementResult elementResult = new DistanceMatrixElementResult();

                JSONObject jsonObjectRow = jsonArrayRows.getJSONObject(i);
                JSONArray jsonArrayElements = jsonObjectRow.getJSONArray("elements");
                for (int q = 0; q < jsonArrayElements.length(); q++) {
                    JSONObject jsonObjectElement = jsonArrayElements.getJSONObject(q);

                    JSONObject jsonObjectDistance = jsonObjectElement.getJSONObject("distance");
                    elementResult.distance = new DistanceMatrixElementItemResult();
                    elementResult.distance.text = jsonObjectDistance.getString("text");
                    elementResult.distance.value = jsonObjectDistance.getInt("value");

                    JSONObject jsonObjectDurantion = jsonObjectElement.getJSONObject("duration");
                    elementResult.duration = new DistanceMatrixElementItemResult();
                    elementResult.duration.text = jsonObjectDurantion.getString("text");
                    elementResult.duration.value = jsonObjectDurantion.getInt("value");

                    String status = jsonObjectElement.getString("status");
                    elementResult.status = status;

                    distanceMatrixResult.Elements.add(elementResult);
                }
            }
            result.ResultData = distanceMatrixResult;
            result.ResultOK = true;

        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;


    }

}
