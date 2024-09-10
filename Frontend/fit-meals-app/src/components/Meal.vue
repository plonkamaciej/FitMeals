<!-- src/components/Meal.vue -->
<template>
    <div class="meal">
      <h4>{{ mealName }}</h4>
      <p>Tłuszcz: {{ macros.fat }} g</p>
      <p>Białko: {{ macros.protein }} g</p>
      <p>Węglowodany: {{ macros.carbs }} g</p>
      <p>Kalorie: {{ macros.calories }} kcal</p>
      <button @click="addProduct">Dodaj produkt</button>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      mealName: String,
      macros: Object
    },
    methods: {
      addProduct() {
        this.$emit('add-product', this.mealName);
      }
    }
  };
  </script>
  
  <style scoped>
  .meal {
    background-color: #e6f9f0; /* Jasnozielony */
    border: 1px solid #b3d9b7; /* Zielony */
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 15px;
  }
  
  button {
    background-color: #003366; /* Granatowy */
    color: #ffffff;
    border: none;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #002244; /* Ciemniejszy granatowy */
  }
  </style>
  