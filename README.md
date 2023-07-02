<p align="center">
    <img src="https://img.shields.io/static/v1?label=%E5%BC%80%E5%8F%91%E6%97%B6%E9%97%B4&message=22/06/02-22/09/15&color=007bff"/>
    <img src="https://img.shields.io/static/v1?label=Spring&message=5.3.1&color=dc3545"/>
    <img src="https://img.shields.io/static/v1?label=MyBatis&message=3.5.7&color=ffc107"/>
    <img src="https://img.shields.io/static/v1?label=Bootstrap&message=3.4.1&color=17a2b8"/> 
    <a href="https://github.com/springbear2020/images-gather" target="_blank">
        <img src="https://img.shields.io/static/v1?label=%E5%BC%80%E6%BA%90%E9%A1%B9%E7%9B%AE&message=images-gather&color=18a745"/>
    </a>
</p>

# 一、快速开始

1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机来获取源代码。
2. 打开工程：使用 IntelliJ IDEA 开发工具打开克隆的仓库或解压的工程文件，而后使用 Maven 项目构建工具更新工程模块依赖。
3. 创建数据库并导入数据：登录 MySQL 控制台，创建一个名为 images_gather 的数据库，并将 document/sql/images_gather.sql 脚本中的建表语句和数据导入到 images_gather 数据库中。
4. 修改配置：
   - 修改 src/main/webapp/static/js/gather-common.js 中的 contextPath 变量值，此为后端接口请求路径。
   - 修改 src/main/resources/jdbc.properties 文件中的数据库连接信息，设置你自己的数据库用户名和密码。
   - 修改 src/main/resources/email.properties 中的邮箱服务器信息，设置你自己的邮箱账号和服务器。
   - 修改 src/main/resources/qiniu.properties 中的七牛云配置信息，设置你自己的七牛云令牌信息。
   - 修改 src/main/resources/baidu.properties 中的百度地图配置信息，设置你自己的百度地图 IP 解析 URL。
5. 部署访问：在 IntelliJ IDEA 中部署 Tomcat 并启动即可访问两码一查首页，需要注意工程上下文路径需与配置的后端接口请求路径相同。
6. 登录系统：
   - 学生：用户名和密码均为 10086。
   - 班长：用户名和密码均为 10087。
   - 班主任：用户名和密码均为 10088。
   - 年级主任：用户名和密码均为 10089。
   - 超级管理员：用户名和密码均为 admin。

# 二、背景调查

2019 年底开始流行的新型冠状病毒（2019-nCoV）疫情给全世界人民带来了无法考量的灾难。新冠疫情泛滥两年多来，已夺走数百万条声明，世界经济几乎陷入停顿，习以为常的生活无法复原，损失极其惨重，全世界都在期盼疫情尽快结束，期待后疫情时代早日到来。

在全国人民众志成城地抗疫下，如今国内疫情形势得到了有效的控制。疫情初期由杭州市于 2020 年 2 月 11 日率先在全国推出的基于绿色、黄色和红色的健康码以个人健康数据为基础，由居民自主通过健康登记系统网上申报，结合新冠肺炎疫情相关数据进行比对核验，生成的个人专属二维码。该二维码作为个人在当地出入通行的一个电子凭证，实现一次申报，全市通用。

健康码的应用涵盖了社区管理、企业复工、交通出行、学校开学、买药登记、超市商场等使用场景，可以协助社区、企业、学校等做好防疫管理及疫情控制等重点工作。在疫情防控和复产复工中，健康码可以实现高效率的人员流动管理，在办公楼、商场、地铁、火车站等人流密集的地点提高过检效率，避免过多的人员接触和聚集。

2020 年 12 月 10 日，国家卫健委、国家医保局、国家中医药管理局联合发布《关于深入推进“互联网＋医疗健康”“五个一”服务行动的通知》，明确要求各地落实 “健康码” 全国互认、一码通行。

2021 年 3 月 23 日，国家卫生健康委员会在发布会上介绍，全国基本实现了健康码的 “一码通行”。3 月 30 日，国家政务服务平台 “防疫健康码” 已整合 “通信大数据行程卡” 相关信息，可在健康码中显示用户是否去过中高风险地区等行程信息，助力健康码 “一码通行”。后续由国家政务服务平台推出的同行密接人员自查与健康码、行程码同属一类防疫工具，旨在为大众提供更加便捷的防疫信息查询和个人健康管理。

# 三、需求分析

如今，各高校内核酸采集检测已为常态化，使用核酸结果验证区域内是否存在患者的同时亦可以通过验证学生健康码、行程码、密接自查（后续简称为两码一查）数据信息的方式监测管理学生的健康状态。

经走访调查，湖北省武汉市某高校要求学生每日上传个人两码一查图片信息以配合防疫，该方式实际实施的过程中相当耗费学生精力。两码一查图片收集信息管理大致过程概述如下：首先由学生个人从健康码、行程码、密接查查询入口查询个人当天的健康信息并截图保存，而后按照统一格式要求对图片进行命名并打包发送给相关负责人。接下来由班长负责校验当天该班所有学生两码一查健康信息，需解压每一位同学的压缩包并依次点击查看两码一查图片信息，过程繁重琐碎易出错，班长还需要劳苦地核对有哪些同学尚未提交今日两码一查，需要对尚未上传同学进行督促并私聊该同学尽快上传两码一查图片信息，并且图片数据的保留保存问题也较为困难。

针对图片收集、文件命名、文件解压、信息查看管理等繁杂过程，我们的两码一查图片收集信息管理系统应运而生，旨在提高学生每日上传图片和信息管理的效率，愿景是减轻每一位同学的负担。主要功能包括由学生自主上传两码一查图片信息、自动规范化命名、后台自动校验健康信息是否存在异常、系统通过邮箱或短信方式自动提醒当天未完成同学、管理员查看班级上传信息、管理员对已上传同学的数据进行复核以及历史记录（上传和登录）查看等功能。该系统作为校园防疫健康信息管理的一站式解决方案，包括学院管理、班级管理、教师管理、学生管理、健康信息查询与统计等功能。

# 四、系统设计

## 4.1 数据库设计

### 4.1.1 实体及属性

1. 用户：用户 ID，用户名，密码，电话，邮箱，用户类型，用户状态，姓名，性别，学校 ID，年级 ID，班级 ID，登录时间，创建时间
2. 学校：学校 ID，校名
3. 年级：年级 ID，年级名称
4. 班级：班级ID，班级名称
5. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID，创建时间
6. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID
7. 邮件记录：邮件记录 ID，邮箱，验证码，发送时间，用户ID

### 4.1.2 实体间关系

1. 用户与学校：一个用户对应一个学校，一个学校下包含多个用户，故用户与学校间是多对一关系。
1. 用户与年级：一个用户对应一个年级，一个年级下包含多个用户，故用户与年级间是多对一关系。
1. 用户与班级：一个用户对应一个班级，一个班级下包含多个用户，故用户与班级间是多对一关系。
1. 用户与上传记录：一个用户可以有多条上传记录，一条上传记录对应一个用户，所以用户与上传记录间是一对多关系。
1. 用户与登录记录：一个用户可以有多条登录记录，一条登录记录对应一个用户，所以用户与登录记录间是一对多关系。
1. 用户与邮件记录：一个用户可以有多条邮件记录，一条邮件记录对应一个用户，所以用户与邮件记录间是一对多关系。
1. 学校与年级：一所学校下可有多个年级，一个年级对应一所学校，所以学校与年级间是一多对关系。
1. 年级与班级：一个年级下可有多个班级，一个班级对应一个年级，所以年级与班级间是一对多关系。

### 4.1.3 逻辑结构设计

1. 用户：用户 ID，用户名，密码，电话，邮箱，用户类型，用户状态，姓名，性别，学校 ID，年级 ID，班级 ID，登录时间，创建时间

   | 数据项               | 数据类型     |
   | :------------------- | :----------- |
   | id（主键、自增）     | int          |
   | username（唯一索引） | varchar(255) |
   | password             | char(32)     |
   | phone                | varchar(255) |
   | email                | varchar(255) |
   | user_type            | int          |
   | user_status          | int          |
   | name                 | varchar(255) |
   | sex                  | char(2)      |
   | school_id（外键）    | int          |
   | grade_id（外键）     | int          |
   | class_id（外键）     | int          |
   | last_login_datetime  | datetime     |
   | create_datetime      | datetime     |

2. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID

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

3. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID

   | 数据项           | 数据类型     |
   | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | ip               | char(15)     |
   | location         | varchar(255) |
   | login_datetime   | datetime     |
   | user_id（外键）  | int          |

4. 邮件记录：邮件记录 ID，邮箱，验证码，发送时间，用户 ID

   | 数据项            | 数据类型     |
   | :---------------- | :----------- |
   | id（主键、自增）  | int          |
   | email             | varchar(255) |
   | code              | varchar(255) |
   | delivery_datetime | datetime     |
   | user_id（外键）   | int          |

5. 学校：学校 ID，校名

   | 数据项           | 数据类型     |
   | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | school           | varchar(255) |

6. 年级：年级 ID，年级名称

   | 数据项           | 数据类型     |
   | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | grade            | varchar(255) |

7. 班级：班级ID，班级名

   | 数据项           | 数据类型     |
   | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | class_name       | varchar(255) |

## 4.2 功能设计

![](document/img/4.1%20功能概览.png)

# 五、效果展示

## 5.1 用户登录

1. 用户名不存或密码：当用户选择登陆时进行用户名和密码正确性校验，当用户名不存在或密码错误时友好提示用户。
2. 记住我：当用户勾选记住我并且输入的用户名和密码无误时，服务器将从响应头中告知浏览器保存用户名和密码信息到 Cookie 中，当用户下次登录系统时直接从 Cookie 中读取用户名和密码的值并自动填充到输入框中。
3. 用户类型和用户状态判断，若用户状态异常或类型不存在则不允许用户使用系统。

![](document/img/5.1%20用户登录.png)

## 5.2 重置密码

1. 获取邮箱验证码时用户名非空校验，邮箱地址格式校验。
2. 用户名和邮箱地址不匹配时不允许修改密码。
3. 点击确认按钮时再次校验邮箱、用户名格式，额外校验验证码长度位数必须为 6 位，新密码长度不小于 6 位。
4. 在用户点击确认按钮重置个人密码之前，必须点击获取按钮获取邮箱验证码。
5. 邮箱验证码发送，用户填入正确的用户名和邮箱，即可请求服务器发送一封邮箱验证码邮件到个人邮箱中作为重置密码的身份校验。

![](document/img/5.2%20重置密码.png)

## 5.3 超级管理员

### 5.3.1 导入班级

管理员可上传班级信息模板 Excel 文件，根据自定义的 sheet-bean-converter.jar 包完成 Excel 数据与 Java Bean 对象间的转换，后可将相应的学生数据转换为用户数据并批量导入到数据库中。

![](document/img/5.3%20导入班级.png)

### 5.3.2 添加主任

管理员可填入年级主任信息并选择学校和班级，从而设置对应的年级主任用户账号。

![](document/img/5.4%20添加主任.png)

### 5.3.3 班级管理

管理员可选择学校、年级、班级，后显示改班级下的所有学生，并供管理员修改学生类型的功能。

![](document/img/5.5%20班级管理.png)

### 5.3.4 角色修改

管理员选择操作学生，并修改其用户角色，可修改为学生、班长或班主任。

![](document/img/5.6%20角色修改.png)

## 5.4 学生

### 5.4.1 图片上传

1. 选择个人健康码图片、行程码图片、密接查图片后方可上传。
2. 图片上传完成，在服务器留存的同时在七牛云平台留存图片信息，数据安全双重保障。

![](document/img/5.7%20图片上传.png)

### 5.4.2 修改密码

1. 系统点击主页面右上角的改密按钮，弹出密码修改模态框。
2. 新密码长度不小于 6 位且两次输入的新密码需保持一致，新密码与原密码一致时不允许修改。
3. 服务端校验新旧密码是否一致，一致时不允许修改个人密码，原密码有误时亦不允许修改个人密码。

![](document/img/5.8%20修改密码.png)

### 5.4.3 历史记录

1. 今日上传记录查询，当用户一天内两次及以上登入系统时，若用户已完成图片上传显示已上传的两码一查图片信息，用户可以重新上传图片。

   ![](document/img/5.9%20今日记录.png)

2. 个人历史登录记录数据分页查看，显示登录的时间、登录IP、地点（由百度地图开发者 API 提供解析服务）信息。

   ![](document/img/5.10%20登录记录.png)

3. 上传记录查看，用户可查看已上传的两码一查图片信息，并提供图片查看的功能。

   ![](document/img/5.11%20上传记录.png)

### 5.4.4 个人资料

1. 提供用户个人资料查看的入口即系统主页面点击【我的】可查看自己的详细信息。
2. 个人信息修改，用户可以修改自己的性别、电话、邮箱等信息，修改过程中对信息进行校验，前后数据未发生变化、数据格式不正确则不允许修改。
3. 个人信息修改成功，3s 后自动刷新页面。

![](document/img/5.12%20个人资料.png)

## 5.5 班长

1. 班长可查看班级上传记录，PC 端可查看最近 14 的上传记录，移动端查看最近 7 天的上传记录，分为未登录人员名单、登录未上传人员名单和已完成人员名单并生成各位同学的图片预览。
2. 班长可点击日期链接下载该天该班级的两码一查上传情况，包含了已上传的图片数据和班级名单 README.txt。

![](document/img/5.13%20班级记录.png)

## 5.6 班主任

班主任功能与班长类似，不同的是班主任无需跳转到上传页面且显示当前的登录信息，包含登录时间、地点、IP 等。

![](document/img/5.14%20班主任.png)

## 5.7 年级主任

### 5.7.1 主任资料

年级主任登录记录、我的资料、修改密码功能与班主任相同，唯一不同的是年级主任可查看年级上传记录。

![](document/img/5.15%20年级主任资料.png)

### 5.7.2 年级记录

1. 班级未上传信息概览。

   ![](document/img/5.16%20未上传班级概览.png)

2. 班级上传详情查看，点击对应班级即可查看该班人员信息，包含了未登录名单、未上传名单、已完成名单和图片概览。

   ![](document/img/5.17%20班级详情.png)

# 六、技术选型

| 前端技术                                                     |
| :----------------------------------------------------------- |
| HTML5、CSS3、JavaScript、jQuery1.7.2、Bootstrap3.4.1、Ajax、正则表达式 |

| 后端技术                                                     |
| :----------------------------------------------------------- |
| Spring5.3.1、SpringMVC5.3.1、MyBatis1.3.2、SpringTest5.3.1、JUnit4.12、Thymeleaf3.0.12、MySQL8.0.27、Druid1.1.15、Logback1.2.3、Lombok1.18.20、Jackson2.9.8、PageHelper5.2.0、Qiniu7.8.0、JavaxMail1.6.2、SheetBeanConverter1.0.0、Tomcat8.0.50、Maven3.8.4、Git2.35.1、百度地图 IP 归属地解析、网易邮箱验证码服务、七牛云平台数据存储 |

| 开发工具                                                     |
| :----------------------------------------------------------- |
| IntelliJ IDEA2021.1、Visual Studio Code、Java1.8.0_311、MySQL8.0.28、Navicat16.0.13、Git2.35.1、Maven3.8.4、Tomcat8.0.50、CentOS7.6、XShell6、Xftp6、Typora1.3.6、Snipaste、ioDraw、XMind |

# 七、待办事项

| 事项                                   | 进度 |
| :------------------------------------- | :--: |
| 密码加密传输和保存                     |  ✓   |
| 支持多种登录方式（用户名、电话、邮箱） |  ✓   |
| 后台管理功能设计实现                   |  ✓   |
| 前后台真正意义上动静分离               |  ✓   |
| 年级上传记录分班级概览与详情查看       |  ✓   |
| 班级上传资料压缩下载（图片、名单）     |  ✓   |
| 负载均衡                               |      |
| 持续化集成与部署                       |      |
| 七牛云平台图片防盗（私有空间）         |      |
| 邮箱验证码的最大有效时间               |      |
| Python 两码一查状态识别                |      |
| 请求发送邮件时增加人机验证             |      |
| Redis 缓存提高系统并发能力             |      |

# 八、许可证

```
MIT License

Copyright (c) 2023 Spring-_-Bear

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

