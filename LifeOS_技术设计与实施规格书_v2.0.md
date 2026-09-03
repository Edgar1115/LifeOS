# LifeOS --- 个人生活管理与 AI Agent 系统

## 技术设计、数据库、API 与 Java 工程实施规格书 v2.0

> 基于 v1.0 规格书与 sky-take-out 工程组织方式优化。

------------------------------------------------------------------------

# 1. 项目定位

LifeOS 是一个 Personal Operating System。

核心目标：

    记录生活
        ↓
    结构化个人数据
        ↓
    统一 Life Timeline
        ↓
    搜索与语义记忆
        ↓
    Insight 分析
        ↓
    Personal Agent
        ↓
    行动执行

系统包含：

-   Diary
-   Note
-   Idea
-   Goal
-   Todo
-   Schedule
-   Health Record
-   Timeline
-   Search
-   Insight
-   Notification
-   AI Agent

------------------------------------------------------------------------

# 2. v2.0 核心调整

v1.0：

    大量 Maven Module
    每个业务域独立工程

v2.0：

    领域边界保持
    工程数量减少

原则：

> 代码按照领域隔离，Maven Module 按技术职责聚合。

------------------------------------------------------------------------

# 3. Maven 工程结构

    lifeos/

    ├── pom.xml

    ├── lifeos-common

    ├── lifeos-domain

    ├── lifeos-application

    ├── lifeos-infrastructure

    ├── lifeos-agent

    ├── lifeos-public

    └── lifeos-server

------------------------------------------------------------------------

# 4. Module 职责

## lifeos-common

共享基础能力。

包含：

    Result
    ErrorCode
    Exception
    DomainEvent
    CurrentUser
    IdGenerator
    Clock
    JSON Utils

禁止：

    Note
    Todo
    Goal
    Agent

业务概念。

------------------------------------------------------------------------

## lifeos-domain

领域模型层。

结构：

    domain

    ├── identity

    ├── capture
    │   ├── diary
    │   ├── note
    │   └── idea

    ├── planning
    │   ├── goal
    │   ├── todo
    │   └── schedule

    ├── health
    │   ├── sleep
    │   ├── diet
    │   ├── workout
    │   └── water

    ├── timeline

    ├── publication

    ├── insight

    └── notification

规则：

Domain 不依赖：

    Spring
    MyBatis
    Redis
    LLM

------------------------------------------------------------------------

## lifeos-application

业务流程层。

负责：

-   Application Service
-   DTO
-   Command
-   Query API
-   Domain Event 发布

调用链：

    Controller

    ↓

    Application Service

    ↓

    Domain

    ↓

    Repository Port

------------------------------------------------------------------------

## lifeos-infrastructure

技术实现层。

负责：

    MySQL
    MyBatis
    Redis
    Qdrant
    Object Storage
    Wechat
    LLM Provider

结构：

    infrastructure

    ├── persistence

    ├── redis

    ├── search

    ├── storage

    ├── wechat

    └── llm

------------------------------------------------------------------------

## lifeos-agent

AI Agent 独立模块。

负责：

    Conversation
    Planning
    Tool Calling
    Memory
    LLM Gateway
    Streaming

禁止：

    Agent -> Mapper
    Agent -> SQL
    Agent -> Repository

------------------------------------------------------------------------

## lifeos-public

公开内容系统。

负责：

    Public Note
    Markdown Render
    HTML Security
    SEO

------------------------------------------------------------------------

## lifeos-server

Spring Boot 启动模块。

负责：

    Configuration
    Filter
    Exception Handler
    Application Bootstrap

------------------------------------------------------------------------

# 5. 依赖规则

                      server

                        |

                  application

                  /          \

            domain          common


                  ↑

           infrastructure

规则：

-   Domain 最纯净
-   Application 编排业务
-   Infrastructure 实现接口
-   Server 负责启动

------------------------------------------------------------------------

# 6. 核心领域

## Capture

负责信息捕获。

包括：

    Diary
    Note
    Idea

区别：

Diary：

    记录发生过的事情

Note：

    长期知识资产

Idea：

    快速捕获想法

------------------------------------------------------------------------

## Planning

负责目标和执行。

模型：

    Goal

    ↓

    Milestone

    ↓

    Todo

Schedule 独立表示时间安排。

------------------------------------------------------------------------

## Health

负责：

    Sleep
    Diet
    Workout
    Water

所有健康数据最终进入：

    LifeEvent

------------------------------------------------------------------------

# 7. Timeline

Timeline 是系统统一事件索引。

不是事实数据库。

结构：

    Business Data

    ↓

    Domain Event

    ↓

    LifeEvent

    ↓

    Timeline

LifeEvent：

    id
    userId

    eventType

    sourceType
    sourceId

    title
    summary

    startTime
    endTime

    metadata

------------------------------------------------------------------------

# 8. Agent 架构

原则：

> LLM 不保存事实。

流程：

    User

    ↓

    Agent

    ↓

    Tool

    ↓

    Application API

    ↓

    Database

    ↓

    Evidence

    ↓

    LLM Response

Tool：

只负责：

    查询
    创建
    修改
    执行动作

------------------------------------------------------------------------

# 9. Search

搜索分三层：

    Structured Query

    ↓

    Keyword Search

    ↓

    Semantic Search

索引：

    Diary
    Note
    Idea
    Goal
    LifeEvent
    Summary

不直接 Embedding：

    Water Record
    Workout Set

------------------------------------------------------------------------

# 10. Insight

Insight 不依赖 LLM 扫描全部数据。

架构：

    Statistics

    ↓

    Rule Engine

    ↓

    Insight

    ↓

    LLM Explanation

规则：

    Water Deficit

    Sleep Deficit

    Todo Overdue

    Goal Risk

    Workout Inactivity

------------------------------------------------------------------------

# 11. Notification

统一提醒能力。

包括：

    Reminder Rule

    Reminder Task

    Notification Inbox

    Wechat Push

严重程度：

    INFO

    MEDIUM

    HIGH

------------------------------------------------------------------------

# 12. 数据流

    Diary
    Note
    Idea
    Todo
    Workout
    Sleep
    Water

            |

            ↓

    Domain Event

            |

            ↓

    LifeEvent

            |

            ├── Search

            ├── Insight

            └── Agent Memory

------------------------------------------------------------------------

# 13. 第一阶段开发计划

## Phase 0

工程基础：

    Maven Structure

    Result

    Exception

    RequestId

    MySQL

    MyBatis

    Flyway

------------------------------------------------------------------------

## Phase 1

Identity：

    User

    JWT

    CurrentUser

------------------------------------------------------------------------

## Phase 2

Capture：

    Diary

    Note

    Idea

------------------------------------------------------------------------

## Phase 3

Planning：

    Goal

    Todo

    Schedule

------------------------------------------------------------------------

## Phase 4

Health：

    Water

    Sleep

    Workout

    Diet

------------------------------------------------------------------------

## Phase 5

Timeline：

    LifeEvent

------------------------------------------------------------------------

## Phase 6

Agent：

先实现：

    Read Only Agent

再增加：

    Write Tool

------------------------------------------------------------------------

# 14. 最终目标

LifeOS 不只是：

    日记软件
    +
    Todo 软件
    +
    健康记录软件

而是：

> 一个以个人长期生活数据为核心，通过 Timeline、Search、Insight 和 Agent
> 构建的 Personal Operating System。

------------------------------------------------------------------------

# 15. 工程原则

必须遵守：

    业务事实归 Domain

    业务流程归 Application

    技术实现归 Infrastructure

    AI 推理归 Agent

    统一时间索引归 Timeline

    主动分析归 Insight

    通知能力归 Notification

禁止：

    万能 LifeService

    万能 UserService

    Agent 直接查数据库
