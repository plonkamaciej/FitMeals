<template>
  <div>
    <h2>Dziennik</h2>
    <!-- Przykład użycia komponentu Posiłków -->
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

export default {
  data() {
    return {
      showSearchModal: false,
      currentMealId: null, // Identyfikator aktualnie wybranego posiłku
      meals: [
        { id: 1, name: 'Śniadanie', products: [] },
        { id: 2, name: 'Lunch', products: [] },
        { id: 3, name: 'Obiad', products: [] },
        { id: 4, name: 'Przekąski', products: [] }
      ]
    };
  },
  methods: {
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
            // Jeśli produkt już istnieje, dodaj makroskładniki
            existingProduct.grams += product.grams;
            existingProduct.macros.fat += product.macros.fat * (product.grams / 100);
            existingProduct.macros.protein += product.macros.protein * (product.grams / 100);
            existingProduct.macros.carbs += product.macros.carbs * (product.grams / 100);
            existingProduct.macros.calories += product.macros.calories * (product.grams / 100);
          } else {
            // Jeśli produkt nie istnieje, dodaj go do posiłku
            meal.products.push(product);
          }
        });
        
        // Możesz również zaktualizować makroskładniki posiłku
        this.closeSearchModal();
      }
    }
  },
  components: {
    MealItem,
    SearchFood
  }
};
</script>
