package br.com.etrind.etrindappmotorista.Business;

import android.content.Context;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Dal.LogDal;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;

public class LogBusiness {
    Context ctx;
    LogDal logDal;
    public LogBusiness(Context context){
        this.ctx = context;
        logDal = new LogDal(context);
    }

    public GenericResult Insert(String tipo, String dataHora, String descricao){
        LogEntity obj = new LogEntity(tipo, dataHora, descricao);
        GenericResult result = new GenericResult();

        //Manter Histórico
        result = logDal.Deletar(48);

        if(result.ResultOK)
            result = logDal.Insert(obj);

        return result;
    }

    public GenericResult Listar(String tipo){
        return logDal.Listar(tipo);
    }

    public GenericResult LimparLog(){
        return logDal.DeletarTodos();
    }
}
