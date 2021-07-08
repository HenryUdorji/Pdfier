package com.finals.pdfier.data.models;

//
// Created by  on 7/7/2021.
//
public class PdfMeta {
    private int id;
    private String title;
    private String author;
    private String subject;
    private String keywords;
    private String creator;
    private String producer;
    private String creationDate;
    private String modDate;
    private String totalPages;
    private byte[] bitmapBytes;
    private long analyzeDate;
    //private String author;


    public PdfMeta() {
    }

    public PdfMeta(String title, String author, String subject, String keywords,
                   String creator, String producer, String creationDate,
                   String modDate, String totalPages, byte[] bitmapBytes, long analyzeDate) {
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.keywords = keywords;
        this.creator = creator;
        this.producer = producer;
        this.creationDate = creationDate;
        this.modDate = modDate;
        this.totalPages = totalPages;
        this.bitmapBytes = bitmapBytes;
        this.analyzeDate = analyzeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public byte[] getBitmapBytes() {
        return bitmapBytes;
    }

    public void setBitmapBytes(byte[] bitmapBytes) {
        this.bitmapBytes = bitmapBytes;
    }

    public long getAnalyzeDate() {
        return analyzeDate;
    }

    public void setAnalyzeDate(long analyzeDate) {
        this.analyzeDate = analyzeDate;
    }
}
