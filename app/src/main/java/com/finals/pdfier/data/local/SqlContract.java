package com.finals.pdfier.data.local;

import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.finals.pdfier.data.local.SqlContract.TableConstants.COLUMN_AUTHOR;
import static com.finals.pdfier.data.local.SqlContract.TableConstants.COLUMN_TITLE;

//
// Created by  on 7/7/2021.
//
public final class SqlContract {
    public SqlContract() {
    }


    public static final String DATABASE_NAME = "pdfier.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "pdf_meta_tbl";

    public static class TableConstants implements BaseColumns {
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_KEYWORDS = "keywords";
        public static final String COLUMN_CREATOR = "creator";
        public static final String COLUMN_PRODUCER = "producer";
        public static final String COLUMN_CREATION_DATE = "creationDate";
        public static final String COLUMN_MOD_DATE = "modDate";
        public static final String COLUMN_TOTAL_PAGES = "total_pages";
        public static final String COLUMN_IMAGE = "bitmap_bytes";
        public static final String COLUMN_ANALYZE_DATE = "analyze_date";
    }

    public static final String SQL_CREATE_PDF_META = "CREATE TABLE pdf_meta_tbl ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TableConstants.COLUMN_TITLE + " CHAR, " +
            TableConstants.COLUMN_AUTHOR + " CHAR, " +
            TableConstants.COLUMN_SUBJECT + " CHAR, " +
            TableConstants.COLUMN_KEYWORDS + " CHAR, " +
            TableConstants.COLUMN_CREATOR + " CHAR, " +
            TableConstants.COLUMN_PRODUCER + " CHAR, " +
            TableConstants.COLUMN_CREATION_DATE + " CHAR, " +
            TableConstants.COLUMN_MOD_DATE + " CHAR, " +
            TableConstants.COLUMN_TOTAL_PAGES + " CHAR, " +
            TableConstants.COLUMN_IMAGE + " BLOB, " +
            TableConstants.COLUMN_ANALYZE_DATE + " LONG " +
            " )";
}
