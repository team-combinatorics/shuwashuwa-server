# Webhook

1. 将代码推送到RELEASE分支
2. 等待两分钟

后端服务器更新成功！:)

#### 通过update-server.sh手动部署和更新服务器

1. 确保安装了`docker`与`docker-compose`

   如果您是Ubuntu用户，运行`sudo apt update && sudo apt install docker.io docker-compose`

   如果您是Windows/Mac用户，安装Docker Desktop

2. `sudo chmod +x update-server.sh && sudo ./update-server.sh`

   为了保证安全性，微信小程序的appsecret不会被存储到任何文件里。

   如果您之前没有添加过环境变量，请您手动输入微信小程序的appid和appsecret。

#### 部署Webhook

1. 安装Webhook

   对于Ubuntu和Debian用户，可以`sudo apt install webhook`

   如果您使用其他操作系统，可以在[这里](https://github.com/adnanh/webhook/releases)下载二进制文件

2. 修改update-server.yml中的仓库和分支

3. 添加systemd service (可选)

   修改update-server.service中的appid和appsecret，然后将其放入`/etc/systemd/system`中

   随后执行`sudo systemctl daemon-reload && sudo systemctl enable update-server.service`

4. 配置GitHub WebHook

   请参阅[GitHub文档](https://docs.github.com/en/free-pro-team@latest/developers/webhooks-and-events/about-webhooks)

