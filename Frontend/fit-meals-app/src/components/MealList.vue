<template>
  <div class="meal-list">
    <h1>Meal List</h1>
    <ul>
      <li v-for="meal in meals" :key="meal.id">
        <div class="meal-info">
          <h3>{{ meal.name }}</h3>
          <button @click="viewMeal(meal.id)">View Details</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "MealList",
  data() {
    return {
      meals: [
        { id: 1, name: "Breakfast", details: "Details about breakfast" },
        { id: 2, name: "Lunch", details: "Details about lunch" },
        { id: 3, name: "Dinner", details: "Details about dinner" },
        { id: 4, name: "Snacks", details: "Details about snacks" }
      ]
    };
  },
  methods: {
    viewMeal(mealId) {
      // Logika do przeglądania szczegółów posiłku lub przekierowanie do innej strony
      alert(`View details for meal ID: ${mealId}`);
    }
  }
};
</script>

<style scoped>
.meal-list {
  font-family: Arial, sans-serif;
  margin: 20px;
}

.meal-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
</style>
