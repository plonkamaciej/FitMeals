<template>
    <div>
      <h2>Dziennik</h2>
      <!-- Przykład użycia komponentu Posiłków -->
      <MealItem v-for="meal in meals" :key="meal.id" :meal="meal" @add-product="showSearch" />
      
      <!-- Modal wyszukiwania produktów -->
      <SearchFood v-if="showSearchModal" @products-selected="addProducts" @close="closeSearchModal" @cancel="closeSearchModal" />
    </div>
  </template>
  
  <script>
  import MealItem from './MealItem.vue';
  import SearchFood from './SearchFood.vue';
  
  export default {
    data() {
      return {
        showSearchModal: false,
        meals: [
          { id: 1, name: 'Śniadanie', products: [] },
          { id: 2, name: 'Lunch', products: [] },
          { id: 3, name: 'Obiad', products: [] },
          { id: 4, name: 'Przekąski', products: [] }
        ]
      };
    },
    methods: {
      showSearch() {
        this.showSearchModal = true;
      },
      closeSearchModal() {
        this.showSearchModal = false;
      },
      addProducts(products) {
        console.log('Produkty dodane:', products);
        this.closeSearchModal();
      }
    },
    components: {
      MealItem,
      SearchFood
    }
  };
  </script>
  