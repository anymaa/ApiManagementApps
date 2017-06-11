package com.can.anyma.apimanagementapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anyma on 16.05.2017.
 */

public class Sorun {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Yetkili")
    @Expose
    private String yetkili;
    @SerializedName("Bolge")
    @Expose
    private String bolge;
    @SerializedName("Aciklama")
    @Expose
    private String aciklama;
    @SerializedName("Tarih")
    @Expose
    private Object tarih;
    @SerializedName("Gorsel")
    @Expose
    private Object gorsel;
    @SerializedName("Cozuldu")
    @Expose
    private Object cozuldu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYetkili() {
        return yetkili;
    }

    public void setYetkili(String yetkili) {
        this.yetkili = yetkili;
    }

    public String getBolge() {
        return bolge;
    }

    public void setBolge(String bolge) {
        this.bolge = bolge;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Object getTarih() {
        return tarih;
    }

    public void setTarih(Object tarih) {
        this.tarih = tarih;
    }

    public Object getGorsel() {
        return gorsel;
    }

    public void setGorsel(Object gorsel) {
        this.gorsel = gorsel;
    }

    public Object getCozuldu() {
        return cozuldu;
    }

    public void setCozuldu(Object cozuldu) {
        this.cozuldu = cozuldu;
    }




    public Sorun(String yetkili, String bolge, String aciklama) {
        this.yetkili = yetkili;
        this.bolge = bolge;
        this.aciklama = aciklama;

    }
}
