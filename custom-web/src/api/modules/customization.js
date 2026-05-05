import request from '../request'

export function saveCustomization(data) {
  return request.post('/customization/save', data)
}

export function getCustomization(id) {
  return request.get(`/customization/${id}`)
}

export function getMyCustomizations(params) {
  return request.get('/customization/my', { params })
}
