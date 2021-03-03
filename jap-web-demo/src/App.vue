<template>
  <div id="app">
    <notifications :duration="3000"
                   :width="500"
                   animation-name="v-fade-left"
                   position="top right">

      <template slot="body" slot-scope="props">
        <div :class="'custom-template ' + props.item.type">
          <div class="custom-template-icon">
            <i class="fe fe-info"></i>
          </div>
          <div class="custom-template-content">
            <div class="custom-template-title">
              {{props.item.title}}
            </div>
            <div class="custom-template-text"
                 v-html="props.item.text"></div>
          </div>
          <div @click="props.close" class="custom-template-close">
            <i class="fe fe-x"></i>
          </div>
        </div>
      </template>
    </notifications>
    <navbar></navbar>
    <router-view/>
  </div>
</template>

<script>
import navbar from '@/components/navbar.vue'
export default {
  name: 'App',
  components: {
    navbar
  },
}
</script>

<style>

</style>
