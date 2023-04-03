package com.example.baston.ui.slideshow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baston.R;
import com.example.baston.databinding.FragmentSlideshowBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SlideshowFragment extends Fragment implements View.OnClickListener{

    TextView txtSaat;
    Handler handle = null;
    Runnable runnable = null;
    String zaman;
    Button btnAlarmAyarla;
    TextClock txtAlarmSaati;
    private TimePickerDialog timePickerDialog;
    final static int islem_kodu = 1;

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slideshow);

        txtSaat = (TextView)findViewById(R.id.txtSaat);
        btnAlarmAyarla = (Button)findViewById(R.id.btnAlarmAyarla);
        btnAlarmAyarla.setOnClickListener(this);

        final SimpleDateFormat bicim = new SimpleDateFormat("dd:MM:yy HH:mm:ss");

        zaman = bicim.format(new Date());
        txtSaat.setText(zaman);

        handle = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                zaman = bicim.format(new Date());
                txtSaat.setText(zaman);
                handle.postDelayed(runnable, 1000);
            }
        };
        runnable.run();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        alarmKur(true);
    }

    private void alarmKur (boolean tumgunsaat) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                SlideshowFragment,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                tumgunsaat);
        timePickerDialog.setTitle("Alarm Ayarla");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int saat, int dakika) {
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY,saat);
            calSet.set(Calendar.MINUTE,dakika);
            calSet.set(Calendar.SECOND,0);
            calSet.set(Calendar.MILLISECOND,0);

            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE,1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar alarmCalendar) {
        Toast.makeText(getApplicationContext(), "Alarm is set.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), islem_kodu, intent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),pendingIntent);
    }
}