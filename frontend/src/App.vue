<script setup>
import { ref, onMounted, computed } from "vue";

const breakpoints = ref([]);

onMounted(() => {
  window.updateBreakpoints = (newBreakpoints) => {
    breakpoints.value = newBreakpoints;
  };
});

const groupedBreakpoints = computed(() => {
  const grouped = {};
  for (const { file, line } of breakpoints.value) {
    if (!grouped[file]) grouped[file] = [];
    grouped[file].push(line);
  }
  return Object.entries(grouped).map(([file, lines]) => ({
    file,
    lines: lines.sort((a, b) => a - b),
  }));
});
</script>

<template>
  <div class="container">
    <h3 class="title">Track your breakpoints</h3>
    <p class="count">Number of breakpoints: {{ breakpoints.length }}</p>
    <ul class="breakpoints-list">
      <li v-for="bp in groupedBreakpoints" :key="bp.file" class="breakpoint-item">
        <strong class="file-name">{{ bp.file }}</strong>
        <span class="lines">lines: {{ bp.lines.join(", ") }}</span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  max-width: 600px;
  margin: auto auto auto 20px;
  padding: 20px;
  text-align: left;

  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
}

.title {
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--highlight-color);
}

.count {
  font-size: 1rem;
  margin-bottom: 15px;
}

.breakpoints-list {
  list-style: none;
  padding: 0;
}

.breakpoint-item {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px;
  border-bottom: 1px solid var(--border-color);
}

.file-name {
  font-weight: bold;
  color: var(--highlight-color);
  display: inline-block;
  max-width: 100%;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.lines {
  font-size: 0.9rem;
  color: var(--secondary-text-color);
}
</style>