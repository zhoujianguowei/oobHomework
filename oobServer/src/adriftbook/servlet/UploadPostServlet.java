package adriftbook.servlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.EBook;
import adriftbook.entity.Post;
import adriftbook.entity.PostContent;
import adriftbook.entity.SubUploadFile;
import adriftbook.entity.User;
import adriftbook.utils.CommonUtils;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.MysqlDbConnection;

import static adriftbook.utils.CommonUtils.transformFileNameToOctalByteString;
/**
 * Created by Administrator on 2016/5/4.
 */
public class UploadPostServlet extends HttpServlet
{

    //    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException
//    {
////        super.doGet(req, resp);
//        doPost(req, resp);
//    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doPost(req, resp);
       /* String uploadRootPath = req.getServletContext().getRealPath("/");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream
                .write(("当前根目录是:" + uploadRootPath).getBytes(Constant.DEFAULT_CODE));*/
//        DiskFileItemFactory diskFileItemFactory=new DiskFileItemFactory();
        resp.setContentType("text/html");
        // 创建文件项目工厂对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置文件上传路径
        String uploadRoot = this.getServletContext().getRealPath("/");
        int outIndex = uploadRoot.indexOf("out");
        if (outIndex > -1)
        {
            uploadRoot = uploadRoot.substring(0, outIndex);
            uploadRoot += "upload" + File.separator;
        }
        // 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
        String temp = System.getProperty("java.io.tmpdir");
        // 设置缓冲区大小为 5M
        factory.setSizeThreshold(1024 * 1024 * 5);
        // 设置临时文件夹为temp
        factory.setRepository(new File(temp));
        // 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setHeaderEncoding(Constant.DEFAULT_CODE);
        // 解析结果放在List中
        JSONObject resJObj = new JSONObject();
        ServletOutputStream outputStream = resp.getOutputStream();
        try
        {
            resJObj.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
            List<FileItem> list = servletFileUpload.parseRequest(req);
            int userId = -1, postType = 0, bookCount = 0;
            String postTitle = null, postContent = null;
            for (FileItem item : list)
            {
                String name = item.getFieldName();
                if (name.equals("content"))
//                    inputStream2File(item.getInputStream(),
//                            uploadRoot +
//                                    File.separator + item.getName());
                    postContent = inputStream2String(item.getInputStream());
                if (name.equals("bookcount"))
                    bookCount = Integer.parseInt(item.getString());
                if (name.equals(User.USER_ID))
                    userId = Integer.parseInt(item.getString());
                if (name.equals(Post.POST_TYPE))
                    postType = Integer.parseInt(item.getString());
                if (name.equals(Post.POST_TITLE))
                    postTitle = item.getString();
                if (name.equals(Post.POST_CONTENT))
                    postContent = item.getString();
            }
            if (userId == -1 || postType == 0 || bookCount == 0 ||
                    postTitle == null || postContent == null)
            {
                resJObj.put(Constant.INFO_KEY, "上传文件不合法");
                outputStream
                        .write(resJObj.toString().getBytes(Constant.DEFAULT_CODE));
                return;
            }
            ArrayList<SubUploadFile> uploadFileList = new ArrayList<>();
            for (int i = 0; i < bookCount; i++)
                uploadFileList.add(new SubUploadFile());
            for (FileItem listItem : list)
            {
                String fieldName = listItem.getFieldName();
                InputStream is = listItem.getInputStream();
                String[] indexItem = getUploadFileIndex(fieldName);
                if (indexItem != null)
                {
                    int index = Integer.parseInt(indexItem[0]);
                    String item = indexItem[1];
                    if (item.equals(SubUploadFile.FILE))
                    {
                        uploadFileList.get(index)
                                .setFile(new File(listItem.getName()));
                        uploadFileList.get(index).setFileInputStream(is);
                    } else if (item.equals(SubUploadFile.IMAGE_FILE))
                    {
                        uploadFileList.get(index)
                                .setImageFile(new File(listItem.getName()));
                        uploadFileList.get(index).setImageInputStream(is);
                    } else if (item.equals(SubUploadFile.FILE_AUTHOR))
                        uploadFileList.get(index)
                                .setFileAuthor(inputStream2String(is));
                    else if (item.equals(SubUploadFile.FILE_NAME))
                        uploadFileList.get(index)
                                .setFileName(inputStream2String(is));
                }
            }
            for (int i = 0; i < uploadFileList.size(); i++)
                writeFileToStorage(uploadFileList.get(i), uploadRoot, userId);
            //数据库相关操作
            Post post = new Post();
            post.setPostContent(new PostContent(postContent));
            post.setPostTitle(postTitle);
            post.setPostType(postType);
            post.setReadCount(0);
            post.setPostUser(MysqlCheckUtil.getUserInfo(userId));
            ArrayList<AdriftBook> adriftBookArrayList = getAdriftbookList(
                    uploadFileList, postType);
            //做原子提交操作
            Connection connection = MysqlDbConnection.getConnection();
            connection.setAutoCommit(false);
            int postId = insertPostTable(connection, post);
            if (!insertBookTable(connection, postId, adriftBookArrayList, postType))
            {
                connection.rollback();
                resJObj.put(Constant.STATUS_KEY, "数据库插入异常");
                outputStream
                        .write(resJObj.toString().getBytes(Constant.DEFAULT_CODE));
                connection.close();
                return;
            }
            connection.commit();
            connection.close();
            resJObj.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
            resJObj.put(Constant.INFO_KEY, "文件上传成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        outputStream.write(resJObj.toString().getBytes(Constant.DEFAULT_CODE));
    }
    private boolean insertBookTable(Connection connection, int postId,
                                    ArrayList<AdriftBook> adriftBookArrayList,
                                    int postType)
    {
        ResultSet rSet = MysqlDbConnection
                .getResultSet(connection, "select max(book_id) from book");
        int bookId = -1;
        try
        {
            rSet.next();
            bookId = rSet.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        bookId++;
        for (AdriftBook adriftBook : adriftBookArrayList)
        {
            String insertBookStr = null;
            if (postType == Post.EBOOK_AREA)
            {
                EBook eBook = (EBook) adriftBook;
                if (eBook.getBookImageUrl() != null)
                    insertBookStr =
                            "insert into book(book_id,bookname,author,type,bookimageurl,ebookurl,post_id) values(" +
                                    bookId + ",'" + eBook.getBookName() + "','" +
                                    eBook.getAuthor() + "'," + eBook.getType() +
                                    ",'" + eBook.getBookImageUrl() + "','" +
                                    eBook.getEbookUrl() + "'," + postId + ")";
                else
                    insertBookStr =
                            "insert into book(book_id,bookname,author,type,ebookurl,post_id) values(" +
                                    bookId + ",'" + eBook.getBookName() + "','" +
                                    eBook.getAuthor() + "'," + eBook.getType() +
                                    ",'" +
                                    eBook.getEbookUrl() + "'," + postId + ")";
            } else
            {
                if (adriftBook.getBookImageUrl() != null)
                    insertBookStr =
                            "insert into book(book_id,bookname,author,type,bookimageurl,post_id) values(" +
                                    bookId + ",'" + adriftBook.getBookName() +
                                    "','" +
                                    adriftBook.getAuthor() + "'," +
                                    adriftBook.getType() +
                                    ",'" + adriftBook.getBookImageUrl() + "'," +
                                    postId + ")";
                else
                    insertBookStr =
                            "insert into book(book_id,bookname,author,type,post_id) values(" +
                                    bookId + ",'" + adriftBook.getBookName() +
                                    "','" +
                                    adriftBook.getAuthor() + "'," +
                                    adriftBook.getType() +
                                    "," +
                                    postId + ")";
            }
            if (
                    !MysqlDbConnection
                            .executeWithoutCloseConnection(connection,
                                    insertBookStr))
                return false;
            bookId++;
        }
        return true;
    }
    /**
     * 向post表格插入数据
     * @param post
     * @return
     */
    private int insertPostTable(Connection connection, Post post)
    {
        ResultSet rSet = null;
        rSet = MysqlDbConnection
                .getResultSet(connection, "select max(post_id) from post");
        int insertPostId = -1;
        try
        {
            rSet.next();
            int maxId = rSet.getInt(1);
            post.setPostId(maxId + 1);
            insertPostId = maxId + 1;
            String insertPostSqlString =
                    "insert into post(post_id,posttitle,content,posttype,postdate,readcount,user_id) values(" +
                            post.getPostId() + ",'" + post.getPostTitle() + "','" +
                            post.getPostContent().getPostContentDetail() + "'," +
                            post.getPostType() + "," +
                            Calendar.getInstance(Locale.CHINA).getTimeInMillis() + "," +
                            post.getReadCount() + "," +
                            post.getPostUser().getUserId() + ")";
            if (!MysqlDbConnection
                    .executeWithoutCloseConnection(connection, insertPostSqlString))
                return -1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return insertPostId;
    }
    private ArrayList<AdriftBook> getAdriftbookList(
            ArrayList<SubUploadFile> uploadFileList, int postType)
    {
        ArrayList<AdriftBook> adriftBooks = new ArrayList<AdriftBook>();
        if (postType == Post.EBOOK_AREA)
            for (SubUploadFile uploadFile : uploadFileList)
            {
                EBook eBook = new EBook();
                eBook.setEbookUrl(uploadFile.getFileUrl());
                eBook.setBookImageUrl(uploadFile.getImageFileUrl());
                eBook.setAuthor(uploadFile.getFileAuthor());
                eBook.setBookName(uploadFile.getFileName());
                eBook.setType(AdriftBook.EBOOK);
                adriftBooks.add(eBook);
            }
        else
        {
            for (SubUploadFile uploadFile : uploadFileList)
            {
                AdriftBook adriftBook = new AdriftBook();
                adriftBook.setBookImageUrl(uploadFile.getImageFileUrl());
                adriftBook.setAuthor(uploadFile.getFileAuthor());
                adriftBook.setBookName(uploadFile.getFileName());
                adriftBook.setType(AdriftBook.ENTITYBOOK);
                adriftBooks.add(adriftBook);
            }
        }
        return adriftBooks;
    }
    /**
     * 根据posttype写入到指定的文件位置
     * @param subUploadFile
     * @param userId
     */
    private void writeFileToStorage(SubUploadFile subUploadFile, String uploadRoot,
                                    int userId)
    {
        File imageDir = new File(uploadRoot, "bookImage");
        File fileDir = new File(uploadRoot, "ebookFile");
        if (!imageDir.exists())
            imageDir.mkdirs();
        if (!fileDir.exists())
            fileDir.mkdirs();
        File imageFile = subUploadFile.getImageFile();
        File file = subUploadFile.getFile();
        Calendar calendar = Calendar.getInstance();
        String extentionName = null;
        if (imageFile != null)
        {
            int index = imageFile.getName().lastIndexOf(".");
            String imageName = userId + "" + calendar.getTimeInMillis() +
                    imageFile.getName().substring(index);
            imageName = CommonUtils.transformFileNameToOctalByteString(
                    imageName);
            subUploadFile
                    .setImageFileUrl(imageName);
            inputStream2File(subUploadFile.getImageInputStream(), new File(imageDir,
                    imageName));
        }
        if (file != null)
        {
            int index = file.getName().lastIndexOf(".");
            String fileName = userId + "" + calendar.getTimeInMillis() +
                    file.getName().substring(index);
            fileName = transformFileNameToOctalByteString(fileName);
            subUploadFile.setFileUrl(fileName);
            inputStream2File(subUploadFile.getFileInputStream(),
                    new File(fileDir,
                            fileName));
        }
    }
    /**
     * 获取上传文件的文件名称，File类型通过Map形式上传，只能获得文件名
     * 获取不到对应的输入流
     * @param fileItem
     * @return
     */
    private String getUploadFileName(FileItem fileItem)
    {
        String fileName = null;
        if (fileItem instanceof DiskFileItem)
        {
            DiskFileItem diskFileItem = (DiskFileItem) fileItem;
            try
            {
                Field cachedContentField = diskFileItem.getClass()
                        .getDeclaredField("cachedContent");
                cachedContentField.setAccessible(true);
                byte[] cachedContent = (byte[]) cachedContentField.get(diskFileItem);
                fileName = new String(cachedContent, Constant.DEFAULT_CODE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return fileName;
    }
    /**
     * 返回post[index][item]其中的index和item
     * 中括号是特殊字符需要加双斜杠
     * @param fieldName
     * @return
     */
    private String[] getUploadFileIndex(String fieldName)
    {
        String[] indexItem = null;
        String reg = "post\\[(\\d+)\\]\\[(\\S+)\\]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fieldName);
        if (matcher.find())
        {
            indexItem = new String[2];
            indexItem[0] = matcher.group(1);
            indexItem[1] = matcher.group(2);
        }
        return indexItem;
    }
    // 流转化成字符串
    public static String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1)
        {
            baos.write(i);
        }
        byte[] byteArray = baos.toByteArray();
        baos.close();
        return new String(byteArray, Constant.DEFAULT_CODE);
    }
    public static void inputStream2File(InputStream is, File saveFile)
    {
        try
        {
            inputStream2File(is, saveFile.getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // 流转化成文件
    public static void inputStream2File(InputStream is, String savePath)
            throws Exception
    {
//        System.out.println("文件保存路径为:" + savePath);
        File file = new File(savePath);
        InputStream inputSteam = is;
        BufferedInputStream fis = new BufferedInputStream(inputSteam);
        FileOutputStream fos = new FileOutputStream(file);
        int f;
        while ((f = fis.read()) != -1)
        {
            fos.write(f);
        }
        fos.flush();
        fos.close();
        fis.close();
        inputSteam.close();
    }
}

