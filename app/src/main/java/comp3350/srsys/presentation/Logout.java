package comp3350.srsys.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Logout implements ILogout {

    AppCompatActivity activity;

    public Logout(AppCompatActivity activity) {
        this.activity = activity;
    }

    /* Call this method anytime the user logs out */
    @Override
    public void broadCastLogout() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.example.swapper.ACTION_LOGOUT");
        activity.sendBroadcast(broadcastIntent);
    }

    /* Call this method in onCreate() of any activity we want to make sure the user needs
     * to be logged in to access that activity. For example, the user account page should only
     * be accessible if the user is logged in, so we call this method and pass the activity */
    @Override
    public void ensureUserLoggedIn() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.swapper.ACTION_LOGOUT");
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                openLoginPage();
                activity.finish();
            }
        }, intentFilter);
    }

    /* Call this method in onCreate() of any activity we want to make sure the user needs
     * to be logged in to access that activity. For example, the user account page should only
     * be accessible if the user is logged in, so we call this method and pass the activity */
    private void openLoginPage() {
        Intent intent = new Intent(activity, LoginPageUI.class);
        activity.startActivity(intent);
        String toShow = "Logout Successful!";
        Toast.makeText(activity, toShow, Toast.LENGTH_SHORT).show();
    }
}
