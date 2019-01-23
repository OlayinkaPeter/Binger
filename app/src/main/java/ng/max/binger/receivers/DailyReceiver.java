package ng.max.binger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ng.max.binger.services.SyncService;


public class DailyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, SyncService.class);
        context.startService(intent);

        Toast.makeText(context, "Service started",
                Toast.LENGTH_LONG).show();
    }
}