# docker

增加docker支持，能让您们用一行代码快速搭建测试环境

#### ！需要后端进行的工作！

您们需要修改连接的数据库：

MySQL连接：`mysql:3306`

数据库名称：`MYSQL_DATABASE` (环境变量)

用户：`MYSQL_USER`  (环境变量)

密码：`MYSQL_PASSWORD` (环境变量)

#### 准备工作

1. 安装`docker`与`docker-compose`

   如果您是Ubuntu用户，运行`sudo apt update && sudo apt install docker.io docker-compose`

   如果您是Windows/Mac用户，安装Docker Desktop

2. 从GitHub上Clone项目shuwashuwa-server，确保docker对文件夹有写权限

#### 上线！

1. 在`./docker`文件夹内打开终端
2. 修改`.env`文件中的配置
3. `docker-compose up`
4. 打开浏览器，访问http://localhost:8848/swagger-ui.html，显示正常则表明容器已经启动

#### 下线

1. `docker-compose down`

2. 如果您需要清除数据库，`sudo rm -rf ../db/`

   需要注意，当db文件夹不为空时，MySQL容器不会初始化数据库（也就是不会有新增用户和运行sql命令的过程）

