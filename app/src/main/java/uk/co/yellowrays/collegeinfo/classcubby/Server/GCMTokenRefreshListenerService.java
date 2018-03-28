package uk.co.yellowrays.collegeinfo.classcubby.Server;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Vino on 10/10/2017.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    /**
     * When token refresh, start service to get new token
     */
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }

}
