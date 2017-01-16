package com.ustclin.petchicken.bean;

/**
 * author: LinKun
 * email: linkun199011@163.com
 * created on: 2017/1/9:21:49
 * description
 */
public class CustomConverBean {
    private int id;
    private String petContent = null;
    private String masterContent = null;

    public CustomConverBean(String petContent, String masterContent) {
//        this.id = id;
        this.petContent = petContent;
        this.masterContent = masterContent;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetContent() {
        return petContent;
    }

    public void setPetContent(String petContent) {
        this.petContent = petContent;
    }

    public String getMasterContent() {
        return masterContent;
    }

    public void setMasterContent(String masterContent) {
        this.masterContent = masterContent;
    }
}
