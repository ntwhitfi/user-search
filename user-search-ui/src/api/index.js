/* eslint-disable */
import axios from "axios";

const userSearchInstance = axios.create({
  baseURL: "https://bemzagwyfb.execute-api.us-east-1.amazonaws.com/dev",
  headers: {'Content-Type': 'application/json'}
});

const userAuthInstance = axios.create({
  baseURL: "https://5n4l7vcv1m.execute-api.us-east-1.amazonaws.com/dev",
  headers: {'Content-Type': 'application/json'}
});

const userSearchContextPath = "/user/search";
const searchQueryTypeParameter = "query"

const api = {
  searchUser(searchQuery) {
    let queryString = `?type=${searchQueryTypeParameter}&query=${searchQuery}`
    return userSearchInstance.get(userSearchContextPath + queryString);
  },
  login(user) {
    new Promise ((resolve, reject) => {
      userAuthInstance.post('/auth', user)
        .then(resp => {
          const token = resp.data.token
          localStorage.setItem('authToken', token) // store the token in localstorage
          resolve(resp)
      })
      .catch(err => {
        localStorage.removeItem('authToken') // if the request fails, remove any possible user token if possible
        reject(err)
      })
    })
  },
  setAuthHeader(token) {
    userSearchInstance.defaults.headers.common['Authorization'] = token
  },
  removeAuthHeader() {
    delete userSearchInstance.defaults.headers.common['Authorization']
  }
};

export default api;
