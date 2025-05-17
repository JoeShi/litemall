# LiteMall 商品订单表结构与统计报表API

## 商品与订单相关的数据库表结构

### 商品相关表

1. **litemall_goods** - 商品基本信息表
   - `id`: 商品ID
   - `goods_sn`: 商品编号
   - `name`: 商品名称
   - `category_id`: 商品所属类目ID
   - `brand_id`: 品牌ID
   - `gallery`: 商品宣传图片列表，JSON数组格式
   - `keywords`: 商品关键字，逗号间隔
   - `brief`: 商品简介
   - `is_on_sale`: 是否上架
   - `sort_order`: 排序
   - `pic_url`: 商品页面商品图片
   - `share_url`: 商品分享海报
   - `is_new`: 是否新品首发
   - `is_hot`: 是否人气推荐
   - `unit`: 商品单位
   - `counter_price`: 专柜价格
   - `retail_price`: 零售价格
   - `detail`: 商品详细介绍，富文本格式

2. **litemall_goods_product** - 商品货品表
   - `id`: 货品ID
   - `goods_id`: 商品表的商品ID
   - `specifications`: 商品规格值列表，JSON数组格式
   - `price`: 商品货品价格
   - `number`: 商品货品数量
   - `url`: 商品货品图片

3. **litemall_goods_specification** - 商品规格表
   - `id`: 规格ID
   - `goods_id`: 商品表的商品ID
   - `specification`: 商品规格名称
   - `value`: 商品规格值
   - `pic_url`: 商品规格图片

4. **litemall_goods_attribute** - 商品参数表
   - `id`: 参数ID
   - `goods_id`: 商品表的商品ID
   - `attribute`: 商品参数名称
   - `value`: 商品参数值

5. **litemall_category** - 类目表
   - `id`: 类目ID
   - `name`: 类目名称
   - `keywords`: 类目关键字，JSON数组格式
   - `desc`: 类目广告语介绍
   - `pid`: 父类目ID
   - `icon_url`: 类目图标
   - `pic_url`: 类目图片
   - `level`: 类目级别
   - `sort_order`: 排序

6. **litemall_brand** - 品牌商表
   - `id`: 品牌ID
   - `name`: 品牌商名称
   - `desc`: 品牌商简介
   - `pic_url`: 品牌商图片
   - `sort_order`: 排序
   - `floor_price`: 品牌商的商品低价

### 订单相关表

1. **litemall_order** - 订单表
   - `id`: 订单ID
   - `user_id`: 用户ID
   - `order_sn`: 订单编号
   - `order_status`: 订单状态
   - `aftersale_status`: 售后状态
   - `consignee`: 收货人名称
   - `mobile`: 收货人手机号
   - `address`: 收货具体地址
   - `message`: 用户订单留言
   - `goods_price`: 商品总费用
   - `freight_price`: 配送费用
   - `coupon_price`: 优惠券减免
   - `integral_price`: 用户积分减免
   - `groupon_price`: 团购优惠价减免
   - `order_price`: 订单费用
   - `actual_price`: 实付费用
   - `pay_id`: 微信付款编号
   - `pay_time`: 微信付款时间
   - `ship_sn`: 发货编号
   - `ship_channel`: 发货快递公司
   - `ship_time`: 发货开始时间
   - `confirm_time`: 用户确认收货时间
   - `comments`: 待评价订单商品数量
   - `end_time`: 订单关闭时间

2. **litemall_order_goods** - 订单商品表
   - `id`: 订单商品ID
   - `order_id`: 订单表的订单ID
   - `goods_id`: 商品表的商品ID
   - `goods_name`: 商品名称
   - `goods_sn`: 商品编号
   - `product_id`: 商品货品表的货品ID
   - `number`: 商品货品的购买数量
   - `price`: 商品货品的售价
   - `specifications`: 商品货品的规格列表
   - `pic_url`: 商品货品图片或者商品图片
   - `comment`: 订单商品评论状态

3. **litemall_cart** - 购物车商品表
   - `id`: 购物车ID
   - `user_id`: 用户ID
   - `goods_id`: 商品ID
   - `goods_sn`: 商品编号
   - `goods_name`: 商品名称
   - `product_id`: 商品货品表的货品ID
   - `price`: 商品货品的价格
   - `number`: 商品货品的数量
   - `specifications`: 商品规格值列表，JSON数组格式
   - `checked`: 购物车中商品是否选择状态
   - `pic_url`: 商品图片或者商品货品图片

## 后台统计报表相关API设计与实现

### 1. 统计报表控制器 (AdminStatController)

位于 `litemall-admin-api/src/main/java/org/linlinjava/litemall/admin/web/AdminStatController.java`

该控制器提供了三个主要的统计API端点：

#### 1.1 用户统计 API

```java
@RequiresPermissions("admin:stat:user")
@RequiresPermissionsDesc(menu = {"统计管理", "用户统计"}, button = "查询")
@GetMapping("/user")
public Object statUser() {
    List<Map> rows = statService.statUser();
    String[] columns = new String[]{"day", "users"};
    StatVo statVo = new StatVo();
    statVo.setColumns(columns);
    statVo.setRows(rows);
    return ResponseUtil.ok(statVo);
}
```

- **URL**: `/admin/stat/user`
- **方法**: GET
- **权限**: 需要 `admin:stat:user` 权限
- **功能**: 统计每日新增用户数量
- **返回数据**: 包含日期和用户数量的统计数据

#### 1.2 订单统计 API

```java
@RequiresPermissions("admin:stat:order")
@RequiresPermissionsDesc(menu = {"统计管理", "订单统计"}, button = "查询")
@GetMapping("/order")
public Object statOrder() {
    List<Map> rows = statService.statOrder();
    String[] columns = new String[]{"day", "orders", "customers", "amount", "pcr"};
    StatVo statVo = new StatVo();
    statVo.setColumns(columns);
    statVo.setRows(rows);
    return ResponseUtil.ok(statVo);
}
```

- **URL**: `/admin/stat/order`
- **方法**: GET
- **权限**: 需要 `admin:stat:order` 权限
- **功能**: 统计每日订单数量、顾客数量、订单金额和客单价
- **返回数据**: 包含日期、订单数、顾客数、订单金额和客单价的统计数据

#### 1.3 商品统计 API

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

- **URL**: `/admin/stat/goods`
- **方法**: GET
- **权限**: 需要 `admin:stat:goods` 权限
- **功能**: 统计每日订单商品数量和订单商品金额
- **返回数据**: 包含日期、订单数、商品数量和订单金额的统计数据

### 2. 统计服务实现 (StatService)

位于 `litemall-db/src/main/java/org/linlinjava/litemall/db/service/StatService.java`

```java
@Service
public class StatService {
    @Resource
    private StatMapper statMapper;

    public List<Map> statUser() {
        return statMapper.statUser();
    }

    public List<Map> statOrder() {
        return statMapper.statOrder();
    }

    public List<Map> statGoods() {
        return statMapper.statGoods();
    }
}
```

该服务类调用 StatMapper 接口中定义的方法来获取统计数据。

### 3. 统计数据访问接口 (StatMapper)

位于 `litemall-db/src/main/java/org/linlinjava/litemall/db/dao/StatMapper.java`

```java
public interface StatMapper {
    List<Map> statUser();
    List<Map> statOrder();
    List<Map> statGoods();
}
```

### 4. 统计SQL实现 (StatMapper.xml)

位于 `litemall-db/src/main/resources/org/linlinjava/litemall/db/dao/StatMapper.xml`

#### 4.1 用户统计SQL

```xml
<select id="statUser" resultType="map">
    select
    substr(add_time,1,10) as day,
    count(distinct id) as users
    from litemall_user
    group by substr(add_time,1,10)
</select>
```

- **功能**: 按天统计新增用户数量
- **返回**: 日期和用户数量

#### 4.2 订单统计SQL

```xml
<select id="statOrder" resultType="map">
    select
    substr(add_time,1,10) as day,
    count(id) as orders,
    count(distinct user_id) as customers,
    sum(actual_price) as amount,
    round(sum(actual_price)/count(distinct user_id),2) as pcr
    from litemall_order
    where order_status in(401,402)
    group by substr(add_time,1,10)
</select>
```

- **功能**: 按天统计已完成订单的数量、顾客数量、订单金额和客单价
- **条件**: 订单状态为401(用户收货)或402(系统自动确认)
- **返回**: 日期、订单数、顾客数、订单金额和客单价

#### 4.3 商品统计SQL

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

- **功能**: 按天统计订单商品数量和订单商品金额
- **返回**: 日期、订单数、商品数量和订单金额

### 5. 仪表盘控制器 (AdminDashbordController)

位于 `litemall-admin-api/src/main/java/org/linlinjava/litemall/admin/web/AdminDashbordController.java`

```java
@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallGoodsProductService productService;
    @Autowired
    private LitemallOrderService orderService;

    @GetMapping("")
    public Object info() {
        int userTotal = userService.count();
        int goodsTotal = goodsService.count();
        int productTotal = productService.count();
        int orderTotal = orderService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", userTotal);
        data.put("goodsTotal", goodsTotal);
        data.put("productTotal", productTotal);
        data.put("orderTotal", orderTotal);

        return ResponseUtil.ok(data);
    }
}
```

- **URL**: `/admin/dashboard`
- **方法**: GET
- **功能**: 获取系统概览数据，包括用户总数、商品总数、产品总数和订单总数
- **返回数据**: 包含各项总数的统计数据

### 6. 统计数据结构 (StatVo)

位于 `litemall-admin-api/src/main/java/org/linlinjava/litemall/admin/vo/StatVo.java`

```java
public class StatVo {
    private String[] columns = new String[0];
    private List<Map> rows = new ArrayList<>();

    // Getters and setters
    
    public void add(Map... r) {
        rows.addAll(Arrays.asList(r));
    }
}
```

这个类用于封装统计数据，包含列名和行数据。

## 总结

LiteMall系统的商品和订单相关表设计完整，能够支持电商系统的基本功能。统计报表API设计简洁明了，主要提供了三类统计数据：用户统计、订单统计和商品统计，以及一个系统概览的仪表盘API。

统计报表的实现采用了分层架构：
1. 控制器层(Controller)：处理HTTP请求，权限控制，数据封装
2. 服务层(Service)：业务逻辑处理
3. 数据访问层(Mapper)：通过SQL语句从数据库获取统计数据

这种设计使得系统具有良好的可维护性和扩展性，可以方便地添加新的统计报表功能。