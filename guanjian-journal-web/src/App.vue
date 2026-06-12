<template>
  <Login v-if="isLoginPage" />
  <Home v-else />
</template>

<script setup lang="ts">
import Home from './views/front/Home.vue'
import Login from './views/front/Login.vue'

const isLoginPage = window.location.pathname.replace(/\/+$/, '') === '/login'
</script>
