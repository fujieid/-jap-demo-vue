<template>
  <div class="container">

    <div class="jumbotron mt-2 p-4">
      <h1 class="display-4">JAP统一登录认证软件</h1>
      <p class="lead">Just auth for any app</p>
      <!--<hr class="my-4">
      <a class="btn btn-info" href="https://gitee.com/yadong.zhang/JustAuth" target="_blank" role="button">Gitee <img src="https://gitee.com/yadong.zhang/JustAuth/badge/star.svg?theme=gvp" alt="star"></a>
      <a class="btn btn-info" href="https://github.com/zhangyd-c/JustAuth" target="_blank" role="button">Github <img src="https://img.shields.io/github/stars/zhangyd-c/JustAuth.svg?style=social" alt="github"></a>-->
      <a class="btn btn-light" href="javascript:;;" @click="logout" role="button">退出</a>
      <a class="btn btn-light" href="javascript:;;" @click="enableSso" role="button">切换 Session / SSO（Cookie）</a>
      <span v-if="strategy" class="ml-2">
            当前登录使用的策略：<span class="badge badge-light">{{strategy}}</span>
        </span>
      <span class="ml-2">
            <span class="badge badge-primary" v-if="sso" >启用 SSO</span>
            <span class="badge badge-light" v-else >启用 Session</span>
        </span>
    </div>
    <div class="alert alert-danger mb-2" role="alert" v-if="error != null">{{error}}</div>
    <div class="card">
      <div class="card-body">
        访问资源服务：<a href="javascript:;;" @click="requestApiUser">/api/user</a>，如果已登录，会显示接口返回的内容，否则会提示未登录
      </div>
    </div>
    <div class="alert alert-success mt-2" role="alert" v-if="apiUserMsg != null">{{apiUserMsg}}</div>

    <div class="row mt-3">
      <div class="col col-4">
        <div class="accordion" id="accordionExample">
          <div class="card">
            <div class="card-header p-2" id="simpleHeader">
              <h2 class="mb-0">
                <button class="btn btn-link btn-block text-left text-secondary" type="button" data-toggle="collapse" data-target="#simpleContainer" aria-expanded="true" aria-controls="simpleContainer">
                  <i class="fas fa-coffee mr-2"></i>simple
                </button>
              </h2>
            </div>
            <div id="simpleContainer" class="collapse show" aria-labelledby="simpleHeader" data-parent="#accordionExample">
              <div class="card-body p-2">
                <ul class="list-unstyled mb-0 pl-2">
                  <li>
                    <a href="/simpleLogin">账号密码登录</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="card">
            <div class="card-header p-2" id="socialHeader">
              <h2 class="mb-0">
                <button class="btn btn-link btn-block text-left text-secondary collapsed" type="button" data-toggle="collapse" data-target="#socialContainer" aria-expanded="false" aria-controls="socialContainer">
                  <i class="fas fa-coffee mr-2"></i>social
                </button>
              </h2>
            </div>
            <div id="socialContainer" class="collapse" aria-labelledby="socialHeader" data-parent="#accordionExample">
              <div class="card-body p-2">
                <ul class="list-unstyled mb-0 pl-2">
                  <li>
                    <img src="https://cdn.jsdelivr.net/gh/justauth/justauth-oauth-logo@1.5/gitee.png" alt="" style="width: 20px;position: relative;top: -2px;;" class="mr-1">
                    <a href="javascript:;;" @click="socialLogin('gitee')">Gitee 登录</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="card">
            <div class="card-header p-2" id="oauth2Header">
              <h2 class="mb-0">
                <button class="btn btn-link btn-block text-left text-secondary collapsed" type="button" data-toggle="collapse" data-target="#oauth2Container" aria-expanded="false" aria-controls="oauth2Container">
                  <i class="fas fa-coffee mr-2"></i>oauth2
                </button>
              </h2>
            </div>
            <div id="oauth2Container" class="collapse" aria-labelledby="oauth2Header" data-parent="#accordionExample">
              <div class="card-body p-2">
                <ul class="list-unstyled mb-0 pl-2">
                  <li>
                    <a href="javascript:;;" @click="oauth2Login('code')">1. 授权码模式登录</a>
                  </li>
                  <li>
                    <a href="javascript:;;" @click="oauth2Login('implicit')">2. 隐式授权模式登录</a>
                  </li>
                  <li>
                    <a href="javascript:;;" @click="oauth2Login('password')">3. 密码授权模式登录</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="card">
            <div class="card-header p-2" id="oidcHeader">
              <h2 class="mb-0">
                <button class="btn btn-link btn-block text-left text-secondary collapsed" type="button" data-toggle="collapse" data-target="#oicContainer" aria-expanded="false" aria-controls="oicContainer">
                  <i class="fas fa-coffee mr-2"></i>oidc
                </button>
              </h2>
            </div>
            <div id="oicContainer" class="collapse" aria-labelledby="oidcHeader" data-parent="#accordionExample">
              <div class="card-body p-2">
                <ul class="list-unstyled mb-0 pl-2">
                  <li>
                    <a href="javascript:;;" @click="oidcLogin">OIDC 登录</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col col-8">
        <pre v-if="userInfo != null" class="rounded"><code class="json">{{userInfo}}</code></pre>
        <pre v-if="error != null" class="rounded"><code class="html">{{error}}</code></pre>
      </div>
    </div>
  </div>
</template>

<script>
import storage from '@/utils/storage'
export default {
  name: 'index',
  data () {
    return {
      error: null,
      apiUserMsg: null,
      sso: true,
      strategy: null,
      userInfo: null
    }
  },
  mounted () {
    const self = this
    if (self.$route.query.error) {
      self.error = decodeURIComponent(self.$route.query.error)
    }
    self.checkToken(function (){
      self.getUserInfo()
    })
  },
  methods: {
    async checkToken (callback) {
      try {
        const result = await this.$http.get(
            `/api/checkToken?token=${storage.getToken()}`
        )
        const data = result.data
        if(!data) {
          console.log('未登录')
          this.error = 'Not logged in.'
        } else {
          callback && callback()
        }
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async getUserInfo () {
      try {
        const result = await this.$http.get(
            '/api/getLoginInfo'
        )
        const data = result.data
        console.log(data)
        this.strategy = data.strategy
        this.sso = data.sso
        this.userInfo = data.userJson
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async socialLogin (platform) {
      try {
        const result = await this.$http.post(
            '/api/social/login/' + platform
        )
        const data = result.data
        console.log(data)
        if((typeof data) == 'string') {
          window.location.href = data
        } else {
          // 直接使用 data 也行，这儿直接更新首页
          // this.userInfo = data
          // 保存用户 token
          storage.setToken(data.token);
          window.location.href = "/"
        }

      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async oauth2Login (type) {
      try {
        const types = {
          code: '/api/oauth2/login/jai',
          implicit: '/api/oauth2/login/implicit/jai',
          password: '/api/oauth2/login/password/jai',
        }
        const result = await this.$http.post(
            types[type]
        )
        const data = result.data
        console.log(data)
        if((typeof data) == 'string') {
          window.location.href = data
        } else {
          // 保存用户 token
          storage.setToken(data.token);
          window.location.href = "/"
        }
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async oidcLogin () {
      try {
        const result = await this.$http.post(
            '/api/oidc/login/jai'
        )
        const data = result.data
        console.log(data)
        if((typeof data) == 'string') {
          window.location.href = data
        } else {
          // 保存用户 token
          storage.setToken(data.token);
          window.location.href = "/"
        }
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async logout () {
      try {
        const result = await this.$http.get(
            '/api/logout'
        )
        // 清楚用户 token
        storage.removeToken();
        window.location.reload()
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async enableSso () {
      try {
        const result = await this.$http.get(
            '/api/enableSso'
        )
        window.location.reload()
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    },
    async requestApiUser () {
      try {
        const result = await this.$http.get(
            '/api/api/user'
        )
        this.apiUserMsg = result.data
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
a {
  color: #42b983;
}
</style>
