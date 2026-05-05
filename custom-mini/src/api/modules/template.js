import { get } from '../request'

export const getTemplateList = (params) => get('/template/list', params)
export const getTemplateDetail = (id) => get(`/template/${id}`)
export const getTemplateCategories = () => get('/template/categories')
