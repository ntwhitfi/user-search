<template>
  <div class="home">
    <UserSearch/>
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
