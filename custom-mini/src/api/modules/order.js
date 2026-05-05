import { get, post, put } from '../request'

export const createOrder = (data) => post('/order/create', data)
export const getOrderList = (params) => get('/order/list', params)
export const getOrderDetail = (id) => get(`/order/${id}`)
export const cancelOrder = (id) => put(`/order/${id}/cancel`)
