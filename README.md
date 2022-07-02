> 项目部署运行步骤如下：
>
> 方案一：
>
> 1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机
> 2. 打开工程：使用 IntelliJ IDEA  打开克隆的仓库或解压的工程文件
> 3. 创建数据库和表并插入数据：登录 MySQL ，创建 `panda_book_house` 数据库，将 `src/main/resources/sql/panda_book_house.sql` 文件中的数据库表导入 panda_book_house 数据库中
> 4. 修改数据库连接信息：修改 `src/main/resources/properties/jdbc.properties` 中的数据库连接信息，设置你自己的用户名和密码 
> 5. 修改邮箱服务器信息（可选）：修改 `src/main/resources/properties/email.properties` 中的邮箱连接信息，设置你自己的邮箱账号和服务器（不设置则注册时获取邮箱验证码功能不可用）个人邮箱开启 smtp 功能指导博客：[smtp 开启](https://blog.csdn.net/smilehappiness/article/details/108145215)
> 6. 部署访问：在 IntelliJ IDEA 中部署 Tomcat 即可访问熊猫书屋首页
> 7. 登录：账号登录默认用户名和密码均为 `admin`
>
> 方案二：
>
> 1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机
>
> 2. 拷贝 war 包：将 `RELEASE` 目录下的 `panda-book-house.war` 包拷贝到 `Tomcat` 安装目录下的 `webapps` 目录中
>
> 3. 创建数据库和表并插入数据：登录 MySQL ，创建 `panda_book_house` 数据库，将 `src/main/resources/sql/panda_book_house.sql` 文件中的数据库表导入 panda_book_house 数据库中
>
> 4. 创建数据库用户：在 MySQL 控制台创建 `admin` 用户，密码也为 `admin`，并赋予 admin 用户所有操作权限
>
>    ```sql
>    create user 'admin'@'localhost' identified by 'admin';
>    grant all on online_bookhouse.* to 'admin'@'localhost' with grant option;
>    ```
>
> 5. 启动 Tomcat：双击 Tomcat 安装目录下 `bin` 目录中的 `startup.bat` 启动 Tomcat
>
> 6. 访问首页：在浏览器地址栏输入 `http://localhost:8080/panda-book-house/` 即可访问熊猫书屋首页
>
> 7. 登录：账号登录默认用户名和密码均为 `admin`