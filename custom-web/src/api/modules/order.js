import request from '../request'

export function createOrder(data) {
  return request.post('/order/create', data)
}

export function getOrderList(params) {
  return request.get('/order/list', { params })
}

export function getOrderDetail(id) {
  return request.get(`/order/${id}`)
}

export function cancelOrder(id) {
  return request.put(`/order/${id}/cancel`)
}
