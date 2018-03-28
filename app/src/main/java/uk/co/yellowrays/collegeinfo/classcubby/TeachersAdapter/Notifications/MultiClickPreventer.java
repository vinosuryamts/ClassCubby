package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications;

import android.view.View;

/**
 * Created by vinos on 2/15/2017.
 */

public class MultiClickPreventer {

    private static final long DELAY_IN_MS = 1000;

    public static void preventMultiClick(final View view) {
        if (!view.isClickable()) {
            return;
        }
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, DELAY_IN_MS);
    }

}
