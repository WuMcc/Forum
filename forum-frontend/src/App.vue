<script setup>
import { useDark, useToggle } from '@vueuse/core'
import Interact from "@/components/Interact.vue";

useDark({
  selector: 'html',
  attribute: 'class',
  valueDark: 'dark',
  valueLight: 'light'
})

useDark({
  onChanged(dark) { useToggle(dark) }
})

</script>

<template>
  <header>
    <div class="wrapper">
      <router-view/>
    </div>
  </header>

</template>

<style scoped>
header {
  line-height: 1.5;
}
</style>
