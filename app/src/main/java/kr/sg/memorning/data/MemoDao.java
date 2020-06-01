package kr.sg.memorning.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MemoDao {

    @Insert(onConflict = REPLACE) // (onConflict = REPLACE) 해당하는곳에 데이터가 있으면 Update, 없으면 Insert
    Long insert(Memo memo);

    @Query("DELETE FROM memo_table WHERE id = :id")
    int deleteMemo(int id);


    @Query("SELECT * from memo_table ORDER BY id ASC")
    LiveData<List<Memo>> getAllMemos();

    @Query("SELECT * from memo_table ORDER BY memoName ASC")
    LiveData<List<Memo>> nameAllMemos();

    /* LiveData 사용하는 이유 메인 엑티비티에서 실시간으로 DB에 저장되어 있는 메모들을 불러와 출력하는데
       메모들에 대한 추가, 갱신 , 삭제가 될 경우 실시간으로 보여줘야 하기 때문에 사용.
    */


    @Query("SELECT * FROM memo_table WHERE id = :id")
    Memo selectMemo(int id);


    @Query("DELETE FROM memo_table")
    void deleteAll();
}
