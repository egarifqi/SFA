package com.example.bismillahcoba;

/**
 * Created by isramela on 05/03/18.
 */

public class ProdukDetail {
    private String default_code;
    private String name;
    private String barcode;
    private String hargagt;
    private String hargamt;
    private String stock;
    private String qty;

    public String getStock (){
        return stock;
    }

    public void setStock(String stock){
        this.stock=stock;
    }

    public String getQty (){
        return qty;
    }

    public void setQty(String qty){
        this.qty=qty;
    }

    public String getHargagt() {
        return hargagt;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setHargagt(String hargagt) {
        this.hargagt = hargagt;
    }

    public String getProdukKode (){
        return default_code;
   }

    public void setProdukKode(String default_code){
       this.default_code =default_code;
   }

    public String getNama() {
        return name;
    }

    public void setNama(String name) {
        this.name =name;

    }
    public String getHargamt(){
        return hargamt;
    }

    public void setHargamt(String hargamt){
        this.hargamt=hargamt;
    }


}
