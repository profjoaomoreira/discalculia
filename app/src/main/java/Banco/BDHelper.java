package Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

    static int VERSAO = 1;
    static String DATABASE = "CalculeiBD.db";

    public BDHelper(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Ranking(Codigo integer primary key autoincrement, " +
                "nome text not null," +
                "pontuacao int not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
