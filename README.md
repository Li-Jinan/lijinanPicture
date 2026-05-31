# 智能协同云图库

基于 Vue 3 + Spring Boot + COS + WebSocket 的企业级智能协同云图库平台。

## 项目介绍

本项目面向图片素材管理、个人空间、团队协作和图片分析等场景，支持公开图库、私有空间、团队空间、实时协同编辑、批量管理、用量分析和 AI 图片处理能力。

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

## 运行说明

前端进入 `lijinan-picture-frontend` 目录安装依赖并启动：

```bash
npm install
npm run dev
```

后端进入对应后端目录后，按需修改 `application.yml` 中的数据库、缓存、对象存储和 AI 服务配置，再启动 Spring Boot 应用。
