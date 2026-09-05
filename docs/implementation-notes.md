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