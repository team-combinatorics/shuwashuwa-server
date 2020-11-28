# docker

增加docker支持，能让您们用一行代码快速搭建测试环境

#### 准备工作

1. 安装`docker`与`docker-compose`

   如果您是Ubuntu用户，运行`sudo apt update && sudo apt install docker.io docker-compose`

   如果您是Windows/Mac用户，安装Docker Desktop

2. 从GitHub上Clone项目shuwashuwa-server，确保docker对文件夹有写权限

#### 快速上手

1. 您应该从茨菇小哥哥/群主Misaki/Leesou那里收到了*.jar文件，将它放到`target/`目录下（没有这个文件夹就新建）

2. `cd ./docker-run`

3. 修改`.env`文件中的配置`TARGET_JAR`为您收到文件的文件路径（例如默认情况是`../target/shuwashuwa-0.0.1-SNAPSHOT.jar`）

4. `docker-compose up`

5. 打开浏览器，访问http://localhost:8848/swagger-ui.html，显示正常则表明容器已经启动

#### 我想自己编译Jar

1. `cd ./docker-build`
2. 修改`.env`文件中的配置（主要是目标目录`$TARGET_DIR`和Jar文件名`$TARGET_JAR`）
3. `docker-compose up --build`
4. 打开浏览器，访问http://localhost:8848/swagger-ui.html，显示正常则表明容器已经启动

#### 下线

1. `docker-compose down`

2. 如果您需要清除数据库，`sudo rm -rf ../db/`

   需要注意，当db文件夹不为空时，MySQL容器不会初始化数据库（也就是不会有新增用户和运行sql命令的过程）

