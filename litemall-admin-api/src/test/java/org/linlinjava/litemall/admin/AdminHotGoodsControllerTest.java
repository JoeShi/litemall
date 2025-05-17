package org.linlinjava.litemall.admin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.linlinjava.litemall.admin.web.AdminHotGoodsController;
import org.linlinjava.litemall.db.service.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class AdminHotGoodsControllerTest {

    private AdminHotGoodsController controller;
    private StatService statService;

    @Before
    public void setUp() {
        controller = new AdminHotGoodsController();
        statService = new MockStatService();
        
        // 使用反射设置statService
        try {
            java.lang.reflect.Field field = AdminHotGoodsController.class.getDeclaredField("statService");
            field.setAccessible(true);
            field.set(controller, statService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStatHotGoods7Days() {
        // 调用控制器方法
        Object result = controller.statHotGoods7Days();
        
        // 验证结果是否为非空
        assertNotNull(result);
        
        // 验证返回的数据结构
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals(0, resultMap.get("errno"));
        assertNotNull(resultMap.get("data"));
    }

    @Test
    public void testStatHotGoods30Days() {
        // 调用控制器方法
        Object result = controller.statHotGoods30Days();
        
        // 验证结果是否为非空
        assertNotNull(result);
        
        // 验证返回的数据结构
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals(0, resultMap.get("errno"));
        assertNotNull(resultMap.get("data"));
    }
    
    // 模拟StatService实现
    private class MockStatService extends StatService {
        @Override
        public List<Map> statHotGoods7Days() {
            List<Map> mockData = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("goods_id", 1);
            item.put("goods_name", "测试商品");
            item.put("sales_count", 100);
            mockData.add(item);
            return mockData;
        }
        
        @Override
        public List<Map> statHotGoods30Days() {
            List<Map> mockData = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("goods_id", 1);
            item.put("goods_name", "测试商品");
            item.put("sales_count", 100);
            mockData.add(item);
            return mockData;
        }
    }
}