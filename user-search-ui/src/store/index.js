import Vue from 'vue'
import Vuex from 'vuex'
import Axios from 'axios'
import api from '../api/index'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    status: '',
    isLoading: false,
    token: localStorage.getItem('authToken') || '',
    user: {}
  },
  mutations: {
    auth_request(state){
      state.isLoading = true
    },
    auth_success(state, token, user){
      state.status = 'success'
      state.token = token
      state.user = user
      state.isLoading = false
    },
    auth_error(state){
      state.status = 'error'
      state.isLoading = false
    },
    logout(state){
      state.status = ''
      state.token = ''
    }
  },
  actions: {
    login({commit}, user){
      return new Promise((resolve, reject) => {
        commit('auth_request')
        Axios({url: 'https://api.usersearch.dev.ntwhitfi.com/auth', data: user, method: 'POST' })
        //api.login(user)
        .then(resp => {
          const token = resp.data.authToken
          localStorage.setItem('authToken', token)
          api.setAuthHeader(token)
          //userSearchInstance.defaults.headers.common['Authorization'] = token
          commit('auth_success', token)
          resolve(resp)
        })
        .catch(err => {
          commit('auth_error')
          localStorage.removeItem('authToken')
          reject(err)
        })
      })
    },
    logout({commit}){
      return new Promise((resolve) => {
        commit('logout')
        localStorage.removeItem('authToken')
        api.removeAuthHeader()
        resolve()
      })
    }
  },
  modules: {
  },
  getters: {
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status
  }
})
