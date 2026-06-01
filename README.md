# 今安智能协同云图库

面向企业和团队图片资产管理的智能协同云图库平台，覆盖公开图库浏览、图片上传审核、个人空间、团队空间、实时协同编辑、空间数据分析和 AI 图片处理等场景。

项目基于 Vue 3 + Spring Boot 构建，后端重点围绕权限体系、缓存体系、分库分表、事务控制和实时协同能力设计，适合作为后端开发岗位面试中的综合型项目展示。

## 在线体验

- 项目地址：http://124.221.85.117:18082/
- 登录页：http://124.221.85.117:18082/user/login
- 接口文档：http://124.221.85.117:18082/api/doc.html
- OpenAPI JSON：http://124.221.85.117:18082/api/v2/api-docs

演示账号：

- 账号：`lijinan`
- 密码：`lijinan123`
- 权限：管理员

说明：公开图库支持游客浏览，面试官打开项目地址后不登录也可以看到示例图片；登录演示账号后可以体验图片上传、管理后台、空间管理和分析功能。

## 项目介绍

今安智能协同云图库用于解决图片素材在团队内分散存储、权限边界不清、检索效率低和多人协作成本高的问题。系统将图片资源按照公开图库、个人空间和团队空间进行分层管理，并通过权限模型、缓存优化、数据分片和事件驱动协同机制保障多用户场景下的稳定性。

核心后端能力包括：

- 权限体系：基于 Sa-Token、Redis Session、自定义注解和 Spring AOP 实现统一鉴权，支持用户、管理员、空间成员等多类权限场景。
- 缓存设计：构建 Redis + Caffeine 多级缓存体系，引入随机过期时间缓解缓存雪崩，降低高频查询对数据库的压力。
- 数据规模：基于 ShardingSphere 自定义分表算法实现团队空间图片动态分表，并通过分片节点校验保证路由准确性。
- 并发控制：采用分段锁与 TransactionTemplate 控制空间容量、批量编辑和协作编辑的事务边界，提升复杂写入场景稳定性。
- 实时协同：基于 WebSocket + Disruptor 处理编辑消息，引入编辑锁避免多人操作冲突，提高实时编辑吞吐能力。
- 文件服务：支持 COS 对象存储；同时提供本地上传兜底能力，便于 Docker/1Panel 环境快速部署和演示。

## 功能模块

- 公开图库：游客可浏览公开图片，用户可按关键词、分类、标签等条件检索图片。
- 图片管理：支持文件上传、URL 上传、图片编辑、图片审核、批量抓取和批量管理。
- 用户体系：支持注册、登录、管理员后台和基于角色的访问控制。
- 个人空间：用户可维护自己的私有图片资产，支持空间容量控制和图片分析。
- 团队空间：支持团队成员管理、角色权限分配和多人实时协同编辑。
- 数据分析：提供空间用量、分类、标签、图片大小、用户行为等统计分析。
- 智能能力：支持以图搜图、颜色搜索和 AI 扩图等图片增强能力。

## 技术选型

后端：

- Java 17
- Spring Boot
- MySQL + MyBatis-Plus
- Redis + Caffeine
- Sa-Token
- WebSocket
- Disruptor
- ShardingSphere
- COS 对象存储 / 本地文件上传
- Knife4j / OpenAPI

前端：

- Vue 3
- Vite
- TypeScript
- Ant Design Vue
- Pinia
- Axios
- OpenAPI 前端代码生成

部署：

- 腾讯云服务器
- 1Panel
- Docker Compose
- Nginx
- MySQL
- Redis

## 页面入口

- `/`：公开图库首页
- `/user/login`：用户登录
- `/add_picture`：创建图片
- `/admin/userManage`：用户管理
- `/admin/pictureManage`：图片管理
- `/admin/spaceManage`：空间管理
- `/my_space`：我的空间
- `/space/:id`：空间详情
- `/space_analyze`：空间分析

## 目录结构

```text
lijinan-picture-frontend/      前端项目
lijinan-picture-backend/       后端项目
lijinan-picture-backend-ddd/   DDD 版本后端项目
```

## 本地运行

前端：

```bash
cd lijinan-picture-frontend
npm install
npm run dev
```

后端：

```bash
cd lijinan-picture-backend
mvn spring-boot:run
```

后端配置默认支持通过环境变量覆盖数据库、Redis、COS、本地上传目录和 AI 服务配置，便于本地开发、Docker Compose 和服务器部署共用同一套代码。

## 面试关注点

- 不是单纯 CRUD：项目包含权限、缓存、事务、分片、文件服务、实时通信和部署运维等完整后端链路。
- 可在线验证：项目已部署到腾讯云，面试官可直接访问公开图库和接口文档，也可使用演示账号体验管理功能。
- 有工程化落地：前后端分离、OpenAPI 代码生成、Docker Compose 部署、Nginx 反向代理和 1Panel 运维管理均已落地。
- 有性能和稳定性设计：针对热点查询、团队空间数据增长、多人协作编辑和上传服务可用性做了专门设计。
