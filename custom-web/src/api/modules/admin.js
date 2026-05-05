import request from '../request'

// 统计
export function getDashboardStats() {
  return request.get('/admin/dashboard/stats')
}

// 模板管理
export function adminGetTemplates(params) {
  return request.get('/admin/template/list', { params })
}

export function adminSaveTemplate(data) {
  return request.post('/admin/template/save', data)
}

export function adminDeleteTemplate(id) {
  return request.delete(`/admin/template/${id}`)
}

export function adminUpdateTemplateStatus(id, status) {
  return request.put(`/admin/template/${id}/status`, null, { params: { status } })
}

// 订单管理
export function adminGetOrders(params) {
  return request.get('/admin/order/list', { params })
}

export function adminConfirmOrder(id) {
  return request.put(`/admin/order/${id}/confirm`)
}

export function adminProduceOrder(id) {
  return request.put(`/admin/order/${id}/produce`)
}

export function adminShipOrder(id, remark) {
  return request.put(`/admin/order/${id}/ship`, { remark })
}

export function adminCompleteOrder(id) {
  return request.put(`/admin/order/${id}/complete`)
}

// 素材管理
export function adminGetMaterials(params) {
  return request.get('/admin/material/list', { params })
}

export function adminSaveMaterial(data) {
  return request.post('/admin/material/save', data)
}

export function adminDeleteMaterial(id) {
  return request.delete(`/admin/material/${id}`)
}
