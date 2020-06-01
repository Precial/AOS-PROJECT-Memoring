package kr.sg.memorning.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    private static KeyboardUtil instance;
    private KeyboardUtil() {  }

    public static KeyboardUtil getInstance(){
        if(instance == null){
            instance = new KeyboardUtil();
        }
        return instance;
    }

    public void hideKeyboard(Context context){
        // 키보드 숨기기
        InputMethodManager immhide = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showKeyboard(Context context){
        // 키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
