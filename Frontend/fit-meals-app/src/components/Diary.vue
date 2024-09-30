<template>
  <div>
    <h2>Dziennik</h2>
    
    <!-- Wybór daty -->
    <label for="datePicker">Wybierz datę:</label>
    <input type="date" v-model="selectedDate" @change="fetchDiary"/>

    <!-- Przykład użycia komponentu Posiłków -->
    <div v-for="meal in meals" :key="meal.id">
      <h3>{{ meal.name }}</h3>
      <MealItem
        :meal="meal"
        @add-product="showSearch(meal.id)"
      />
    </div>

    <!-- Modal wyszukiwania produktów -->
    <SearchFood 
      v-if="showSearchModal" 
      @products-selected="addProducts" 
      @close="closeSearchModal" 
    />

    <!-- Przycisk do zapisywania dziennika -->
    <button @click="saveDiary">Zapisz dziennik</button>
  </div>
</template>

<script>
import MealItem from './MealItem.vue';
import SearchFood from './SearchFood.vue';
import axios from 'axios';

export default {
  data() {
    return {
      selectedDate: new Date().toISOString().slice(0, 10), // Domyślna data (dzisiejsza)
      showSearchModal: false,
      currentMealId: null,
      meals: [
        { id: 1, name: 'Śniadanie', products: [] },
        { id: 2, name: 'Lunch', products: [] },
        { id: 3, name: 'Obiad', products: [] },
        { id: 4, name: 'Przekąski', products: [] }
      ]
    };
  },
  methods: {
    // Pobieranie dziennika na wybrany dzień
    fetchDiary() {
      const userId = localStorage.getItem('userId');
      axios.get(`/api/diary/${userId}/${this.selectedDate}`)
        .then(response => {
          const diary = response.data;
          if (diary) {
            this.meals = [
              { id: 1, name: 'Śniadanie', products: diary.breakfast?.products || [] },
              { id: 2, name: 'Lunch', products: diary.lunch?.products || [] },
              { id: 3, name: 'Obiad', products: diary.dinner?.products || [] },
              { id: 4, name: 'Przekąski', products: diary.snack?.products || [] }
            ];
          }
        })
        .catch(error => {
          console.error('Błąd podczas pobierania dziennika:', error);
        });
    },

    // Pokazanie modalu wyszukiwania produktów
    showSearch(mealId) {
      this.currentMealId = mealId;
      this.showSearchModal = true;
    },

    // Zamknięcie modalu
    closeSearchModal() {
      this.showSearchModal = false;
      this.currentMealId = null;
    },

    // Dodanie produktów do posiłku
    addProducts(products) {
      if (this.currentMealId !== null) {
        const meal = this.meals.find(m => m.id === this.currentMealId);
        meal.products.push(...products);
        this.closeSearchModal();
      }
    },

    // Zapisz dziennik do bazy danych
    saveDiary() {
      const userId = localStorage.getItem('userId');
      axios.post(`/api/diary/${userId}/${this.selectedDate}`, {
        breakfast: this.meals.find(meal => meal.name === 'Śniadanie'),
        lunch: this.meals.find(meal => meal.name === 'Lunch'),
        dinner: this.meals.find(meal => meal.name === 'Obiad'),
        snack: this.meals.find(meal => meal.name === 'Przekąski'),
        totalCalories: this.calculateTotalCalories(),
        totalProtein: this.calculateTotalProtein(),
        totalFat: this.calculateTotalFat(),
        totalCarbs: this.calculateTotalCarbs()
      })
      .then(() => {
        alert('Dziennik zapisany!');
      })
      .catch(error => {
        console.error('Błąd podczas zapisywania dziennika:', error);
      });
    },

    // Obliczanie sum makroskładników
    calculateTotalCalories() {
      return this.meals.reduce((sum, meal) => sum + meal.products.reduce((pSum, p) => pSum + p.macros.calories, 0), 0);
    },
    calculateTotalProtein() {
      return this.meals.reduce((sum, meal) => sum + meal.products.reduce((pSum, p) => pSum + p.macros.protein, 0), 0);
    },
    calculateTotalFat() {
      return this.meals.reduce((sum, meal) => sum + meal.products.reduce((pSum, p) => pSum + p.macros.fat, 0), 0);
    },
    calculateTotalCarbs() {
      return this.meals.reduce((sum, meal) => sum + meal.products.reduce((pSum, p) => pSum + p.macros.carbs, 0), 0);
    }
  },
  components: {
    MealItem,
    SearchFood
  },
  mounted() {
    // Pobierz dziennik na domyślną datę po zalogowaniu
    this.fetchDiary();
  }
};
</script>


<style scoped>
button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  font-size: 16px;
  border-radius: 5px;
  margin-top: 20px;
}

button:hover {
  background-color: #218838;
}
</style>
