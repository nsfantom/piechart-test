package tm.nsfantom.piechart;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by user on 1/24/18.
 */

public class PieChartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
