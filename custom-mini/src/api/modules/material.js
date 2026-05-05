import { get } from '../request'

export const getMaterials = (params) => get('/material/list', params)
