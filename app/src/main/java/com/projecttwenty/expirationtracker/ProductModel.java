package com.projecttwenty.expirationtracker;

public class ProductModel {
    String Title,Category,ExpiryDate,ExpiryCycle,Exprice,Reminder,Location,note,img,status,id;

    public ProductModel ()
    {

    }
    public ProductModel(String title, String category, String expiryDate, String expiryCycle, String exprice, String reminder, String location, String note,String img,String status,String id) {
        Title = title;
        Category = category;
        ExpiryDate = expiryDate;
        ExpiryCycle = expiryCycle;
        Exprice = exprice;
        Reminder = reminder;
        Location = location;
        this.note = note;
        this.img = img;
        this.status=status;
        this.id=id;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getExpiryCycle() {
        return ExpiryCycle;
    }

    public void setExpiryCycle(String expiryCycle) {
        ExpiryCycle = expiryCycle;
    }

    public String getExprice() {
        return Exprice;
    }

    public void setExprice(String exprice) {
        Exprice = exprice;
    }

    public String getReminder() {
        return Reminder;
    }

    public void setReminder(String reminder) {
        Reminder = reminder;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
