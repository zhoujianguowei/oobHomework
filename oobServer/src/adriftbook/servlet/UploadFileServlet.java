package adriftbook.servlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by Administrator on 2016/5/4.
 */
public class UploadFileServlet extends HttpServlet
{

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doGet(req, resp);
        doPost(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doPost(req, resp);
       /* String uploadRootPath = req.getServletContext().getRealPath("/");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream
                .write(("当前根目录是:" + uploadRootPath).getBytes(Constant.DEFAULT_CODE));*/
//        DiskFileItemFactory diskFileItemFactory=new DiskFileItemFactory();
        System.out.println("请求到达了");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        // 创建文件项目工厂对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置文件上传路径
        String upload = this.getServletContext().getRealPath("/");
        int outIndex = upload.indexOf("out");
        if (outIndex > -1)
        {
            upload = upload.substring(0, outIndex);
            upload+="upload"+File.separator+"bookImage";
        }
        // 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
        String temp = System.getProperty("java.io.tmpdir");
        // 设置缓冲区大小为 5M
        factory.setSizeThreshold(1024 * 1024 * 5);
        // 设置临时文件夹为temp
        factory.setRepository(new File(temp));
        // 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        // 解析结果放在List中
        try
        {
            List<FileItem> list = servletFileUpload.parseRequest(req);
            for (FileItem item : list)
            {
                String name = item.getFieldName();
                InputStream is = item.getInputStream();
                if (name.contains("content"))
                {
                    System.out.println(inputStream2String(is));
                } else if (name.contains("file"))
                {
                    try
                    {
                        inputStream2File(is, upload + "\\" + item.getName());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            out.write("success");
        }
        catch (FileUploadException e)
        {
            e.printStackTrace();
            out.write("failure");
        }
        out.flush();
        out.close();
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
        return baos.toString();
    }
    // 流转化成文件
    public static void inputStream2File(InputStream is, String savePath)
            throws Exception
    {
        System.out.println("文件保存路径为:" + savePath);
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

