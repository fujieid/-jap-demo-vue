import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/pages/index'
import SimpleLogin from '@/pages/simpleLogin'
import socialCallback from '@/pages/callback/social'
import oauthCallback from '@/pages/callback/oauth'
import oidcCallback from '@/pages/callback/oidc'
import oauthImplicitCallback from '@/pages/callback/oauthImplicit'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/simpleLogin',
      name: 'SimpleLogin',
      component: SimpleLogin
    },
    {
      path: '/callback',
      redirect: '/callback',
      name: 'callback',
      component: {
        render (c) { return c('router-view') }
      },
      children: [
        {
          path: 'social/:platform',
          name: 'social',
          component: socialCallback
        },
        {
          path: 'oauth',
          name: 'oauth',
          component: oauthCallback
        },
        {
          path: 'oauthImplicit',
          name: 'oauthImplicit',
          component: oauthImplicitCallback
        },
        {
          path: 'oidc',
          name: 'oidc',
          component: oidcCallback
        }
      ]
    },
  ]
})
