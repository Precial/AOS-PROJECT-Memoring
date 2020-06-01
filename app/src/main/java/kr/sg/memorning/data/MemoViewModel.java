package kr.sg.memorning.data;


import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MemoViewModel extends AndroidViewModel {
    private static final String TAG =  MemoViewModel.class.getSimpleName();
    private final MemoRepository repository;
    private final LiveData<List<Memo>> allMemos;
    private final LiveData<List<Memo>> nameMemos;

    public MemoViewModel(Application application) {
        super(application);
        repository = new MemoRepository(application);
        allMemos =  repository.getAllMemos();
        nameMemos = repository.nameAllMemos();
    }


    @SuppressLint("NewApi")
    public Integer insert(Memo memo) { return Math.toIntExact(repository.insert(memo));}
    public void deleteMemo(int id) { repository.deleteMemo(id); }
    public Memo selectMemo(int id) { return repository.selectMemo(id); }
    public LiveData<List<Memo>> getAllMemos() { return allMemos; }
    public LiveData<List<Memo>> nameAllMemos() { return nameMemos; }

    public void deleteMemoAll(){repository.deleteMemoAll();};


}
