<template>
    <div class="modal-overlay" @click.self="cancel">
      <div class="modal-content">
        <h2>Wyszukaj produkt</h2>
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Wyszukaj produkt..."
          @input="filterProducts"
        />
  
        <ul>
          <li
            v-for="product in filteredProducts"
            :key="product.id"
          >
            <label>
              <input type="checkbox" v-model="selectedProducts" :value="product" />
              {{ product.name }}
            </label>
            <button @click="toggleProductDetails(product.id)">
              {{ product.showDetails ? 'Ukryj' : 'Pokaż' }} makroelementy
            </button>
            
            <div v-if="product.showDetails">
              <p>Tłuszcz: {{ product.macros.fat }} g</p>
              <p>Białko: {{ product.macros.protein }} g</p>
              <p>Węglowodany: {{ product.macros.carbs }} g</p>
              <p>Kalorie: {{ product.macros.calories }} kcal</p>
              <label>Ilość (w gramach):</label>
              <input type="number" v-model.number="product.grams" @input="updateMacros(product)" />
            </div>
          </li>
        </ul>
  
        <button @click="save">Zapisz</button>
        <button @click="cancel">Anuluj</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: '',
        products: [
          { id: 1, name: 'Jabłko', macros: { fat: 0.2, protein: 0.3, carbs: 14, calories: 52 }, grams: 100, showDetails: false },
          { id: 2, name: 'Banan', macros: { fat: 0.3, protein: 1.1, carbs: 23, calories: 89 }, grams: 100, showDetails: false },
          { id: 3, name: 'Kurczak', macros: { fat: 3.6, protein: 31, carbs: 0, calories: 165 }, grams: 100, showDetails: false },
          { id: 4, name: 'Ryż', macros: { fat: 0.3, protein: 2.7, carbs: 28, calories: 130 }, grams: 100, showDetails: false }
        ],
        filteredProducts: [],
        selectedProducts: []
      };
    },
    methods: {
      filterProducts() {
        const query = this.searchQuery.toLowerCase();
        this.filteredProducts = this.products.filter(product =>
          product.name.toLowerCase().includes(query)
        );
      },
      updateMacros(product) {
        const grams = product.grams || 100; // Domyślna wartość to 100 gramów
        const ratio = grams / 100;
  
        product.macros.fat = (product.originalMacros.fat * ratio).toFixed(2);
        product.macros.protein = (product.originalMacros.protein * ratio).toFixed(2);
        product.macros.carbs = (product.originalMacros.carbs * ratio).toFixed(2);
        product.macros.calories = (product.originalMacros.calories * ratio).toFixed(2);
      },
      toggleProductDetails(productId) {
        const product = this.products.find(p => p.id === productId);
        product.showDetails = !product.showDetails;
      },
      save() {
        const addedProducts = this.selectedProducts.map(product => ({
          ...product,
          macros: {
            fat: parseFloat(product.macros.fat),
            protein: parseFloat(product.macros.protein),
            carbs: parseFloat(product.macros.carbs),
            calories: parseFloat(product.macros.calories)
          },
          grams: product.grams
        }));
        this.$emit('products-selected', addedProducts); // Emitujemy dane produktów
        this.cancel(); // Wywołujemy metodę anulowania
      },
      cancel() {
        this.$emit('cancel'); // Emitujemy event anulowania
      }
    },
    mounted() {
      this.filteredProducts = this.products;
      this.products.forEach(product => {
        product.originalMacros = { ...product.macros };
      });
    }
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); /* Ciemny overlay */
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
    max-width: 100%;
  }
  
  h2 {
    margin-top: 0;
  }
  
  input[type="text"] {
    width: 100%;
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #003366; /* Granatowy */
    border-radius: 4px;
  }
  
  ul {
    list-style-type: none;
    padding: 0;
  }
  
  li {
    cursor: pointer;
    padding: 8px;
    border-bottom: 1px solid #b3d9b7; /* Zielony */
  }
  
  li:hover {
    background-color: #e6f9f0; /* Jasnozielony */
  }
  
  input[type="number"] {
    margin-top: 5px;
    width: 100%;
    padding: 5px;
  }
  
  button {
    background-color: #003366; /* Granatowy */
    color: #ffffff;
    border: none;
    border-radius: 10px;
    padding: 10px 15px;
    cursor: pointer;
    margin-top: 10px;
    margin-left: 10px;
  }
  
  button:hover {
    background-color: #002244; /* Ciemniejszy granatowy */
  }
  </style>
  