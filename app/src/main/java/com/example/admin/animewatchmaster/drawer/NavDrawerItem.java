package com.example.admin.animewatchmaster.drawer;

/**
 * Created by abraham on 11/6/2016.
 */
public class NavDrawerItem {

    private String title;
    private String subtitle;
    private int icon;
    private int chooseicon = 0;
    private boolean isChecked = false;


    public NavDrawerItem(String title){
        this.title = title;
    }

    public NavDrawerItem(String title,int icon) {
        this.title = title;
        this.chooseicon = icon;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getChooseicon(){return chooseicon;}

    public void setChooseicon(int ico){chooseicon = ico;}

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        if(subtitle == null) {
            return "";
        }
        return subtitle;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
