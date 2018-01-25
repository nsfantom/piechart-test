package tm.nsfantom.piechart;

import android.app.Application;

import timber.log.Timber;
import tm.nsfantom.piechart.util.PrefStorage;

/**
 * Created by user on 1/24/18.
 */

public class PieChartApp extends Application {

    private PrefStorage prefStorage;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public PrefStorage getPrefStorage(){
        if(prefStorage==null)
            prefStorage = new PrefStorage(this);
        return prefStorage;
    }
}
