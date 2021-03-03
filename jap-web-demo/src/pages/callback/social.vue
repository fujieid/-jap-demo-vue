<template>
  <div class="container">
    <div class="card mt-5 social">
      <div class="card-body">
        <h1>Social Callback</h1>
        <i class="fas fa-spinner mr-2 mb-2"></i>第三方登录完成，开始处理回调请求...
        <p>{{code}}</p>
        <p>{{state}}</p>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs'
import storage from '@/utils/storage'
export default {
  name: 'socialCallback',
  data () {
    return {
      msg: 'Social Callback',
      code: null,
      state: null
    }
  },
  mounted () {
    const code = this.$route.query.code
    const state = this.$route.query.state
    this.code = code
    this.state = state
    console.log(code, state)
    if(code) {
      this.socialLogin({
        code: code,
        state: state
      })
    } else {
      // 非法的回调
    }
  },
  methods: {
    async socialLogin (param) {
      try {
        const result = await this.$http.post(
            '/api/social/login/gitee',
            qs.stringify(param)
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
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.social {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
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
