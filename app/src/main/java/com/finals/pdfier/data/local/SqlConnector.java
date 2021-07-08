package com.finals.pdfier.data.local;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.finals.pdfier.data.local.SqlContract.TableConstants;
import com.finals.pdfier.data.models.PdfMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.provider.BaseColumns._ID;

//
// Created by  on 7/7/2021.
//
public class SqlConnector extends SQLiteOpenHelper {

    @SuppressLint("StaticFieldLeak")
    private static SqlConnector instance;

    public synchronized static SqlConnector getInstance(Context context) {
        if (instance == null) {
            instance = new SqlConnector(context.getApplicationContext());
        }
        return instance;
    }

    public SqlConnector(@Nullable Context context) {
        super(context, SqlContract.DATABASE_NAME, null, SqlContract.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlContract.SQL_CREATE_PDF_META);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SqlContract.DATABASE_TABLE);
        onCreate(db);
    }

    public void savePdfMeta(PdfMeta pdfMeta) {
        SQLiteDatabase database = instance.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableConstants.COLUMN_TITLE, pdfMeta.getTitle());
            contentValues.put(TableConstants.COLUMN_AUTHOR, pdfMeta.getAuthor());
            contentValues.put(TableConstants.COLUMN_KEYWORDS, pdfMeta.getKeywords());
            contentValues.put(TableConstants.COLUMN_CREATION_DATE, pdfMeta.getCreationDate());
            contentValues.put(TableConstants.COLUMN_CREATOR, pdfMeta.getCreator());
            contentValues.put(TableConstants.COLUMN_MOD_DATE, pdfMeta.getModDate());
            contentValues.put(TableConstants.COLUMN_PRODUCER, pdfMeta.getProducer());
            contentValues.put(TableConstants.COLUMN_SUBJECT, pdfMeta.getSubject());
            contentValues.put(TableConstants.COLUMN_TOTAL_PAGES, pdfMeta.getTotalPages());
            contentValues.put(TableConstants.COLUMN_IMAGE, pdfMeta.getBitmapBytes());
            contentValues.put(TableConstants.COLUMN_ANALYZE_DATE, pdfMeta.getAnalyzeDate());

            database.insertWithOnConflict(SqlContract.DATABASE_TABLE, null, contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*********************************************
     * Returns the whole saved data pertaining All
     * @param
     * @return
     */
    public List<PdfMeta> getAllPdfMeta() {
        List<PdfMeta> pdfMetas = new ArrayList<>();
        String query = "SELECT * FROM " + SqlContract.DATABASE_TABLE + " ORDER BY analyze_date DESC";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PdfMeta pdfMeta = new PdfMeta();
                pdfMeta.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                pdfMeta.setTitle(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_TITLE)));
                pdfMeta.setAuthor(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_AUTHOR)));
                pdfMeta.setCreationDate(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_CREATION_DATE)));
                pdfMeta.setCreator(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_CREATOR)));
                pdfMeta.setKeywords(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_KEYWORDS)));
                pdfMeta.setModDate(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_MOD_DATE)));
                pdfMeta.setProducer(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_PRODUCER)));
                pdfMeta.setTotalPages(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_TOTAL_PAGES)));
                pdfMeta.setSubject(cursor.getString(cursor.getColumnIndex(TableConstants.COLUMN_SUBJECT)));
                pdfMeta.setBitmapBytes(cursor.getBlob(cursor.getColumnIndex(TableConstants.COLUMN_IMAGE)));

                pdfMetas.add(pdfMeta);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return pdfMetas;
    }
}
