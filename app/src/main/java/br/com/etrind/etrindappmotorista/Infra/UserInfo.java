package br.com.etrind.etrindappmotorista.Infra;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

@SuppressWarnings("unused")
public class UserInfo {
    private final SharedPreferences prefs;
    private final String userkeyIdMotorista = "userkeyIdMotorista";
    private final String userkeyCnh = "userkeyCnh";
    private final String userkeyNome = "userkeyNome";
    private final String userkeyFbToken = "userkeyfbToken";
    private final String userkeyUserAtivo = "userkeyAtivo";
    private final String userkeyAuthToken = "userkeyAuthorizationToken";
    private final String userkeyTempoAttEta = "userkeyTempoAttEta";
    private final String userkeyEtaUltimoEnvio = "userkeyEtaUltimoEnvio";

    public UserInfo(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void authTokenSet(String value) {
        prefs.edit().putString(userkeyAuthToken, value).apply();
    }
    public String authTokenGet() {
        return prefs.getString(userkeyAuthToken,"");
    }

    public void idMotoristaSet(String value) {
        prefs.edit().putString(userkeyIdMotorista, value).apply();
    }
    public String idMotoristaGet() {
        return prefs.getString(userkeyIdMotorista,"");
    }

    public void cnhSet(String value) {
        prefs.edit().putString(userkeyCnh, value).apply();
    }
    public String cnhGet() {
        return prefs.getString(userkeyCnh,"");
    }

    public void fbTokenSet(String value) {
        prefs.edit().putString(userkeyFbToken, value).apply();
    }
    public String fbTokenGet() {
        return prefs.getString(userkeyFbToken,"");
    }

    public void userActiveSet(Boolean value) {
        prefs.edit().putBoolean(userkeyUserAtivo, value).apply();
    }
    public Boolean userActiveGet() {
        return prefs.getBoolean(userkeyUserAtivo,false);

    }

    public void userNameSet(String value) {
        prefs.edit().putString(userkeyNome, value).apply();
    }
    public String userNameGet() {
        return prefs.getString(userkeyNome,"");
    }

    public void tempoAttEtaSet(Integer value) {
        prefs.edit().putInt(userkeyTempoAttEta, value).apply();
    }
    public Integer tempoAttEtaGet() {
        return prefs.getInt(userkeyTempoAttEta,0);
    }

    public void etaUltimoEnvioSet(String value) {
        prefs.edit().putString(userkeyEtaUltimoEnvio, value).apply();
    }
    public String etaUltimoEnvioGet() {
        return prefs.getString(userkeyEtaUltimoEnvio,"");
    }

}
