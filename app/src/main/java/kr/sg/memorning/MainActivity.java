package kr.sg.memorning;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import kr.sg.memorning.data.Memo;
import kr.sg.memorning.data.MemoViewModel;
import kr.sg.memorning.util.KeyboardUtil;

public class MainActivity extends AppCompatActivity {

    private String TAG="sgjang";

    private MemoViewModel memoViewModel;
    private Context mContext;
    private ListView listView;
    private CustomDialog dialog;

    public int daiquiri = 2;

    private int MAIN_CODE=1004;


    // 메인에서 바로 값 저장
     private EditText mainEdit;
     private Button mainInsert;

    // 정렬 기능에 사용되는 스위치
     private Switch aSwitch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        mContext = this;
        listView = findViewById(R.id.listView);
        memoViewModel = ViewModelProviders.of(this).get(MemoViewModel.class);


        liveData(daiquiri);


        aSwitch=(Switch)findViewById(R.id.swith);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if (isChecked){
                    //Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_SHORT).show();
                    daiquiri = 1;
                    liveData(daiquiri);


                }else{

                   // Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_SHORT).show();
                    daiquiri = 2;
                    liveData(daiquiri);


                }
            }
        });



       /* if(daiquiri==1) {


            memoViewModel.nameAllMemos().observe(this, new Observer<List<Memo>>() {
                @Override
                public void onChanged(List<Memo> memos) {
                    ListAdapter adapter = listView.getAdapter();
                    if (adapter instanceof ArrayAdapter) {
                        ArrayAdapter<Memo> arrayAdapter = (ArrayAdapter<Memo>) adapter;

                        arrayAdapter.clear();
                        if (memos != null)
                            arrayAdapter.addAll(memos);


                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        adapter = new ArrayAdapter<>(listView.getContext(), R.layout.memo_list_item, R.id.listTextView, memos);
                        listView.setAdapter(adapter);


                    }
                }
            });

        } else if(daiquiri==2){
            memoViewModel.getAllMemos().observe(this, new Observer<List<Memo>>() {
                @Override
                public void onChanged(List<Memo> memos) {
                    ListAdapter adapter = listView.getAdapter();
                    if (adapter instanceof ArrayAdapter) {
                        ArrayAdapter<Memo> arrayAdapter = (ArrayAdapter<Memo>) adapter;

                        arrayAdapter.clear();
                        if (memos != null)
                            arrayAdapter.addAll(memos);


                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        adapter = new ArrayAdapter<>(listView.getContext(), R.layout.memo_list_item, R.id.listTextView, memos);
                        listView.setAdapter(adapter);


                    }
                }
            });
        }*/


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);
                alertDialogBuilder.setTitle("메모");
                alertDialogBuilder
                        .setMessage("해당 메모를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        Object item = parent.getItemAtPosition(position);
                                        if (item instanceof Memo) {

                                            memoViewModel.deleteMemo(((Memo) item).getId());

                                        }
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                Memo item = (Memo) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(mContext,MemoView.class);
                intent.putExtra("dataNum",item.getId());
                startActivity(intent);
            }
        });

        /*
            파일 읽고, 쓰기 권한 여부 확인 후 동의 하기
         */

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        // 메인에서 값 저장하기.
        mainEdit=(EditText)findViewById(R.id.mainEdit);
        mainInsert=(Button)findViewById(R.id.mainInsert);

        mainInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mainEdit.getText().toString().trim();
                Intent intent = new Intent(mContext,MemoView.class);
                intent.putExtra("mainInsert",name);
                startActivityForResult(intent,MAIN_CODE);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {

        super.onActivityResult(requestCode, resultCode, Data);

        if(requestCode == MAIN_CODE) {

            if(resultCode == RESULT_OK) {
                KeyboardUtil.getInstance().hideKeyboard(this);
                mainEdit.setText("");
                Toast.makeText(MainActivity.this, "성공 완료.", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public void Dialog(){
        dialog = new CustomDialog(MainActivity.this,
                "사용 안내", // 제목
                "환영합니다.", // 내용
                leftListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }
    //다이얼로그 클릭이벤트
    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            //Toast.makeText(MainActivity.this, "버튼을 클릭하였습니다.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };



    @Override protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("메모 종료");
        alertDialogBuilder
                .setMessage("메모를 종료 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn1:
                Intent intent = new Intent(mContext, MemoView.class);
                intent.putExtra("mode",true);
                startActivity(intent);
                return true;

            case R.id.action_btn2:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);
                alertDialogBuilder.setTitle("메모");
                alertDialogBuilder
                        .setMessage("메모 전체를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                           deleteAll();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;

            case R.id.action_btn3:
                Dialog();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void deleteAll(){
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                memoViewModel.deleteMemoAll();
            }
        });
        thread.start();
    }

    private void liveData(int i){

        if(i==1){
            Log.d(TAG,"현재 값은"+daiquiri);
            memoViewModel.nameAllMemos().observe((LifecycleOwner) mContext, new Observer<List<Memo>>() {
                @Override
                public void onChanged(List<Memo> memos) {
                    ListAdapter adapter = listView.getAdapter();

                        adapter = new ArrayAdapter<>(listView.getContext(), R.layout.memo_list_item, R.id.listTextView, memos);
                        listView.setAdapter(adapter);



                }
            });



        } else{
            Log.d(TAG,"현재 값은"+daiquiri);
            memoViewModel.getAllMemos().observe((LifecycleOwner) mContext, new Observer<List<Memo>>() {
                @Override
                public void onChanged(List<Memo> memos) {
                    ListAdapter adapter = listView.getAdapter();

                        adapter = new ArrayAdapter<>(listView.getContext(), R.layout.memo_list_item, R.id.listTextView, memos);
                        listView.setAdapter(adapter);

            /*        public void onChanged(List<Memo> memos) {
                        ListAdapter adapter = listView.getAdapter();
                        if (adapter instanceof ArrayAdapter) {
                            ArrayAdapter<Memo> arrayAdapter = (ArrayAdapter<Memo>) adapter;

                            arrayAdapter.clear();
                            if (memos != null)
                                arrayAdapter.addAll(memos);


                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            adapter = new ArrayAdapter<>(listView.getContext(), R.layout.memo_list_item, R.id.listTextView, memos);
                            listView.setAdapter(adapter);


                        }
                    }*/


                }
            });

        }


    }


}