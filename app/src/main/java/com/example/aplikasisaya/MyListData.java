package com.example.aplikasisaya;

import org.osmdroid.util.GeoPoint;

public class MyListData {
    private String tanggal;
    private String jam;
    private String lintang;
    private String bujur;
    private String magnitude;
    private String kedalaman;
    private String wilayah;
    private String dirasakan;
    private GeoPoint posisi;
    private int imgId;

    public MyListData(String tanggal, String jam, String lintang, String bujur, String magnitude, String kedalaman, String wilayah, String dirasakan, String Coordinates) {
        this.tanggal = tanggal;
        this.jam = jam;
        this.lintang = lintang;
        this.bujur = bujur;
        this.magnitude = magnitude;
        this.kedalaman = kedalaman;
        this.wilayah = wilayah;
        this.dirasakan = dirasakan;
        this.imgId = Float.parseFloat(magnitude) > 5 ? 1 : 0;
        this.posisi = new GeoPoint(Double.parseDouble(Coordinates.split(",")[0]), Double.parseDouble(Coordinates.split(",")[1]));
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getLintang() {
        return lintang;
    }

    public void setLintang(String lintang) {
        this.lintang = lintang;
    }

    public String getBujur() {
        return bujur;
    }

    public void setBujur(String bujur) {
        this.bujur = bujur;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getKedalaman() {
        return kedalaman;
    }

    public void setKedalaman(String kedalaman) {
        this.kedalaman = kedalaman;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public String getDirasakan() {
        return dirasakan;
    }

    public void setDirasakan(String dirasakan) {
        this.dirasakan = dirasakan;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public GeoPoint getPosisi() {
        return posisi;
    }

    public void setPosisi(GeoPoint posisi) {
        this.posisi = posisi;
    }
}