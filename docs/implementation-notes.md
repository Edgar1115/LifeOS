# LifeOS 实现细节记录

> 本文件用于记录实现项目时的细节处理（决策、坑、约定等）。
> 仅在用户明确要求记录时才追加内容。

## 2026-09-04 Lombok @Getter 重构

**背景**：骨架重构（29d72ca）后，37 个领域/公共类里都是手写 getter，样板代码量大且重复。

**做法**：
- 根 `pom.xml` 统一引入 `org.projectlombok:lombok`（1.18.30），`scope=provided`，只编译期生效、不打入产物；各模块无需单独声明。
- 用 `@Getter` 替换约 597 行手写 getter（+86/-597），涉及 `lifeos-common`、`lifeos-domain`、`lifeos-agent` 三个模块共 37 个类。
- 保留手动 setter 和带业务语义的修改方法（如 `updateProfile`）不动，只替换纯读取的 getter。

**踩坑**：
- 个别文件（`UserStatus.java`、`TodoPriority.java`）加了 `@Getter` 却漏写 `import lombok.Getter;`，编译直接报「找不到符号」。
- 排查方法是 `grep -rl "@Getter"` 找到所有使用点，逐个比对是否都有 import；首次编译失败正是被这两个文件暴露的。

**验证**：`mvn compile` 通过。

**提交**：`2a77cb2 refactor: 领域模型用 Lombok @Getter 替代手写 getter`（已推送 main）。

**示例代码（代表性片段）**：

根 pom 引入 Lombok（所有模块通用，只编译期生效）：
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
    <scope>provided</scope>
</dependency>
```

类上注解替换手写 getter（`User.java`）：
```java
@Getter
public class User extends AggregateRoot {
    private final String openid;
    private final String unionid;
    private String nickname;
    private String avatarUrl;
    private UserStatus status;
    // ... 保留 setter 与业务修改方法，只替换纯读取 getter
}
```

带业务语义的方法不动（`AggregateRoot.java` 只删了 getter，保留 setId/setCreatedAt/setUpdatedAt）：
```java
@Getter
public abstract class AggregateRoot implements Serializable {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    // 手写 getter 全删，setter 保留
}
```

## 2026-09-05 集合字段对外只读（不可变视图）

**背景**：审查领域模型时发现 `@Getter` 会为 `List` 字段直接生成返回内部引用的 getter，外部可 `getXxx().add/remove/clear` 直接篡改聚合内部状态，破坏封装。

**排查**：`grep -rn "private.*\(List\|Set\|Map\|Collection\|Queue\|Deque\)"` 定位到两个受影响领域类：
- `Diary.java` — `List<String> tags`（外部可 `getTags().add(...)`）
- `Meal.java` — `List<FoodItem> items`（外部可 `getItems().add(...)`）
- `LlmRequest`（agent 的 record DTO）和 `JsonUtils`（static final）经确认不属于此问题。

**做法（方案一：不可变视图，零拷贝）**：
1. 字段上加 `@Getter(AccessLevel.NONE)` 关闭 Lombok 自动生成 getter。
2. 手写 getter 返回 `Collections.unmodifiableList(...)`。

```java
@Getter(AccessLevel.NONE)
private List<String> tags = new ArrayList<>();

public List<String> getTags() {
    return Collections.unmodifiableList(tags);
}
```
- 读操作（遍历/contains/size）不受影响；外部写操作抛 `UnsupportedOperationException`。
- 修改统一走业务方法（`Diary.update()` / `Meal.addItem()`），内部 `new ArrayList<>(...)` 防御性拷贝保留。

**备选方案（未采用）**：每次 get 返回 `new ArrayList<>(tags)` 拷贝——零共享但每 access 有拷贝开销，不如不可变视图干净。

**踩坑**：
- `Meal.java` 原文件结尾残留多余空行和冗余 `}`，用 Write 整体重写解决（逐段 Edit 的空行数匹配不稳）。
- `Diary.java` 曾误写作 `extends Aggregate`（应为 `AggregateRoot`），读后已修正。

**验证**：`mvn compile -pl lifeos-domain,lifeos-common -am` 通过。

**提交**：未提交（待用户确认）。

## 2026-09-05 Flyway 数据库版本迁移接入

**背景**：Project 从骨架阶段进入 Phase 1（Identity/User），需要数据库结构版本管理。之前 application.yml 里就预留了 `# Flyway 后续接入时打开` 的注释。

**做法**：
- `lifeos-server/pom.xml` 引入 `spring-boot-starter-flyway` + `flyway-mysql`（版本由 Spring Boot 4.0.6 BOM 管理，Flyway 11.14.1）。
- `application.yml` 启用 `spring.flyway`（enabled + validate-on-migrate）。
- 新增 `db/migration/V1__init_schema.sql`：建 user 表（对齐 `User` 聚合 + `AggregateRoot` 基类）。

**踩坑（关键）**：
- **Spring Boot 4 模块化是最大的坑**。Spring Boot 4 把庞大的 `spring-boot-autoconfigure` 拆成按技术隔离的小模块，只加裸 `flyway-core` **不会自动配置**，必须显式引入 `spring-boot-starter-flyway`（内含 `spring-boot-flyway` 自动配置 jar）。症状：jar 里有 flyway-core 但启动时没有任何 Flyway 日志，数据库空表。
- **存量的 JDBC URL bug**：骨架里 `characterEncoding=utf8mb4` 不是合法 Java charset 名，Connector/J 报 `Unsupported character encoding 'utf8mb4'`。正确值是 `UTF-8`。这是项目首次真正连数据库才暴露的埋坑。
- **本机 3306 被系统级 mysqld 占用**（`/usr/local/mysql` 装在跑），所以本地开发用独立 Docker 容器 `lifeos-mysql` 映射宿主 3307，`application-local.yml` 指向 3307 隔离。

**验证**：Docker 起 MySQL 8.4；应用 local profile 启动，Flyway 日志 `Successfully applied 1 migration ... now at version v1`；`flyway_schema_history` 记录 version=1 success=1，user 表 8 字段全部创建，与领域模型对齐。

**提交**：`b9a5e9b feat: 接入 Flyway 数据库版本迁移`（已推送 main）。

## 2026-09-05 user 表名 → life_user（V2 迁移）

**背景**：`user` 是 SQL 保留字/常见词，业务表统一 `life_` 前缀更清晰，避免关键字冲突与歧义。

**做法**：新增 `V2__rename_user_to_life_user.sql`：`ALTER TABLE user RENAME TO life_user;`

**关键决策**：
- **用新 V2 迁移重命名，而不是改已提交的 V1**。因为 V1 已在库中执行并记录到 `flyway_schema_history`，且启用了 `validate-on-migrate`，改 V1 会触发 checksum 校验失败。Flyway 的不可变式规范——已执行脚本永不改动，只追加新版本。
- 已验证：V1→V1 改表名是"先删表名"的破坏性操作已避免，本方案安全。
- 实测：应用启动，Flyway 依次执行 V2（和后续 V3），`life_user` 表建立成功，历史记录 v1/v2 均 success。

**验证**：`SHOW TABLES` → `life_user`；`flyway_schema_history` version=2 success=1。

## 2026-09-05 unionid 唯一索引（V3 迁移）

**背景**：审查时发现 `unionid` 在 V1 建的是普通索引 `idx_user_unionid`。微信 unionid 是同一开发者账号下跨应用（公众号/小程序/开放平台）的统一身份标识，**一个自然人只能对应一个 LifeOS 用户**，所以必须唯一。

**做法**：新增 `V3__make_unionid_unique.sql`：
```sql
ALTER TABLE `life_user`
    DROP INDEX `idx_user_unionid`,
    ADD UNIQUE KEY `uk_user_unionid` (`unionid`);
```

**关键点**：
- MySQL 唯一索引**允许多个 NULL 并存**——未绑定微信（unionid 为 NULL）的行互不冲突，可空字段不受唯一约束影响。
- 仍走新迁移而非改 V1（checksum 校验，同前）。

**验证**：`SHOW INDEX` 显示 `uk_user_unionid`（Non_unique=0）；历史 v1/v2/v3 均 success；unionid 重复插入会被 MySQL 拒绝。

## 2026-09-05 UserStatus 枚举 DB 表示 vs MyBatis 默认 EnumHandler 不一致

**背景**：发现数据库、领域模型、MyBatis 三方对 `UserStatus` 的存储表示不一致——这是接入 Flyway 做真实建表后才暴露的：
- **数据库**（V1）：`status TINYINT`，存整数 `1`（ACTIVE）/`0`（DISABLED）
- **领域模型**：`UserStatus.dbValue` + `fromDbValue()` 明确按整数存储的意图
- **MyBatis 默认**：`default-enum-type-handler: EnumTypeHandler` 按**枚举名字**存取字符串（如 `'ACTIVE'`）

Handler 按名字对着整数列读写时必然出错。**TodoPriority 同理**（value 1/3/5，`fromValue()`），只是还没建表，一并记着。

**做法（方案 A：自定义 TypeHandler）**：
1. `lifeos-infrastructure/.../typehandler/UserStatusTypeHandler.java`：
   - `@MappedTypes(UserStatus.class)` 精确匹配
   - `setNonNullParameter` 写 `dbValue`（1/0）
   - `getNullableResult` 调 `UserStatus.fromDbValue()` 还原，并处理 NULL（`wasNull()`）
2. `application.yml`：
   - **移除** `default-enum-type-handler`（否则全局按名字存，与自定义精确匹配冲突）
   - 加 `type-handlers-package: com.edgar.lifeos.infrastructure.persistence.mybatis.typehandler` 自动注册

```java
@MappedTypes(UserStatus.class)
public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserStatus p, JdbcType jt) throws SQLException {
        ps.setInt(i, p.getDbValue());
    }
    @Override
    public UserStatus getNullableResult(ResultSet rs, String col) throws SQLException {
        int v = rs.getInt(col);
        return v == 0 && rs.wasNull() ? null : UserStatus.fromDbValue(v);
    }
    // getNullableResult(int) / getNullableResult(CallableStatement) 同理
}
```

**备选方案（未采用）**：
- EnumOrdinalTypeHandler 按 `ordinal()` 存——ACTIVE ordinal=0 而 dbValue=1，值冲突，不可用。
- 改表结构存枚举名——破坏性大、与 dbValue 语义相悖。

**验证**：`mvn package` 编译通过；应用 local profile 启动成功（TypeHandler 经 `type-handlers-package` 扫描注册无报错）。

> 注：TodoPriority 与此同模式（value 1/3/5 vs ordinal 0/1/2），后续持久化时需同样处理（创建 `TodoPriorityTypeHandler`）。

**提交**：本批（V2/V3/TypeHandler/application.yml）待一起提交。