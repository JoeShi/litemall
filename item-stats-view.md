# 商品统计页面实现分析

## 概述

管理员后台界面中【统计报表】栏目下的【商品统计】页面用于展示商城中商品的订单量、下单货品数量和下单货品总额等统计数据，以折线图的形式直观呈现。

## 前端实现

### 路由配置

在 `litemall-admin/src/router/index.js` 中，【统计报表】栏目的路由配置如下：

```javascript
{
  path: '/stat',
  component: Layout,
  redirect: 'noredirect',
  alwaysShow: true,
  name: 'statManage',
  meta: {
    title: 'app.menu.stat',
    icon: 'chart'
  },
  children: [
    // ...其他统计页面
    {
      path: 'goods',
      component: () => import('@/views/stat/goods'),
      name: 'statGoods',
      meta: {
        perms: ['GET /admin/stat/goods'],
        title: 'app.menu.stat_goods',
        noCache: true
      }
    }
  ]
}
```

### 页面组件

商品统计页面的前端实现位于 `litemall-admin/src/views/stat/goods.vue`，主要使用了 v-charts 库来绘制折线图：

```vue
<template>
  <div class="app-container">
    <ve-line :extend="chartExtend" :data="chartData" :settings="chartSettings"/>
  </div>
</template>

<script>
import { statGoods } from '@/api/stat'
import VeLine from 'v-charts/lib/line'

export default {
  components: { VeLine },
  data() {
    return {
      chartData: {},
      chartSettings: {},
      chartExtend: {}
    }
  },
  created() {
    statGoods().then(response => {
      this.chartData = response.data.data
      this.chartSettings = {
        labelMap: {
          'orders': '订单量',
          'products': '下单货品数量',
          'amount': '下单货品总额'
        }
      }
      this.chartExtend = {
        xAxis: { boundaryGap: true }
      }
    })
  }
}
</script>
```

### API 调用

前端通过 `statGoods` 函数调用后端 API 获取数据，该函数定义在 `litemall-admin/src/api/stat.js` 中：

```javascript
export function statGoods(query) {
  return request({
    url: '/stat/goods',
    method: 'get',
    params: query
  })
}
```

## 后端实现

### 控制器

后端控制器位于 `litemall-admin-api/src/main/java/org/linlinjava/litemall/admin/web/AdminStatController.java`，处理商品统计的请求：

```java
@RequiresPermissions("admin:stat:goods")
@RequiresPermissionsDesc(menu = {"统计管理", "商品统计"}, button = "查询")
@GetMapping("/goods")
public Object statGoods() {
    List<Map> rows = statService.statGoods();
    String[] columns = new String[]{"day", "orders", "products", "amount"};
    StatVo statVo = new StatVo();
    statVo.setColumns(columns);
    statVo.setRows(rows);
    return ResponseUtil.ok(statVo);
}
```

### 服务层

服务层实现位于 `litemall-db/src/main/java/org/linlinjava/litemall/db/service/StatService.java`：

```java
@Service
public class StatService {
    @Resource
    private StatMapper statMapper;

    // ...其他方法

    public List<Map> statGoods() {
        return statMapper.statGoods();
    }
}
```

### 数据访问层

数据访问接口定义在 `litemall-db/src/main/java/org/linlinjava/litemall/db/dao/StatMapper.java`：

```java
public interface StatMapper {
    // ...其他方法
    List<Map> statGoods();
}
```

### SQL 查询

具体的 SQL 查询实现在 `litemall-db/src/main/resources/org/linlinjava/litemall/db/dao/StatMapper.xml` 中：

```xml
<select id="statGoods" resultType="map">
    select
    substr(add_time,1, 10) as day,
    count(distinct order_id) as orders,
    sum(number) as products,
    sum(number*price) as amount
    from litemall_order_goods
    group by substr(add_time,1, 10)
</select>
```

## 数据流程

1. 用户访问管理后台的【统计报表】-【商品统计】页面
2. 前端组件 `goods.vue` 在创建时调用 `statGoods()` API
3. 请求发送到后端 `/admin/stat/goods` 接口
4. `AdminStatController` 的 `statGoods()` 方法处理请求
5. 调用 `StatService` 的 `statGoods()` 方法获取数据
6. `StatService` 通过 `StatMapper` 执行 SQL 查询
7. SQL 查询从 `litemall_order_goods` 表中统计每天的订单数、商品数量和总金额
8. 数据返回给前端，前端使用 v-charts 将数据渲染为折线图

## 统计指标说明

商品统计页面展示了以下三个关键指标：

1. **订单量**：每天的订单数量（按订单ID去重）
2. **下单货品数量**：每天售出的商品总数量
3. **下单货品总额**：每天售出商品的总金额（数量×单价）

这些数据按天进行分组，以折线图的形式展示，帮助管理员了解商品销售的趋势变化。