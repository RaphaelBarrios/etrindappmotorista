package br.com.etrind.etrindappmotorista.Dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;

public class LogDal {
    private SQLiteDatabase db;
    private DbDal banco;
    private Context ctx;

    public LogDal(Context context){
        banco = DbDal.getInstance(context);
        ctx = context;
    }

    public GenericResult Insert(LogEntity obj){
        GenericResult result = new GenericResult();

        try {
            long resultdb;

            db = banco.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("Tipo", obj.Tipo);
            valores.put("DataHora", obj.DataHora);
            valores.put("Descricao", obj.Descricao);

            resultdb = db.insert("Log", null, valores);
            db.close();

            if (resultdb ==-1) {
                result.ResultOK = false;
                result.Message = "Não foi possível inserir";
            }else {
                result.ResultOK = true;
            }
        }catch (Exception ex){
            result.ResultOK = false;
            result.Message = ex.getMessage();
        }

        return result;

    }

    public GenericResult Listar(String tipo){
        GenericResult result = new GenericResult();

        ArrayList<LogEntity> list = new ArrayList<LogEntity>();
        LogEntity obj = null;

        Cursor cursor = null;
        db = banco.getReadableDatabase();
        String sql = "SELECT * FROM Log WHERE Tipo = ? ORDER BY LogId DESC";

        try {
            cursor = db.rawQuery(sql, new String[]{tipo});

            while (cursor.moveToNext()) {
                obj = Mapping(cursor);
                if (obj != null)
                    list.add(obj);

            }

            result.ResultOK = true;
            result.ResultData = list;

        }catch (Exception ex){

            result.ResultOK = false;
            result.ResultData = null;
            result.Message = ex.getMessage();

        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return result;
    }

    public GenericResult Deletar(int horasManter){
        GenericResult result = new GenericResult();

        try{
            long resultdb;
            db = banco.getWritableDatabase();
            resultdb = db.delete("Log", "DataHora < date('now','-" + horasManter + " hour')", null);
            db.close();

            result.ResultOK = true;

        }catch (Exception ex){
            result.ResultOK = false;
            result.Message = ex.getMessage();
        }

        return result;
    }

    public GenericResult DeletarTodos(){
        GenericResult result = new GenericResult();

        try{
            String sql = " DELETE FROM Log";
            db = banco.getWritableDatabase();
            db.execSQL(sql);
            result.ResultOK = true;

        }catch (Exception ex){
            result.ResultOK = false;
            result.Message = ex.getMessage();
        }

        return result;
    }

    private LogEntity Mapping(Cursor c){
        LogEntity obj = new LogEntity();

        obj.LogId = Integer.parseInt(c.getString(c.getColumnIndexOrThrow("LogId")));
        obj.Tipo =  c.getString(c.getColumnIndexOrThrow("Tipo"));
        obj.DataHora = c.getString(c.getColumnIndexOrThrow("DataHora"));
        obj.Descricao =  c.getString(c.getColumnIndexOrThrow("Descricao"));

        return obj;
    }
}
