package tp.uvt.miniprojet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    NotificationCompat.Builder notification;
    private  static final int uniqueID = 45612;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE")
        {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(context, "Have Wifi Connection", Toast.LENGTH_SHORT).show();
                //Build Notification
                notification = new NotificationCompat.Builder(context);
                notification.setAutoCancel(true);
                notification.setSmallIcon(R.drawable.ic_sync);
                notification.setTicker("E-Note");
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("E-Note");
                notification.setContentText("Vous êtes connecter à un réseaux " );
                Intent intent1 = new Intent(context, NavigationDrawer.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                //Build Notification and issues it
                NotificationManager notificationManager =  (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueID, notification.build());
            }
            else
            {
                Toast.makeText(context, "Don't have Wifi Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
