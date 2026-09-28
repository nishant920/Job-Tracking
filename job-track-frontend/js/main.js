import { isLoggedIn, setToken, clearToken } from './auth.js';
import {
  loginApi,
  signupApi,
  saveJobApi,
  searchJobsApi,
  updateJobStatusApi,
  deleteJobApi
} from './api.js';
import {
  showMessage,
  updateAuthUI,
  showSigninForm,
  showSignupForm,
  renderJobs
} from './ui.js';

document.addEventListener('DOMContentLoaded', () => {
  let jobs = [];

  // DOM Elements
  const authSection = document.getElementById('authSection');
  const loginStatus = document.getElementById('loginStatus');
  const logoutBtn = document.getElementById('logoutBtn');

  const signinBtn = document.getElementById('showSigninBtn');
  const signupBtn = document.getElementById('showSignupBtn');
  const signinBox = document.getElementById('signinSection');
  const signupBox = document.getElementById('signupSection');

  const signinForm = document.getElementById('signinForm');
  const signupForm = document.getElementById('signupForm');
  const jobForm = document.getElementById('jobForm');
  const searchForm = document.getElementById('searchForm');

  const signinMessage = document.getElementById('signinMessage');
  const signupMessage = document.getElementById('signupMessage');
  const jobMessage = document.getElementById('jobMessage');
  const searchMessage = document.getElementById('searchMessage');

  const addJobBtn = document.getElementById('addJobBtn');
  const addJobHint = document.getElementById('addJobHint');
  const jobsList = document.getElementById('jobsList');

  // Helper to re-render jobs with active callbacks
  function refreshJobList() {
    renderJobs(jobs, jobsList, {
      onStatusChange: handleStatusChange,
      onDelete: handleDeleteJob
    });
  }

  function handleAuthUI() {
    updateAuthUI({
      authSection,
      loginStatus,
      logoutBtn,
      addJobBtn,
      addJobHint,
      onSignedOut: () => {
        jobs = [];
        refreshJobList();
      }
    });
  }

  // Toggle Forms
  signinBtn.addEventListener('click', () =>
    showSigninForm({ signinBox, signupBox, signinBtn, signupBtn, signupMessage })
  );
  signupBtn.addEventListener('click', () =>
    showSignupForm({ signinBox, signupBox, signinBtn, signupBtn, signinMessage })
  );

  // Sign In Handler
  signinForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('signinEmail').value;
    const password = document.getElementById('signinPassword').value;
    const rememberMe = document.getElementById('rememberMe').checked;

    try {
      const { ok, data } = await loginApi(email, password);
      if (ok) {
        setToken(data, rememberMe);
        signinForm.reset();
        showMessage(signinMessage, 'Signed in successfully.', 'success');
        handleAuthUI();
      } else {
        showMessage(signinMessage, data || 'Login failed.', 'error');
      }
    } catch {
      showMessage(signinMessage, 'Could not connect to backend.', 'error');
    }
  });

  // Sign Up Handler
  signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('signupName').value;
    const email = document.getElementById('signupEmail').value;
    const password = document.getElementById('signupPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
      showMessage(signupMessage, 'Passwords do not match.', 'error');
      return;
    }

    try {
      const { ok, data } = await signupApi(name, email, password);
      if (ok) {
        signupForm.reset();
        showMessage(signupMessage, 'Account created. Please verify your email.', 'success');
      } else {
        showMessage(signupMessage, data || 'Signup failed.', 'error');
      }
    } catch {
      showMessage(signupMessage, 'Could not connect to backend.', 'error');
    }
  });

  // Logout Handler
  logoutBtn.addEventListener('click', () => {
    clearToken();
    showMessage(jobMessage, '');
    showMessage(searchMessage, '');
    handleAuthUI();
  });

  // Add Job Handler
  jobForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!isLoggedIn()) {
      showMessage(jobMessage, 'Please sign in before adding a job.', 'error');
      return;
    }

    const skillsText = document.getElementById('skillSet').value;
    const skills = skillsText.split(',').map(s => s.trim()).filter(s => s !== '');

    const jobData = {
      profile: document.getElementById('profile').value,
      company: {
        name: document.getElementById('companyName').value,
        location: document.getElementById('location').value
      },
      salary: Number(document.getElementById('salary').value),
      status: document.getElementById('status').value,
      experience: Number(document.getElementById('experience').value || 0),
      skillSet: skills,
      platform: document.getElementById('platform').value,
      appliedDate: document.getElementById('appliedDate').value || null
    };

    try {
      const { ok, data } = await saveJobApi(jobData);
      if (ok) {
        jobs.unshift(data);
        jobForm.reset();
        refreshJobList();
        showMessage(jobMessage, 'Job added successfully.', 'success');
      } else {
        showMessage(jobMessage, data || 'Could not add job.', 'error');
      }
    } catch {
      showMessage(jobMessage, 'Could not connect to backend.', 'error');
    }
  });

  // Search Jobs Handler
  searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!isLoggedIn()) {
      showMessage(searchMessage, 'Please sign in before searching jobs.', 'error');
      return;
    }

    const profile = document.getElementById('searchProfile').value;

    try {
      const { ok, data } = await searchJobsApi(profile);
      if (ok) {
        jobs = data;
        refreshJobList();
        showMessage(searchMessage, `${jobs.length} job(s) found.`, 'success');
      } else {
        showMessage(searchMessage, data || 'Search failed.', 'error');
      }
    } catch {
      showMessage(searchMessage, 'Could not connect to backend.', 'error');
    }
  });

  // Update Status Callback
  async function handleStatusChange(jobId, newStatus) {
    try {
      const { ok, data } = await updateJobStatusApi(jobId, newStatus);
      if (ok) {
        jobs = jobs.map(j => (j.id == jobId ? data : j));
        refreshJobList();
        showMessage(searchMessage, 'Status updated.', 'success');
      } else {
        showMessage(searchMessage, data || 'Could not update status.', 'error');
      }
    } catch {
      showMessage(searchMessage, 'Could not connect to backend.', 'error');
    }
  }

  // Delete Job Callback
  async function handleDeleteJob(jobId) {
    try {
      const { ok, data } = await deleteJobApi(jobId);
      if (ok) {
        jobs = jobs.filter(j => j.id != jobId);
        refreshJobList();
        showMessage(searchMessage, 'Job deleted.', 'success');
      } else {
        showMessage(searchMessage, data || 'Could not delete job.', 'error');
      }
    } catch {
      showMessage(searchMessage, 'Could not connect to backend.', 'error');
    }
  }

  // Initialize Page on load
  handleAuthUI();
});
