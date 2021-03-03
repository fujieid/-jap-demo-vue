export default {
  getToken () {
    const token = localStorage.getItem('_JAP.token')
    if (token && typeof token === 'string' && 'null' !== token && token.length > 0) {
      return token
    } else {
      return ''
    }
  },
  removeToken () {
    localStorage.removeItem('_JAP.token')
  },
  setToken (token) {
    if (typeof token === 'string' && token.length > 0) {
      localStorage.setItem('_JAP.token', token)
    }
  },
  set (key, value) {
    localStorage.setItem(key, value)
  },
  get (key) {
    return localStorage.getItem(key)
  },
  remove (key) {
    localStorage.removeItem(key)
  }
}
