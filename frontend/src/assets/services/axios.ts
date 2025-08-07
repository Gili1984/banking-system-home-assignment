import axios from 'axios'

export const accuontServerApi = axios.create({
  baseURL: 'http://localhost:8081/api/v1', // שנה לפי השרת שלך
})