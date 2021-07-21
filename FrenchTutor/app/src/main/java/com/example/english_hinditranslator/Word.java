package com.example.english_hinditranslator;

public class Word {
    //Attributes
    public final int NO_IMAGE_VIEW = -1;
    private final String mdefaultTranslation;
    private final String mfrenchTranslation;
    private final int mimageResource;
    private final int mmediaResource;

    public Word(String defaultTranslation, String frenchTranslation, int mediaResource){
        this.mdefaultTranslation=defaultTranslation;
        this.mfrenchTranslation=frenchTranslation;
        mmediaResource = mediaResource;
        mimageResource = NO_IMAGE_VIEW;
    }

    public Word(String mdefaultTranslation, String mfrenchTranslation, int mimageResource, int mediaResource) {
        this.mdefaultTranslation = mdefaultTranslation;
        this.mfrenchTranslation = mfrenchTranslation;
        this.mimageResource = mimageResource;
        mmediaResource = mediaResource;
    }

    //Methods
    public String getdefaultTranslation(){
        return this.mdefaultTranslation;
    }

    public String getFrenchTranslation() {
        return mfrenchTranslation;
    }

    public int getImageResourceId(){return mimageResource;}

    public int getMmediaResource() {
        return mmediaResource;
    }
}
