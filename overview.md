# LiteMall 项目概览

LiteMall 是一个小商场系统，采用前后端分离的架构设计，由以下几个部分组成：
- Spring Boot 后端
- Vue 管理员前端
- 微信小程序用户前端
- Vue 用户移动端

## 项目模块结构

### 后端模块
- **litemall-core**: 核心模块，提供配置、工具类、第三方服务接口等
- **litemall-db**: 数据库模块，提供数据访问服务
- **litemall-admin-api**: 管理后台API服务
- **litemall-wx-api**: 微信小程序API服务
- **litemall-all**: 集成模块，整合所有后端服务
- **litemall-all-war**: 集成模块，以WAR形式部署

### 前端模块
- **litemall-admin**: 管理后台前端，基于Vue和Element-UI
- **litemall-wx**: 微信小程序前端
- **litemall-vue**: 移动端H5商城，基于Vue和Vant
- **renard-wx**: 另一套微信小程序前端实现

### 其他目录
- **deploy**: 部署相关脚本和配置
- **doc**: 项目文档
- **docker**: Docker部署相关配置
- **storage**: 存储目录

## 技术栈

### 后端技术栈
- **核心框架**: Spring Boot 2.1.5
- **安全框架**: Shiro
- **持久层**: MyBatis
- **数据库**: MySQL 8.0
- **连接池**: Druid
- **分页插件**: PageHelper
- **API文档**: Swagger2
- **JSON处理**: Jackson
- **日志框架**: Logback

### 前端技术栈
#### 管理后台 (litemall-admin)
- **框架**: Vue 2.6.10
- **UI组件**: Element-UI 2.15.6
- **路由**: Vue Router 3.0.2
- **状态管理**: Vuex 3.1.0
- **HTTP客户端**: Axios
- **图表**: ECharts 4.2.1

#### 微信小程序 (litemall-wx)
- **框架**: 原生微信小程序框架
- **UI组件**: Vant Weapp

#### 移动端H5 (litemall-vue)
- **框架**: Vue 2.5.17
- **UI组件**: Vant 2.0.6
- **路由**: Vue Router 3.0.1
- **状态管理**: Vuex 3.4.0

## 主要功能

### 小商城功能
- 首页展示
- 专题列表、专题详情
- 分类列表、分类详情
- 品牌列表、品牌详情
- 新品首发、人气推荐
- 优惠券列表、优惠券选择
- 团购活动
- 搜索功能
- 商品详情、商品评价、商品分享
- 购物车
- 下单流程
- 订单列表、订单详情、订单售后
- 地址管理、收藏、足迹、意见反馈
- 客服功能

### 管理平台功能
- 会员管理
- 商城管理
- 商品管理
- 推广管理
- 系统管理
- 配置管理
- 统计报表

## 数据库设计

系统使用MySQL数据库，主要表结构包括：
- 用户相关: litemall_user, litemall_address, litemall_collect, litemall_footprint
- 商品相关: litemall_goods, litemall_category, litemall_brand, litemall_goods_product
- 订单相关: litemall_order, litemall_order_goods, litemall_cart
- 营销相关: litemall_coupon, litemall_groupon, litemall_topic
- 系统相关: litemall_admin, litemall_permission, litemall_role, litemall_system

## 部署环境要求

- JDK 1.8+
- MySQL 5.7+
- Maven 3.0+
- Node.js 8.9+
- 微信开发者工具

## 项目特点

1. 前后端分离架构
2. 模块化设计，功能解耦
3. 多种前端实现（管理后台、微信小程序、移动H5）
4. 完整的电商业务流程
5. 支持微信登录、微信支付
6. 统一的API接口设计
7. 完善的权限管理系统

## 第三方服务集成

- 微信登录 (weixin-java-miniapp)
- 微信支付 (weixin-java-pay)
- 腾讯云短信 (qcloudsms)
- 对象存储服务 (腾讯云COS、阿里云OSS、七牛云)
- 邮件服务 (Spring Boot Mail)