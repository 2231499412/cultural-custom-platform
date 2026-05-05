import request from '../request'

export function getTemplateList(params) {
  return request.get('/template/list', { params })
}

export function getTemplateDetail(id) {
  return request.get(`/template/${id}`)
}

export function getTemplateCategories() {
  return request.get('/template/categories')
}
