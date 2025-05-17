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
}