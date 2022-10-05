package br.com.etrind.etrindappmotorista.Business;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.etrind.etrindappmotorista.Entity.MotoristaEntity;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.HttpGet;
import br.com.etrind.etrindappmotorista.Infra.HttpPost;

@SuppressWarnings("TryWithIdenticalCatches")
public class MotoristaBusiness {

    public MotoristaBusiness(){
    }

    public GenericResult Entrar(MotoristaEntity motoristaEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_ENTRAR + "?Cnh=" + motoristaEntity.Cnh;
        HttpGet httpGet = new HttpGet();

        try {
            String strResponse = httpGet.execute(new String[]{url}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResultEntrar(strResponse);
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

    public GenericResult Registar(MotoristaEntity motoristaEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_REGISTRAR + "?cnh=" + motoristaEntity.Cnh + "&telefone=" + motoristaEntity.Telefone;

        HttpPost httPost = new HttpPost();

        try {
            String strResponse = httPost.execute(new String[]{url}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResultRegistrar(strResponse);
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

    public GenericResult EnviarCodigoSeguranca(String codSeguranca, String idAgenda){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_REGISTRAR_VALIDAR_CODIGO + "?codSeguranca=" + codSeguranca + "&IdAgenda=" + idAgenda;
        HttpPost httpPost = new HttpPost();

        try {
            String strResponse = httpPost.execute(new String[]{url}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResultEnviarCodSeguranca(strResponse);
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

    public GenericResult Sair(MotoristaEntity motoristaEntity){
        GenericResult result = new GenericResult();
        String url = Constantes.REMOTE_URL + Constantes.REMOTE_URL_SAIR + "?idMotorista=" + motoristaEntity.IdMotorista;
        HttpPost httpPost = new HttpPost();

        try {
            String strResponse = httpPost.execute(new String[]{url}).get();

            if(strResponse.contains("RequestError")){
                result.ResultOK = false;
                result.Message = strResponse;
            }else{
                result = ConverterParaGenericResultSair(strResponse);
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

    private GenericResult ConverterParaGenericResultEntrar(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");

            if(reader.optJSONObject("ResultData") != null){
                JSONObject readerData = reader.getJSONObject("ResultData");
                MotoristaEntity motoristaEntity = new MotoristaEntity();
                motoristaEntity.IdMotorista = readerData.getString("IdMotorista");
                motoristaEntity.Nome = readerData.getString("Nome");
                motoristaEntity.Cnh = readerData.getString("Cnh");
                motoristaEntity.Telefone = readerData.getString("Telefone");
                motoristaEntity.TokenApi = readerData.getString("TokenApi");
                motoristaEntity.MotoristaAutenticado = readerData.getBoolean("MotoristaAutenticado");
                result.ResultData  = motoristaEntity;
            }

        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }

    private GenericResult ConverterParaGenericResultRegistrar(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");

            result.ResultData  = reader.getString("ResultData");


        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }

    private GenericResult ConverterParaGenericResultEnviarCodSeguranca(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");
            result.ResultData  = reader.getString("ResultData");


        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }

    private GenericResult ConverterParaGenericResultSair(String strResponse){

        JSONObject reader;
        GenericResult result = new GenericResult();

        try {
            reader = new JSONObject(strResponse);
            result.ResultOK  = reader.getBoolean("ResultOK");
            result.Message  = reader.getString("Message");
            result.ResultData  = reader.getString("ResultData");;
        } catch (JSONException e) {
            result.ResultOK  = false;
            result.Message  = "Erro ao converter StringResponse: " + e.getMessage();
            e.printStackTrace();
        }

        return result;

    }




}
