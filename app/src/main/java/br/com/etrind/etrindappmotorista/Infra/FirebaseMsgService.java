package br.com.etrind.etrindappmotorista.Infra;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.etrind.etrindappmotorista.Main.NotificacaoDetalheActivity;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings("ConstantConditions")
public class FirebaseMsgService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "appmotorista_notification_channel_fb1";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);

        UserInfo userInfo = new UserInfo(getApplicationContext());
        userInfo.fbTokenSet(s);
        Log.d("FB TOKEN", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }

    }

    private RemoteViews getRemoteView(String title, String message){
        @SuppressLint("RemoteViewLayout") RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.design_notification);
        remoteView.setTextViewText(R.id.tvNotificationTitle, title);
        remoteView.setTextViewText(R.id.tvNotificationMensagem, message);
        //remoteView.setImageViewResource(R.id.ivNotificationLogo, R.drawable.);
        return remoteView;
    }

    private void generateNotification(String title, String message){

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, NotificacaoDetalheActivity.class);
        resultIntent.putExtra("Titulo",title);
        resultIntent.putExtra("Mensagem",message);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                1,
                resultIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                CHANNEL_ID
        );
        builder.setSmallIcon(R.drawable.ic_dlh_notification_message);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setContent(getRemoteView(title, message));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(1006, builder.build());


    }


}
