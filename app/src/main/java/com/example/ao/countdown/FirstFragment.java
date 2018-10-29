package com.example.ao.countdown;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class FirstFragment extends Fragment {
    private NotificationManager manager;
    int fullminute=30;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.first_fragment, container, false);

        //修改计时值
        TextView textView=(TextView)view.findViewById(R.id.firstView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showchoose(v);
            }
        });
        //倒计时
        CountDownTimer countDownTimer;

        Button btn=(Button)view.findViewById(R.id.mainbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            boolean flag=false;
            @Override
            public void onClick(View v) {
                if(!flag){
                    startTimer();
                    Button btn=(Button)v.findViewById(R.id.mainbutton);
                    btn.setText("Stop");
                    flag=true;
                }
                else{
                    Log.i("停止", "onClick: ");
                    stopTimer();
                    Button btn=(Button)v.findViewById(R.id.mainbutton);
                    btn.setText("Start");
                    flag=false;
                }
            }
        });
        ImageButton music=(ImageButton) view.findViewById(R.id.music1);
        music.setOnClickListener(new View.OnClickListener(){
            boolean tag=false;
            public void onClick(View v){
                if(!tag){
                    start();
                    tag=true;
                }else{
                    stop();
                    tag=false;
                }
            }
        });
        return view;
//        return inflater.inflate(R.layout.first_fragment,null);
    }
    private CountDownTimer countDownTimer;
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

//        TextView tv=(TextView)getView().findViewById(R.id.first);
//        tv.setText("这是主页面");

    }
    public int minute(long time){
        int minute=(int)time/1000/60;
        return minute;
    }
    public int second(long time){
        int second=(int)time/1000%60;
        return second;
    }
    public void startTimer() {
        TextView view=getView().findViewById(R.id.firstView);
        final String s=String.valueOf(view.getText());
        final int t=Integer.parseInt(s);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(fullminute*60*1000, 1000){
                TextView minute=(TextView)getView().findViewById(R.id.firstView);
                TextView second=(TextView)getView().findViewById(R.id.firstView2);
                public void onTick(long millisUntilFinished) {
                    if(minute(millisUntilFinished)==0){
                        minute.setText(String.valueOf(minute(millisUntilFinished))+"0");
                    }
                    else
                        minute.setText(String.valueOf(minute(millisUntilFinished)));

                    if(second(millisUntilFinished)<10){
                        second.setText(String.valueOf("0"+second(millisUntilFinished)));
                    }
                    else
                        second.setText(String.valueOf(second(millisUntilFinished)));
                }

                public void onFinish() {
                    nofication();
                    second.setText("00");
                    stop();
                    Toast.makeText(getActivity(),"计时结束",Toast.LENGTH_SHORT).show();
                }
            }.start();
        }
    }

    /**
     * 结束倒计时
     */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;

        }
    }
    private void showchoose(final View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("时长选择")
//                .setMessage("是否删除")
                .setPositiveButton("15", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView view=v.findViewById(R.id.firstView);
                        Log.i(String.valueOf(view.getText()), "onClick: ");
                        view.setText("15");
                        fullminute=15;
                    }
                }).setNegativeButton("45",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView view=v.findViewById(R.id.firstView);
                        view.setText("45");
                        fullminute=45;
                    }
                }).setNeutralButton("30",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView view=v.findViewById(R.id.firstView);
                        fullminute=30;
                    }
                });
        builder.create().show();
    }
    public void start(){
        Intent intent = new Intent(getActivity(),AudioService.class);
        getActivity().startService(intent);
    }
    //停止音乐
    public void stop(){
        Intent intent = new Intent(getActivity(),AudioService.class);
        getActivity().stopService(intent);
    }
    public void nofication(){
        NotificationManager manager=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Relax")
                .setContentText("定时结束")
                .setWhen(System.currentTimeMillis());
        Notification notification=builder.build();

        PendingIntent  pendingIntent = PendingIntent.getActivity(getActivity(),0,new Intent(getActivity(),Main.class),0);

//        notification.setLatestEvenInfo(getApplicationContext(),"通知标题","这是个新的通知",pendingIntent);
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
        notification.defaults|=Notification.DEFAULT_SOUND;
        manager.notify(1, notification);
    }
}
