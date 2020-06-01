package kr.sg.memorning;

import android.app.Dialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yoohyeok on 2016-12-09.
 */

public class CustomDialog extends Dialog {


    private MainActivity mContext;

    private TextView mTitleView;
    private TextView mContentView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mContent;

    //다음 다이얼로그 이동
    private Button agoButton;
    private Button nextButton;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private View.OnClickListener agoClickListener;
    private View.OnClickListener nextClickListener;

    //
    private ImageView dialog_Image;
    private TextView dialog_Test;

    // 카운트
    private int image_cnt=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_custom_dialog);


        mTitleView = (TextView) findViewById(R.id.dialog_title);
        mContentView = (TextView) findViewById(R.id.dialog_text);
        mLeftButton = (Button) findViewById(R.id.dialog_btn);
        //mRightButton = (Button) findViewById(R.id.dialog_btn2);


        agoButton = (Button)findViewById(R.id.dialog_btn_left);
        nextButton = (Button)findViewById(R.id.dialog_btn_right);


        dialog_Image=(ImageView)findViewById(R.id.dialog_view);
        dialog_Image.setImageResource(R.drawable.test1);
        dialog_Test=(TextView)findViewById(R.id.test);
        dialog_Test.setText(R.string.dialog_content1);


        // 제목과 내용을 생성자에서 셋팅한다.
        mTitleView.setText(mTitle);
        mContentView.setText(mContent);

        agoButton.setOnClickListener(agoClickListener);
        nextButton.setOnClickListener(nextClickListener);
        // 이전, 다음화면 이벤트 셋팅
        agoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (image_cnt > 1){
                    image_cnt = image_cnt - 1;
                } else {
                    image_cnt=1;
                }

                dialog_Image.setImageResource(R.drawable.test + image_cnt);
                if(image_cnt == 1){
                    dialog_Test.setText(R.string.dialog_content1);
                } else if(image_cnt == 2) {
                    dialog_Test.setText(R.string.dialog_content2);
                } else if(image_cnt == 3) {
                    dialog_Test.setText(R.string.dialog_content3);
                } else {
                    dialog_Test.setText("값 없음");
                }


            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (image_cnt < 3){
                    image_cnt = image_cnt +1;
                } else {
                    image_cnt=3;
                }
                dialog_Image.setImageResource(R.drawable.test + image_cnt);

                if(image_cnt == 1){
                    dialog_Test.setText(R.string.dialog_content1);
                } else if(image_cnt == 2) {
                    dialog_Test.setText(R.string.dialog_content2);
                } else if(image_cnt == 3) {
                    dialog_Test.setText(R.string.dialog_content3);
                } else {
                    dialog_Test.setText("값 없음");
                }


            /*    image_cnt = image_cnt + 1;
                if (image_cnt <= 3) {
                    dialog_Image.setImageResource(R.drawable.test + image_cnt);
                    String testt= String.valueOf(image_cnt);
                    dialog_Test.setText(testt);
                }*/
            }
        });


        // 클릭 이벤트 셋팅

        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        } else {

        }
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public CustomDialog(Context context, String title, String content,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = singleListener;
    }

}