package br.com.etrind.etrindappmotorista.Business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoFiltroEntity;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoItinerarioEntity;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;

@SuppressWarnings("TryWithIdenticalCatches")
public class AtivacaoBusiness {
    private final String authToken;

    public AtivacaoBusiness(String authToken){
        this.authToken = authToken;
    }

    public GenericResult Listar(AtivacaoFiltroEntity ativacaoFiltroEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_ATIVACAO_LISTAR + "?IdMotorista=" + ativacaoFiltroEntity.IdMotorista;
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

            ArrayList<AtivacaoEntity> ativacoes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mJsonObjectProperty = jsonArray.getJSONObject(i);

                AtivacaoEntity ativacaoEntity = new AtivacaoEntity();
                ativacaoEntity.Numero = mJsonObjectProperty.getInt("Numero");
                ativacaoEntity.Data = mJsonObjectProperty.getString("Data");
                ativacaoEntity.Processo = mJsonObjectProperty.getString("Processo");
                ativacaoEntity.Transportadora = mJsonObjectProperty.getString("Transportadora");
                ativacaoEntity.OrigemTitulo = mJsonObjectProperty.getString("OrigemTitulo");
                ativacaoEntity.ViagemEmAndamento = mJsonObjectProperty.getBoolean("ViagemEmAndamento");
                ativacaoEntity.LatitudeDestino = mJsonObjectProperty.getDouble("LatitudeDestino");
                ativacaoEntity.LongitudeDestino = mJsonObjectProperty.getDouble("LongitudeDestino");
                ativacaoEntity.TempoAttEta = mJsonObjectProperty.getInt("TempoAttEta");

                JSONArray jsonArrayItinerarios = mJsonObjectProperty.getJSONArray("ListaItinerarios");

                //if(!(jsonArrayItinerarios == null)){
                    for (int q = 0; q < jsonArrayItinerarios.length(); q++) {
                        JSONObject mJsonObjectPropertyItinerarios = jsonArrayItinerarios.getJSONObject(q);
                        AtivacaoItinerarioEntity ativacaoItinerarioEntity = new AtivacaoItinerarioEntity();
                        ativacaoItinerarioEntity.Descricao = mJsonObjectPropertyItinerarios.getString("Descricao");
                        ativacaoEntity.ListaItinerarios.add(ativacaoItinerarioEntity);
                        ativacaoEntity.ListaItinerariosStr += ativacaoItinerarioEntity.Descricao + "\n";
                    }
                    if(ativacaoEntity.ListaItinerariosStr.length() > 0){
                        ativacaoEntity.ListaItinerariosStr = ativacaoEntity.ListaItinerariosStr.substring(0, ativacaoEntity.ListaItinerariosStr.length() - 1);
                    }
                //}

                ativacoes.add(ativacaoEntity);
            }
            result.ResultData = ativacoes;


        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;


    }

    public AtivacaoEntity ObterAtivacaoAtual(String idMotorista){
        GenericResult result;
        AtivacaoFiltroEntity filtro = new AtivacaoFiltroEntity(idMotorista);
        result = this.Listar(filtro);

        if(!result.ResultOK) return null;

        for(AtivacaoEntity ativacaoEntity : (ArrayList<AtivacaoEntity>)result.ResultData){
            if(ativacaoEntity.ViagemEmAndamento.equals(true)){
                return ativacaoEntity;
            }
        }
        return  null;
    }

}
