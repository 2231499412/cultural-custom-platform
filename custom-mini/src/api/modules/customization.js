import { get, post } from '../request'

export const saveCustomization = (data) => post('/customization/save', data)
export const getCustomization = (id) => get(`/customization/${id}`)
export const getMyCustomizations = (params) => get('/customization/my', params)
