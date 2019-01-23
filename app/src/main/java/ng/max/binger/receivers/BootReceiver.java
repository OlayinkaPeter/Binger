package ng.max.binger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ng.max.binger.services.SyncService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, SyncService.class);
            context.startService(serviceIntent);
        }
    }

}
