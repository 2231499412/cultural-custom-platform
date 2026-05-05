import { get, post } from '../request'

export const getUserInfo = () => get('/user/info')
export const wxLogin = (data) => post('/auth/wx-login', data)
export const login = (data) => post('/auth/login', data)
export const register = (data) => post('/auth/register', data)
