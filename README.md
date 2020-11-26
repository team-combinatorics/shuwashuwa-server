# shuwashuwa-server

## TODO


以下为leesou说他要做的

- 删除单个用户的接口（传code，其实跟登陆时一样）
- 删除全部用户的接口
- 用户信息更新接口，在初次填报或修改信息时都会用到，写Controller，然后在service层打印出传来的信息（不用调用DAO实现）
- 前端获取用户信息的接口，返回一个JSON

以下为misaki说她要做的

- 做token
  - TokenUtil.java：改参数表
  - UserServiceImpl：改对TokenUtil的调用
  - 包含：userid, 权限
- 鉴权
  - 深入理解handler和interceptor（至少搞懂返回值有啥用
  - annotation/：增加更多种类的权限注释
  - AuthorizationInterceptor：增加对以上注释的检查