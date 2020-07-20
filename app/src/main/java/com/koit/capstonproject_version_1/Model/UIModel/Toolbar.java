package com.koit.capstonproject_version_1.Model.UIModel;

public class Toolbar {

    private static Toolbar mInstance;

    public Toolbar() {
    }

    public static Toolbar getInstance() {
        if (mInstance == null) {
            mInstance = new Toolbar();
        }
        return mInstance;
    }

    public void setTvToolbar(String s){
        
    }


}
