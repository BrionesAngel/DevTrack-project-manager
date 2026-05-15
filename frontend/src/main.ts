import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { QueryClient, VueQueryPlugin } from '@tanstack/vue-query'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()
const queryClient = new QueryClient()


app.use(pinia)
app.use(VueQueryPlugin, { queryClient })
app.use(router)

app.mount('#app')
