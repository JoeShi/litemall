<template>
  <div class="app-container">
    <!-- 现有的折线图组件 -->
    <ve-line :extend="chartExtend" :data="chartData" :settings="chartSettings" />

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
        style="width: 100%"
      >
        <el-table-column
          label="排名"
          width="80"
          align="center"
        >
          <template slot-scope="scope">
            <span :class="{'top-rank': scope.$index < 3}">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="goods_id"
          label="商品ID"
          width="100"
          align="center"
        />
        <el-table-column
          prop="goods_name"
          label="商品名称"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="sales_count"
          label="销售数量"
          width="120"
          align="center"
          sortable
        />
        <el-table-column
          label="操作"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <router-link :to="'/goods/detail?id=' + scope.row.goods_id">
              <el-button type="text" size="small">查看详情</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="hotGoods7Days.length === 0 && !loading7Days" class="no-data">
        暂无数据
      </div>
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
        style="width: 100%"
      >
        <el-table-column
          label="排名"
          width="80"
          align="center"
        >
          <template slot-scope="scope">
            <span :class="{'top-rank': scope.$index < 3}">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="goods_id"
          label="商品ID"
          width="100"
          align="center"
        />
        <el-table-column
          prop="goods_name"
          label="商品名称"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="sales_count"
          label="销售数量"
          width="120"
          align="center"
          sortable
        />
        <el-table-column
          label="操作"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <router-link :to="'/goods/detail?id=' + scope.row.goods_id">
              <el-button type="text" size="small">查看详情</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="hotGoods30Days.length === 0 && !loading30Days" class="no-data">
        暂无数据
      </div>
    </div>
  </div>
</template>

<script>
import { statGoods, statHotGoods7Days, statHotGoods30Days } from '@/api/stat'
import VeLine from 'v-charts/lib/line'
import { Message } from 'element-ui'

export default {
  name: 'StatGoods',
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
    this.fetchStatGoods()

    // 加载热门商品数据
    this.fetchHotGoods7Days()
    this.fetchHotGoods30Days()
  },
  methods: {
    // 获取商品统计数据
    fetchStatGoods() {
      statGoods().then(response => {
        if (response.data.errno === 0) {
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
        } else {
          Message.error('获取商品统计数据失败')
        }
      }).catch(() => {
        Message.error('获取商品统计数据失败')
      })
    },
    // 获取7天热门商品数据
    fetchHotGoods7Days() {
      this.loading7Days = true
      statHotGoods7Days().then(response => {
        if (response.data.errno === 0) {
          this.hotGoods7Days = response.data.data.rows || []
        } else {
          Message.error('获取7天热门商品数据失败')
        }
        this.loading7Days = false
      }).catch(error => {
        console.error('获取7天热门商品数据失败:', error)
        Message.error('获取7天热门商品数据失败')
        this.loading7Days = false
      })
    },
    // 获取30天热门商品数据
    fetchHotGoods30Days() {
      this.loading30Days = true
      statHotGoods30Days().then(response => {
        if (response.data.errno === 0) {
          this.hotGoods30Days = response.data.data.rows || []
        } else {
          Message.error('获取30天热门商品数据失败')
        }
        this.loading30Days = false
      }).catch(error => {
        console.error('获取30天热门商品数据失败:', error)
        Message.error('获取30天热门商品数据失败')
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
  color: #303133;
}
.top-rank {
  color: #f56c6c;
  font-weight: bold;
}
.no-data {
  text-align: center;
  color: #909399;
  padding: 20px 0;
  font-size: 14px;
}

@media screen and (max-width: 768px) {
  .hot-goods-container {
    padding: 10px;
  }
  .el-table {
    width: 100%;
    overflow-x: auto;
  }
}
</style>
