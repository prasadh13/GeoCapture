//The SQLite database implementation is referred from http://www.vogella.com/tutorials/AndroidSQLite/article.html 
package com.example.geoimg;
public class DatabaseStorage {
	 
    private int id;
    private double latitude;
    private double longitude; 
    private String author;
 
    public DatabaseStorage(){}
 
    public DatabaseStorage(double longitude,double latitude, String author) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
        this.author = author;
    }
 
    //getters & setters
    public double getLatitude() {
        return this.latitude; 
    }
    
    public double getLongitude() {
        return this.longitude; 
    }
    
    public String getAuthor() {
        return this.author; 
    }
    
    
    public void setLatitude(double latitude) {
        this.latitude = latitude; 
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude; 
    }
    
    public void setAuthor(String Author) {
        this.author = Author; 
    }
    
    
    
    
 
    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + latitude + ", author=" + author
                + "]";
    }
}
