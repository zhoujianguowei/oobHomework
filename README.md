# 北大面向对象分析与设计-校园图书漂流系统

## 1. 项目背景和目标:  

高校图书馆中有大量的有价值的图书，尤其是各种年代久远的经典书籍，然而，图书馆也有其劣势，那就是书籍更新速度不够快，热门书籍数量不够多，增加购书数量又会使图书馆的购书成本大大提高，读者往往需要预约很久才能拿到，所以就会有很多人选择自己买书，然而往往看完之后又会搁置，不仅增大了个人的开销，又会造成图书资源的浪费。

学生的教科书一般只需要使用一次，大部分人的教科书会在自己使用完之后搁置几年，然后在毕业的时候将教科书贱卖，这样又会造成图书资源的浪费。

本系统的目的在于建立一个平台，使得学生能够将不再使用的图书漂流起来或者捐赠出去，实现资源共享。使知识能够低成本的在同学之间传递，把闲散的书籍收集起来，构建一个知识全面的共享资源库，使书籍充分体现它的价值。用户还可以交流读书体会，以书会友，在此可以找到志趣相投的朋友。
分享电子资源，用户可以分享稀缺的电子资源。
书籍推荐，有两部分，其一为用户向别人推荐书籍，其二为系统根据用户评价、热门以及读书爱好来为用户做出书籍推荐。
二手书籍出售，出售闲置书籍，减少浪费。

## 2. 功能概述:

本系统会为每个用户建立唯一的身份，为每本书籍建立唯一的图书ID，并会为每个用户建立读书历史信息记录，为及每本书籍建立漂流足迹。还会根据读书分享、用户评价等信息为用户建立评价体系。

本系统由以下几个模块组成：图书漂流，读书分享，电子资源分享，二手交易，个人。

图书漂流：

求漂区：用户可以在此区域发布求漂书籍信息。

放漂区：用户在此区域发布将要放漂的书籍信息。

推荐区：根据用户间评分高的读书分享，在此为用户推荐对应书籍。

热门区：热门的求漂书籍，在此可以发现最新的读书潮流。

公共书架：此处的书籍为用户捐赠的不再保留所有权的书籍。

读书分享:

用户可以在此分享自己的读书笔记，并为书籍做推荐

电子资源分享：

如果某些书籍很难买到实体书籍，但是大家又有阅读需求，那么有电子资源的用户就可以在此模块下分享电子资源。

二手书籍交易：

用户可以在本模块下出售自己的书籍，本模块比较适合于教科书等书籍的交流。

个人：

此模块下包括用户注册，个人信息，个人书架（外漂书籍），读者足迹（读过别人的书籍），个人读书分享，个人推荐书籍等。

## 截图展示

### 登录注册界面

![注册界面](https://github.com/zhoujianguowei/oobHomework/raw/master/img/1.png)

![登录界面](https://github.com/zhoujianguowei/oobHomework/raw/master/img/2.png) 

### 主界面

查询支持按照书名和作者名称模糊匹配。

![主界面1](https://github.com/zhoujianguowei/oobHomework/raw/master/img/3.png)  ![主界面2](https://github.com/zhoujianguowei/oobHomework/raw/master/img/4.png)

### 帖子详情页面

![帖子详情](https://github.com/zhoujianguowei/oobHomework/raw/master/img/5.png) ![评论](https://github.com/zhoujianguowei/oobHomework/raw/master/img/6.png)

### 发帖页面
支持发送文本和上传电子书籍以及图片资源，支持批量上传。

![发帖页面](https://github.com/zhoujianguowei/oobHomework/raw/master/img/7.png)
### 个人中心页面
一共包括4部分内容：个人基本信息、外漂书籍（自己发布的书籍）、读书分享（自己的评论）、浏览历史。

![个人中心](https://github.com/zhoujianguowei/oobHomework/raw/master/img/8.png) ![外漂书籍](https://github.com/zhoujianguowei/oobHomework/raw/master/img/9.png)
![读书分享](https://github.com/zhoujianguowei/oobHomework/raw/master/img/10.png)![浏览历史](https://github.com/zhoujianguowei/oobHomework/raw/master/img/11.png)

### 上传下载搜索功能展示
上传支持进度提示
![搜索](https://github.com/zhoujianguowei/oobHomework/raw/master/img/12.png) ![上传](https://github.com/zhoujianguowei/oobHomework/raw/master/img/13.png)
![搜索](https://github.com/zhoujianguowei/oobHomework/raw/master/img/14.png)![搜索](https://github.com/zhoujianguowei/oobHomework/raw/master/img/15.png)