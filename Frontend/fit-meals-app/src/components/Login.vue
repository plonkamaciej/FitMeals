<template>
    <div class="login-page">
      <h2>Zaloguj się</h2>
      <form @submit.prevent="login">
        <div>
          <label for="username">Nazwa użytkownika:</label>
          <input type="text" v-model="username" required />
        </div>
        <div>
          <label for="password">Hasło:</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit">Zaloguj</button>
      </form>
      <p>Nie masz konta? <router-link to="/register">Zarejestruj się</router-link></p>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: '',
        password: '',
      };
    },
    methods: {
      login() {
        // Wyślij żądanie logowania na backend
        console.log('Logowanie...', this.username, this.password);
      },
    },
  };
  </script>
  
  <style scoped>
  .login-page {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
  }
  
  button {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  </style>
  