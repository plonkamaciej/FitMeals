<template>
  <div id="app">
    <Journal />
  </div>
</template>



<script>

import Journal from './components/Journal.vue';

export default {
  name: 'App',
  components: {
   Journal
  }  
}
</script>

<style>
* {
  box-sizing: border-box;
}

html {
  font-family: sans-serif;
}

body {
  margin: 0;
}
</style>