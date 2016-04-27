//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : EBook.java
//  @ Date : 2016/4/23
//  @ Author : 
//
//


package adriftbook.entity;

public class EBook extends AdriftBook
{
	private String ebookUrl;
	public EBook(String name,String ebookUrl)
	{
		super(name);
		this.ebookUrl=ebookUrl;
		type=1;
	}
	public EBook(String name,String author,String ebookUrl)
	{
		super(name,author);
		this.ebookUrl=ebookUrl;
	}
	@Override public String toString()
	{
		return  super.toString()+"EBook{" +
				"ebookUrl='" + ebookUrl + '\'' +
				'}';
	}
	public String getEbookUrl()
	{
		return ebookUrl;
	}
	public void setEbookUrl(String ebookUrl)
	{
		this.ebookUrl = ebookUrl;
	}
}
