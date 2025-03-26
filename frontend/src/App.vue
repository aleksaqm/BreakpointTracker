<script setup>
import { ref } from "vue";

const breakpoints = ref([]);

window.updateBreakpoints = (newBreakpoints) => {
  console.log("Updating breakpoints:", newBreakpoints);
  breakpoints.value = newBreakpoints;
};
</script>

<template>
  <div>
    <h1>Breakpoints</h1>
    <ul>
      <li v-for="bp in breakpoints" :key="bp.file + bp.line">
        {{ bp.file }}: Line {{ bp.line }}
      </li>
    </ul>
  </div>
</template>
