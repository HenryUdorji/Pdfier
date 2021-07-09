package com.finals.pdfier.ui;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finals.pdfier.data.models.PdfMeta;
import com.finals.pdfier.databinding.ItemPdfMetaCardBinding;
import com.finals.pdfier.utils.BitmapUtils;

import java.util.List;

public class PdfMetaRVAdapter extends RecyclerView.Adapter<PdfMetaRVAdapter.PdfViewHolder> {
    private final List<PdfMeta> pdfMetas;

    public PdfMetaRVAdapter(List<PdfMeta> pdfMetas) {
        this.pdfMetas = pdfMetas;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPdfMetaCardBinding binding = ItemPdfMetaCardBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PdfViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfMeta pdfMeta = pdfMetas.get(position);

        holder.binding.docPageCount.setText("Total pages: " + pdfMeta.getTotalPages());
        holder.binding.title.setText(pdfMeta.getTitle());
        holder.binding.date.setText(pdfMeta.getCreationDate());
        Bitmap bitmap = BitmapUtils.convertByteArrayToBitmap(pdfMeta.getBitmapBytes());
        holder.binding.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return pdfMetas.size();
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        private final ItemPdfMetaCardBinding binding;

        public PdfViewHolder(@NonNull ItemPdfMetaCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
