<template>
  <div class="home">
    <UserLogin/>
  </div>
</template>

<script>
// @ is an alias to /src
import UserLogin from '@/components/UserLogin.vue'

export default {
  name: 'home',
  components: {
    UserLogin
  }
}
</script>