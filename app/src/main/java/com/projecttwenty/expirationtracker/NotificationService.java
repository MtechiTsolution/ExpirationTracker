package com.projecttwenty.expirationtracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationService extends IntentService {

	private NotificationManager mNotificationManager;
	private String mMessage,title,content;
	private int mMillis;

	NotificationCompat.Builder builder;

	List<ProductModel> models;
	DatabaseReference dr;
	ProductModel messages;


	public NotificationService() {
		super("com.projecttwenty.NotificationService");

	}


	@Override
	protected void onHandleIntent(Intent intent) {
		mMessage = intent.getStringExtra(CommonConstants.EXTRA_MESSAGE);
		content = intent.getStringExtra(CommonConstants.content);

		mMillis = intent.getIntExtra(CommonConstants.EXTRA_TIMER,
				CommonConstants.DEFAULT_TIMER_DURATION);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		String action = intent.getAction();
		if (action.equals(CommonConstants.ACTION_NOTIFY)) {
			createNotification(intent, mMessage);
		} else if (action.equals(CommonConstants.ACTION_SNOOZE)) {
			mNotificationManager.cancel(CommonConstants.NOTIFICATION_ID);
			Log.d(CommonConstants.DEBUG_TAG, getString(R.string.snoozing));
			createNotification(intent, getString(R.string.done_snoozing));
		} else if (action.equals(CommonConstants.ACTION_DISMISS)) {
			mNotificationManager.cancel(CommonConstants.NOTIFICATION_ID);
		}
	}

	private void createNotification(Intent intent, String msg) {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Sets up the Snooze and Dismiss action buttons that will appear in the
		// expanded view of the notification.
		Intent dismissIntent = new Intent(this, NotificationService.class);
		dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
		PendingIntent piDismiss = PendingIntent.getService(this, 0,
				dismissIntent, 0);

		Intent snoozeIntent = new Intent(this, NotificationService.class);
		snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
		PendingIntent piSnooze = PendingIntent.getService(this, 0,
				snoozeIntent, 0);

		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setContentTitle(CommonConstants.title)
				.setContentText(content)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setPriority(2)
				.setLights(Color.BLUE, 5000, 5000)
				.setSound(
						Uri.parse("file:///sdcard/Notifications/hey_listen.mp3"))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.addAction(R.drawable.ic_stat_dismiss,
						getString(R.string.dismiss), piDismiss)
				.addAction(R.drawable.ic_stat_snooze,
						getString(R.string.snooze), piSnooze);

		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);
		startTimer(mMillis);
	}

	private void createNotification(NotificationCompat.Builder builder) {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.notify(CommonConstants.NOTIFICATION_ID,
				builder.build());
	}

	private void startTimer(int millis) {
		Log.d(CommonConstants.DEBUG_TAG, getString(R.string.timer_start));
		try {
			Thread.sleep(millis);

		} catch (InterruptedException e) {
			Log.d(CommonConstants.DEBUG_TAG, getString(R.string.sleep_error));
		}
		Log.d(CommonConstants.DEBUG_TAG, getString(R.string.timer_finished));
		createNotification(builder);
	}





}
