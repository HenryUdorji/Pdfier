package com.finals.pdfier.ui;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.finals.pdfier.data.local.SqlConnector;
import com.finals.pdfier.data.models.PdfMeta;
import com.finals.pdfier.databinding.ActivityHomeBinding;
import com.finals.pdfier.databinding.DialogAnalyzePdfBinding;
import com.finals.pdfier.databinding.DialogErrorPdfBinding;
import com.finals.pdfier.utils.BitmapUtils;
import com.finals.pdfier.utils.BottomNavigationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private ActivityHomeBinding binding;
    private ActivityResultLauncher<String[]> getPdfLauncher;
    private Dialog analysisDialog;
    private PdfiumCore pdfiumCore;
    private PdfDocument pdfDocument;
    private Bitmap pdfBitmap;
    private boolean isClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigation();
        initViews();
    }

    private void initViews() {
        getPdfLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(), uri -> {
                    if (uri != null) {
                        //Check if the picked file is actually a pdf
                        //If it is a pdf then we extract the bitmap from it
                        //Else show the error dialog
                        String extension  = getMimeType(uri);
                        boolean isUriPdf;
                        if (extension.toLowerCase(Locale.ROOT).equals("pdf")) {
                            isUriPdf = true;
                        }else isUriPdf = false;

                        Toast.makeText(this, extension, Toast.LENGTH_LONG).show();

                        if (isUriPdf) {
                            binding.scrollView.setVisibility(View.VISIBLE);
                            binding.documentInfoInclude.getRoot().setVisibility(View.GONE);
                            binding.noItemInclude.getRoot().setVisibility(View.GONE);
                            createBitmapFromPdf(uri);
                        }else {
                            binding.scrollView.setVisibility(View.GONE);
                            showErrorDialog(null);
                        }

                    }else {
                        binding.noItemInclude.getRoot().setVisibility(View.GONE);
                        binding.scrollView.setVisibility(View.GONE);
                    }
                });

        binding.pickPdfFab.setOnClickListener(v -> {
            if (!isClosed && pdfDocument != null && pdfiumCore != null) {
                pdfiumCore.closeDocument(pdfDocument); // important!
            }

            isClosed = true;
            getPdfLauncher.launch(new String[]{"application/*"});
        });

        binding.analyzePdfBtn.setOnClickListener(v -> {
            showAnalysisDialog();
            new Handler().postDelayed(() -> {
                analysisDialog.dismiss();
                binding.documentInfoInclude.getRoot().setVisibility(View.VISIBLE);
                setupPdfMetaData(pdfiumCore, pdfDocument);
            }, 2000);
        });
    }

    private void showAnalysisDialog() {
        analysisDialog = new Dialog(HomeActivity.this);
        DialogAnalyzePdfBinding analyzePdfBinding =
                DialogAnalyzePdfBinding.inflate(analysisDialog.getLayoutInflater());
        analysisDialog.setContentView(analyzePdfBinding.getRoot());
        analysisDialog.setCanceledOnTouchOutside(false);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        analysisDialog.getWindow().setBackgroundDrawable(inset);
        analysisDialog.show();
    }

    private void showErrorDialog(String message) {
        Dialog errorDialog = new Dialog(HomeActivity.this);
        DialogErrorPdfBinding errorPdfBinding =
                DialogErrorPdfBinding.inflate(errorDialog.getLayoutInflater());
        errorDialog.setContentView(errorPdfBinding.getRoot());
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        errorDialog.getWindow().setBackgroundDrawable(inset);
        errorDialog.show();

        if (message != null) {
            errorPdfBinding.noItemMsg.setText(message);
        }
    }

    private void createBitmapFromPdf(Uri pdfUri)  {
        try {
            int pageNum = 0;
            pdfiumCore = new PdfiumCore(this);
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            pdfDocument = pdfiumCore.newDocument(fd);

            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);

            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param
            pdfBitmap = bitmap;
            binding.pdfImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            //If the file is a pdf but we are unable to generate
            //a bitmap from it.It means the pdf file is corrupted
            showErrorDialog("This PDF file is corrupted!");
            binding.scrollView.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void setupPdfMetaData(PdfiumCore pdfiumCore, PdfDocument pdfDocument) {
        PdfDocument.Meta meta = pdfiumCore.getDocumentMeta(pdfDocument);

        int pageCount = pdfiumCore.getPageCount(pdfDocument);

        Log.d(TAG, "setupPdfMetaData: "+meta.getTitle());

        binding.documentInfoInclude.title.setText("Title = " + (!meta.getTitle().isEmpty() ?
                meta.getTitle() : "Not available"));
        binding.documentInfoInclude.author.setText("Author = " + (!meta.getAuthor().isEmpty() ?
                meta.getAuthor() : "Not available"));
        binding.documentInfoInclude.subject.setText("Subject = " + (!meta.getSubject().isEmpty() ?
                meta.getSubject() : "Not available"));
        binding.documentInfoInclude.keywords.setText("Keywords = " + (!meta.getKeywords().isEmpty() ?
                meta.getKeywords() : "Not available"));
        binding.documentInfoInclude.creator.setText("Creator = " + (!meta.getCreator().isEmpty() ?
                meta.getCreator() : "Not available"));
        binding.documentInfoInclude.producer.setText("Producer = " + (!meta.getProducer().isEmpty() ?
                meta.getProducer() : "Not available"));
        binding.documentInfoInclude.creationDate.setText("Creation Date = " + (!meta.getCreationDate().isEmpty() ?
                meta.getCreationDate() : "Not available"));
        binding.documentInfoInclude.modDate.setText("ModDate = " + (!meta.getModDate().isEmpty() ?
                meta.getTitle() : "Not available"));
        binding.documentInfoInclude.docPageCount.setText("Total Pages = " + pageCount);


        PdfMeta pdfMeta = new PdfMeta(
                meta.getTitle(),
                meta.getAuthor(),
                meta.getSubject(),
                meta.getKeywords(),
                meta.getCreator(),
                meta.getProducer(),
                meta.getCreationDate(),
                meta.getModDate(),
                String.valueOf(pageCount),
                BitmapUtils.convertBitmapToByteArray(pdfBitmap),
                System.currentTimeMillis()
        );
        try {
            SqlConnector.getInstance(this).savePdfMeta(pdfMeta);
        }catch (Exception e) {
            Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

    }

    private void setupBottomNavigation() {
        BottomNavigationUtils.enableBottomNavigation(this, binding.navigation);
        Menu menu = binding.navigation.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
    }

    public String getMimeType(Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(this.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isClosed) {
            pdfiumCore.closeDocument(pdfDocument); // important!
        }
        isClosed = true;
    }
}