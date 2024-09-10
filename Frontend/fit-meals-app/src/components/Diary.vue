<!-- src/components/Diary.vue -->
<template>
    <div class="diary">
      <div class="macros">
        <h2>Aktualne makroelementy</h2>
        <p>Tłuszcz: {{ totalMacros.fat }} g</p>
        <p>Białko: {{ totalMacros.protein }} g</p>
        <p>Węglowodany: {{ totalMacros.carbs }} g</p>
        <p>Kalorie: {{ totalMacros.calories }} kcal</p>
      </div>
      
      <div class="meals">
        <Meal
          v-for="meal in mealList"
          :key="meal.name"
          :mealName="meal.name"
          :macros="meal.macros"
          @add-product="openSearchFood"
        />
      </div>
  
      <!-- Modal wyszukiwania produktów -->
      <SearchFood v-if="showSearchFood" @product-selected="handleProductSelected" @cancel="closeSearchFood" />
    </div>
  </template>
  
  <script>
  import Meal from './MealItem.vue';
  import SearchFood from './SearchFood.vue';
  
  export default {
    components: {
      Meal,
      SearchFood
    },
    data() {
      return {
        totalMacros: {
          fat: 0,
          protein: 0,
          carbs: 0,
          calories: 0
        },
        mealList: [
          { name: 'Śniadanie', macros: { fat: 0, protein: 0, carbs: 0, calories: 0 } },
          { name: 'Lunch', macros: { fat: 0, protein: 0, carbs: 0, calories: 0 } },
          { name: 'Obiad', macros: { fat: 0, protein: 0, carbs: 0, calories: 0 } },
          { name: 'Przekąski', macros: { fat: 0, protein: 0, carbs: 0, calories: 0 } }
        ],
        showSearchFood: false,
        currentMealName: null
      };
    },
    methods: {
      openSearchFood(mealName) {
        console.log('Otwieranie wyszukiwarki dla posiłku:', mealName);
        this.currentMealName = mealName;
        this.showSearchFood = true;
      },
      handleProductsSelected(products) {
      products.forEach(product => {
        const meal = this.mealList.find(m => m.name === this.currentMealName);
        if (meal) {
          meal.macros.fat += product.macros.fat;
          meal.macros.protein += product.macros.protein;
          meal.macros.carbs += product.macros.carbs;
          meal.macros.calories += product.macros.calories;

          this.totalMacros.fat += product.macros.fat;
          this.totalMacros.protein += product.macros.protein;
          this.totalMacros.carbs += product.macros.carbs;
          this.totalMacros.calories += product.macros.calories;
        }
      });
      this.closeSearchFood();
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
  