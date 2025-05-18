import request from '@/utils/request'

export function statUser(query) {
  return request({
    url: '/stat/user',
    method: 'get',
    params: query
  })
}

export function statOrder(query) {
  return request({
    url: '/stat/order',
    method: 'get',
    params: query
  })
}

export function statGoods(query) {
  return request({
    url: '/stat/goods',
    method: 'get',
    params: query
  })
}

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
