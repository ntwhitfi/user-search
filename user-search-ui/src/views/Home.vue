<template>
  <div class="home">
    <UserSearch msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import UserSearch from '@/components/UserSearch.vue'

export default {
  name: 'home',
  components: {
    UserSearch
  }
}
</script>
