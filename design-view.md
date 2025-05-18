# 商品统计页面热门商品功能设计文档

## 1. 需求概述

在管理员后台【统计报表】栏目中的【商品统计】页面增加【近期热门商品】展示功能，以表格形式展示最近7天和最近30天的热门商品销售数据。后端API已经实现完毕，现在需要在前端页面中集成这些数据。

## 2. 功能设计

### 2.1 功能描述

- 在现有的商品统计折线图下方，添加两个表格，分别展示最近7天和最近30天的热门商品
- 每个表格显示销售量前10的商品
- 表格中展示商品ID、商品名称、销售数量、排名和商品链接
- 为每个表格提供刷新按钮，用于手动刷新数据
- 页面加载时自动请求数据

### 2.2 页面布局设计

```
+----------------------------------+
|                                  |
|        现有商品统计折线图         |
|                                  |
+----------------------------------+
|                                  |
|        最近7天热门商品            |
|  [刷新]                          |
|  +----------------------------+  |
|  | 排名 | ID | 商品名称 | ... |  |
|  +----------------------------+  |
|  | 1    | 1  | 商品1    | ... |  |
|  | 2    | 2  | 商品2    | ... |  |
|  | ...  | ...| ...     | ... |  |
|  +----------------------------+  |
|                                  |
+----------------------------------+
|                                  |
|        最近30天热门商品           |
|  [刷新]                          |
|  +----------------------------+  |
|  | 排名 | ID | 商品名称 | ... |  |
|  +----------------------------+  |
|  | 1    | 1  | 商品1    | ... |  |
|  | 2    | 2  | 商品2    | ... |  |
|  | ...  | ...| ...     | ... |  |
|  +----------------------------+  |
|                                  |
+----------------------------------+
```

### 2.3 表格设计

每个表格包含以下列：
1. **排名**：根据销售数量的排名（前端计算）
2. **商品ID**：商品的唯一标识符
3. **商品名称**：商品的名称
4. **销售数量**：在统计周期内的销售数量
5. **商品链接**：链接到商品详情页面

## 3. 技术设计

### 3.1 前端API调用设计

在 `litemall-admin/src/api/stat.js` 中添加两个新函数，用于调用热门商品API：

```javascript
export function statHotGoods7Days() {
  return request({
    url: '/stat/hot-goods/7days',
    method: 'get'
  })
}

export function statHotGoods30Days() {
  return request({
    url: '/stat/hot-goods/30days',
    method: 'get'
  })
}
```

### 3.2 前端组件设计

修改 `litemall-admin/src/views/stat/goods.vue` 文件，添加热门商品表格组件：

```vue
<template>
  <div class="app-container">
    <!-- 现有的折线图组件 -->
    <ve-line :extend="chartExtend" :data="chartData" :settings="chartSettings"/>
    
    <!-- 最近7天热门商品 -->
    <div class="hot-goods-container">
      <div class="hot-goods-header">
        <h3>最近7天热门商品</h3>
        <el-button size="mini" type="primary" @click="fetchHotGoods7Days">刷新</el-button>
      </div>
      <el-table
        v-loading="loading7Days"
        :data="hotGoods7Days"
        border
        style="width: 100%">
        <el-table-column
          label="排名"
          width="80"
          align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="goods_id"
          label="商品ID"
          width="100"
          align="center">
        </el-table-column>
        <el-table-column
          prop="goods_name"
          label="商品名称"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="sales_count"
          label="销售数量"
          width="120"
          align="center">
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          align="center">
          <template slot-scope="scope">
            <router-link :to="'/goods/detail?id=' + scope.row.goods_id">
              <el-button type="text" size="small">查看详情</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 最近30天热门商品 -->
    <div class="hot-goods-container">
      <div class="hot-goods-header">
        <h3>最近30天热门商品</h3>
        <el-button size="mini" type="primary" @click="fetchHotGoods30Days">刷新</el-button>
      </div>
      <el-table
        v-loading="loading30Days"
        :data="hotGoods30Days"
        border
        style="width: 100%">
        <el-table-column
          label="排名"
          width="80"
          align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="goods_id"
          label="商品ID"
          width="100"
          align="center">
        </el-table-column>
        <el-table-column
          prop="goods_name"
          label="商品名称"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="sales_count"
          label="销售数量"
          width="120"
          align="center">
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          align="center">
          <template slot-scope="scope">
            <router-link :to="'/goods/detail?id=' + scope.row.goods_id">
              <el-button type="text" size="small">查看详情</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { statGoods, statHotGoods7Days, statHotGoods30Days } from '@/api/stat'
import VeLine from 'v-charts/lib/line'

export default {
  components: { VeLine },
  data() {
    return {
      chartData: {},
      chartSettings: {},
      chartExtend: {},
      hotGoods7Days: [],
      hotGoods30Days: [],
      loading7Days: false,
      loading30Days: false
    }
  },
  created() {
    // 加载原有的商品统计数据
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
    
    // 加载热门商品数据
    this.fetchHotGoods7Days()
    this.fetchHotGoods30Days()
  },
  methods: {
    fetchHotGoods7Days() {
      this.loading7Days = true
      statHotGoods7Days().then(response => {
        this.hotGoods7Days = response.data.data.rows
        this.loading7Days = false
      }).catch(() => {
        this.loading7Days = false
      })
    },
    fetchHotGoods30Days() {
      this.loading30Days = true
      statHotGoods30Days().then(response => {
        this.hotGoods30Days = response.data.data.rows
        this.loading30Days = false
      }).catch(() => {
        this.loading30Days = false
      })
    }
  }
}
</script>

<style scoped>
.hot-goods-container {
  margin-top: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, .1);
}
.hot-goods-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}
.hot-goods-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}
</style>
```

## 4. 数据流程

1. 用户访问管理后台的【统计报表】-【商品统计】页面
2. 前端组件 `goods.vue` 在创建时调用三个API：
   - `statGoods()` - 获取原有的商品统计数据
   - `statHotGoods7Days()` - 获取最近7天热门商品数据
   - `statHotGoods30Days()` - 获取最近30天热门商品数据
3. 请求分别发送到后端对应的接口
4. 数据返回后，前端将原有数据渲染为折线图，将热门商品数据渲染为表格
5. 用户可以通过点击刷新按钮手动更新热门商品数据

## 5. 权限控制

使用与现有商品统计页面相同的权限控制，确保只有具有 `admin:stat:goods` 权限的管理员可以访问此页面。

## 6. 注意事项

1. **性能考虑**：
   - 页面加载时会同时发起三个API请求，可能会影响页面加载速度
   - 考虑添加加载状态指示器，提升用户体验

2. **数据展示**：
   - 确保表格中的数据按销售量降序排列
   - 考虑为销售量较高的商品添加视觉强调（如不同颜色）

3. **响应式设计**：
   - 确保在不同屏幕尺寸下表格能够正常显示
   - 在小屏幕设备上可能需要横向滚动

4. **错误处理**：
   - 添加适当的错误处理机制，当API请求失败时显示友好的错误提示
   - 提供重试机制，允许用户手动刷新数据

5. **数据一致性**：
   - 确保表格中的排名与后端返回的数据顺序一致
   - 如果后端数据未按销售量排序，需要在前端进行排序处理