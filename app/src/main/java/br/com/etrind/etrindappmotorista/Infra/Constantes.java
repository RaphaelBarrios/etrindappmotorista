package br.com.etrind.etrindappmotorista.Infra;

public class Constantes {

    public static final String REMOTE_URL = "http://191.232.164.250/WebApiAppFca/";
    public static final String REMOTE_URL_REGISTRAR = "PostEnvioSmsApp";
    public static final String REMOTE_URL_ENTRAR = "GetMotorista";
    public static final String REMOTE_URL_SAIR = "LogOutApp";
    public static final String REMOTE_URL_REGISTRAR_VALIDAR_CODIGO = "PostRecebimentoCodSeguranca";
    public static final String REMOTE_URL_ATUALIZAR_TOKEN = "UpdateToken";
    public static final String REMOTE_URL_ATIVACAO_LISTAR = "GetAtivacoes";
    public static final String REMOTE_URL_LOCALIZACAO_ENVIAR = "UpdateLocalizacao";
    public static final String REMOTE_URL_TEMPO_ETA_ENVIAR = "GetAtualizarTempoEta";
    public static final String REMOTE_URL_LOCALIZACAO_LISTAR = "GetChavesNFe";
    public static final String REMOTE_URL_ATUALIZAR_DANFE = "PostChaveNFe";
    public static final String REMOTE_URL_NOTIFICACAO_LISTAR = "GetNotificacoes";
    public static final int LOCATION_SERVICE_ID = 2;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

    public static final int REQUEST_CODE_LOCATION_PERMISSION_COARSE_FINE = 2;
    public static final int REQUEST_CODE_LOCATION_PERMISSION_BACKGROUND = 3;

    public static  final String LOG_TIPO_LOCALIZACAO = "LOG LOCALIZAÇÃO";
    public static  final String LOG_TIPO_TEMPO_ESTIMADO_VIAGEM = "LOG TEMPO ESTIMADO VIAGEM";

    public static final String REMOTE_URL_GOOGLE_DISTANCE_MATRIX = "https://maps.googleapis.com/maps/api/distancematrix";
    public static final String REMOTE_URL_GOOGLE_DISTANCE_MATRIX_KEY = "AIzaSyBXZpPVibgSfTqfZ6md8dvAls0hVjGkoRw";
}
