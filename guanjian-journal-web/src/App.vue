<template>
  <Register v-if="currentPath === '/register'" />
  <Login v-else-if="currentPath === '/login'" />
  <Profile v-else-if="currentPath === '/profile'" />
  <Home v-else />
</template>

<script setup lang="ts">
import Home from './views/front/Home.vue'
import Login from './views/front/Login.vue'
import Profile from './views/front/Profile.vue'
import Register from './views/front/Register.vue'

const currentPath = window.location.pathname.replace(/\/+$/, '') || '/'
</script>
