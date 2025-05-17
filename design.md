# 热门商品统计API设计文档

## 1. 需求概述

为管理后台添加查看当前热门商品的功能，基于销售量统计最近7天和最近30天的热门商品。只需实现API，不涉及前端界面的修改。

## 2. 功能设计

### 2.1 功能描述

- 提供两个API端点，分别查询最近7天和最近30天的热门商品
- 每个API返回销售量前10的商品
- 返回的商品信息包括：商品ID、商品名称和销售数量
- 只有管理员可以访问这些API

### 2.2 API设计

#### 2.2.1 最近7天热门商品API

- **URL**: `/admin/stat/hot-goods/7days`
- **方法**: GET
- **权限**: 需要管理员权限
- **功能**: 统计最近7天销售量前10的商品
- **返回数据**: 包含商品ID、商品名称和销售数量的统计数据

#### 2.2.2 最近30天热门商品API

- **URL**: `/admin/stat/hot-goods/30days`
- **方法**: GET
- **权限**: 需要管理员权限
- **功能**: 统计最近30天销售量前10的商品
- **返回数据**: 包含商品ID、商品名称和销售数量的统计数据

### 2.3 响应格式

使用与现有统计API相同的响应格式（StatVo），结构如下：

```json
{
  "errno": 0,
  "data": {
    "columns": ["goods_id", "goods_name", "sales_count"],
    "rows": [
      {
        "goods_id": 1,
        "goods_name": "商品1",
        "sales_count": 100
      },
      {
        "goods_id": 2,
        "goods_name": "商品2",
        "sales_count": 90
      },
      ...
    ]
  },
  "errmsg": "成功"
}
```

## 3. 技术设计

### 3.1 控制器设计

创建一个新的控制器类 `AdminHotGoodsController`，负责处理热门商品的API请求。

```java
@RestController
@RequestMapping("/admin/stat/hot-goods")
@Validated
public class AdminHotGoodsController {
    // 实现代码
}
```

### 3.2 服务层设计

在 `StatService` 中添加两个新方法，用于获取热门商品数据：

```java
public List<Map> statHotGoods7Days() {
    // 实现代码
}

public List<Map> statHotGoods30Days() {
    // 实现代码
}
```

### 3.3 数据访问层设计

在 `StatMapper` 接口中添加两个新方法：

```java
List<Map> statHotGoods7Days();
List<Map> statHotGoods30Days();
```

在 `StatMapper.xml` 中添加对应的SQL查询：

```xml
<select id="statHotGoods7Days" resultType="map">
    <!-- SQL查询 -->
</select>

<select id="statHotGoods30Days" resultType="map">
    <!-- SQL查询 -->
</select>
```

### 3.4 SQL查询设计

SQL查询将从 `litemall_order_goods` 表中统计商品销售量，并关联 `litemall_goods` 表获取商品名称。查询条件包括时间范围（最近7天或30天），并按销售量降序排序，限制返回前10条记录。

#### 最近7天热门商品SQL：

```sql
SELECT 
    g.id AS goods_id,
    g.name AS goods_name,
    SUM(og.number) AS sales_count
FROM 
    litemall_order_goods og
JOIN 
    litemall_goods g ON og.goods_id = g.id
WHERE 
    og.add_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
    AND og.deleted = 0
    AND g.deleted = 0
GROUP BY 
    og.goods_id
ORDER BY 
    sales_count DESC
LIMIT 10
```

#### 最近30天热门商品SQL：

```sql
SELECT 
    g.id AS goods_id,
    g.name AS goods_name,
    SUM(og.number) AS sales_count
FROM 
    litemall_order_goods og
JOIN 
    litemall_goods g ON og.goods_id = g.id
WHERE 
    og.add_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
    AND og.deleted = 0
    AND g.deleted = 0
GROUP BY 
    og.goods_id
ORDER BY 
    sales_count DESC
LIMIT 10
```

### 3.5 权限控制设计

使用Shiro框架进行权限控制，为API添加权限注解：

```java
@RequiresPermissions("admin:stat:hotgoods")
@RequiresPermissionsDesc(menu = {"统计管理", "热门商品"}, button = "查询")
```

## 4. 实现步骤

1. 在 `StatMapper.java` 中添加新方法
2. 在 `StatMapper.xml` 中添加对应的SQL查询
3. 在 `StatService.java` 中添加新方法
4. 创建 `AdminHotGoodsController.java` 控制器
5. 实现API端点
6. 添加权限控制

## 5. 注意事项

- 确保SQL查询性能良好，可能需要为相关表添加索引
- 考虑缓存热门商品数据，避免频繁查询数据库
- 确保正确处理时区问题，使用系统配置的时区
- 考虑数据量大时的性能问题，可能需要优化SQL查询