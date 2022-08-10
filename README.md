> 开发时间：2022.06.02 - 至今

# 一、快速开始

方案一：

1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机

2. 打开工程：使用 `IntelliJ IDEA`  打开克隆的仓库或解压的工程文件

3. 创建数据库和表并插入数据：登录 MySQL ，创建 `images_gather` 数据库，将 `src/main/resources/sql/images_gather.sql` 文件中的数据库表导入 images_gather 数据库中

4. 修改配置信息：修改 `src/main/resources/properties/application.properties` 中一系列配置信息

   > 1. 修改 MySQL 数据库连接信息，设置你自己的用户名和密码
   > 2. 修改百度地图 IP 归属地解析 url（可选，不修改则登录记录中 IP 归属地解析功能不可用）
   > 3. 修改七牛云平台云存储信息（可选，不修改则图片云存储功能不可用）
   > 4. 修改邮箱服务器信息（可选，不修改则密码找回功能不可用，个人邮箱 [smtp 开启](https://blog.csdn.net/smilehappiness/article/details/108145215)）

5. 部署访问：在 IntelliJ IDEA 中部署 Tomcat 即可访问两码一查登录首页

6. 登录：账号登录默认用户名和密码均为 `admin`

方案二（IP 解析、七牛云服务、邮箱服务不可用）：

1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机

2. 拷贝 war 包：将 `RELEASE` 目录下的 `images-gather.war` 包拷贝到 `Tomcat` 安装目录下的 `webapps` 目录中

3. 创建数据库和表并插入数据：登录 MySQL ，创建 `images_gather` 数据库，将 `src/main/resources/sql/images_gather.sql` 文件中的数据库表导入 images_gather 数据库中

4. 创建数据库用户：在 MySQL 控制台创建 `admin` 用户，密码也为 `admin`，并赋予 admin 用户所有操作权限

   ```sql
   create user 'admin'@'localhost' identified by 'admin';
   grant all on images_gather.* to 'admin'@'localhost' with grant option;
   ```

5. 启动 Tomcat：双击 Tomcat 安装目录下 `bin` 目录中的 `startup.bat` 启动 Tomcat

6. 访问首页：在浏览器地址栏输入 `http://localhost:8080/images-gather/` 即可访问两码一查登录首页

7. 登录：账号登录默认用户名和密码均为 `admin`


# 二、背景调查

2019 年底开始流行的新型冠状病毒（2019-nCoV）疫情给全世界人民带来了无法考量的灾难。新冠疫情泛滥两年多来，已夺走数百万条声明，世界经济几乎陷入停顿，习以为常的生活无法复原，损失极其惨重，全世界都在期盼疫情尽快结束，期待后疫情时代早日到来。在全国人民众志成城地抗疫下，如今国内疫情形势得到了有效的控制。疫情初期由杭州市于 2020 年 2 月 11 日率先在全国推出的基于绿色、黄色和红色的健康码以个人健康数据为基础，由居民自主通过健康登记系统网上申报，结合新冠肺炎疫情相关数据进行比对核验，生成的个人专属二维码。该二维码作为个人在当地出入通行的一个电子凭证，实现一次申报，全市通用。健康码的应用涵盖了社区管理、企业复工、交通出行、学校开学、买药登记、超市商场等使用场景，可以协助社区、企业、学校等做好防疫管理及疫情控制等重点工作。在疫情防控和复产复工中，健康码可以实现高效率的人员流动管理，在办公楼、商场、地铁、火车站等人流密集的地点提高过检效率，避免过多的人员接触和聚集。2020 年 12 月 10 日，国家卫健委、国家医保局、国家中医药管理局联合发布《关于深入推进“互联网＋医疗健康”“五个一”服务行动的通知》，明确要求各地落实 “健康码” 全国互认、一码通行。2021 年 3 月 23 日，国家卫生健康委员会在发布会上介绍，全国基本实现了健康码的 “一码通行”。3 月 30 日，国家政务服务平台 “防疫健康码” 已整合 “通信大数据行程卡” 相关信息，可在健康码中显示用户是否去过中高风险地区等行程信息，助力健康码 “一码通行”。后续由国家政务服务平台推出的同行密接人员自查与健康码、行程码同属一类防疫工具，旨在为大众提供更加便捷的防疫信息查询和个人健康管理。

# 三、需求分析

如今，各高校内核酸采集检测已为常态化，使用核酸结果验证区域内是否存在患者的同时亦可以通过验证学生健康码、行程码、密接自查（后续简称为两码一查）数据信息的方式监测管理学生的健康状态。经走访调查，湖北省武汉市某高校要求学生每日上传个人两码一查图片信息以配合防疫，该方式实际实施的过程中相当耗费学生精力。两码一查图片收集信息管理大致过程概述如下：首先由学生个人从健康码、行程码、密接查查询入口查询个人当天的健康信息并截图保存，而后按照统一格式要求对图片进行命名并打包发送给相关负责人。接下来由班长负责校验当天该班所有学生两码一查健康信息，需解压每一位同学的压缩包并依次点击查看两码一查图片信息，过程繁重琐碎易出错，班长还需要劳苦地核对有哪些同学尚未提交今日两码一查，需要对尚未上传同学进行督促并私聊该同学尽快上传两码一查图片信息，并且图片数据的保留保存问题也较为困难。针对图片收集、文件命名、文件解压、信息查看管理等繁杂过程，我们的两码一查图片收集信息管理系统应运而生，旨在提高学生每日上传图片和信息管理的效率，愿景是减轻每一位同学的负担。主要功能包括由学生自主上传两码一查图片信息、自动规范化命名、后台自动校验健康信息是否存在异常、系统通过邮箱或短信方式自动提醒当天未完成同学、管理员查看班级上传信息、管理员对已上传同学的数据进行复核以及历史记录（上传和登录）查看等功能。该系统作为校园防疫健康信息管理的一站式解决方案，包括学院管理、班级管理、教师管理、学生管理、健康信息查询与统计等功能。

# 四、系统设计

## 4.1、网络架构

![在这里插入图片描述](https://img-blog.csdnimg.cn/b852c6e796da45c48f3b3db434d00fb5.png#pic_center)


## 4.2、系统架构

![在这里插入图片描述](https://img-blog.csdnimg.cn/4f89710ee1bb478dab8e9ecc17f68dc2.png#pic_center)


## 4.3、数据库设计

### 4.3.1、实体及属性

1. 用户：用户 ID，用户名，密码，上次登录时间，用户类型，用户状态，学生 ID
2. 学生：学生 ID，学号，姓名，性别，电话，邮箱，专业，班级，年级，学院，学校， 用户 ID
3. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID
4. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID

### 4.3.2、实体间关系

1. 用户与学生：一个用户对应一个学生，一个学生只有一个用户账号，故用户与学生之间是一对一的关系
2. 用户与上传记录：一个用户有多条上传记录，一条上传记录只对应一个用户，故用户与上传记录之间是一对多的关系
3. 用户与登录记录：一个用户有多条登录记录，一条登录记录只对应一个用户，故用户与登录记录之间是一对多的关系

### 4.3.3、逻辑结构设计

1. 用户：用户 ID，用户名，密码，上次登录时间，用户类型，用户状态，学生 ID

   | 数据项               | 数据类型     |
      | :------------------- | :----------- |
   | id（主键、自增）     | int          |
   | username（唯一索引） | varchar(255) |
   | password             | varchar(255) |
   | last_login_datetime  | datetime     |
   | user_type            | int          |
   | user_status          | int          |
   | student_id（外键）   | int          |

2. 学生：学生 ID，学号，姓名，性别，电话，邮箱，专业，班级，年级，学院，学校， 用户 ID

   | 数据项             | 数据类型     |
      | :----------------- | :----------- |
   | id（主键、自增）   | int          |
   | number（唯一索引） | varchar(255) |
   | name               | varchar(255) |
   | sex                | char(1)      |
   | phone              | varchar(255) |
   | email（唯一索引）  | varchar(255) |
   | class_name         | varchar(255) |
   | major              | varchar(255) |
   | grade              | varchar(255) |
   | college            | varchar(255) |
   | school             | varchar(255) |
   | user_id（外键）    | int          |

3. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID

   | 数据项             | 数据类型     |
      | :----------------- | :----------- |
   | id（主键、自增）   | int          |
   | upload_status      | int          |
   | upload_datetime    | datetime     |
   | local_health_url   | varchar(255) |
   | local_schedule_url | varchar(255) |
   | local_closed_url   | varchar(255) |
   | cloud_health_url   | varchar(255) |
   | cloud_schedule_url | varchar(255) |
   | cloud_closed_url   | varchar(255) |
   | user_id（外键）    | int          |

4. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID

   | 数据项           | 数据类型     |
      | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | ip               | char(15)     |
   | location         | varchar(255) |
   | login_datetime   | datetime     |
   | user_id（外键）  | int          |

## 4.4、功能设计

![在这里插入图片描述](https://img-blog.csdnimg.cn/b04af3a7149747a6af4e7329469b6abb.png#pic_center)


# 五、功能演示

## 5.1、用户登录

1. 用户名不存或密码错误

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/e85be9dabaf24fd080f72ad38f0c87df.png#pic_center)


2. 记住我，当用户勾选记住我并且输入的用户名和密码无误时，服务器将从响应头中告知浏览器保存用户名和密码信息到 Cookie 中，当用户下次登录系统时直接从 Cookie 中读取用户名和密码的值并自动填充到输入框中

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/4a9bff95957a49efa028f544a5c2ad94.png#pic_center)


3. 用户类型和用户状态判断，若用户状态异常或类型不存在则不允许用户使用系统

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/5155b49e3fb24bfdaa632c1bbde9ccab.png#pic_center)


## 5.2、重置密码

1. 获取邮箱验证码时用户名非空校验，邮箱地址格式校验

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/de6c68a6b2a74bddb8d4371c79ae85df.png#pic_center)


2. 用户名和邮箱地址不匹配时不允许修改密码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/6a5fb26c89a6446994f5041904befb9f.png#pic_center)


3. 点击确认按钮时再次校验邮箱、用户名格式，额外校验验证码长度位数必须为 6 位，新密码长度不小于 6 位

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2629a310a6a04a53a47d265e5f29daaa.png#pic_center)


4. 在用户点击确认按钮重置个人密码之前，必须点击获取按钮获取邮箱验证码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/83aeaba81f224396a1ab3d6d7a7f1a12.png#pic_center)


5. 邮箱验证码发送，用户填入正确的用户名和邮箱，即可请求服务器发送一封邮箱验证码邮件到个人邮箱中作为重置密码的身份校验

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/060e8d1aef564d84ad8279433410f39d.png#pic_center)




6. 服务端会再次对用户输入的各项内容进行校验，校验通过则发送邮件，内容如下

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a9d6ecea65924ed59e3ea932ccb995d4.png#pic_center)


7. 错误的邮箱验证码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/63a8353186fd43aaa676a73c53ae865e.png#pic_center)


8. 密码重置成功 3s 后会自动跳转到登录页面

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/59fb5005a0e94fdcb456f0b0d9f1f38d.png#pic_center)


## 5.3、系统界面

![在这里插入图片描述](https://img-blog.csdnimg.cn/bab11e9fbf8f4a6684b3eb991daed671.png#pic_center)


## 5.4、修改密码

1. 系统点击主页面右上角的改密按钮，弹出密码修改模态框

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/9721530d35014ec389f0e6239945f768.png#pic_center)


2. 新密码长度不小于 6 位且两次输入的新密码需保持一致，新密码与原密码一致时不允许修改

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/ce0da8bc41c54c12a07121ac416a9438.png#pic_center)


3. 服务端校验新旧密码是否一致，一致时不允许修改个人密码，原密码有误时亦不允许修改个人密码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/55e76099a7c949468615a54333dff730.png#pic_center)


4. 密码修改成功

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/8fd61600db57402da330606555ae75cc.png#pic_center)


## 5.5、图片上传

1. 选择个人健康码图片、行程码图片、密接查图片后方可上传

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/35f1278c15f1433ba2a4c3c8b1f3d168.png#pic_center)


2. 图片上传完成，在服务器留存的同时在七牛云平台留存图片信息，数据安全双重保障

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b1b35feeee164914b5ba880f149958bb.png#pic_center)


> 七牛云平台图片数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/383ea3edaaec42f884b28ce9f14e04a1.png#pic_center)


> 服务器本地数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/1018e6320f8341a886192f0161c30902.png#pic_center)


## 5.6、历史记录

1. 今日上传记录查询，当用户一天内两次及以上登入系统时，若用户已完成图片上传则跳转到已完成页面并显示已上传的两码一查图片信息，提供用户重新上传图片的入口

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a4701288c9c0497f8031b185b9022d29.png#pic_center)


2. 个人历史登录记录数据分页查看，显示登录的时间、登录IP、地点（由百度地图开发者 API 提供解析服务）信息

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2f8236b4863748baaeacd6b5c8f05744.png#pic_center)


3. 上传记录查看，用户可查看已上传的两码一查图片信息，并提供图片查看的功能

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/fc92dcb57d8f4440b108bf7075b8f403.png#pic_center)


## 5.7、个人资料

1. 提供用户个人资料查看的入口即系统主页面点击【我的】可查看自己的详细信息

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/cc7ff4f793334217926d0a69574491e6.png#pic_center)


2. 个人信息修改，用户可以修改自己的性别、电话、邮箱等信息，修改过程中对信息进行校验，前后数据未发生变化、数据格式不正确则不允许修改

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/08feb8a14ff1486f80ae893465150ce0.png#pic_center)


3. 个人信息修改成功，3s 后自动刷新页面

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/0e74d03e24bf4302acca9f96d9663abf.png#pic_center)


## 5.8、退出系统

每个页面中均有【两码一查】回到主页、【退出】退出系统功能

![在这里插入图片描述](https://img-blog.csdnimg.cn/c04fb34ca1df42fe9096b3c1095bc000.png#pic_center)


## 5.9、班级记录

班长可查看班级上传记录，PC 端可查看最近 15 的上传记录，移动端查看最近 5 天的上传记录，分为未登录人员名单、登录未上传人员名单和已完成人员名单并生成各位同的图片预览

![在这里插入图片描述](https://img-blog.csdnimg.cn/43b05f8b8ce846549cadc634945a45ff.png#pic_center)


## 5.10、权限控制

配置了登录拦截器、用户权限拦截器和管理员权限拦截器进行权限控制，当对应的用户账号未登入系统时则跳转到登录页面

![在这里插入图片描述](https://img-blog.csdnimg.cn/2787bbd8b19f41af9e579e768e297c29.png#pic_center)


## 5.11、导入班级

超级管理员可在服务端根据 Excel 中的学生行数据，由自定义的 sheet-bean-converter.jar 包完成 Excel 数据与 Java Bean 对象间的转换，后可将响应的数据保存到数据库

# 六、技术选型

## 6.1、涉及技术

| 前端技术                                                     |
| :----------------------------------------------------------- |
| HTML5、CSS3、JavaScript、jQuery1.7.2、Bootstrap3.4.1、Ajax、正则表达式 |

| 后端技术                                                     |
| :----------------------------------------------------------- |
| Spring5.3.1、SpringMVC5.3.1、MyBatis1.3.2、SpringTest5.3.1、JUnit4.12、Thymeleaf3.0.12、MySQL8.0.27、Druid1.1.15 |
| Logback1.2.3、Lombok1.18.20、Jackson2.9.8、PageHelper5.2.0、Qiniu7.8.0、JavaxMail1.6.2、SheetBeanConverter1.0.0 |
| Tomcat8.0.50、Maven3.8.4、Git2.35.1、百度地图 IP 归属地解析  |

## 6.2、开发工具

| Develop tools                                                |
| :----------------------------------------------------------- |
| IntelliJ IDEA2021.1、Visual Studio Code、Java1.8.0_311、MySQL8.0.28、Navicat16.0.13、Git2.35.1、Maven3.8.4 |
| Tomcat8.0.50、CentOS7.6、XShell6、Xftp6、Typora1.3.6、Snipaste、ioDraw、XMind |

# 七、TODO

| Num  | 事项                                                         |
| :--: | :----------------------------------------------------------- |
|  1   | 使用 @ControllerAdvice、@ExceptionHandler 注解处理自定义异常 |
|  2   | 邮箱验证码的最大有效时间                                     |
|  3   | 密码加密                                                   |
|4|Python 图片识别|
|5|请求发送邮件时增加人机验证|
|6|支持多种登录方式（用户名、电话、邮箱）|
|7|root 管理员的用户信息管理功能设计实现|
|8|七牛云平台图片防盗（私有空间）|
|9|前后台真正意义上动静分离|
|10|负载均衡|
|11|持续化集成与部署|
|12|年级上传记录查看|
|13|班级资料压缩下载（图片、名单）|


再次重构整个项目，实现前后端真正分离
优化数据库和页面设计，提高系统并发能力
修复一系列已知问题，优化程序逻辑和代码
增加班级上传记录文件下载功能
增加用户角色区分，明确系统权限
增加后台管理，实现信息批量导入
增加 Redis 做缓存，引入密码加密功能
邮件发送记录和下载记录
