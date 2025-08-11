import axios from 'axios'

export const accuontServerApi = axios.create({
  baseURL: 'http://localhost:8081/api/v1', // שנה לפי השרת שלך
})

export const transactionServerApi = axios.create({
  baseURL: 'http://localhost:8082/api/v2/transactions', // שנה לפי השרת שלך
})