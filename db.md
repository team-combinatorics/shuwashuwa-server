# 数据库设计


## 用户表`user_info`

| 字段名 | 字段类型 | 说明|
|-|-|-|
| `userid` | `int` | 自增长 |
| `openid` | `int` | 微信提供的用户`id` |
| `user_name` | `varchar(30)` | 用户姓名 |
| `nick_name` | `varchar(30)` | 用户昵称 |
| `phone_number` | `varchar(30)` | 用户手机号 |
| `email` | `varchar(100)` | 用户邮箱 |
| `identity` | `varchar(40)` | 类别：学生、教职工、校外人员 |
| `department` | `varchar(40)` | 院系或工作部门 |
| `grade` | `varchar(40)`  | 年级 |
| `student_id` | `varchar(15)`  | 学号 |
| `comment` | `text` | 备注 |

## 维修单`service_form`

| 字段名 | 字段类型 | 说明|
|-|-|-|
| `formid` | `int` | 自增长，表单id |
| `brand` | `varchar(15)` | 电脑品牌 |
| `conputer_model` | `varchar(30)` | 电脑型号 |
| `cpu_model` | `varchar(30)` | CPU型号 |
| `has_discrete_graphics` | `tinyint` | 是否有独显 |
| `graphics_model` |  | 显卡型号 |
| `laptop_type` |  | 笔记本类型 |
| `bought_time` |  | 购买时间 |
| `is_under_warranty` |  | 是否在保 |
| `problem_description` |  | 问题描述 |
| `problem_type` |  | 问题类型（硬件/软件） |
| `description_by_volunteer` |  | 志愿者填写的描述 |
| `status` |  | 维修单状态 |
| `feedback` |  | 用户反馈信息 |
| `activiti_id` |  | 预约活动id |
| `time_slot` |  | 预约时间段 |