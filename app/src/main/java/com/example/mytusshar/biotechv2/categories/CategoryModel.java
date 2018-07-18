package com.example.mytusshar.biotechv2.categories;

/**
 * Created by mytusshar on 28-Mar-17.
 */
public class CategoryModel {

    public String categoryId;
    public String categoryName;
    public String categoryImage;
    public CategoryModel(){

    }

    public void setCategoryId(String id){
        categoryId = id;
    }

    public String getCategoryId(){
        return categoryId;
    }

    public void setCategoryName(String name){
        categoryName = name;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryImage(String url){
        categoryImage = url;
    }

    public String getCategoryImage(){
        return categoryImage;
    }

}
