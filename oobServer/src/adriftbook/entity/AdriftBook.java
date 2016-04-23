//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : AdriftBook.java
//  @ Date : 2016/4/23
//  @ Author : 
//
//
package adriftbook.entity;
public class AdriftBook
{

    private int bookId;
    private String name;
    private String author;
    private float rating;
    public AdriftBook(String name)
    {
        this.name=name;
    }
    public AdriftBook(String name,String author)
    {
        this(name);
        this.author=author;
    }

    public int getRiviewPeopleCount()
    {
        return riviewPeopleCount;
    }
    public void setRiviewPeopleCount(int riviewPeopleCount)
    {
        this.riviewPeopleCount = riviewPeopleCount;
    }
    private int riviewPeopleCount;//对该书评论的个数
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
    public float getRating()
    {
        return rating;
    }
    public void setRating(float rating)
    {
        this.rating = rating;
    }
}