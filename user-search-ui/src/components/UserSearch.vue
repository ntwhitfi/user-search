<template>
  <div class="userSearch">
    <div class="loading-overlay" v-if="isLoading">
      <b-spinner style="margin: auto; width: 8rem; height: 8rem;" variant="primary" label="Text Centered"></b-spinner>
    </div>
    <b-card>
      <b-input-group class="mt-3">
        <b-form-input 
          id="search-input"
          v-model="searchQuery"
          required
          placeholder="Search users by name or department"
        ></b-form-input>
        <b-input-group-append>
          <b-button @click="searchRecords" variant="info">search</b-button>
        </b-input-group-append>
      </b-input-group>
      <b-table ref="userTable" striped hover :items="records"></b-table>
    </b-card>
  </div>
</template>

<script>
import api from "../api";

export default {
  name: 'UserSearch',
  props: {
  },
  data() {
    return {
      records: [],
      searchQuery: "",
      isLoading: false
    }
  },
  computed: {
    getRecords() {
      return this.records
    }
  },
  methods: {
      async searchRecords(evt){
        evt.preventDefault()
        this.isLoading = true
        let searchResponse = await api.searchUser(this.searchQuery)
        this.records = searchResponse.data.userRecords
        this.isLoading = false
        this.$refs.userTable.refresh();
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
