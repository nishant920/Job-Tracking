// Automatically switch between localhost (local dev) and Render (live)
const BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:7070'
  : 'https://job-tracking-omar.onrender.com'; // <-- Replace with your Render URL
export const USER_API = `${BASE_URL}/api/v1/user`;
export const JOB_API = `${BASE_URL}/api/v1/job`;
export const AI_API = `${BASE_URL}/api/v1/ai/generate`;