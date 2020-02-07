<template>
  <div class="userSearch">
    <div class="loading-overlay" v-if="isLoading">
      <b-spinner style="margin: auto; width: 8rem; height: 8rem;" variant="primary" label="Text Centered"></b-spinner>
    </div>
    <b-card>
      <b-input-group class="mt-3">
        <b-form-input 
          id="username-input"
          v-model="username"
          required
          placeholder="Username"
        ></b-form-input>
        <b-form-input 
          id="password-input"
          v-model="password"
          type="password"
          required
          placeholder="Password"
        ></b-form-input>
        <b-input-group-append>
          <b-button @click="login" variant="info">login</b-button>
        </b-input-group-append>
      </b-input-group>
    </b-card>
  </div>
</template>

<script>
export default {
  name: 'UserSearch',
  props: {
  },
  data() {
    return {
      username: "",
      password: "",
      isLoading: false
    }
  },
  computed: {
    isLoggedIn : function(){ return this.$store.getters.isLoggedIn}
  },
  methods: {
    login: function () {
        this.isLoading = true
        let username = this.username
        let password = this.password
        this.$store.dispatch('login', { username, password }).then(() => {
          this.isLoading = false
          this.$router.push('/')
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
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
.loading-overlay {
  position: fixed;
  z-index: 99999;
  width: 100%;
  height: 100%;
  background-color: #F5F5F5;
  opacity: 0.5;
}
</style>
