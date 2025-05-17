# 热门商品统计API实现计划

本文档提供了实现热门商品统计API的步骤分解和对应的提示词，可以按照顺序逐步完成需求。

## 步骤1：在StatMapper接口中添加热门商品统计方法

**提示词**：
```
请在StatMapper接口中添加两个新方法，用于获取最近7天和最近30天的热门商品数据。接口位于`litemall-db/src/main/java/org/linlinjava/litemall/db/dao/StatMapper.java`。

方法签名如下：
1. `List<Map> statHotGoods7Days();` - 获取最近7天热门商品
2. `List<Map> statHotGoods30Days();` - 获取最近30天热门商品

请参考现有的statUser()、statOrder()和statGoods()方法的实现风格。
```

## 步骤2：测试StatMapper接口修改

**提示词**：
```
请检查StatMapper接口的修改是否正确。确认以下几点：
1. 方法名称是否符合命名规范
2. 返回类型是否正确（List<Map>）
3. 是否与现有方法风格一致
4. 是否有语法错误

如果有问题，请修正。
```

## 步骤3：在StatMapper.xml中添加SQL查询

**提示词**：
```
请在StatMapper.xml文件中添加对应的SQL查询，用于实现最近7天和最近30天热门商品的统计。文件位于`litemall-db/src/main/resources/org/linlinjava/litemall/db/dao/StatMapper.xml`。

需要添加两个select语句：
1. id为"statHotGoods7Days"的查询，统计最近7天销售量前10的商品
2. id为"statHotGoods30Days"的查询，统计最近30天销售量前10的商品

SQL查询需要从litemall_order_goods表中统计商品销售量，并关联litemall_goods表获取商品名称。
返回的字段应包括：goods_id（商品ID）、goods_name（商品名称）和sales_count（销售数量）。
按销售量降序排序，并限制返回前10条记录。

参考以下SQL结构：
```xml
<select id="statHotGoods7Days" resultType="map">
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
</select>
```

请同样实现30天的查询，只需修改时间范围条件。
```

## 步骤4：测试SQL查询

**提示词**：
```
请检查添加的SQL查询是否正确。确认以下几点：
1. XML语法是否正确
2. SQL语句是否有语法错误
3. 表名和字段名是否正确
4. 查询条件是否符合需求（时间范围、排序、限制条数等）
5. 返回的字段是否包含goods_id、goods_name和sales_count

如果有问题，请修正。
```

## 步骤5：在StatService中添加服务方法

**提示词**：
```
请在StatService类中添加两个新方法，用于调用Mapper层的方法获取热门商品数据。文件位于`litemall-db/src/main/java/org/linlinjava/litemall/db/service/StatService.java`。

方法实现如下：
```java
public List<Map> statHotGoods7Days() {
    return statMapper.statHotGoods7Days();
}

public List<Map> statHotGoods30Days() {
    return statMapper.statHotGoods30Days();
}
```

请参考现有的statUser()、statOrder()和statGoods()方法的实现风格。
```

## 步骤6：测试StatService方法

**提示词**：
```
请检查StatService中添加的方法是否正确。确认以下几点：
1. 方法名称是否与Mapper接口一致
2. 返回类型是否正确
3. 是否正确调用了Mapper接口的方法
4. 是否与现有方法风格一致
5. 是否有语法错误

如果有问题，请修正。
```

## 步骤7：创建AdminHotGoodsController控制器

**提示词**：
```
请创建一个新的控制器类AdminHotGoodsController，用于处理热门商品的API请求。文件应位于`litemall-admin-api/src/main/java/org/linlinjava/litemall/admin/web/AdminHotGoodsController.java`。

控制器类应包含以下内容：
1. 类注解：@RestController、@RequestMapping("/admin/stat/hot-goods")、@Validated
2. 注入StatService
3. 两个API端点方法，分别处理7天和30天的热门商品查询

参考以下代码结构：
```java
package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.vo.StatVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/stat/hot-goods")
@Validated
public class AdminHotGoodsController {
    private final Log logger = LogFactory.getLog(AdminHotGoodsController.class);

    @Autowired
    private StatService statService;

    // 在这里添加API端点方法
}
```

请参考AdminStatController的实现风格。
```

## 步骤8：测试控制器类创建

**提示词**：
```
请检查AdminHotGoodsController类的创建是否正确。确认以下几点：
1. 包名是否正确
2. 导入的包是否完整
3. 类注解是否正确
4. 是否正确注入了StatService
5. 是否有语法错误

如果有问题，请修正。
```

## 步骤9：实现热门商品API端点

**提示词**：
```
请在AdminHotGoodsController类中实现两个API端点方法，分别用于查询最近7天和最近30天的热门商品。

参考以下代码实现：
```java
@RequiresPermissions("admin:stat:hotgoods")
@RequiresPermissionsDesc(menu = {"统计管理", "热门商品"}, button = "7天统计")
@GetMapping("/7days")
public Object statHotGoods7Days() {
    List<Map> rows = statService.statHotGoods7Days();
    String[] columns = new String[]{"goods_id", "goods_name", "sales_count"};
    StatVo statVo = new StatVo();
    statVo.setColumns(columns);
    statVo.setRows(rows);
    return ResponseUtil.ok(statVo);
}

@RequiresPermissions("admin:stat:hotgoods")
@RequiresPermissionsDesc(menu = {"统计管理", "热门商品"}, button = "30天统计")
@GetMapping("/30days")
public Object statHotGoods30Days() {
    List<Map> rows = statService.statHotGoods30Days();
    String[] columns = new String[]{"goods_id", "goods_name", "sales_count"};
    StatVo statVo = new StatVo();
    statVo.setColumns(columns);
    statVo.setRows(rows);
    return ResponseUtil.ok(statVo);
}
```

请确保方法的实现与现有的统计API保持一致的风格。
```

## 步骤10：测试API端点实现

**提示词**：
```
请检查API端点方法的实现是否正确。确认以下几点：
1. 权限注解是否正确
2. URL映射是否正确
3. 方法名称是否符合命名规范
4. 是否正确调用了Service层的方法
5. 返回的数据格式是否正确（使用StatVo封装）
6. 列名是否与SQL查询返回的字段一致
7. 是否有语法错误

如果有问题，请修正。
```

## 步骤11：添加单元测试

**提示词**：
```
请为新添加的功能编写单元测试。创建一个测试类`AdminHotGoodsControllerTest`，位于`litemall-admin-api/src/test/java/org/linlinjava/litemall/admin/web/AdminHotGoodsControllerTest.java`。

测试类应包含以下内容：
1. 测试最近7天热门商品API
2. 测试最近30天热门商品API

参考以下代码结构：
```java
package org.linlinjava.litemall.admin.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminHotGoodsControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testStatHotGoods7Days() throws Exception {
        // 测试代码
    }

    @Test
    public void testStatHotGoods30Days() throws Exception {
        // 测试代码
    }
}
```

请根据项目中现有的测试类实现方式来完善测试代码。
```

## 步骤12：测试单元测试

**提示词**：
```
请检查单元测试类的实现是否正确。确认以下几点：
1. 包名是否正确
2. 导入的包是否完整
3. 测试方法是否正确实现
4. 是否测试了API的正确性
5. 是否有语法错误

如果有问题，请修正。
```

## 步骤13：集成测试

**提示词**：
```
请编写一个简单的集成测试脚本，用于测试整个热门商品统计功能的流程。测试脚本应包括：
1. 调用7天热门商品API
2. 调用30天热门商品API
3. 验证返回的数据格式是否正确
4. 验证返回的数据内容是否符合预期

可以使用curl命令或其他HTTP客户端工具来实现。
```

## 步骤14：文档更新

**提示词**：
```
请更新项目文档，添加关于热门商品统计API的说明。在项目的API文档中添加以下内容：

## 热门商品统计API

### 最近7天热门商品

- **URL**: `/admin/stat/hot-goods/7days`
- **方法**: GET
- **权限**: 需要管理员权限
- **功能**: 统计最近7天销售量前10的商品
- **返回数据**: 包含商品ID、商品名称和销售数量的统计数据

### 最近30天热门商品

- **URL**: `/admin/stat/hot-goods/30days`
- **方法**: GET
- **权限**: 需要管理员权限
- **功能**: 统计最近30天销售量前10的商品
- **返回数据**: 包含商品ID、商品名称和销售数量的统计数据

### 响应示例

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
      }
    ]
  },
  "errmsg": "成功"
}
```
```

## 步骤15：最终检查

**提示词**：
```
请对整个实现进行最终检查，确保所有代码都正确无误。检查以下几点：
1. 所有文件的修改是否正确
2. 代码风格是否与项目保持一致
3. 命名是否符合规范
4. 权限控制是否正确
5. 返回的数据格式是否正确
6. 单元测试和集成测试是否通过
7. 文档是否更新

如果有任何问题，请修正。
```

## 总结

以上步骤提供了实现热门商品统计API的完整流程，每个步骤都包含了详细的提示词，可以指导AI完成代码实现。按照这些步骤逐一执行，可以确保代码质量和功能正确性。