# LifeOS — 个人生活管理与 AI Agent 系统

## 技术设计、数据库、API 与 Java 工程实施规格书 v1.0

> **项目定位：** 以微信小程序为主要客户端的个人生活管理系统，通过结构化生活数据、时间线、Goal、周期总结与 Personal Life
> Agent，构建一个“可记录、可检索、可分析、可提醒、可行动”的个人数字记忆系统。
>
> **核心思想：**
>
> ```text
> 记录生活
>    ↓
> 形成结构化个人数据
>    ↓
> 建立统一 Life Timeline
>    ↓
> AI 理解与检索长期记忆
>    ↓
> 周/月总结与趋势分析
>    ↓
> 主动 Insight / Reminder
>    ↓
> Agent 协助用户采取行动
> ```
>
> **架构原则：**
>
> - 高内聚、低耦合；
> - 模块化单体优先，不为“高级”过早拆微服务；
> - 每个业务域拥有自己的实体、Service、Repository 和数据库表；
> - 跨模块禁止直接操作其他模块的 Mapper；
> - 模块之间优先使用 Application API、Domain Event、Port；
> - MySQL 是业务事实数据源；
> - Redis 只保存缓存、短期状态和短期对话 Memory；
> - AI 不能成为业务事实源；
> - Agent 查询事实必须通过 Tool 查询系统数据；
> - 重要写操作必须经过业务 Service；
> - Note 发布版本与私人编辑版本完全分离；
> - 所有重要行为最终能够映射为统一 `LifeEvent`。

---

# 1. 产品定义

LifeOS 不只是：

```text
日记软件
+
笔记软件
+
Todo
+
健身记录
```

而是：

> **Personal Data Platform + Personal Memory + Personal Agent**

系统最终回答五类问题。

### 1.1 记录

> 今天发生了什么？

包括：

- Diary；
- Note；
- Idea；
- Todo；
- Schedule；
- Sleep；
- Diet；
- Workout；
- Water；
- Goal Progress。

---

### 1.2 检索

> 我以前什么时候做过某件事情？

例如：

```text
我上次练腿是什么时候？

我什么时候写过 AQS？

我之前是不是想过一个人生管理系统？

8 月 17 日晚上我做了什么？
```

---

### 1.3 分析

> 最近我的生活状态怎么样？

包括：

- 睡眠趋势；
- 饮水趋势；
- 健身频率；
- Todo 完成率；
- Goal 推进情况；
- 情绪变化；
- 学习/工作主题；
- 饮食习惯。

---

### 1.4 提醒

例如：

```text
连续 4 天饮水不足

连续 5 天平均睡眠不足 6.5h

Goal 距离 Deadline 还有 7 天，但完成度只有 35%

存在逾期 Todo

连续两周没有健身
```

---

### 1.5 行动

Agent 可以执行：

```text
创建 Idea

创建 Todo

创建 Schedule

创建 Reminder

记录饮水

查询 Goal

修改 Goal Progress

查找 Note

发布 Note
```

---

# 2. 业务模块

系统划分为八个主要业务域。

```text
LifeOS
│
├── Identity
│
├── Capture
│   ├── Diary
│   ├── Note
│   └── Idea
│
├── Planning
│   ├── Goal
│   ├── Milestone
│   ├── Todo
│   └── Schedule
│
├── Health
│   ├── Sleep
│   ├── Diet
│   ├── Workout
│   └── Water
│
├── Timeline
│
├── Publication
│
├── Search
│
├── Insight
│
├── Notification
│
└── Agent
```

---

# 3. 技术架构

第一版采用：

```text
微信小程序
      │
      │ HTTPS / SSE
      ▼
┌───────────────────────────┐
│       LifeOS Backend      │
│                           │
│ Java Modular Monolith     │
└─────────────┬─────────────┘
              │
    ┌─────────┼──────────┐
    ▼         ▼          ▼
  MySQL     Redis      Object Storage
    │
    ▼
 Search / Vector Store
    │
    ▼
 LLM Provider
```

技术建议：

```text
Java               17+
Spring Boot        4.x
Spring MVC
Spring Security
MyBatis
MySQL              8.x
Redis
RabbitMQ           后期异步任务
Qdrant             Semantic Search
Maven
JUnit 5
Mockito
Testcontainers
```

Markdown：

```text
flexmark-java
+
HTML Sanitizer
```

对象存储：

```text
开发：
Local Storage / MinIO

部署：
OSS / COS / S3 Compatible Storage
```

---

# 4. 为什么使用模块化单体

不建议一开始拆：

```text
note-service
todo-service
health-service
agent-service
...
```

LifeOS 初期规模不需要微服务。

但代码层面提前建立严格边界。

结构：

```text
一个部署单元
+
多个 Maven Module
+
明确 Domain Boundary
```

未来：

```text
如果 Agent 独立扩容
→ agent 模块拆服务

如果公开文章流量增大
→ publication 模块拆服务

如果 Search 独立扩展
→ search 模块拆服务
```

业务代码不需要大改。

---

# 5. Maven 工程结构

```text
lifeos/
│
├── pom.xml
│
├── README.md
│
├── docs/
│
├── deploy/
│
├── lifeos-kernel/
│
├── lifeos-contracts/
│
├── lifeos-module-identity/
│
├── lifeos-module-capture/
│
├── lifeos-module-planning/
│
├── lifeos-module-health/
│
├── lifeos-module-timeline/
│
├── lifeos-module-publication/
│
├── lifeos-module-search/
│
├── lifeos-module-insight/
│
├── lifeos-module-notification/
│
├── lifeos-module-agent/
│
├── lifeos-integration/
│
└── lifeos-boot/
```

---

# 6. Maven 依赖规则

最重要的规则：

```text
lifeos-kernel
      ↑
lifeos-contracts
      ↑
各业务模块
```

启动模块：

```text
lifeos-boot
 ↓
所有 Module
 ↓
lifeos-integration
```

禁止：

```text
Capture
 ↓
直接依赖 Health

Health
 ↓
直接依赖 Planning
```

尤其禁止：

```java
@Autowired
private TodoMapper todoMapper;
```

出现在：

```text
Agent
Insight
Timeline
Health
```

等其他模块。

---

# 7. Cross Module 协作

采用三种方式。

## 7.1 Application API

同步查询。

例如 Agent 查询 Goal：

```text
Agent
 ↓
GoalQueryApi
 ↓
Planning Module
```

而不是：

```text
Agent
 ↓
GoalMapper
```

---

## 7.2 Domain Event

异步解耦。

例如：

```text
Todo 完成
 ↓
TodoCompletedEvent
 ↓
Timeline Handler
 ↓
LifeEvent
```

同时：

```text
TodoCompletedEvent
 ↓
Insight
```

---

## 7.3 Port

对外部能力抽象。

例如：

```java
public interface LlmGateway {
    LlmResponse chat(LlmRequest request);
}
```

Application 不关心：

```text
OpenAI
DeepSeek
Claude
Local Model
```

---

# 8. lifeos-kernel

职责：

> 全系统最小 Shared Kernel。

只能放真正跨模块基础设施。

目录：

```text
lifeos-kernel/
└── src/main/java/com/edgar/lifeos/kernel/
    │
    ├── api/
    │   ├── Result.java
    │   ├── PageResult.java
    │   └── ErrorCode.java
    │
    ├── exception/
    │   ├── BusinessException.java
    │   ├── NotFoundException.java
    │   ├── ForbiddenException.java
    │   └── ConflictException.java
    │
    ├── domain/
    │   ├── DomainEvent.java
    │   ├── AggregateRoot.java
    │   └── BaseEntity.java
    │
    ├── security/
    │   ├── CurrentUser.java
    │   └── CurrentUserProvider.java
    │
    ├── id/
    │   ├── IdGenerator.java
    │   └── SnowflakeIdGenerator.java
    │
    ├── time/
    │   └── ClockProvider.java
    │
    └── json/
        └── JsonUtils.java
```

Kernel 禁止出现：

```text
Note
Todo
Workout
Agent
```

等任何业务概念。

---

# 9. lifeos-contracts

只保存跨模块 Contract。

```text
lifeos-contracts/
└── src/main/java/com/edgar/lifeos/contracts/
    │
    ├── event/
    │   ├── DiarySavedEvent.java
    │   ├── NoteCreatedEvent.java
    │   ├── NoteUpdatedEvent.java
    │   ├── IdeaCreatedEvent.java
    │   ├── TodoCreatedEvent.java
    │   ├── TodoCompletedEvent.java
    │   ├── GoalCreatedEvent.java
    │   ├── GoalProgressChangedEvent.java
    │   ├── ScheduleCreatedEvent.java
    │   ├── WaterRecordedEvent.java
    │   ├── SleepRecordedEvent.java
    │   ├── MealRecordedEvent.java
    │   ├── WorkoutFinishedEvent.java
    │   ├── NotePublishedEvent.java
    │   └── InsightCreatedEvent.java
    │
    └── model/
        ├── LifeSourceType.java
        ├── DateRange.java
        └── TimeRange.java
```

这里只保存：

```text
ID
时间
Event Type
必要字段
```

不能保存业务 Service。

---

# 10. Identity Module

负责：

```text
用户
微信登录
JWT
个人设置
```

路径：

```text
lifeos-module-identity/
└── src/main/java/com/edgar/lifeos/identity/
    │
    ├── api/
    │   ├── AuthController.java
    │   ├── UserController.java
    │   ├── AuthApplicationApi.java
    │   └── UserQueryApi.java
    │
    ├── application/
    │   ├── AuthService.java
    │   ├── UserService.java
    │   ├── UserSettingService.java
    │   └── dto/
    │       ├── WechatLoginCommand.java
    │       ├── LoginResult.java
    │       ├── UserProfileVO.java
    │       └── UpdateUserSettingCommand.java
    │
    ├── domain/
    │   ├── User.java
    │   ├── UserSetting.java
    │   ├── UserRepository.java
    │   └── UserStatus.java
    │
    └── infrastructure/
        ├── persistence/
        │   ├── UserMapper.java
        │   ├── UserSettingMapper.java
        │   └── UserRepositoryImpl.java
        │
        ├── security/
        │   ├── JwtTokenService.java
        │   ├── JwtAuthenticationFilter.java
        │   └── SecurityConfig.java
        │
        └── wechat/
            ├── WechatAuthClient.java
            └── WechatAuthClientImpl.java
```

---

# 11. Capture Module

包含：

```text
Diary
Note
Idea
```

但内部仍保持三个独立 Package。

---

# 12. Diary

定义：

> 强日期属性的个人生活记录。

目录：

```text
capture/diary/
├── api/
│   ├── DiaryController.java
│   └── DiaryQueryApi.java
│
├── application/
│   ├── DiaryService.java
│   └── dto/
│       ├── CreateDiaryCommand.java
│       ├── UpdateDiaryCommand.java
│       ├── DiaryDetailVO.java
│       └── DiaryCalendarVO.java
│
├── domain/
│   ├── Diary.java
│   ├── DiaryRepository.java
│   └── MoodLevel.java
│
└── infrastructure/
    ├── DiaryMapper.java
    └── DiaryRepositoryImpl.java
```

Diary 字段：

```text
id
userId

diaryDate

title
content

mood
tags

createdAt
updatedAt
deleted
```

---

# 13. Note

定义：

> 可长期编辑的 Markdown 知识文档。

目录：

```text
capture/note/
├── api/
│   ├── NoteController.java
│   └── NoteQueryApi.java
│
├── application/
│   ├── NoteService.java
│   ├── NoteFolderService.java
│   ├── NoteTagService.java
│   └── dto/
│       ├── CreateNoteCommand.java
│       ├── UpdateNoteCommand.java
│       ├── MoveNoteCommand.java
│       ├── NoteDetailVO.java
│       ├── NoteListVO.java
│       ├── CreateFolderCommand.java
│       └── CreateTagCommand.java
│
├── domain/
│   ├── Note.java
│   ├── NoteFolder.java
│   ├── NoteTag.java
│   ├── NoteRepository.java
│   ├── NoteFolderRepository.java
│   └── NoteTagRepository.java
│
└── infrastructure/
    ├── NoteMapper.java
    ├── NoteFolderMapper.java
    ├── NoteTagMapper.java
    ├── NoteTagRelationMapper.java
    ├── NoteRepositoryImpl.java
    ├── NoteFolderRepositoryImpl.java
    └── NoteTagRepositoryImpl.java
```

Note：

```text
id
userId

folderId

title
contentMarkdown

excerpt

wordCount

createdAt
updatedAt
deleted
```

---

# 14. Idea

定义：

> 极低成本、极短的信息捕获。

限制：

```text
建议 <= 500 字
```

路径：

```text
capture/idea/
├── api/
│   ├── IdeaController.java
│   └── IdeaQueryApi.java
│
├── application/
│   ├── IdeaService.java
│   └── dto/
│       ├── CreateIdeaCommand.java
│       ├── UpdateIdeaCommand.java
│       ├── IdeaVO.java
│       └── ConvertIdeaCommand.java
│
├── domain/
│   ├── Idea.java
│   ├── IdeaStatus.java
│   └── IdeaRepository.java
│
└── infrastructure/
    ├── IdeaMapper.java
    └── IdeaRepositoryImpl.java
```

状态：

```text
RAW
ORGANIZED
CONVERTED_TO_NOTE
CONVERTED_TO_TODO
ARCHIVED
```

---

# 15. Planning Module

包含：

```text
Goal
Milestone
Todo
Schedule
```

---

# 16. Goal

模型：

```text
Goal
 ↓
Milestone
 ↓
Todo
```

目录：

```text
planning/goal/
├── api/
│   ├── GoalController.java
│   └── GoalQueryApi.java
│
├── application/
│   ├── GoalService.java
│   ├── MilestoneService.java
│   └── dto/
│       ├── CreateGoalCommand.java
│       ├── UpdateGoalCommand.java
│       ├── GoalDetailVO.java
│       ├── UpdateGoalProgressCommand.java
│       ├── CreateMilestoneCommand.java
│       └── MilestoneVO.java
│
├── domain/
│   ├── Goal.java
│   ├── GoalStatus.java
│   ├── Milestone.java
│   ├── MilestoneStatus.java
│   ├── GoalRepository.java
│   └── MilestoneRepository.java
│
└── infrastructure/
    ├── GoalMapper.java
    ├── MilestoneMapper.java
    ├── GoalRepositoryImpl.java
    └── MilestoneRepositoryImpl.java
```

Goal：

```text
id
userId

title
description

startDate
deadline

progress

priority
status

createdAt
updatedAt
```

---

# 17. Todo

路径：

```text
planning/todo/
├── api/
│   ├── TodoController.java
│   └── TodoQueryApi.java
│
├── application/
│   ├── TodoService.java
│   └── dto/
│       ├── CreateTodoCommand.java
│       ├── UpdateTodoCommand.java
│       ├── CompleteTodoCommand.java
│       ├── TodoVO.java
│       └── TodoQuery.java
│
├── domain/
│   ├── Todo.java
│   ├── TodoStatus.java
│   ├── TodoPriority.java
│   └── TodoRepository.java
│
└── infrastructure/
    ├── TodoMapper.java
    └── TodoRepositoryImpl.java
```

状态：

```text
TODO
DOING
DONE
CANCELLED
```

---

# 18. Schedule

路径：

```text
planning/schedule/
├── api/
│   ├── ScheduleController.java
│   └── ScheduleQueryApi.java
│
├── application/
│   ├── ScheduleService.java
│   └── dto/
│       ├── CreateScheduleCommand.java
│       ├── UpdateScheduleCommand.java
│       ├── ScheduleDetailVO.java
│       └── ScheduleCalendarVO.java
│
├── domain/
│   ├── ScheduleEvent.java
│   ├── ScheduleType.java
│   └── ScheduleRepository.java
│
└── infrastructure/
    ├── ScheduleMapper.java
    └── ScheduleRepositoryImpl.java
```

---

# 19. Health Module

目录：

```text
lifeos-module-health/
└── com/edgar/lifeos/health/
    │
    ├── sleep/
    ├── diet/
    ├── workout/
    └── water/
```

---

# 20. Sleep

```text
sleep/
├── api/
│   ├── SleepController.java
│   └── SleepQueryApi.java
├── application/
│   ├── SleepService.java
│   └── dto/
│       ├── CreateSleepRecordCommand.java
│       ├── SleepRecordVO.java
│       └── SleepStatisticsVO.java
├── domain/
│   ├── SleepRecord.java
│   ├── SleepQuality.java
│   └── SleepRepository.java
└── infrastructure/
    ├── SleepMapper.java
    └── SleepRepositoryImpl.java
```

---

# 21. Water

```text
water/
├── api/
│   ├── WaterController.java
│   └── WaterQueryApi.java
├── application/
│   ├── WaterService.java
│   └── dto/
│       ├── RecordWaterCommand.java
│       ├── WaterTodayVO.java
│       └── WaterStatisticsVO.java
├── domain/
│   ├── WaterRecord.java
│   ├── WaterGoal.java
│   ├── WaterRepository.java
│   └── WaterGoalRepository.java
└── infrastructure/
    ├── WaterMapper.java
    ├── WaterGoalMapper.java
    └── WaterRepositoryImpl.java
```

---

# 22. Diet

```text
diet/
├── api/
│   ├── DietController.java
│   └── DietQueryApi.java
├── application/
│   ├── DietService.java
│   └── dto/
│       ├── CreateMealCommand.java
│       ├── AddFoodItemCommand.java
│       ├── MealDetailVO.java
│       └── DietStatisticsVO.java
├── domain/
│   ├── Meal.java
│   ├── FoodItem.java
│   ├── MealType.java
│   └── DietRepository.java
└── infrastructure/
    ├── MealMapper.java
    ├── FoodItemMapper.java
    └── DietRepositoryImpl.java
```

---

# 23. Workout

结构：

```text
WorkoutSession
 ↓
Exercise
 ↓
WorkoutSet
```

目录：

```text
workout/
├── api/
│   ├── WorkoutController.java
│   └── WorkoutQueryApi.java
├── application/
│   ├── WorkoutService.java
│   └── dto/
│       ├── CreateWorkoutCommand.java
│       ├── AddExerciseCommand.java
│       ├── AddWorkoutSetCommand.java
│       ├── FinishWorkoutCommand.java
│       ├── WorkoutDetailVO.java
│       └── WorkoutStatisticsVO.java
├── domain/
│   ├── WorkoutSession.java
│   ├── WorkoutExercise.java
│   ├── WorkoutSet.java
│   └── WorkoutRepository.java
└── infrastructure/
    ├── WorkoutSessionMapper.java
    ├── WorkoutExerciseMapper.java
    ├── WorkoutSetMapper.java
    └── WorkoutRepositoryImpl.java
```

---

# 24. Timeline Module

Timeline 是整个系统最重要的基础设施之一。

定义：

> 系统统一的人生事件索引。

目录：

```text
lifeos-module-timeline/
└── com/edgar/lifeos/timeline/
    │
    ├── api/
    │   ├── TimelineController.java
    │   └── TimelineQueryApi.java
    │
    ├── application/
    │   ├── TimelineService.java
    │   └── dto/
    │       ├── LifeEventVO.java
    │       ├── TimelineQuery.java
    │       └── DayTimelineVO.java
    │
    ├── domain/
    │   ├── LifeEvent.java
    │   ├── LifeEventType.java
    │   └── LifeEventRepository.java
    │
    └── infrastructure/
        ├── LifeEventMapper.java
        └── LifeEventRepositoryImpl.java
```

---

# 25. LifeEvent

结构：

```text
id
userId

eventType

sourceType
sourceId

title
summary

startTime
endTime

metadataJson

createdAt
```

例如：

```text
eventType:
WORKOUT_FINISHED

sourceType:
WORKOUT

sourceId:
82372

summary:
胸部训练 63 分钟
```

---

# 26. Publication Module

Note 发布必须与 Note 编辑解耦。

目录：

```text
lifeos-module-publication/
└── com/edgar/lifeos/publication/
    │
    ├── api/
    │   ├── NotePublishController.java
    │   ├── PublicNoteController.java
    │   └── PublicationQueryApi.java
    │
    ├── application/
    │   ├── NotePublishService.java
    │   ├── MarkdownRenderService.java
    │   └── dto/
    │       ├── PublishNoteCommand.java
    │       ├── UpdatePublicationCommand.java
    │       ├── PublicNoteVO.java
    │       └── PublicationVO.java
    │
    ├── domain/
    │   ├── NotePublication.java
    │   ├── NotePublishVersion.java
    │   ├── PublishVisibility.java
    │   ├── PublicationRepository.java
    │   └── PublishVersionRepository.java
    │
    └── infrastructure/
        ├── persistence/
        │   ├── PublicationMapper.java
        │   ├── PublishVersionMapper.java
        │   └── PublicationRepositoryImpl.java
        │
        └── markdown/
            ├── FlexmarkMarkdownRenderer.java
            ├── HtmlSanitizer.java
            └── SlugGenerator.java
```

---

# 27. Note Publish 模型

必须采用：

```text
私人 Note
    │
    │ publish
    ▼
Published Snapshot
```

而不是：

```text
公开网页直接读取 note.content
```

---

# 28. Visibility

```text
PRIVATE
UNLISTED
PUBLIC
```

含义：

### PRIVATE

不对外开放。

### UNLISTED

知道 URL 可以访问。

但：

```text
不显示到公开主页
不参与站内公开搜索
noindex
```

### PUBLIC

任何人可以访问。

---

# 29. Search Module

统一负责：

```text
Keyword Search
Semantic Search
Hybrid Search
```

目录：

```text
lifeos-module-search/
└── com/edgar/lifeos/search/
    │
    ├── api/
    │   ├── SearchController.java
    │   └── SearchApplicationApi.java
    │
    ├── application/
    │   ├── SearchService.java
    │   ├── IndexService.java
    │   └── dto/
    │       ├── SearchQuery.java
    │       ├── SearchResultVO.java
    │       └── IndexDocumentCommand.java
    │
    ├── domain/
    │   ├── SearchDocument.java
    │   ├── SearchDocumentType.java
    │   ├── SearchRepository.java
    │   └── SemanticSearchPort.java
    │
    └── infrastructure/
        ├── mysql/
        │   └── MysqlKeywordSearchAdapter.java
        └── qdrant/
            └── QdrantSemanticSearchAdapter.java
```

---

# 30. Search 范围

可索引：

```text
Diary

Note

Idea

Goal

LifeEvent

Weekly Summary

Monthly Summary
```

不建议把：

```text
每一杯水
每一 WorkoutSet
```

直接做 Embedding。

---

# 31. Insight Module

负责：

```text
数据统计
规则检测
周总结
月总结
趋势分析
```

目录：

```text
lifeos-module-insight/
└── com/edgar/lifeos/insight/
    │
    ├── api/
    │   ├── InsightController.java
    │   ├── SummaryController.java
    │   └── InsightQueryApi.java
    │
    ├── application/
    │   ├── InsightService.java
    │   ├── WeeklySummaryService.java
    │   ├── MonthlySummaryService.java
    │   ├── LifeStatisticsService.java
    │   └── InsightRuleEngine.java
    │
    ├── application/rule/
    │   ├── InsightRule.java
    │   ├── WaterDeficitRule.java
    │   ├── SleepDeficitRule.java
    │   ├── SleepIrregularityRule.java
    │   ├── GoalDeadlineRiskRule.java
    │   ├── TodoOverdueRule.java
    │   ├── WorkoutInactivityRule.java
    │   └── PositiveStreakRule.java
    │
    ├── domain/
    │   ├── Insight.java
    │   ├── InsightType.java
    │   ├── InsightSeverity.java
    │   ├── WeeklySummary.java
    │   ├── MonthlySummary.java
    │   └── InsightRepository.java
    │
    └── infrastructure/
        ├── InsightMapper.java
        ├── WeeklySummaryMapper.java
        ├── MonthlySummaryMapper.java
        └── InsightRepositoryImpl.java
```

---

# 32. Notification Module

Reminder 不属于某个具体业务域。

统一建：

```text
Notification Module
```

负责：

```text
提醒规则
提醒任务
Inbox
微信通知
冷却策略
发送记录
```

目录：

```text
lifeos-module-notification/
└── com/edgar/lifeos/notification/
    │
    ├── api/
    │   ├── ReminderController.java
    │   ├── NotificationController.java
    │   └── NotificationApplicationApi.java
    │
    ├── application/
    │   ├── ReminderService.java
    │   ├── ReminderScheduler.java
    │   ├── NotificationService.java
    │   └── NotificationPolicyService.java
    │
    ├── domain/
    │   ├── ReminderRule.java
    │   ├── ReminderTask.java
    │   ├── ReminderSourceType.java
    │   ├── ReminderTriggerType.java
    │   ├── Notification.java
    │   ├── NotificationSeverity.java
    │   └── NotificationRepository.java
    │
    └── infrastructure/
        ├── persistence/
        │   ├── ReminderRuleMapper.java
        │   ├── ReminderTaskMapper.java
        │   ├── NotificationMapper.java
        │   └── NotificationRepositoryImpl.java
        └── wechat/
            ├── WechatNotificationClient.java
            └── WechatNotificationClientImpl.java
```

---

# 33. Agent Module

Agent 不允许直接调用：

```text
Mapper
Repository
SQL
```

Agent 只知道：

```text
AgentTool
```

目录：

```text
lifeos-module-agent/
└── com/edgar/lifeos/agent/
    │
    ├── api/
    │   ├── AgentController.java
    │   └── AgentConversationApi.java
    │
    ├── application/
    │   ├── AgentService.java
    │   ├── AgentOrchestrator.java
    │   ├── AgentPlanner.java
    │   ├── AgentToolRegistry.java
    │   ├── AgentMemoryService.java
    │   └── AgentResponseStreamer.java
    │
    ├── application/tool/
    │   ├── AgentTool.java
    │   ├── AgentToolContext.java
    │   ├── AgentToolRequest.java
    │   ├── AgentToolResult.java
    │   ├── ToolPermission.java
    │   └── ToolExecutionPolicy.java
    │
    ├── domain/
    │   ├── AgentSession.java
    │   ├── AgentMessage.java
    │   ├── AgentRun.java
    │   ├── AgentToolCall.java
    │   ├── AgentRunStatus.java
    │   └── AgentRepository.java
    │
    ├── port/
    │   ├── LlmGateway.java
    │   ├── ConversationMemoryPort.java
    │   └── SemanticMemoryPort.java
    │
    └── infrastructure/
        ├── persistence/
        │   ├── AgentSessionMapper.java
        │   ├── AgentMessageMapper.java
        │   ├── AgentRunMapper.java
        │   ├── AgentToolCallMapper.java
        │   └── AgentRepositoryImpl.java
        │
        ├── llm/
        │   └── OpenAiCompatibleLlmGateway.java
        │
        └── redis/
            └── RedisConversationMemoryAdapter.java
```

---

# 34. Agent Tool 不能放在 Agent Core

真正访问业务的 Tool 放到：

```text
lifeos-integration
```

因为 Integration 可以同时依赖：

```text
Agent
Capture
Planning
Health
Timeline
Publication
Search
Insight
Notification
```

---

# 35. Integration Module

```text
lifeos-integration/
└── com/edgar/lifeos/integration/
    │
    ├── agenttool/
    │   ├── SearchLifeEventsTool.java
    │   ├── SearchNotesTool.java
    │   ├── SearchIdeasTool.java
    │   ├── GetGoalProgressTool.java
    │   ├── GetHealthStatisticsTool.java
    │   ├── GetWeeklySummaryTool.java
    │   ├── CreateIdeaTool.java
    │   ├── CreateTodoTool.java
    │   ├── CreateScheduleTool.java
    │   ├── RecordWaterTool.java
    │   └── PublishNoteTool.java
    │
    ├── event/
    │   ├── TimelineEventHandler.java
    │   ├── SearchIndexEventHandler.java
    │   ├── InsightEventHandler.java
    │   └── NotificationEventHandler.java
    │
    ├── outbox/
    │   ├── OutboxEvent.java
    │   ├── OutboxEventMapper.java
    │   ├── OutboxPublisher.java
    │   └── OutboxRecoveryJob.java
    │
    └── configuration/
        └── IntegrationConfiguration.java
```

---

# 36. 为什么 Integration Module 很重要

错误设计：

```text
AgentService
 ↓
TodoMapper
 ↓
NoteMapper
 ↓
WorkoutMapper
 ↓
WaterMapper
```

最终 Agent 会变成 God Class。

正确：

```text
Agent
 ↓
AgentTool

Integration
 ↓
TodoApplicationApi
NoteQueryApi
WaterApplicationApi
...
```

这样 Agent 不知道实际业务实现。

---

# 37. Agent Workflow

```text
USER MESSAGE
     ↓
LOAD CONVERSATION MEMORY
     ↓
INTENT ANALYSIS
     ↓
PLAN
     ↓
SELECT TOOL
     ↓
EXECUTE TOOL
     ↓
VALIDATE RESULT
     ↓
NEED MORE TOOL?
 ┌───┴────┐
 Yes      No
 │         │
 REPLAN    ▼
        GENERATE
           ↓
         OUTPUT
```

---

# 38. Agent Tool 分类

## READ_ONLY

可以自动执行。

例如：

```text
search_life_events
search_notes
get_goal_progress
get_sleep_statistics
get_water_statistics
```

---

## MUTATING

会修改系统：

```text
create_todo
create_schedule
record_water
publish_note
update_goal
```

必须：

```text
权限验证
参数验证
幂等
审计
```

其中高风险操作可以：

```text
requiresConfirmation = true
```

---

# 39. Agent Memory

分三层。

## Conversation Memory

Redis：

```text
agent:session:{sessionId}:recent
```

保存：

```text
最近 20～40 条
```

---

## Durable Conversation

MySQL：

```text
agent_message
```

完整历史。

---

## Semantic Life Memory

Qdrant：

```text
Diary
Note
Idea
Goal
Summary
LifeEvent Summary
```

---

# 40. Agent 的基本原则

必须坚持：

> LLM 不负责记住事实。

而是：

```text
用户问题
 ↓
Agent
 ↓
Tool
 ↓
MySQL / Search
 ↓
Evidence
 ↓
LLM
 ↓
自然语言回答
```

---

# 41. 数据库总体设计

所有业务表统一：

```text
BIGINT id

BIGINT user_id

DATETIME(3)

utf8mb4
```

ID：

```text
Snowflake BIGINT
```

---

# 42. sys_user

```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY,
    openid VARCHAR(128) NOT NULL,
    unionid VARCHAR(128),
    nickname VARCHAR(100),
    avatar_url VARCHAR(500),
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_openid (openid),
    KEY idx_unionid (unionid)
);
```

---

# 43. diary

```sql
CREATE TABLE diary (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    diary_date DATE NOT NULL,

    title VARCHAR(200),
    content MEDIUMTEXT NOT NULL,

    mood TINYINT,
    tags_json JSON,

    deleted TINYINT NOT NULL DEFAULT 0,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_date (user_id, diary_date),
    KEY idx_user_create (user_id, create_time)
);
```

---

# 44. note_folder

```sql
CREATE TABLE note_folder (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    parent_id BIGINT,
    name VARCHAR(100) NOT NULL,
    sort_no INT NOT NULL DEFAULT 0,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_parent (user_id, parent_id)
);
```

---

# 45. note

```sql
CREATE TABLE note (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    folder_id BIGINT,

    title VARCHAR(300) NOT NULL,

    content_markdown MEDIUMTEXT NOT NULL,

    excerpt VARCHAR(1000),

    word_count INT NOT NULL DEFAULT 0,

    deleted TINYINT NOT NULL DEFAULT 0,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_folder (user_id, folder_id),
    KEY idx_user_update (user_id, update_time)
);
```

---

# 46. note_tag

```sql
CREATE TABLE note_tag (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,

    create_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_user_name (user_id, name)
);
```

---

# 47. note_tag_relation

```sql
CREATE TABLE note_tag_relation (
    note_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

    PRIMARY KEY(note_id, tag_id),

    KEY idx_tag (tag_id)
);
```

---

# 48. idea

```sql
CREATE TABLE idea (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    content VARCHAR(1000) NOT NULL,

    status VARCHAR(30) NOT NULL,

    converted_type VARCHAR(30),
    converted_id BIGINT,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_create (user_id, create_time),
    KEY idx_user_status (user_id, status)
);
```

---

# 49. goal

```sql
CREATE TABLE goal (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    title VARCHAR(200) NOT NULL,
    description TEXT,

    start_date DATE,
    deadline DATE,

    progress DECIMAL(5,2) NOT NULL DEFAULT 0,

    priority TINYINT NOT NULL DEFAULT 3,
    status VARCHAR(30) NOT NULL,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_status (user_id, status),
    KEY idx_user_deadline (user_id, deadline)
);
```

---

# 50. milestone

```sql
CREATE TABLE milestone (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    goal_id BIGINT NOT NULL,

    title VARCHAR(200) NOT NULL,

    deadline DATE,

    progress DECIMAL(5,2) NOT NULL DEFAULT 0,

    status VARCHAR(30) NOT NULL,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_goal (goal_id),
    KEY idx_user_deadline (user_id, deadline)
);
```

---

# 51. todo

```sql
CREATE TABLE todo (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    goal_id BIGINT,
    milestone_id BIGINT,

    title VARCHAR(300) NOT NULL,
    description TEXT,

    priority TINYINT NOT NULL DEFAULT 3,

    planned_date DATE,
    deadline DATETIME(3),

    status VARCHAR(30) NOT NULL,

    completed_at DATETIME(3),

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_status (user_id, status),
    KEY idx_user_planned (user_id, planned_date),
    KEY idx_user_deadline (user_id, deadline),
    KEY idx_goal (goal_id),
    KEY idx_milestone (milestone_id)
);
```

---

# 52. schedule_event

```sql
CREATE TABLE schedule_event (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    title VARCHAR(300) NOT NULL,
    description TEXT,

    start_time DATETIME(3) NOT NULL,
    end_time DATETIME(3),

    location VARCHAR(300),

    event_type VARCHAR(30),

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_start (user_id, start_time)
);
```

---

# 53. water_goal

```sql
CREATE TABLE water_goal (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    target_ml INT NOT NULL,

    effective_date DATE NOT NULL,

    create_time DATETIME(3) NOT NULL,

    KEY idx_user_date (user_id, effective_date)
);
```

---

# 54. water_record

```sql
CREATE TABLE water_record (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    amount_ml INT NOT NULL,
    recorded_at DATETIME(3) NOT NULL,

    source VARCHAR(30),

    create_time DATETIME(3) NOT NULL,

    KEY idx_user_recorded (user_id, recorded_at)
);
```

---

# 55. sleep_record

```sql
CREATE TABLE sleep_record (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    sleep_at DATETIME(3) NOT NULL,
    wake_at DATETIME(3) NOT NULL,

    duration_minutes INT NOT NULL,

    quality TINYINT,

    note VARCHAR(1000),

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_sleep (user_id, sleep_at)
);
```

---

# 56. meal

```sql
CREATE TABLE meal (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    meal_type VARCHAR(30) NOT NULL,

    meal_time DATETIME(3) NOT NULL,

    note TEXT,

    image_url VARCHAR(500),

    estimated_calories DECIMAL(10,2),

    create_time DATETIME(3) NOT NULL,

    KEY idx_user_time (user_id, meal_time)
);
```

---

# 57. food_item

```sql
CREATE TABLE food_item (
    id BIGINT PRIMARY KEY,
    meal_id BIGINT NOT NULL,

    food_name VARCHAR(200) NOT NULL,

    amount VARCHAR(100),

    calories DECIMAL(10,2),

    protein DECIMAL(10,2),
    carbohydrate DECIMAL(10,2),
    fat DECIMAL(10,2),

    create_time DATETIME(3) NOT NULL,

    KEY idx_meal (meal_id)
);
```

---

# 58. workout_session

```sql
CREATE TABLE workout_session (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    title VARCHAR(200),

    start_time DATETIME(3) NOT NULL,
    end_time DATETIME(3),

    duration_minutes INT,

    note TEXT,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_start (user_id, start_time)
);
```

---

# 59. workout_exercise

```sql
CREATE TABLE workout_exercise (
    id BIGINT PRIMARY KEY,

    session_id BIGINT NOT NULL,

    exercise_name VARCHAR(200) NOT NULL,

    muscle_group VARCHAR(50),

    sort_no INT NOT NULL DEFAULT 0,

    KEY idx_session (session_id)
);
```

---

# 60. workout_set

```sql
CREATE TABLE workout_set (
    id BIGINT PRIMARY KEY,

    exercise_id BIGINT NOT NULL,

    set_no INT NOT NULL,

    weight DECIMAL(10,2),

    reps INT,

    duration_seconds INT,

    create_time DATETIME(3) NOT NULL,

    KEY idx_exercise (exercise_id)
);
```

---

# 61. life_event

```sql
CREATE TABLE life_event (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    event_type VARCHAR(50) NOT NULL,

    source_type VARCHAR(50) NOT NULL,
    source_id BIGINT NOT NULL,

    title VARCHAR(300),
    summary VARCHAR(2000),

    start_time DATETIME(3) NOT NULL,
    end_time DATETIME(3),

    metadata JSON,

    create_time DATETIME(3) NOT NULL,

    KEY idx_user_time (user_id, start_time),
    KEY idx_source (source_type, source_id),
    KEY idx_user_type_time (user_id, event_type, start_time)
);
```

---

# 62. note_publication

```sql
CREATE TABLE note_publication (
    id BIGINT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    note_id BIGINT NOT NULL,

    slug VARCHAR(200) NOT NULL,

    visibility VARCHAR(30) NOT NULL,

    current_version_id BIGINT,

    status VARCHAR(30) NOT NULL,

    published_at DATETIME(3),

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_slug (slug),
    UNIQUE KEY uk_note (note_id)
);
```

---

# 63. note_publish_version

```sql
CREATE TABLE note_publish_version (
    id BIGINT PRIMARY KEY,

    publication_id BIGINT NOT NULL,

    version_no INT NOT NULL,

    title VARCHAR(300) NOT NULL,

    content_markdown MEDIUMTEXT NOT NULL,

    content_html MEDIUMTEXT NOT NULL,

    published_at DATETIME(3) NOT NULL,

    UNIQUE KEY uk_pub_version (
        publication_id,
        version_no
    )
);
```

---

# 64. insight

```sql
CREATE TABLE insight (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,

    type VARCHAR(50) NOT NULL,

    severity VARCHAR(20) NOT NULL,

    title VARCHAR(300) NOT NULL,

    description TEXT NOT NULL,

    evidence_json JSON,

    status VARCHAR(30) NOT NULL,

    detected_at DATETIME(3) NOT NULL,

    expires_at DATETIME(3),

    create_time DATETIME(3) NOT NULL,

    KEY idx_user_status (
        user_id,
        status,
        detected_at
    )
);
```

---

# 65. weekly_summary

```sql
CREATE TABLE weekly_summary (
    id BIGINT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    week_start DATE NOT NULL,
    week_end DATE NOT NULL,

    statistics_json JSON NOT NULL,

    summary_markdown MEDIUMTEXT,

    suggestions_json JSON,

    generated_at DATETIME(3),

    create_time DATETIME(3) NOT NULL,

    UNIQUE KEY uk_user_week (
        user_id,
        week_start
    )
);
```

---

# 66. monthly_summary

结构与 weekly 基本一致：

```text
user_id
year
month
statistics_json
summary_markdown
suggestions_json
generated_at
```

唯一键：

```text
(user_id, year, month)
```

---

# 67. reminder_rule

```sql
CREATE TABLE reminder_rule (
    id BIGINT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    source_type VARCHAR(50),
    source_id BIGINT,

    trigger_type VARCHAR(30) NOT NULL,

    trigger_at DATETIME(3),

    cron_expression VARCHAR(100),

    enabled TINYINT NOT NULL DEFAULT 1,

    create_time DATETIME(3) NOT NULL,
    update_time DATETIME(3) NOT NULL,

    KEY idx_user_enabled (user_id, enabled),
    KEY idx_trigger_at (trigger_at)
);
```

---

# 68. reminder_task

代表真正需要执行的一次提醒。

```text
id
rule_id
user_id

scheduled_at

status

retry_count

sent_at

create_time
```

---

# 69. notification

```text
id
user_id

type
severity

title
content

related_type
related_id

read_status

create_time
```

---

# 70. agent_session

```text
id
user_id

title

status

create_time
update_time
```

---

# 71. agent_message

```text
id
session_id
user_id

role

content

create_time
```

role：

```text
SYSTEM
USER
ASSISTANT
TOOL
```

---

# 72. agent_run

```text
id
user_id
session_id

request_id

status

started_at
finished_at

error_code
error_message
```

---

# 73. agent_tool_call

```text
id

run_id

tool_name

arguments_json
result_json

status

latency_ms

create_time
```

---

# 74. domain_event_outbox

为了模块事件可靠处理：

```sql
CREATE TABLE domain_event_outbox (
    id BIGINT PRIMARY KEY,

    event_id VARCHAR(64) NOT NULL,

    event_type VARCHAR(100) NOT NULL,

    aggregate_type VARCHAR(100),

    aggregate_id BIGINT,

    payload_json JSON NOT NULL,

    status VARCHAR(30) NOT NULL,

    retry_count INT NOT NULL DEFAULT 0,

    next_retry_at DATETIME(3),

    occurred_at DATETIME(3) NOT NULL,

    published_at DATETIME(3),

    UNIQUE KEY uk_event_id (event_id),

    KEY idx_status_retry (
        status,
        next_retry_at
    )
);
```

---

# 75. 为什么需要 Outbox

例如：

```text
Todo 完成
```

必须同时产生：

```text
Todo DB Update

TodoCompletedEvent
```

如果：

```text
数据库提交成功
事件发送失败
```

Timeline 就会丢数据。

因此：

```text
同一个 MySQL Transaction

UPDATE todo
+
INSERT outbox
```

然后：

```text
Outbox Dispatcher
 ↓
Event Handler
```

---

# 76. API 统一格式

所有私人接口：

```text
/api/v1/**
```

公共文章：

```text
/public/v1/**
```

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "requestId": "..."
}
```

---

# 77. Auth API

```http
POST /api/v1/auth/wechat/login
POST /api/v1/auth/refresh
POST /api/v1/auth/logout
```

Login：

```json
{
  "code": "wechat-temp-code"
}
```

---

# 78. Diary API

```http
POST   /api/v1/diaries
GET    /api/v1/diaries/{id}
PUT    /api/v1/diaries/{id}
DELETE /api/v1/diaries/{id}

GET /api/v1/diaries?startDate=&endDate=

GET /api/v1/diaries/calendar?year=2026&month=8
```

---

# 79. Note API

```http
POST   /api/v1/notes
GET    /api/v1/notes/{id}
PUT    /api/v1/notes/{id}
DELETE /api/v1/notes/{id}

GET /api/v1/notes

POST /api/v1/note-folders
PUT  /api/v1/note-folders/{id}

POST /api/v1/note-tags
```

---

# 80. Note Publish API

发布：

```http
POST /api/v1/notes/{id}/publication
```

```json
{
  "visibility": "PUBLIC",
  "slug": "redis-cache-breakdown"
}
```

更新公开版本：

```http
POST /api/v1/notes/{id}/publication/version
```

撤回：

```http
DELETE /api/v1/notes/{id}/publication
```

公开：

```http
GET /public/v1/notes/{slug}
```

HTML 网页：

```text
/p/{slug}
```

---

# 81. Idea API

```http
POST /api/v1/ideas

GET /api/v1/ideas

PUT /api/v1/ideas/{id}

DELETE /api/v1/ideas/{id}

POST /api/v1/ideas/{id}/convert-to-note

POST /api/v1/ideas/{id}/convert-to-todo
```

---

# 82. Goal API

```http
POST /api/v1/goals

GET /api/v1/goals

GET /api/v1/goals/{id}

PUT /api/v1/goals/{id}

PATCH /api/v1/goals/{id}/progress

POST /api/v1/goals/{id}/milestones
```

---

# 83. Todo API

```http
POST /api/v1/todos

GET /api/v1/todos

GET /api/v1/todos/{id}

PUT /api/v1/todos/{id}

POST /api/v1/todos/{id}/complete

POST /api/v1/todos/{id}/cancel
```

Query：

```text
date
status
goalId
priority
```

---

# 84. Schedule API

```http
POST /api/v1/schedules

GET /api/v1/schedules

GET /api/v1/schedules/{id}

PUT /api/v1/schedules/{id}

DELETE /api/v1/schedules/{id}
```

---

# 85. Water API

```http
POST /api/v1/water/records

GET /api/v1/water/today

GET /api/v1/water/statistics

PUT /api/v1/water/goal
```

---

# 86. Sleep API

```http
POST /api/v1/sleep

GET /api/v1/sleep

GET /api/v1/sleep/statistics

PUT /api/v1/sleep/{id}
```

---

# 87. Diet API

```http
POST /api/v1/meals

GET /api/v1/meals

GET /api/v1/meals/{id}

PUT /api/v1/meals/{id}

DELETE /api/v1/meals/{id}
```

---

# 88. Workout API

```http
POST /api/v1/workouts

POST /api/v1/workouts/{id}/exercises

POST /api/v1/workout-exercises/{id}/sets

POST /api/v1/workouts/{id}/finish

GET /api/v1/workouts

GET /api/v1/workouts/{id}

GET /api/v1/workouts/statistics
```

---

# 89. Timeline API

```http
GET /api/v1/timeline
```

参数：

```text
date

startTime
endTime

eventType

cursor
limit
```

例如：

```http
GET /api/v1/timeline?date=2026-08-25
```

---

# 90. Search API

```http
POST /api/v1/search
```

Request：

```json
{
  "query": "以前什么时候想到人生管理系统",
  "types": [
    "IDEA",
    "NOTE",
    "DIARY"
  ]
}
```

---

# 91. Insight API

```http
GET /api/v1/insights

GET /api/v1/insights/{id}

POST /api/v1/insights/{id}/dismiss
```

---

# 92. Summary API

```http
GET /api/v1/summaries/weekly/current

GET /api/v1/summaries/weekly/{week}

GET /api/v1/summaries/monthly/current

GET /api/v1/summaries/monthly/{year}/{month}
```

---

# 93. Reminder API

```http
POST /api/v1/reminders

GET /api/v1/reminders

PUT /api/v1/reminders/{id}

DELETE /api/v1/reminders/{id}
```

---

# 94. Agent API

创建会话：

```http
POST /api/v1/agent/sessions
```

历史：

```http
GET /api/v1/agent/sessions/{id}/messages
```

聊天：

```http
POST /api/v1/agent/sessions/{id}/chat/stream
```

返回：

```text
text/event-stream
```

---

# 95. Agent 示例

用户：

```text
我上一次练腿是什么时候？
```

执行：

```text
Agent
 ↓
SearchWorkoutTool
 ↓
WorkoutQueryApi
 ↓
MySQL
```

---

用户：

```text
帮我记一下，LifeOS 要支持行为关联分析。
```

执行：

```text
CreateIdeaTool
 ↓
IdeaService
 ↓
idea
 ↓
IdeaCreatedEvent
 ↓
Timeline
```

---

# 96. Insight 规则

不要让 LLM 每天扫描全部数据。

应该：

```text
Rule Engine
+
Statistics
+
LLM Explanation
```

例如 Water：

```text
连续3天
daily_water < target * 0.8
```

产生：

```text
WATER_DEFICIT
```

---

# 97. Goal Deadline Risk

可以先使用简单模型：

```text
remainingProgress
÷
remainingDays
```

与：

```text
recentProgressVelocity
```

比较。

如果明显无法完成：

```text
GOAL_DEADLINE_RISK
```

---

# 98. Notification Policy

严重程度：

```text
INFO
MEDIUM
HIGH
```

规则：

```text
INFO
→ 只进入 Inbox

MEDIUM
→ 每日摘要

HIGH
→ 即时提醒
```

每种 Insight 设置：

```text
cooldown
```

避免骚扰。

---

# 99. Weekly Summary

流程：

```text
Structured Data
 ↓
Statistics
 ↓
Text Extraction
 ↓
LLM
 ↓
Weekly Summary
```

必须先计算：

```text
Todo 完成率
睡眠
饮水
健身
饮食
Goal
Diary
Note
Idea
```

再交给 AI。

---

# 100. 周报结构

```text
本周概览

Goal 进展

做得好的地方

主要问题

趋势

行为关联

下周建议

值得继续关注的 Idea
```

---

# 101. Monthly Summary

月报更强调：

```text
长期趋势

习惯变化

Goal Progress

身体状态

执行力变化

反复出现的问题
```

---

# 102. Markdown 安全

严禁：

```text
Markdown
↓
直接 HTML
↓
浏览器
```

必须：

```text
Markdown
 ↓
Parser
 ↓
HTML
 ↓
Sanitizer
 ↓
Safe HTML
```

过滤：

```text
script

iframe

javascript:

onclick

onerror
```

---

# 103. 图片上传

统一：

```http
POST /api/v1/assets
```

返回：

```json
{
  "url": "..."
}
```

Markdown：

```markdown
![](https://...)
```

---

# 104. Cache

非常适合缓存的内容：

```text
PUBLIC Note
```

Key：

```text
note:public:{slug}
```

读取：

```text
Redis
 ↓ MISS
MySQL
 ↓
Redis
```

发布新版本：

```text
DB Commit
 ↓
Delete Cache
```

---

# 105. Redis 其他用途

```text
agent:session:{id}:recent

auth:refresh:{userId}:{tokenId}

rate:agent:{userId}
```

Redis 不保存最终业务事实。

---

# 106. Security

所有私人查询必须带：

```text
user_id = currentUserId
```

例如禁止：

```sql
SELECT *
FROM note
WHERE id = ?
```

必须：

```sql
SELECT *
FROM note
WHERE id = ?
AND user_id = ?
```

防止：

```text
IDOR
```

---

# 107. Public Note Security

Public API 只能读取：

```text
PUBLIC

UNLISTED
```

PRIVATE：

```text
404
```

而不是：

```text
403
```

避免泄露资源存在。

---

# 108. RequestId

所有请求：

```text
X-Request-Id
```

Java：

```text
RequestIdFilter
 ↓
MDC
```

Agent：

```text
HTTP
 ↓
Java
 ↓
Agent Run
 ↓
Tool
 ↓
LLM
```

全链路保持同一个：

```text
traceId
```

---

# 109. lifeos-boot

完整启动模块。

```text
lifeos-boot/
└── src/main/java/com/edgar/lifeos/
    │
    ├── LifeOsApplication.java
    │
    ├── configuration/
    │   ├── MybatisConfiguration.java
    │   ├── RedisConfiguration.java
    │   ├── JacksonConfiguration.java
    │   ├── WebConfiguration.java
    │   ├── AsyncConfiguration.java
    │   └── SchedulingConfiguration.java
    │
    ├── filter/
    │   └── RequestIdFilter.java
    │
    └── exception/
        └── GlobalExceptionHandler.java
```

---

# 110. resources

```text
lifeos-boot/
└── src/main/resources/
    ├── application.yml
    ├── application-local.yml
    ├── application-prod.yml
    │
    ├── db/
    │   └── migration/
    │       ├── V001__identity.sql
    │       ├── V002__capture.sql
    │       ├── V003__planning.sql
    │       ├── V004__health.sql
    │       ├── V005__timeline.sql
    │       ├── V006__publication.sql
    │       ├── V007__insight.sql
    │       ├── V008__notification.sql
    │       ├── V009__agent.sql
    │       └── V010__outbox.sql
    │
    └── logback-spring.xml
```

建议：

```text
Flyway
```

管理数据库版本。

---

# 111. application.yml

基本结构：

```yaml
spring:
  datasource:
    url: ${MYSQL_URL}
    username: ${MYSQL_USER}
    password: ${MYSQL_PASSWORD}

  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}

lifeos:
  jwt:
    secret: ${JWT_SECRET}

  llm:
    base-url: ${LLM_BASE_URL}
    api-key: ${LLM_API_KEY}

  storage:
    type: local
```

Secret 禁止提交 Git。

---

# 112. Package 内部依赖

每个 Domain：

```text
Controller
 ↓
Application Service
 ↓
Domain
 ↓
Repository Interface
 ↑
RepositoryImpl
 ↓
Mapper
```

禁止：

```text
Controller → Mapper

Controller → Redis

Controller → LLM

Domain → Spring

Domain → MyBatis
```

---

# 113. Domain 不依赖 Spring

例如：

```java
public class Goal {

    private Long id;

    private BigDecimal progress;

    public void updateProgress(
        BigDecimal newProgress
    ) {
        ...
    }
}
```

不要：

```java
@Component
public class Goal
```

---

# 114. Service 事务边界

例如完成 Todo：

```java
@Transactional
public void completeTodo(...) {

    Todo todo =
        todoRepository.findOwned(...);

    todo.complete();

    todoRepository.save(todo);

    domainEventPublisher.publish(
        new TodoCompletedEvent(...)
    );
}
```

事务中：

```text
UPDATE todo
+
INSERT outbox
```

---

# 115. 测试结构

每个 Module：

```text
src/test/java/
```

测试分：

```text
domain/
application/
integration/
```

例如：

```text
TodoTest

TodoServiceTest

TodoRepositoryIntegrationTest

TodoControllerTest
```

---

# 116. Agent 测试

重点：

```text
Tool Routing

Tool Permission

Tool Timeout

Tool Failure

LLM Invalid Response

Read Tool

Mutating Tool

Idempotency

Session Isolation
```

---

# 117. Publication 测试

必须包含：

```text
Markdown Render

XSS

PRIVATE access

UNLISTED access

PUBLIC access

Version Snapshot

Slug conflict

Update after publish

Unpublish
```

---

# 118. Outbox 测试

主动模拟：

```text
业务提交成功

Timeline Handler 异常
```

验证：

```text
Outbox = FAILED

Retry

最终 LifeEvent 生成
```

---

# 119. 微信小程序页面

建议底部：

```text
今日
日历
记录
AI
我的
```

---

# 120. 今日页

展示：

```text
今日 Todo

Schedule

Water

Sleep

Workout

Goal Progress

Quick Capture
```

Quick Capture：

```text
Idea
Diary
Note
Water
Meal
Workout
```

---

# 121. 记录页

```text
Diary

Notes

Ideas

Health
```

---

# 122. AI 页

就是：

```text
Personal Agent
```

可以直接问：

```text
我最近睡眠怎么样？

我上次练腿是什么时候？

这个月最主要的问题是什么？

帮我记一个 Idea。

明天下午提醒我修改论文。
```

---

# 123. Public Web

另外建立：

```text
lifeos-public-web/
```

只负责：

```text
公开 Note 页面

Markdown CSS

TOC

Code Highlight

Dark Mode
```

不和小程序代码混在一起。

---

# 124. API 与模块映射

| API             | Module       |
|-----------------|--------------|
| `/auth/**`      | Identity     |
| `/diaries/**`   | Capture      |
| `/notes/**`     | Capture      |
| `/ideas/**`     | Capture      |
| `/goals/**`     | Planning     |
| `/todos/**`     | Planning     |
| `/schedules/**` | Planning     |
| `/water/**`     | Health       |
| `/sleep/**`     | Health       |
| `/meals/**`     | Health       |
| `/workouts/**`  | Health       |
| `/timeline/**`  | Timeline     |
| `/search/**`    | Search       |
| `/insights/**`  | Insight      |
| `/summaries/**` | Insight      |
| `/reminders/**` | Notification |
| `/agent/**`     | Agent        |
| `/public/**`    | Publication  |

---

# 125. 第一阶段不要做的东西

暂时不要：

```text
微服务

Spring Cloud

Kafka

Kubernetes

复杂社交

评论

点赞

关注

Feed 推荐

多人协作笔记

复杂知识图谱
```

这些都会稀释 LifeOS 的核心。

---

# 126. Phase 0 — 工程底座

实现：

```text
Maven 多模块

Result

Exception

RequestId

MySQL

MyBatis

Flyway

Security Skeleton
```

验收：

```text
所有 Module 编译

lifeos-boot 启动

数据库 Migration 成功

Health API 正常
```

---

# 127. Phase 1 — Identity

```text
WeChat Login

JWT

CurrentUser

User Setting
```

---

# 128. Phase 2 — Capture

```text
Diary

Note

Idea

Folder

Tag

Markdown Editor API
```

---

# 129. Phase 3 — Planning

```text
Goal

Milestone

Todo

Schedule
```

---

# 130. Phase 4 — Health

```text
Water

Sleep

Diet

Workout
```

---

# 131. Phase 5 — Timeline

所有核心操作开始产生：

```text
LifeEvent
```

---

# 132. Phase 6 — Publication

```text
Markdown Render

Sanitizer

Slug

Published Snapshot

Public Web
```

---

# 133. Phase 7 — Reminder

```text
Reminder Rule

Reminder Task

Notification Inbox

WeChat Notification
```

---

# 134. Phase 8 — Statistics / Insight

先不要 AI。

实现规则：

```text
Water

Sleep

Todo

Goal

Workout
```

---

# 135. Phase 9 — Weekly / Monthly AI Summary

增加：

```text
LlmGateway

WeeklySummary

MonthlySummary
```

---

# 136. Phase 10 — Search

先：

```text
Structured

Keyword
```

再：

```text
Semantic Search
```

---

# 137. Phase 11 — Agent

第一版只读：

```text
Search

Timeline

Goal

Health

Summary
```

先把：

> “Agent 能准确找到我的人生数据”

做稳。

---

# 138. Phase 12 — Agent Write Tools

增加：

```text
CreateIdea

CreateTodo

CreateSchedule

RecordWater
```

---

# 139. Phase 13 — Personal Watcher

```text
Insight

Notification Policy

Cooldown

主动提醒
```

---

# 140. Phase 14 — Advanced Agent

再考虑：

```text
Planning

Multi Tool

Semantic Memory

Long-Term Summary

行为关联分析
```

---

# 141. 整个系统最重要的五个边界

第一：

> **Note ≠ Diary ≠ Idea**

第二：

> **Goal ≠ Todo ≠ Schedule**

第三：

> **Reminder 是统一能力，不属于某个业务。**

第四：

> **Timeline 是统一索引，不是业务事实表。**

第五：

> **Agent 不能直接拥有业务数据，只能通过 Tool 使用业务能力。**

---

# 142. 最终核心数据链

```text
                Goal
                 │
                 ▼
           Milestone
                 │
                 ▼
               Todo
                 │
                 ▼

Diary ───────┐
Note ────────┤
Idea ────────┤
Schedule ────┤
Sleep ───────┤
Diet ────────┼──→ Domain Events
Workout ─────┤
Water ───────┤
Todo ────────┘
                 │
                 ▼
             LifeEvent
                 │
        ┌────────┴─────────┐
        ▼                  ▼
      Search             Statistics
        │                  │
        ▼                  ▼
 Semantic Memory         Insight
        │                  │
        └────────┬─────────┘
                 ▼
            Personal Agent
                 │
       ┌─────────┼──────────┐
       ▼         ▼          ▼
      Ask       Analyze     Act
```

---

# 143. LifeOS 的最终定位

LifeOS 最终不应该被描述为：

> 一个包含日记、笔记、Todo 和健康记录的微信小程序。

更准确的定义是：

> **LifeOS 是一个以个人长期生活数据为核心的 AI Personal Operating System。系统通过 Diary、Note、Idea、Goal、Todo、Schedule
和健康记录持续构建个人数字记忆，利用 Timeline 与 Semantic Search 实现人生信息检索，并结合周期统计、Insight Engine 和
Personal Agent 完成总结、提醒、趋势分析与行动执行。**

---

# 144. 最核心的工程原则

整个项目开发过程中始终遵守：

```text
业务事实归 Domain

查询能力归 Application API

跨模块通信归 Event / Port

AI 推理归 Agent

外部系统归 Adapter

统一时间索引归 Timeline

统一主动判断归 Insight

统一通知归 Notification
```

绝不形成：

```text
一个 5000 行 LifeService

一个万能 UserService

一个直接查询所有表的 AgentService
```

LifeOS 的代码组织最终必须让开发者看到一个需求时能够非常明确地判断：

> **这个代码应该属于哪个业务域，以及其他业务域应该通过什么接口与它通信。**