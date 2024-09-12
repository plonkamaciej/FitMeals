<template>
  <div class="modal-overlay" @click.self="cancel">
    <div class="modal-content">
      <h2>Wyszukaj produkt</h2>
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Wyszukaj produkt..."
        @input="debouncedSearch"
      />

      <ul>
        <li v-for="product in filteredProducts" :key="product.id">
          <label>
            <input 
              type="checkbox" 
              :checked="isSelected(product)" 
              @change="toggleProductSelection(product)"
            />
            {{ product.name }}
          </label>
          <button @click="toggleProductDetails(product.id)">
            {{ product.showDetails ? 'Ukryj' : 'Pokaż' }} makroelementy
          </button>

          <div v-if="product.showDetails">
            <p>Tłuszcz: {{ product.fat }} g</p>
            <p>Białko: {{ product.protein }} g</p>
            <p>Węglowodany: {{ product.carbs }} g</p>
            <p>Kalorie: {{ product.calories }} kcal</p>
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
import debounce from 'lodash/debounce';

export default {
  data() {
    return {
      searchQuery: '',
      products: [], // Bieżąco wyszukane produkty
      filteredProducts: [], // Wyszukane produkty połączone z zaznaczonymi
      selectedProducts: [] // Lista zaznaczonych produktów
    };
  },
  methods: {
    async searchProducts(query) {
  try {
    const response = await fetch(`http://localhost:8080/api/foods/search/${query}`);
    const data = await response.json();
    
    if (data) {
      console.log("Fetched data:", data); // Loguj dane, aby sprawdzić ich strukturę

      // Dodaj oryginalne makroskładniki do referencji
      data.showDetails = false;
      data.grams = 100; // Domyślna ilość
      data.originalMacros = {
        fat: parseFloat(data.fat) || 0,
        protein: parseFloat(data.protein) || 0,
        carbs: parseFloat(data.carbs) || 0,
        calories: parseFloat(data.calories) || 0
      };

      this.products = [data];
      this.filteredProducts = this.products;
    } else {
      this.products = [];
      this.filteredProducts = [];
    }
  } catch (error) {
    console.error('Error fetching products:', error);
  }
},
    debouncedSearch: debounce(function () {
      if (this.searchQuery) {
        this.searchProducts(this.searchQuery);
      } else {
        this.products = [];
        this.filteredProducts = [...this.selectedProducts]; // Jeśli brak wyszukiwania, pokaż wybrane produkty
      }
    }, 300),
    updateMacros(product) {
     
      const grams = product.grams || 100; // Używamy domyślnej wartości 100 gramów, jeśli brak wartości
      
     if (!product.originalMacros) {
        product.originalMacros = {
          fat: parseFloat(product.fat) || 0, // Używamy parseFloat, by upewnić się, że mamy liczbę
          protein: parseFloat(product.protein) || 0,
          carbs: parseFloat(product.carbs) || 0,
          calories: parseFloat(product.calories) || 0
          };
       }

       if (grams === 0) {
        product.fat = "0.00";
        product.protein = "0.00";
        product.carbs = "0.00";
        product.calories = "0.00";
      } else {
        const ratio = grams / 100;

        product.fat = (product.originalMacros.fat * ratio).toFixed(2);
        product.protein = (product.originalMacros.protein * ratio).toFixed(2);
        product.carbs = (product.originalMacros.carbs * ratio).toFixed(2);
        product.calories = (product.originalMacros.calories * ratio).toFixed(2);
     }

    
    }


,
    toggleProductDetails(productId) {
      const product = this.filteredProducts.find(p => p.id === productId);
      if (product) {
        product.showDetails = !product.showDetails;
      }
    },
    isSelected(product) {
      return this.selectedProducts.some(p => p.id === product.id); // Sprawdzamy, czy produkt jest już zaznaczony
    },
    toggleProductSelection(product) {
      if (this.isSelected(product)) {
        // Usuń produkt, jeśli został już zaznaczony
        this.selectedProducts = this.selectedProducts.filter(p => p.id !== product.id);
      } else {
        // Dodaj produkt do listy zaznaczonych
        this.selectedProducts.push({ ...product });
      }
      this.mergeSelectedWithSearched();
    },
    mergeSelectedWithSearched() {
      // Połącz zaznaczone produkty z wyszukanymi, aby zachować zaznaczenia
      this.filteredProducts = [
        ...this.selectedProducts, // Zaznaczone produkty
        ...this.products.filter(p => !this.isSelected(p)) // Wyszukane produkty, które nie są jeszcze zaznaczone
      ];
    },
    save() {
      const addedProducts = this.selectedProducts.map(product => ({
        ...product,
        macros: {
          fat: parseFloat(product.fat),
          protein: parseFloat(product.protein),
          carbs: parseFloat(product.carbs),
          calories: parseFloat(product.calories)
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
    this.filteredProducts = [...this.selectedProducts]; // Na początku wyświetlamy tylko zaznaczone produkty
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
