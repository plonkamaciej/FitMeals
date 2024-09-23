<template>
  <div>
    <h2>My Diary</h2>
    <div class="meal-section" v-for="mealType in mealTypes" :key="mealType">
      <h3>{{ mealType }}</h3>
      <ul>
        <li v-for="mealItem in getMealItems(mealType)" :key="mealItem.id">
          {{ mealItem.food.name }} - {{ mealItem.quantity }}g
        </li>
      </ul>
      <button @click="openSearch(mealType)">Add product</button>
    </div>
  </div>
</template>

<script>
import SearchFood from '@/components/SearchFood.vue'; // Assuming the search component already exists

export default {
  name: 'Diary',
  data() {
    return {
      mealTypes: ['BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'],
      meals: {
        BREAKFAST: [],
        LUNCH: [],
        DINNER: [],
        SNACK: []
      }
    };
  },
  methods: {
    getMealItems(mealType) {
      // Zwraca produkty dodane do danego posiłku
      return this.meals[mealType];
    },
    openSearch(mealType) {
      // Implementacja otwierania wyszukiwarki
      this.$router.push({ path: '/search-food', query: { mealType } });
    }
  }
};
</script>

  
  <style scoped>
  .diary {
    font-family: Arial, sans-serif;
    margin: 20px;
    color: #333;
  }
  
  .macros {
    background-color: #cce5ff; /* Jasny granatowy */
    padding: 10px;
    border-radius: 8px;
  }
  
  .meals {
    display: flex;
    flex-direction: column;
  }
  </style>
  