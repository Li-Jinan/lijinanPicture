# 领域驱动企业协同云图库

基于 Vue 3 + Spring Boot + COS + WebSocket 的企业级智能协同云图库平台。

## 项目介绍

本项目来自简历项目「领域驱动企业协同云图库」，建设周期为 2026 年 01 月至 2026 年 03 月。系统面向图片素材管理、个人空间、团队协作和图片分析等场景，支持公开图库、私有空间、团队空间、实时协同编辑、批量管理、用量分析和 AI 图片处理能力。

项目基于 Spring Boot + Redis + Caffeine + Sa-Token + Disruptor + WebSocket 构建。通过 Redis 分布式状态管理、Sa-Token 细粒度 RBAC、ShardingSphere 动态分片实现多权限用户数据隔离与高效路由；采用 WebSocket + Disruptor 事件驱动模型支撑实时协同编辑，并结合多级缓存与事务控制机制保障高吞吐场景下的数据一致性与系统稳定性。

核心能力包括：

- 公开图库：用户可以上传、检索和浏览图片素材。
- 管理后台：管理员可以审核、管理图片，并查看系统数据分析。
- 私有空间：个人用户可以管理私有图片，支持检索、分享、编辑和分析。
- 团队空间：企业或团队可以邀请成员，共享图片并实时协同编辑。
- 扩展能力：支持缓存优化、权限控制、分库分表、WebSocket 通信和 AI 绘图模型接入。

## 技术选型

### 后端

- Java Spring Boot
- MySQL + MyBatis-Plus
- Redis + Caffeine
- COS 对象存储
- ShardingSphere 分库分表
- Sa-Token 权限控制
- WebSocket 双向通信
- Disruptor 高性能队列
- DDD 领域驱动设计
- AI 绘图模型接入

### 前端

- Vue 3
- Vite
- TypeScript
- Ant Design Vue
- Axios
- Pinia
- OpenAPI 前端代码生成

## 目录结构

- `lijinan-picture-frontend`：前端项目
- `lijinan-picture-backend`：后端项目
- `lijinan-picture-backend-ddd`：DDD 版本后端项目

## 功能模块

- 用户注册、登录、权限管理
- 图片上传、审核、编辑、删除、检索
- 图片批量抓取和批量编辑
- 私有空间和团队空间管理
- 空间成员管理和角色权限控制
- 空间用量、分类、标签、大小和用户行为分析
- 图片以图搜图、颜色搜索和 AI 扩图
- WebSocket 实时协同编辑

## 简历亮点

- 基于 Redis 实现 Session 分布式存储，结合自定义注解与 Spring AOP 统一鉴权框架，支持 5 类操作级权限控制，在多节点部署下实现稳定扩展，鉴权代码重复率降低 60%。
- 构建 Redis + Caffeine 多级缓存体系，引入随机过期时间机制缓解缓存雪崩问题，在热点高并发场景下将缓存命中率提升至 90%+，显著降低数据库压力。
- 封装 COS 通用文件服务，结合数据万象解析图片元信息，上传前完成 WebP 转码和缩略图生成，图片体积平均压缩 60%，提升页面加载体验。
- 基于阿里云百炼大模型封装异步 AI 绘图服务，设计轮询可视化机制，提升任务成功率并优化用户等待体验。
- 基于 Sa-Token Kit 模式构建多账号 RBAC 权限模型，通过统一请求上下文与注解合并实现方法级鉴权，权限逻辑复用度提升 70%，降低系统维护复杂度。
- 采用分段锁 + TransactionTemplate 实现编程式事务，结合限流与预校验机制控制容量，在协同与批量场景下使批处理效率提升 3 倍。
- 基于 ShardingSphere 自定义分表算法实现团队图片动态分表，并结合分片节点校验机制保障路由准确性，支持千万级数据规模下的稳定查询。
- 采用 Disruptor 无锁队列对 WebSocket 消息进行异步化处理，引入编辑锁机制避免冲突，系统峰值吞吐能力提升 2 倍，保持毫秒级响应。

## 运行说明

前端进入 `lijinan-picture-frontend` 目录安装依赖并启动：

```bash
npm install
npm run dev
```

后端进入对应后端目录后，按需修改 `application.yml` 中的数据库、缓存、对象存储和 AI 服务配置，再启动 Spring Boot 应用。
