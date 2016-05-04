package adriftbook.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.Post;
import adriftbook.entity.PostContent;
import adriftbook.entity.User;
public class MysqlDbConnection
{

    private static String url = "jdbc:mysql://localhost:3306/adriftbook";
    private static String user = "zhoujianguo";
    private static String password = "hello";
    private static Connection conn;
    private static Object obj = new Object();
    public static Connection getConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return conn;
    }
    public static synchronized void execute(String sqlString)
    {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.execute(sqlString);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        closeConnection();
    }
    public static ResultSet getResultSet(String sqlString)
    {
        ResultSet rSet = null;
        try
        {
            synchronized (MysqlDbConnection.class)
            {
                Connection conn = getConnection();
                rSet = conn.prepareStatement(sqlString).executeQuery();
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rSet;
    }
    public static void closeConnection()
    {
        if (conn != null)
            try
            {
                conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    private static ArrayList<User> getUsers()
    {
        ArrayList<User> users = new ArrayList<>();
        String originFirstName =
                "赵\t钱\t孙\t李\t周\t吴\t郑\t王\t冯\t陈\t褚\t卫\t蒋\t沈\t韩\t杨\t朱\t秦\t尤\t许\n" +
                        "何\t吕\t施\t张\t孔\t曹\t严\t华\t金\t魏\t陶\t姜\t戚\t谢\t邹\t喻\t柏\t水\t窦\t章\n" +
                        "云\t苏\t潘\t葛\t奚\t范\t彭\t郎\t鲁\t韦\t昌\t马\t苗\t凤\t花\t方\t俞\t任\t袁\t柳于\t时\t傅\t皮\t卞\t齐\t康\t伍\t余\t元\t卜\t顾\t孟\t平\t黄\t和\t穆\t萧\t尹\n" +
                        "姚\t邵\t堪\t汪\t祁\t毛\t禹\t狄\t米\t贝\t明\t臧\t计\t伏\t成\t戴\t谈\t宋\t茅\t庞\n" +
                        "熊\t纪\t舒\t屈\t项\t祝\t董\t粱\t杜\t阮\t蓝\t闵\t席\t季\t麻\t强\t贾\t路\t娄\t危\n" +
                        "江\t童\t颜\t郭\t梅\t盛\t林\t刁\t钟\t徐\t邱\t骆\t高\t夏\t蔡\t田\t樊\t胡\t凌\t霍\n" +
                        "虞\t万\t支\t柯\t昝\t管\t卢\t莫\t经\t房\t裘\t缪\t干\t解\t应\t宗\t丁\t宣\t贲\t邓\n" +
                        "郁\t单\t杭\t洪\t包\t诸\t左\t石\t崔\t吉\t钮\t龚\t程\t嵇\t邢\t滑\t裴\t陆\t荣\t翁\n" +
                        "荀\t羊\t於\t惠\t甄\t麴\t家\t封\t芮\t羿\t储\t靳\t汲\t邴\t糜\t松\t井\t段\t富\t巫\n" +
                        "乌\t焦\t巴\t弓\t牧\t隗\t山\t谷\t车\t侯\t宓\t蓬\t全\t郗\t班\t仰\t秋\t仲\t伊\t宫\n" +
                        "宁\t仇\t栾\t暴\t甘\t钭\t厉\t戎\t祖\t武\t符\t刘\t景\t詹\t束\t龙\t叶\t幸\t司\t韶\n" +
                        "郜\t黎\t蓟\t薄\t印\t宿\t白\t怀\t蒲\t邰\t从\t鄂\t索\t咸\t籍\t赖\t卓\t蔺\t屠\t蒙\n" +
                        "池\t乔\t阴\t欎\t胥\t能\t苍\t双\t闻\t莘\t党\t翟\t谭\t贡\t劳\t逄\t姬\t申\t扶\t堵\n" +
                        "冉\t宰\t郦\t雍\t舄\t璩\t桑\t桂\t濮\t牛\t寿\t通\t边\t扈\t燕\t冀\t郏\t浦\t尚\t农\n" +
                        "温\t别\t庄\t晏\t柴\t瞿\t阎\t充\t慕\t连\t茹\t习\t宦\t艾\t鱼\t容\t向\t古\t易\t慎\n" +
                        "戈\t廖\t庚\t终\t暨\t居\t衡\t步\t都\t耿\t满\t弘\t匡\t国\t文\t寇\t广\t禄\t阙\t东\n" +
                        "殴\t殳\t沃\t利\t蔚\t越\t夔\t隆\t师\t巩\t厍\t聂\t晁\t勾\t敖\t融\t冷\t訾\t辛\t阚\n" +
                        "那\t简\t饶\t空\t曾\t毋\t沙\t乜\t养\t鞠\t须\t丰\t巢\t关\t蒯\t相\t查\t後\t荆\t红\n" +
                        "游\t竺\t权\t逯\t盖\t益\t桓\t公\t万俟\t司马\t上官\t欧阳\t夏侯\t诸葛\n" +
                        "闻人\t东方\t赫连\t皇甫\t尉迟\t公羊\t澹台\t公冶\t宗政\t濮阳\n" +
                        "淳于\t单于\t太叔\t申屠\t公孙\t仲孙\t轩辕\t令狐\t钟离\t宇文\n" +
                        "长孙\t慕容\t鲜于\t闾丘\t司徒\t司空\t亓官\t司寇\t仉\t督\t子车\n" +
                        "颛孙\t端木\t巫马\t公西\t漆雕\t乐正\t壤驷\t公良\t拓拔\t夹谷\n" +
                        "宰父\t谷粱\t晋楚\t闫法\t汝\t鄢\t涂\t钦\t段干\t百里\t东郭\t南门\n" +
                        "呼延\t归\t海\t羊舌\t微生\t岳\t帅\t缑\t亢\t况\t后\t有\t琴\t梁丘\t左丘";
        String[] firstNames = originFirstName.split("\\t|\\n");
        String originLastName = "\n" +
                "\n" +
                "习惯了一个人懒懒地听着那忧伤的旋律，习惯一个人默默地看着那伤感的文字。总觉得天那么蓝，蓝得让人有点忧郁。夜那么静，静得让人有点伤心。生活那么苍 白，苍白得让人有点无力。现实那么残酷，残酷得让人有点悲哀。感觉那么清晰，清晰得让人有点虚伪。疼痛那么真实，真实得让人有点麻木......\n" +
                "　　\n" +
                "　　选择孤单、选择寂寞、 选择沉沦、选择等待，等待一个没有结局的天长地久。在一个人的世界上演与爱无关的独角戏，把梦想和希望折叠了寄给明天，把悲伤和痛苦陈旧了在记忆里埋葬，用沉默和淡然来掩饰所有不安、无措、还有绝望......\n" +
                "\n" +
                "　 　一个人的盛情、一个人的落寞、一个人的独角戏,，没有华丽的舞台，少了煽情的观众，找不到合适的对手，凑不成完美的对白，妄想用爱弥补一切残 缺......点燃堆在身后的所有寂寞，我也可以披上凤冠霞衣，画上满面油彩，独自把这出无人欣赏的独角戏来卖力演出，没有鲜花，没有掌声，没有祝福。无 所谓，一个人的独角戏，一个人独自导演，一个人诠释精彩，奢望我也可以拥有幸福！\n" +
                "\n" +
                "　　忘了自己的角色，已投入情绪，在剧中尽情释放着 自己的欢乐悲喜......笑，笑得凄然绝美；哭，哭得肝肠寸断......角色是孤单的，对手是不存在的，对白总是自言自语的，而故事也通常不会上演开 始和结局，却是投入得太彻底，沉浸在剧中演得忘情，舞得妖娆......或许正是多了这种残缺不全的魅力，才让人沉迷于此，碎了完美伤了自尊丢了灵魂却依 然留恋，深深惋惜......\n" +
                "\n" +
                "　　终究淡不了欲望，躲不了俗世的欲望，好了伤疤忘了疼。也或许只是因为寂寞，一次次又将这孤单的角色重 新上演，欲罢不能，沉浮一世，萧索一生。是故事太过美丽，让人有了一种幸福的错觉，以至沉溺在剧中不愿醒来，默默等待......是感觉太过真实，让人以 为快乐真的可以这么简单，却忘了这是个说谎的城市。谁把谁当真，不过只是一场戏，何必骗自己......\n" +
                "\n" +
                "　　幸福其实是虚构的，微笑 也可以是伪装的，而快乐，是不该被恐惧的。我知道很多时候的所谓痛苦是自己给的，可悲伤始终无法忽略。十九岁最美丽的年华逐渐消逝，却还是解不开心里的那 把锁！有人说，回忆是一种病，而伤感，则是终身不愈的一种残疾。我想，那就给心自由吧，纵使悲伤再痛苦，也一定会又痊愈的一天......抬头仰望天空， 天，是蓝色的，云，是白色的，而我，是什么颜色呢？\n" +
                "\n" +
                "站在窗前，闭上眼睛，对空中伸出双手，努力想抓住我想要的一些什么， 比如我弄丢了的爱情，比如我曾亲手毁掉的那份执着与感动......甚至不屑的，现在却显得如此珍贵，曾经弄丢了那份虚伪的网络感情，却是如今再也无法拥 有的，想来自己真的可笑！抛开我高傲的本性，拔掉我满身戒备的刺，卸掉我武装冷漠的面具，为了那所谓坚贞不渝的爱情，却不知换来的只是撕心裂肺的痛。想表 现得豁达一点，说不必一生一世，只需曾经拥有，足以告慰平生，可无法释怀的痛楚却紧紧伴随......\n" +
                "\n" +
                "封锁在时间之外，让仅存的一丝美好窖藏了。偶尔细细品尝，可散发出的不是醇香，却是过期了的腐朽不堪气息。无处不在的忧伤让我不得不去面对，回头寻找我曾 误以为的幸福，却发现那不过只是一场邂逅，说说而已的所谓诺言，却让我义无反顾地做了感情的奴隶，如此卑微、如此落魄、如此用心，却成了一场昂贵感情游戏 里的道具，玩够了还能全身而退，没有不解，没有遗憾，绝情得比任何时候都彻底......\n" +
                "\n" +
                "一个转身，留下的，是背影的冰冷。笑容变得虚伪，温存变得不堪，还得承受一世无法释然的苦涩作为代价......可笑的自己，仓皇无措草草收拾这凌乱的残局，却发现心早已碎得支离。我无法找寻正确的出路，想回到原点，却发现根本无路可退......\n";
        originLastName = PingyinUtils.filterPunctuations(originLastName);
        HashSet<String> lastNameSet = new HashSet<>();
        int i = 0;
        for (; i < originLastName.length(); i++)
            if (!originLastName.substring(i, i + 1).equals(" "))
                lastNameSet.add(originLastName.substring(i, i + 1));
        String[] lastNames = new String[lastNameSet.size()];
        i = 0;
        for (String lastName : lastNameSet)
            lastNames[i++] = lastName;
        for (i = 0; i < 120; i++)
        {
            User user = new User();
            user.setUserLevel(0);
            user.setUserName(firstNames[new Random().nextInt(firstNames.length)] +
                    lastNames[new Random().nextInt(lastNames.length)]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(calendar.getTimeInMillis() -
                    new Random().nextInt(60) * 24 * 3600 * 1000L);
            user.setRegisterDate(calendar);
            user.setPassword(
                    String.valueOf(123456 + new Random().nextInt(22222222)));
            users.add(user);
        }
        return users;
    }
    private static void insertUserTable(User user)
    {
        String sqlString =
                "insert into user(username,userpassword,registerdate) values('" +
                        user.getUserName() + "','" + user.getPassword() + "'," +
                        user.getRegisterDate().getTimeInMillis() + ")";
        MysqlDbConnection.execute(sqlString);
    }
    private static ArrayList<Post> getPosts()
    {
        ArrayList<Post> posts = new ArrayList<>();
        String[] postTitles = new String[]{"好人", "好书", "大拍卖", "清空大甩卖", "国家栋梁求才若渴",
                "结伴求书"};
        for (int i = 0; i < 1000; i++)
        {
            Post post = new Post();
            post.setReadCount(new Random().nextInt(100));
            post.setPostTitle(postTitles[new Random().nextInt(postTitles.length)]);
            PostContent postContent = new PostContent(
                    "我左青龙，右白虎，老牛在腰间，龙头在胸口，人挡杀人佛挡杀佛。");
//            AdriftBook book=new AdriftBook(
            post.setPostContent(postContent);
            post.setPostType(new Random().nextInt(3) + 1);
            User user = new User();
            user.setUserId(new Random().nextInt(120));
            post.setPostUser(user);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(calendar.getTimeInMillis() -
                    new Random().nextInt(60) * 24 * 3600 * 1000);
            post.setPostDate(calendar);
            posts.add(post);
        }
        return posts;
    }
    private static void insertPostTable(Post post)
    {
        String sqlString =
                "insert into post(posttitle,content,readcount,user_id,postdate,posttype) values('" +
                        post.getPostTitle() + "','" +
                        post.getPostContent().getPostContentDetail() + "'," +
                        post.getReadCount() + "," + post.getPostUser().getUserId() +
                        "," + post.getPostDate().getTimeInMillis() + "," +
                        post.getPostType() + ")";
        MysqlDbConnection.execute(sqlString);
    }
    private static ArrayList<AdriftBook> getBooks()
    {
        ArrayList<AdriftBook> books = new ArrayList<AdriftBook>();
        for (int i = 0; i < 4000; i++)
        {
            AdriftBook book = new AdriftBook();
            book.setAuthor("郭霖");
            book.setBookImageUrl(Constant.UPLOAD_BOOK_IMAGE_DIR +
                    "271751232714415261646662606060626566657164.png");
            book.setBookName("第一行代码Android");
            book.setReviewPeopleCount(new Random().nextInt(100));
            book.setRating(new Random().nextFloat());
            book.setType(AdriftBook.ENTITYBOOK);
            Post post = new Post();
            if (i < 1000)
                post.setPostId(i + 1);
            else
                post.setPostId(new Random().nextInt(1000) + 1);
            book.setPost(post);
            books.add(book);
        }
        return books;
    }
    private static void insertBookTable(AdriftBook book)
    {
        String sqlString =
                "insert into book(bookname,  author,bookimageurl,rating,post_id,reviewPeopleCount) values('" +
                        book.getBookName() + "','" + book.getAuthor() + "','" +
                        book.getBookImageUrl() + "'," + book.getRating() + "," +
                        book.getPost().getPostId() + "," +
                        book.getReviewPeopleCount() + ")";
        MysqlDbConnection.execute(sqlString);
    }
    private static ArrayList<Comment> getComments()
    {
        ArrayList<Comment> comments = new ArrayList<>();
        HashSet<String> uniqueCommentSet = new HashSet<>();
        while (uniqueCommentSet.size() < 10000)
        {
            Comment comment = new Comment();
            comment.setCommentContent(
                    "年轻人，我看你骨骼惊奇，天赋异秉，相貌堂堂，印堂蹭亮，面色红润，紫光环绕，脚下生风，举手有力，乃千年不出，万年不出的绝世练武奇才，贫道飞升在即，特有本武林绝学，葵花宝典2精编珍藏加厚版跳楼吐血价出售与你，不要二三万，更不要二三千，只要188，你发发。仅此一本，年轻人珍惜啊！");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(
                    calendar.getTimeInMillis() - new Random().nextInt(3600) * 1000L);
            comment.setReviewDate(calendar);
            User user = new User();
            user.setUserId(new Random().nextInt(120) + 1);
            comment.setCommentUser(user);
            AdriftBook book = new AdriftBook();
            book.setBookId(new Random().nextInt(4000) + 1);
            comment.setCommentBook(book);
            String concat = user.getUserId() + "" + book.getBookId();
            if (!uniqueCommentSet.contains(concat))
            {
                comments.add(comment);
                uniqueCommentSet.add(concat);
            }
        }
        return comments;
    }
    private static void insertCommentTable(Comment comment)
    {
        String sqlString =
                "insert into comment(commentcontent,reviewdate,user_id,book_id) values('" +
                        comment.getCommentContent() + "'," +
                        comment.getReviewDate().getTimeInMillis() + "," +
                        comment.getCommentUser().getUserId() + "," +
                        comment.getCommentBook().getBookId() + ")";
        MysqlDbConnection.execute(sqlString);
    }
    /**
     * 连接数据库netbookstore,同时预先插入图书
     *
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
       /* ArrayList<User> users=getUsers();
        for(User user:users)
            insertUserTable(user);*/
      /*  ArrayList<Post> posts = getPosts();
        for (Post post : posts)
            insertPostTable(post);*/
       /* ArrayList<AdriftBook> books = getBooks();
        for (AdriftBook book : books)
            insertBookTable(book);*/
        ArrayList<Comment> comments = getComments();
        for (Comment comment : comments)
            insertCommentTable(comment);
    }
}
