package br.com.etrind.etrindappmotorista.Dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbDal extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "EtrindAppMotorista.db";
    private static final int VERSAO = 1;
    private static DbDal mInstance = null;

    public static DbDal getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DbDal(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private DbDal(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql = " CREATE TABLE Log("
                + "LogId integer primary key autoincrement,"
                + "Tipo text,"
                + "DataHora text,"
                + "Descricao text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql;

        sql = " DROP TABLE IF EXISTS Log";
        db.execSQL(sql);

        onCreate(db);
    }

}
