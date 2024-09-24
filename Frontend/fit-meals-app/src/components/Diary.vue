<template>
  <div>
    <h2>Dziennik</h2>
    <!-- Wybór daty -->
    <input type="date" v-model="selectedDate" @change="fetchDiaryForDate" />

    <!-- Posiłki -->
    <MealItem 
      v-for="meal in meals" 
      :key="meal.id" 
      :meal="meal" 
      @add-product="showSearch(meal.id)" 
    />

    <!-- Modal wyszukiwania produktów -->
    <SearchFood 
      v-if="showSearchModal" 
      @products-selected="addProducts" 
      @close="closeSearchModal" 
      @cancel="closeSearchModal" 
    />
  </div>
</template>

<script>
import MealItem from './MealItem.vue';
import SearchFood from './SearchFood.vue';
import axios from 'axios';

export default {
  data() {
    return {
      selectedDate: new Date().toISOString().split('T')[0], // Aktualna data domyślnie
      showSearchModal: false,
      currentMealId: null,
      meals: [
        { id: 1, name: 'Śniadanie', products: [] },
        { id: 2, name: 'Lunch', products: [] },
        { id: 3, name: 'Obiad', products: [] },
        { id: 4, name: 'Przekąski', products: [] }
      ],
    };
  },
  methods: {
    // Pobierz dane dziennika dla wybranego dnia
    fetchDiaryForDate() {
      axios.get(`/api/diary/${this.selectedDate}`)
        .then(response => {
          const diary = response.data;
          this.meals = diary.meals;
        })
        .catch(error => {
          console.error("Błąd pobierania dziennika:", error);
        });
    },

    showSearch(mealId) {
      this.currentMealId = mealId;
      this.showSearchModal = true;
    },

    closeSearchModal() {
      this.showSearchModal = false;
      this.currentMealId = null;
    },

    addProducts(products) {
      if (this.currentMealId !== null) {
        const meal = this.meals.find(m => m.id === this.currentMealId);
        
        products.forEach(product => {
          const existingProduct = meal.products.find(p => p.id === product.id);
          
          if (existingProduct) {
            existingProduct.grams += product.grams;
            existingProduct.macros.fat += product.macros.fat * (product.grams / 100);
            existingProduct.macros.protein += product.macros.protein * (product.grams / 100);
            existingProduct.macros.carbs += product.macros.carbs * (product.grams / 100);
            existingProduct.macros.calories += product.macros.calories * (product.grams / 100);
          } else {
            meal.products.push(product);
          }
        });
        
        this.closeSearchModal();
      }
    }
  },
  mounted() {
    // Pobierz dane dziennika dla aktualnie wybranej daty po załadowaniu komponentu
    this.fetchDiaryForDate();
  },
  components: {
    MealItem,
    SearchFood
  }
};
</script>