<template>
    <div class="register-page">
      <h2>Zarejestruj się</h2>
      <form @submit.prevent="register">
        <div>
          <label for="username">Nazwa użytkownika:</label>
          <input type="text" v-model="username" required />
        </div>
        <div>
          <label for="email">Email:</label>
          <input type="email" v-model="email" required />
        </div>
        <div>
          <label for="password">Hasło:</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit">Zarejestruj</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: '',
        email: '',
        password: '',
      };
    },
    methods: {
      register() {
        // Wyślij żądanie rejestracji na backend
        console.log('Rejestracja...', this.username, this.email, this.password);
      },
    },
  };
  </script>
  
  <style scoped>
  .register-page {
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
  