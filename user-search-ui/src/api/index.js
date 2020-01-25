/* eslint-disable */
import axios from "axios";

const userSearchInstance = axios.create({
  baseURL: "https://cokg6uuo7d.execute-api.us-east-1.amazonaws.com/dev",
  headers: {'Content-Type': 'application/json'}
});

const userSearchContextPath = "/user/search";
const searchQueryTypeParameter = "query"

const api = {
  searchUser(searchQuery) {
    let queryString = `?type=${searchQueryTypeParameter}&query=${searchQuery}`
    return userSearchInstance.get(userSearchContextPath + queryString);
  }
};

export default api;
