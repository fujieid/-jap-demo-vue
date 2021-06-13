import axios from 'axios'
import {storage} from '../utils'
import Vue from 'vue'

axios.defaults.withCredentials = true

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charse=UTF-8'
axios.defaults.headers.put['Content-Type'] = 'application/json;charse=UTF-8'
axios.defaults.headers.delete['Content-Type'] = 'application/json;charse=UTF-8'

// 每个请求加上Token
axios.interceptors.request.use(config => {
  config.headers['Authorization'] = storage.getToken()
  config.timeout = 300000
  config.timeoutErrorMessage = '资源服务器暂不可用！'
  return config
})

axios.interceptors.response.use(
  response => {
    nextEvent()
    const {code, message} = response.data
    if (!code && !message) {
      return response.data || {}
    }
    if (code === 200) {
      return response.data || {}
    }
    alert(response.data.message)
    return Promise.reject({
      message: response.data.message
    })
  },
  error => {
    nextEvent()
    return Promise.reject(error)
  }
)

function sendRequest (method, url, data, meta) {
  return axios({
    method,
    url,
    data,
    meta
  }).catch(error => {
    nextEvent()
    throw error
  })
}

function get (url, params, meta) {
  preEvent()
  return sendRequest('get', url, params, meta)
}

function post (url, data, meta) {
  preEvent()
  return sendRequest('post', url, data, meta)
}

function del (url, data, meta) {
  preEvent()
  return sendRequest('delete', url, data, meta)
}

function put (url, params, meta) {
  preEvent()
  return sendRequest('put', url, params, meta)
}

function preEvent () {
  maskShow()
}

function nextEvent () {
  maskHide()
}

function maskShow () {
}

function maskHide () {
}

export default class Api {
  static install (Vue) {
    Object.defineProperty(Vue.prototype, '$http', {
      get () {
        return {
          put,
          get,
          post,
          del
        }
      }
    })
  }
}
