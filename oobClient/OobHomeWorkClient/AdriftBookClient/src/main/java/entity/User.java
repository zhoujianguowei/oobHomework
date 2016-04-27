//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : User.java
//  @ Date : 2016/4/23
//  @ Author :
//
//
package entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
public class User
{

    private String userName;
    private int userId;
    private String password;
    private Calendar registerDate;
    private int userLevel;
    ArrayList<Post> posts;
    public ArrayList<Post> getPosts()
    {
        return posts;
    }
    public void setPosts(ArrayList<Post> posts)
    {
        this.posts = posts;
    }
    public User(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        registerDate = Calendar.getInstance();
        posts = new ArrayList<Post>();
    }
    @Override public String toString()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId=" + userId +
                ", password='" + password + '\'' +
                ", registerDate=" + simpleDateFormat.format(registerDate.getTime()) +
                ", userLevel=" + userLevel +
                ", posts=" + posts +
                '}';
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public Calendar getRegisterDate()
    {
        return registerDate;
    }
    public void setRegisterDate(Calendar registerDate)
    {
        this.registerDate = registerDate;
    }
    public int getUserLevel()
    {
        return userLevel;
    }
    public void setUserLevel(int userLevel)
    {
        this.userLevel = userLevel;
    }
    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
}