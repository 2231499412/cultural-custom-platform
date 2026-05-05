import request from '../request'

export function getMaterials(params) {
  return request.get('/material/list', { params })
}
