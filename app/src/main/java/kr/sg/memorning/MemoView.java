package kr.sg.memorning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.sg.memorning.data.Memo;
import kr.sg.memorning.data.MemoViewModel;
import kr.sg.memorning.util.KeyboardUtil;

public class MemoView extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = null;
    private MemoViewModel memoViewModel;
    private Context mContext = null;

    // 모드 ( true : 수정 모드 , false : 뷰 모드 )
    private boolean mIsModifyStutus = false;

    // 메인 INSERT
    private String main_Insert = null;

    //
    private Menu menu;

    // 레이아웃
    private LinearLayout ll_modify = null;
    private LinearLayout ll_viewer = null;


    // 뷰 모드 일때
    private Button btn_edit = null;
    private Button btn_delete = null;
    private TextView textRead = null;

    // 수정 모드 일때
    private Button btn_cancel = null;
    private Button btn_insert = null;
    private EditText edt_Content;

    // 컬러 선택 버튼
    private Button color_black;
    private Button color_red;
    private Button color_yellow;
    private Button color_green;

    // 글자 배경 버튼
    private Button color_white;
    private Button color_peach;
    private Button color_remon;
    private Button color_gray;

    // 폰트 선택 버튼
    private Button font_normal;
    private Button font_bold;
    private Button font_italic;
    private Button font_boldItalic;


    // 글자 세팅
    private String fontColor = "#000000";
    private String bgColor = "#FFFFFF";
    private String fontStyle = "normal";

    private String setFont="";

    // 글자 크기
    private Button size_plus;
    private Button size_minus;
    private TextView size_view;
    private int size_cnt=15;

    // 파일 저장 경로
    private File saveFile;
    private int fileTmp=0;


    // 글 작성일자 텍스트뷰
    private TextView writeTime;
    private String wwTime = "";


    private int content_id = 0;



    ProgressDialog progressDialog = null;

    Memo mMemo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_view);


        mContext = this;

        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel.class);

        //progressDialog 생성 및 세팅.
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("잠시만 기다려 주세요...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        // 레이아웃 연결
        ll_modify = (LinearLayout)findViewById(R.id.ll_edit);
        ll_viewer = (LinearLayout)findViewById(R.id.ll_view);

        Intent intent = getIntent();


        mIsModifyStutus = intent.getBooleanExtra("mode",false); // 엑티비티 진입 시 수정 or 뷰 모드인지 판단 값.

        main_Insert=intent.getStringExtra("mainInsert");

        if (main_Insert != null){
            sendMain();
        }

        /*
           뷰 초기화
        */

        // 뷰 모드 일때
        edt_Content = (EditText)findViewById(R.id.edit1);
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_delete = (Button)findViewById(R.id.btn_delete );

        // 수정 모드 일때
        textRead = (TextView) findViewById(R.id.textRead);
        btn_insert = (Button)findViewById(R.id.btn_insert);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);

        // 컬러 버튼
        color_black = (Button) findViewById(R.id.color_black);
        color_red = (Button) findViewById(R.id.color_red);
        color_yellow = (Button) findViewById(R.id.color_yellow);
        color_green = (Button) findViewById(R.id.color_green);

        // 배경 버튼
        color_white = (Button) findViewById(R.id.color_white);
        color_peach = (Button) findViewById(R.id.color_peach);
        color_remon = (Button) findViewById(R.id.color_remon);
        color_gray = (Button) findViewById(R.id.color_gray);

        // 폰트 버튼
        font_normal = (Button) findViewById(R.id.font_normal);
        font_bold = (Button) findViewById(R.id.font_bold);
        font_italic = (Button) findViewById(R.id.font_italic);
        font_boldItalic = (Button) findViewById(R.id.font_boldItalic);

        // 글자 사이즈 버튼
        size_plus = (Button)findViewById(R.id.size_plus);
        size_minus = (Button)findViewById(R.id.size_minus);
        size_view = (TextView)findViewById(R.id.size_view);

        // 작성일 텍스트 뷰
        writeTime = (TextView)findViewById(R.id.writeTime);



        if (!mIsModifyStutus){
            content_id = intent.getIntExtra("dataNum",0);
        }

        setupMode();

        // 버튼 클릭시 동작.
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_insert.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        // 컬러 버튼 클릭시 동작.
        color_black.setOnClickListener(this);
        color_red.setOnClickListener(this);
        color_yellow.setOnClickListener(this);
        color_green.setOnClickListener(this);

        // 배경 버튼 클릭시 동작.
        color_white.setOnClickListener(this);
        color_peach.setOnClickListener(this);
        color_remon.setOnClickListener(this);
        color_gray.setOnClickListener(this);

        // 폰트 버튼 클릭시 동작.
        font_normal.setOnClickListener(this);
        font_bold.setOnClickListener(this);
        font_italic.setOnClickListener(this);
        font_boldItalic.setOnClickListener(this);

        // 사이즈 버튼 클릭시.
        size_plus.setOnClickListener(this);
        size_minus.setOnClickListener(this);


        /*
            글자 입력 수 제한.
         */


       /* edt_Content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 5) {
                    Log.e("sgjang","lengng:  "+s.toString().length());
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("최대 입력 수는 5자리 입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });*/

    }



    public void sendMain(){

        Intent intent = new Intent();
        setResult(RESULT_OK,intent);


        String name = main_Insert;
        Memo memo = null;

        // 메모 insert 시 새로 생성한 데이터인지 기존 데이터 수정인지 판단 후. memo 값 부여.
        if (mMemo!=null){
            memo = mMemo;
        }else{
            memo = new Memo();
        }

        //작성일 표시
        long writeTimee = System.currentTimeMillis();
        Date date = new Date(writeTimee); // Date 객체 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        wwTime = sdf.format(date);



        memo.setMemoName(name);

        memo.setFontColor(fontColor);

        memo.setBgColor(bgColor);

        memo.setFontStyle(fontStyle);

        memo.setFontSize(size_cnt);

        memo.setWriteTime(wwTime);


        memoViewModel.insert(memo);


        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*
               메모 뷰 모드 시
            */
            case R.id.btn_edit:
                mIsModifyStutus = true;
                setupMode();
                break;

            case R.id.btn_delete:
                if (mMemo!=null && content_id > 0){
                    memoViewModel.deleteMemo(content_id);
                    onBackPressed();
                }
                break;


            /*
               메모 편집 시
            */
            case R.id.btn_insert:
                String name = edt_Content.getText().toString().trim();
                Memo memo = null;


                // 메모 insert 시 새로 생성한 데이터인지 기존 데이터 수정인지 판단 후. memo 값 부여.


                if (mMemo!=null){
                    memo = mMemo;
                }else{
                    memo = new Memo();
                }

                //작성일 표시
                long writeTimee = System.currentTimeMillis();
                Date date = new Date(writeTimee); // Date 객체 생성
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                wwTime = sdf.format(date);


                memo.setMemoName(name);

                memo.setFontColor(fontColor);

                memo.setBgColor(bgColor);

                memo.setFontStyle(fontStyle);

                memo.setFontSize(size_cnt);

                memo.setWriteTime(wwTime);


                int content_idx = memoViewModel.insert(memo);

                // 메모 수정 후 바로 뷰모드에서 텍스트 뜨기 위해 insert 후 return 값 부여.
                if (content_id == 0){
                    content_id = content_idx;
                }

                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsModifyStutus = false;
                        setupMode();

                        KeyboardUtil.getInstance().hideKeyboard(mContext);
                        progressDialog.dismiss();
                    }
                },1500);
                break;

            case R.id.btn_cancel:
                KeyboardUtil.getInstance().hideKeyboard(this);


                    if(mIsModifyStutus == true){
                        finish();
                    } else {
                        mIsModifyStutus = false;
                        setupMode();
                    }

                break;


                /*
                    컬러 버튼 선택시.
                 */
            case R.id.color_black:
                Toast.makeText(getApplicationContext(),"black",Toast.LENGTH_SHORT).show();
                fontColor = "#000000";
                edt_Content.setTextColor(Color.parseColor(fontColor));
                break;

            case R.id.color_red:
                Toast.makeText(getApplicationContext(),"red",Toast.LENGTH_SHORT).show();
                fontColor = "#FF0000";
                edt_Content.setTextColor(Color.parseColor(fontColor));
                break;
            case R.id.color_yellow:
                Toast.makeText(getApplicationContext(),"yellow",Toast.LENGTH_SHORT).show();
                fontColor = "#FFF56E";
                edt_Content.setTextColor(Color.parseColor(fontColor));
                break;
            case R.id.color_green:
                Toast.makeText(getApplicationContext(),"green",Toast.LENGTH_SHORT).show();
                fontColor = "#52E252";
                edt_Content.setTextColor(Color.parseColor(fontColor));
                break;


                /*
                    배경 버튼 선택시.
                 */

            case R.id.color_white:
                Toast.makeText(getApplicationContext(),"white",Toast.LENGTH_SHORT).show();
                bgColor = "#F4FFFF";
                edt_Content.setBackgroundColor(Color.parseColor(bgColor));
                break;

            case R.id.color_peach:
                Toast.makeText(getApplicationContext(),"peach",Toast.LENGTH_SHORT).show();
                bgColor = "#FFB6C1";
                edt_Content.setBackgroundColor(Color.parseColor(bgColor));
                break;
            case R.id.color_remon:
                Toast.makeText(getApplicationContext(),"remon",Toast.LENGTH_SHORT).show();
                bgColor = "#FFFF96";
                edt_Content.setBackgroundColor(Color.parseColor(bgColor));
                break;
            case R.id.color_gray:
                Toast.makeText(getApplicationContext(),"gray",Toast.LENGTH_SHORT).show();
                bgColor = "#dcdcdc";
                edt_Content.setBackgroundColor(Color.parseColor(bgColor));
                break;


                /*
                    폰트 버튼 선택시.
                 */

            case R.id.font_normal:
                Toast.makeText(getApplicationContext(),"normal",Toast.LENGTH_SHORT).show();
                fontStyle = "normal";
                edt_Content.setTypeface(null, Typeface.NORMAL);
                break;

            case R.id.font_bold:
                Toast.makeText(getApplicationContext(),"bold",Toast.LENGTH_SHORT).show();
                fontStyle = "bold";
                edt_Content.setTypeface(null, Typeface.BOLD);
                break;

            case R.id.font_italic:
                Toast.makeText(getApplicationContext(),"italic",Toast.LENGTH_SHORT).show();
                fontStyle = "italic";
                edt_Content.setTypeface(null, Typeface.ITALIC);
                break;

            case R.id.font_boldItalic:
                Toast.makeText(getApplicationContext(),"boldItalic",Toast.LENGTH_SHORT).show();
                fontStyle = "boldItalic";
                edt_Content.setTypeface(null, Typeface.BOLD_ITALIC);
                break;


                /*
                    사이즈 버튼 클릭시
                 */

            case R.id.size_plus:
                if(size_cnt >=40){
                    Toast.makeText(getApplicationContext(),"크기 40이 최대 사이즈입니다!!",Toast.LENGTH_SHORT).show();
                }else{
                    size_cnt++;
                }
                size_view.setText(String.valueOf(size_cnt));

                break;

            case R.id.size_minus:
                if(size_cnt <= 10){
                    Toast.makeText(getApplicationContext(),"크기 10이 최소 사이즈입니다!! ",Toast.LENGTH_SHORT).show();
                }else{
                    size_cnt--;
                }
                size_view.setText(String.valueOf(size_cnt));
                break;

        }
    }

    private void setMemoFromDB(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mMemo = memoViewModel.selectMemo(content_id);
                if (mMemo != null){
                    textRead.setText(mMemo.getMemoName());
                    String color = mMemo.getFontColor();
                    textRead.setTextColor(Color.parseColor(color));

                    String bgColor = mMemo.getBgColor();
                    textRead.setBackgroundColor(Color.parseColor(bgColor));

                    int fontsize = mMemo.getFontSize();
                    textRead.setTextSize(fontsize);

                    // Log.e("sgjang","style:"+setFont);

                    String style = mMemo.getFontStyle();

                    setFont(style);

                    writeTime.setText(mMemo.getWriteTime());

                }
            }
        });
        thread.start();

        setFont(setFont);
    }



    private void setupMode(){
        if (mIsModifyStutus){
            ll_viewer.setVisibility(View.GONE);
            ll_modify.setVisibility(View.VISIBLE);

            if (mMemo!=null){
                edt_Content.setText(mMemo.getMemoName());
                fontColor = mMemo.getFontColor();
                edt_Content.setTextColor(Color.parseColor(mMemo.getFontColor()));

                bgColor = mMemo.getBgColor();
                edt_Content.setBackgroundColor(Color.parseColor(mMemo.getBgColor()));

                size_cnt=mMemo.getFontSize();
                size_view.setText(String.valueOf(mMemo.getFontSize()));


                String vStyle = mMemo.getFontStyle();
                fontStyle = vStyle;
                viewFont(vStyle);

            }


            edt_Content.requestFocus(); // 포커스 설정
            edt_Content.setSelection(edt_Content.length()); // 포커스 맨 뒤로
            KeyboardUtil.getInstance().showKeyboard(mContext);


        }else{
            ll_viewer.setVisibility(View.VISIBLE);
            ll_modify.setVisibility(View.GONE);


            if (content_id > 0){
                setMemoFromDB();
            }
        }
    }



    private void setFont(String style){
        if (style.equals("normal")){
            textRead.setTypeface(null, Typeface.NORMAL);
        }
        else if(style.equals("bold")){
            textRead.setTypeface(null,Typeface.BOLD);
        }
        else if(style.equals("italic")){
            textRead.setTypeface(null,Typeface.ITALIC);
        }
        else if(style.equals("boldItalic")){
            textRead.setTypeface(null,Typeface.BOLD_ITALIC);
        }
    }




    private void viewFont(String style){
        if (style.equals("normal")){
            edt_Content.setTypeface(null, Typeface.NORMAL);

        }
        else if(style.equals("bold")){
            edt_Content.setTypeface(null,Typeface.BOLD);

        }
        else if(style.equals("italic")){
            edt_Content.setTypeface(null,Typeface.ITALIC);

        }
        else if(style.equals("boldItalic")){
            edt_Content.setTypeface(null,Typeface.BOLD_ITALIC);

        }
    }

    //파일 Write
    private void writeFile(boolean mode){
        String str = mMemo.getMemoName();

        // 파일 저장인 경우 mode = true (디바이스 저장), 파일 공유인 공유 mode = false (앱 내 캐시 저장)
        if(mode == true) {
            saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Memo");
            //fileTmp=mMemo.getId();
        }
        else {
            saveFile = new File(getCacheDir().getAbsolutePath()+"/Memo");
            //fileTmp=0;
        }

        //File saveFile = new File("/Memo");
        Log.i("mylog","cur dir:" + saveFile);
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        try {
            long now = System.currentTimeMillis(); // 현재시간 받아오기
            Date date = new Date(now); // Date 객체 생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(date);

            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/Memo"+mMemo.getId()+".txt"));
            buf.append(nowTime + " "); // 날짜 쓰기
            buf.append(str); // 파일 쓰기
            buf.newLine(); // 개행
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 캐시 삭제 하기
    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        }
        catch(Exception e){}
    }
    @Override protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsModifyStutus) {
            // getMenuInflater().inflate(R.menu.submenu, menu);
        } else{
            getMenuInflater().inflate(R.menu.submenu, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        KeyboardUtil.getInstance().hideKeyboard(this);
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // 파일 저장인 경우
        /*    case R.id.action_fileSend:
                writeFile(true);
                return true;*/

            // 텍스트 공유인 경우
            case R.id.action_share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mMemo.getMemoName());
                Intent chooser = Intent.createChooser(intent, "공유하기");
                startActivity(chooser);
                return true;

            // 파일로 공유인 경우
            case R.id.action_shareFile:
                writeFile(false);

                Intent shareFile = new Intent(android.content.Intent.ACTION_SEND);
                shareFile.setType("text/plain");
                String filePath = getCacheDir().getAbsolutePath()+"/Memo"+"/Memo"+mMemo.getId()+".txt";
                shareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(filePath)));
                Intent chooserFile = Intent.createChooser(shareFile, "파일로 공유하기");
                startActivity(chooserFile);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 앱 종료시 캐시 삭제.
    @Override
    public void onDestroy() {
        super.onDestroy();
        clearApplicationCache(null);
    }




}

