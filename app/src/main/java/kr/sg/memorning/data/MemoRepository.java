package kr.sg.memorning.data;

import android.app.Application;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

class MemoRepository {
    private static final String TAG = MemoRepository.class.getSimpleName();

    private final MemoDao memoDao;
    private final LiveData<List<Memo>> allMemos;
    private final LiveData<List<Memo>> nameMemos;

    MemoRepository(Application application) {
        MemoRoomDatabase db = MemoRoomDatabase.getDatabase(application);
        memoDao = db.memoDao();
        allMemos = memoDao.getAllMemos();
        nameMemos = memoDao.nameAllMemos();
    }

    public Long insert(Memo memo) {
        try {
            return new AsyncTask<Memo, Void, Long>() {
                @Override
                protected Long doInBackground(Memo... memos) {
                    if (memoDao == null)
                        return -1L;
                    return memoDao.insert(memos[0]);
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);
                    Log.d(TAG, "insert : " + aLong);
                }
            }.execute(memo).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return -1L;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public void deleteMemo(int id) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (memoDao == null)
                    return -1;

                return memoDao.deleteMemo(integers[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "deleteMemo : " + integer);
            }
        }.execute(id);
    }

    public Memo selectMemo(int id) {
        return memoDao.selectMemo(id);
    }



    public LiveData<List<Memo>> getAllMemos() {
        return allMemos;
    }
    public LiveData<List<Memo>> nameAllMemos() {
        return nameMemos;
    }

    public void deleteMemoAll(){
        memoDao.deleteAll();
    }

}