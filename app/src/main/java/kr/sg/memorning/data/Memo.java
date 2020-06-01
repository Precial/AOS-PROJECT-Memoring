package kr.sg.memorning.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "memo_table",indices={@Index(value={"id"},unique = true)})
public class Memo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name =  "memoName")
    private String memoName;

    @ColumnInfo(name = "fontColor")
    private String fontColor;

    @ColumnInfo(name = "bgColor")
    private String bgColor;

    @ColumnInfo(name = "fontStyle")
    private String fontStyle;

    @ColumnInfo(name = "fontSize")
    private int fontSize;

    @ColumnInfo(name = "writeTime" )
    private String writeTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(@NonNull String memoName) {
        this.memoName = memoName;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    @NonNull
    @Override
    public String toString(){return String.format("%s",memoName);}

}
