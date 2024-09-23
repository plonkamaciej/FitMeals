<template>
  <div id="app">
    <h1>Welcome to Fit Meals</h1>
    <nav>
      <router-link v-if="isLoggedIn" to="/diary">My Diary</router-link>
      <router-link v-if="!isLoggedIn" to="/login">Login</router-link>
      <router-link v-if="!isLoggedIn" to="/register">Register</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: false, // Zmienna przechowująca stan logowania
    };
  },
  created() {
    // Sprawdzamy czy użytkownik jest zalogowany (np. przez obecność tokena w localStorage)
    this.isLoggedIn = !!localStorage.getItem("authToken");
  },
};
</script>

<style>
* {
  box-sizing: border-box;
}

html {
  font-family: sans-serif;
}

body {
  margin: 0;
}

nav {
  background-color: #f8f9fa;
  padding: 10px;
}

nav a {
  margin-right: 10px;
  text-decoration: none;
  color: #007bff;
}

nav a:hover {
  text-decoration: underline;
}
</style>
