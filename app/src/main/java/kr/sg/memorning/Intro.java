package kr.sg.memorning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Intro extends AppCompatActivity {

    TextView introText;
    ImageView introImage;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //액션바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //상단바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        introText = (TextView)findViewById(R.id.introText);
        introImage = (ImageView)findViewById(R.id.introImage);



        // 스레드 호출
        IntroThread introThread = new IntroThread(handler);
        introThread.start();



    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){

            if (msg.what == 1){
                introImage.setImageResource(R.drawable.m);
               // introText.setText("1");
            } else if (msg.what == 2){
                introImage.setImageResource(R.drawable.me);
               // introText.setText("2");
            } else if (msg.what == 3) {
                introImage.setImageResource(R.drawable.mem);
               // introText.setText("3");
            } else if (msg.what == 4) {
                introImage.setImageResource(R.drawable.memo);
              //  introText.setText("4");
            } else if (msg.what == 5) {
                Intent intent = new Intent(Intro.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        }


    };



}
