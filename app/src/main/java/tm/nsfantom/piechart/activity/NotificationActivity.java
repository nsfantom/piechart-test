package tm.nsfantom.piechart.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import timber.log.Timber;
import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 0;
    Handler handler = new Handler();
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.btnNotify.setOnClickListener(v->showNotification());
        binding.btnNotifyDelay.setOnClickListener(v -> showNotificationWithDelay());
    }

    private void showNotificationWithDelay(){
        final long delay = Long.parseLong(binding.etDelay.getText().toString().isEmpty()?"0":binding.etDelay.getText().toString());
        handler.postDelayed(()-> this.runOnUiThread(()->
                createNotification(getNotificationBuilder())),delay*1000);

        Timber.e("parsed DELAY: %s", delay*1000);
    }

    private void showNotification() {
        createNotification(getNotificationBuilder());
    }

    private void createNotification(NotificationCompat.Builder builder){
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator==null ||!vibrator.hasVibrator()) Toast.makeText(this,"device without vibro",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

//        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private long[] parseVibro(){
        try{
            final String[] array = binding.etVibroArray.getText().toString().split(",");
            long[] music = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                music[i]=Long.parseLong(array[i]);
            }
            return music;
        }catch (Exception e){
            Timber.e(e,"parseVibro: %s", e.getMessage());
        }
        return new long[]{1000,1000,1000};
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"vibro")
                .setSmallIcon(R.mipmap.ic_done_white_24dp)
                .setContentTitle("Device Connected")
                .setContentText("Click to monitor")
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setTicker("Prime warning!!!");
        //Vibration
        builder.setVibrate(parseVibro());

        //LED
        builder.setLights(Color.RED, 3000, 3000);

        //Ton
        //builder.setSound(Uri.parse("uri://sound.mp3"));

        return builder;
    }
}
