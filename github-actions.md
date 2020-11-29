# GitHub Actions

![Java CI with Maven - Build and Test](https://github.com/team-combinatorics/shuwashuwa-server/workflows/Build%20and%20Test/badge.svg)    ![Java CI with Maven - Upload JAR](https://github.com/team-combinatorics/shuwashuwa-server/workflows/Upload%20Jar/badge.svg)

目前仅仅实现了自动在`main`分支上编译Jar包的功能。

这里先给您们谢罪，因为GitHub Actions默认在main分支上启用，因此把main分支玩坏了。

#### 我在哪里能找到Jar？

![image-20201129021407237](https://i.loli.net/2020/11/29/WcSEkJK8mFlXeTA.png)

1. 您在shuwashuwa-server仓库上方标签栏点击Actions，找到All Workflows中名称带Upload Jar的

![image-20201129021553938](https://i.loli.net/2020/11/29/fZH596KEepdz8GR.png)

2. 您在右侧的Artifact中能看见一个zip压缩包，这是保存在GitHub上的Jar包。如果您连GitHub速度不怎么样，您可以点击下方Download Link的链接，从境内服务器下载。（仅有7天有效期）

#### 我想编译新的Jar

由于GitHub Action的使用存在限制，因此无法在每次`main`分支的`commit`时进行编译。触发编译有以下几种方式：

**手动 为shuwashuwa-server项目点个star（之后可以取消）**

**自动 修改main分支中的pom.xml**

自动 通过GitHub API

其中最简单的方式就是点Star（不过您们最好不要点的太频繁）

#### 自动化测试

每一次`main`分支出现新的`commit`时，Build and Test 便会尝试用Maven编译项目，并运行单元测试。



若您们认为当前GitHub Action的功能配置不太合理，欢迎联系我或自己修改。