<template>
  <div class="meal-item">
    <h3>{{ meal.name }}</h3>
    
    <!-- Wyświetlanie aktualnych makroskładników -->
    <p>Tłuszcz: {{ totalMacros.fat }} g</p>
    <p>Białko: {{ totalMacros.protein }} g</p>
    <p>Węglowodany: {{ totalMacros.carbs }} g</p>
    <p>Kalorie: {{ totalMacros.calories }} kcal</p>
    
    <!-- Lista produktów -->
    <ul>
      <li v-for="product in meal.products" :key="product.id">
        {{ product.name }} - {{ product.grams }}g
      </li>
    </ul>
    
    <!-- Przycisk dodawania produktów -->
    <button @click="$emit('add-product')">Dodaj produkt</button>
  </div>
</template>

<script>
export default {
  props: {
    meal: Object // Oczekujemy obiektu posiłku
  },
  computed: {
    // Obliczanie sumy makroskładników dla posiłku
    totalMacros() {
      return this.meal.products.reduce(
        (totals, product) => {
          totals.fat += product.fat * (product.grams / 100);
          totals.protein += product.protein * (product.grams / 100);
          totals.carbs += product.carbs * (product.grams / 100);
          totals.calories += product.calories * (product.grams / 100);
          return totals;
        },
        { fat: 0, protein: 0, carbs: 0, calories: 0 }
      );
    }
  }
};
</script>

<style scoped>
.meal-item {
  border: 1px solid #b3d9b7; /* Zielony */
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 8px;
}

button {
  background-color: #003366; /* Granatowy */
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px;
  cursor: pointer;
}

button:hover {
  background-color: #002244; /* Ciemniejszy granatowy */
}
</style>
