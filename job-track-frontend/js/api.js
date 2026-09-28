import { USER_API, JOB_API } from './config.js';
import { getAuthHeaders } from './auth.js';

export async function loginApi(email, password) {
  const response = await fetch(`${USER_API}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const data = await response.text();
  return { ok: response.ok, data };
}

export async function signupApi(name, email, password) {
  const response = await fetch(`${USER_API}/save`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, email, password })
  });
  const data = await response.text();
  return { ok: response.ok, data };
}

export async function saveJobApi(jobData) {
  const response = await fetch(`${JOB_API}/save`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(jobData)
  });
  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }
  const errorText = await response.text();
  return { ok: false, data: errorText };
}

export async function searchJobsApi(profile) {
  const response = await fetch(`${JOB_API}/search?profile=${encodeURIComponent(profile)}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }
  const errorText = await response.text();
  return { ok: false, data: errorText };
}

export async function updateJobStatusApi(jobId, newStatus) {
  const response = await fetch(`${JOB_API}/${jobId}/status`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify({ status: newStatus })
  });
  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }
  const errorText = await response.text();
  return { ok: false, data: errorText };
}

export async function deleteJobApi(jobId) {
  const response = await fetch(`${JOB_API}/delete/${jobId}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });
  const data = await response.text();
  return { ok: response.ok, data };
}

export async function fetchJobStatsApi() {
  const response = await fetch(`${JOB_API}/stats`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }
  return { ok: false, data: null };
}
