#!/bin/bash

# 测试脚本：验证热门商品统计API
# 后端运行在 localhost:8080

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}开始测试热门商品统计API...${NC}"

# 检查后端服务是否运行
echo -e "${YELLOW}检查后端服务是否运行...${NC}"
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/ | grep -q "200\|302\|404"; then
    echo -e "${GREEN}后端服务正在运行。${NC}"
else
    echo -e "${RED}后端服务可能未运行，请确认服务已启动并监听在 localhost:8080。${NC}"
    exit 1
fi

# 使用已知成功的登录凭据
echo -e "${YELLOW}1. 登录管理员账号获取token...${NC}"
LOGIN_RESULT=$(curl -s -X POST -H "Content-Type: application/json" -d '{"username":"admin123","password":"admin123"}' http://localhost:8080/admin/auth/login)
echo -e "登录响应: $LOGIN_RESULT"

# 提取token - 修复提取方式
TOKEN=$(echo $LOGIN_RESULT | grep -o '"token":"[^"]*"' | cut -d':' -f2 | tr -d '"')

if [ -z "$TOKEN" ]; then
    echo -e "${RED}登录失败，无法获取token。请确认后端服务是否正常运行，以及用户名密码是否正确。${NC}"
    exit 1
else
    echo -e "${GREEN}登录成功，获取到token: $TOKEN${NC}"
fi

# 测试最近7天热门商品API
echo -e "${YELLOW}2. 测试最近7天热门商品API...${NC}"
echo -e "请求URL: http://localhost:8080/admin/stat/hot-goods/7days"
SEVEN_DAYS_RESULT=$(curl -s -H "X-Litemall-Admin-Token: $TOKEN" http://localhost:8080/admin/stat/hot-goods/7days)

# 检查响应是否成功
if echo $SEVEN_DAYS_RESULT | grep -q '"errno":0'; then
    echo -e "${GREEN}最近7天热门商品API测试成功!${NC}"
    echo -e "响应数据:"
    # 使用jq格式化JSON输出
    echo $SEVEN_DAYS_RESULT | jq .
else
    echo -e "${RED}最近7天热门商品API测试失败!${NC}"
    echo "错误响应: $SEVEN_DAYS_RESULT"
fi

# 测试最近30天热门商品API
echo -e "${YELLOW}3. 测试最近30天热门商品API...${NC}"
echo -e "请求URL: http://localhost:8080/admin/stat/hot-goods/30days"
THIRTY_DAYS_RESULT=$(curl -s -H "X-Litemall-Admin-Token: $TOKEN" http://localhost:8080/admin/stat/hot-goods/30days)

# 检查响应是否成功
if echo $THIRTY_DAYS_RESULT | grep -q '"errno":0'; then
    echo -e "${GREEN}最近30天热门商品API测试成功!${NC}"
    echo -e "响应数据:"
    # 使用jq格式化JSON输出
    echo $THIRTY_DAYS_RESULT | jq .
else
    echo -e "${RED}最近30天热门商品API测试失败!${NC}"
    echo "错误响应: $THIRTY_DAYS_RESULT"
fi

echo -e "${YELLOW}测试完成${NC}"