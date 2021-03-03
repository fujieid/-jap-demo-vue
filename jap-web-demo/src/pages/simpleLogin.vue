<template>
  <div class="container">

    <div class="jumbotron mt-2 p-4">
      <h1 class="display-4">JAP统一登录认证软件</h1>
      <p class="lead">Just auth for any app</p>
    </div>
    <div class="alert alert-danger mb-2" role="alert" v-if="error != null">{{error}}</div>
    <div class="alert alert-success mb-2" role="alert" v-if="userInfo != null">{{userInfo}}</div>
    <div class="row mt-3">
      <div class="col-12">
        <div>
          <label for="username">账号：</label>
          <input id="username" v-model="formData.username" name="username" type="text">
        </div>
        <div>
          <label for="password">密码：</label>
          <input id="password" v-model="formData.password" name="password" type="text">
        </div>
        <div>
          <label for="password">记住我：</label>
          <input id="rememberMe" v-model="formData.rememberMe" name="rememberMe" type="checkbox">
        </div>
        <div>
          <input class="btn btn-primary" type="button" value="登录" @click="login">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs'
import storage from '@/utils/storage'
export default {
  name: 'simpleLogin',
  data () {
    return {
      error: null,
      formData: {
        username: 'jap',
        password: 'jap',
        rememberMe: true,
      },
      userInfo: null
    }
  },
  methods: {
    async login () {
      try {
        const result = await this.$http.post(
            '/api/simple/login',
            qs.stringify(this.formData)
        )
        console.log(result)
        this.userInfo = result.data
        // 保存用户 token
        storage.setToken(result.data.token);
      } catch (error) {
        this.error = (error.name ? error.name + ': ' : '') + error.message
      }
    }
  }
}
</script>

<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
