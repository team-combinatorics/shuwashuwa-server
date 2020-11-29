# docker

增加docker-compose支持，能让您用一行命令快速搭建测试环境

#### 准备工作

1. 安装`docker`与`docker-compose`

   如果您是Ubuntu用户，运行`sudo apt update && sudo apt install docker.io docker-compose`

   如果您是Windows/Mac用户，安装Docker Desktop

2. 从GitHub上Clone项目shuwashuwa-server，确保docker对文件夹有写权限

#### 快速上手

1. 您应该从茨菇小哥哥/群主米歇尔/Leesou那里到了*.jar文件。

   若您不想修改配置文件，将它放到`target`目录下（没有这个文件夹就新建）

2. `cd ./docker-run`

3. （可选）修改`.env`文件中的配置`TARGET_PATH`为您收到文件的文件路径

   （默认情况是`../target/shuwashuwa-0.0.1-SNAPSHOT.jar`）

4. `docker-compose up`

5. 打开浏览器，访问http://localhost:8848/swagger-ui.html，显示正常则表明容器已经启动

#### 我想自己编译Jar

1. `cd ./docker-build`
2. （可选）修改`.env`文件中的配置（主要是目标目录`$TARGET_DIR`和Jar文件名`$TARGET_JAR`）
3. `docker-compose up --build`
4. 打开浏览器，访问http://localhost:8848/swagger-ui.html，显示正常则表明容器已经启动

#### 关闭服务

1. `docker-compose down`

#### 高级操作

1. 如果您需要清除数据库：`sudo rm -rf ./db/`

   需要注意，当db文件夹不为空时，MySQL容器不会初始化数据库（也就是不会有新增用户和运行sql命令的过程）

2. 如果您使用的是`docker-build`，您可以直接访问http://localhost:8080管理数据库。考虑到adminer可能造成额外的安全风险，因此`docker-run`中默认不启用adminer，您可以编辑`docker-compose.yml`，取消其中adminer的注释。

3. 修改配置

   以下为建议在生产环境修改的配置：

   `MYSQL_ROOT_PASSWORD` MySQL的Root密码（存储目录无数据库时生效）

   `MYSQL_DATABASE`  MySQL的数据库名称（存储目录无数据库时生效）

   `MYSQL_USER` MySQL的用户名（存储目录无数据库时生效）

   `MYSQL_PASSWORD` MySQL的用户密码（存储目录无数据库时生效）

   `DBSTORE` MySQL数据库存储目录

   `TARGET_DIR` Maven的target目录

   `TARGET_JAR` 生成Jar的文件名

   `TARGET_PATH` 需要运行的Jar的路径

   如果您不知道自己在做什么，请谨慎修改其他配置。

#### 故障排除

1. `Error setting permission for ca.pem`

   如果您在WSL中运行docker-compose并映射了宿主机的文件夹，可能会出现神必的权限问题。建议Windows用户使用Docker Desktop。

2. `shuwashuwa Exit code 127`

   找不到Jar，请您检查.env对应的Jar文件是否存在
3. `/usr/bin/env: bash\r not found` 或其他带`\r`的错误

   您需要手动把`docker-run`和`docker-build`下的文件修改为LF编码

4. 如果您出现其他错误且难以自行解决，欢迎提issue