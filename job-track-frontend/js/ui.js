import { isLoggedIn } from './auth.js';

export function showMessage(element, message, type = '') {
  element.textContent = message;
  element.className = `form-message ${type}`;
}

export function updateAuthUI({
  authSection,
  loginStatus,
  loginToggleBtn,
  logoutBtn,
  addJobBtn,
  addJobHint,
  onSignedOut
}) {
  if (isLoggedIn()) {
    loginStatus.textContent = 'Signed in';
    authSection.classList.add('is-hidden');
    logoutBtn.classList.remove('is-hidden');
    if (loginToggleBtn) loginToggleBtn.classList.add('is-hidden');
    addJobBtn.disabled = false;
    addJobHint.textContent = 'Add a new application to your tracker.';
  } else {
    loginStatus.textContent = 'Not signed in';
    authSection.classList.add('is-hidden'); // Hidden by default when not logged in
    logoutBtn.classList.add('is-hidden');
    if (loginToggleBtn) loginToggleBtn.classList.remove('is-hidden');
    addJobBtn.disabled = false; // Enabled so user can attempt to add a job
    addJobHint.textContent = 'Fill in the details to add an application.';
    if (onSignedOut) onSignedOut();
  }

  const layout = authSection?.closest('.page-layout');
  if (layout) {
    layout.classList.toggle('auth-visible', !authSection.classList.contains('is-hidden'));
  }
}

export function showSigninForm({ signinBox, signupBox, signinBtn, signupBtn, signupMessage }) {
  signinBox.classList.remove('is-hidden');
  signupBox.classList.add('is-hidden');
  signinBtn.classList.add('active');
  signupBtn.classList.remove('active');
  showMessage(signupMessage, '');
}

export function showSignupForm({ signinBox, signupBox, signinBtn, signupBtn, signinMessage }) {
  signupBox.classList.remove('is-hidden');
  signinBox.classList.add('is-hidden');
  signupBtn.classList.add('active');
  signinBtn.classList.remove('active');
  showMessage(signinMessage, '');
}

export function updateStats(jobs) {
  document.getElementById('totalCount').textContent = jobs.length;
  document.getElementById('appliedCount').textContent = jobs.filter(j => j.status === 'APPLIED').length;
  document.getElementById('interviewedCount').textContent = jobs.filter(j => j.status === 'INTERVIEWED').length;
  document.getElementById('offerCount').textContent = jobs.filter(j => j.status === 'OFFER').length;
  document.getElementById('rejectedCount').textContent = jobs.filter(j => j.status === 'REJECTED').length;
}

export function renderJobs(jobs, jobsList, { onStatusChange, onDelete }) {
  jobsList.innerHTML = '';
  updateStats(jobs);

  if (!jobs || jobs.length === 0) {
    jobsList.innerHTML = '<p class="login-status">No jobs to show yet.</p>';
    return;
  }

  jobs.forEach((job) => {
    const jobCard = document.createElement('article');
    jobCard.className = 'job-card';

    const jobInfo = document.createElement('div');
    const companyName = document.createElement('h3');
    const jobMeta = document.createElement('div');

    companyName.textContent = job.companyName;
    jobMeta.className = 'job-meta';

    addMetaText(jobMeta, `Status: ${job.status}`);
    addMetaText(jobMeta, `Salary: ${job.salary}`);
    addMetaText(jobMeta, `Applied: ${job.appliedDate || 'Not added'}`);

    jobInfo.append(companyName, jobMeta);

    const jobActions = document.createElement('div');
    jobActions.className = 'job-actions';

    const statusSelect = document.createElement('select');
    statusSelect.dataset.jobId = job.id;
    addStatusOptions(statusSelect);
    statusSelect.value = job.status;
    statusSelect.addEventListener('change', (e) => onStatusChange(job.id, e.target.value));

    const deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.className = 'delete-button';
    deleteButton.dataset.jobId = job.id;
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', () => onDelete(job.id));

    jobActions.append(statusSelect, deleteButton);
    jobCard.append(jobInfo, jobActions);
    jobsList.appendChild(jobCard);
  });
}

function addMetaText(parent, text) {
  const item = document.createElement('span');
  item.textContent = text;
  parent.appendChild(item);
}

function addStatusOptions(selectElement) {
  const statuses = [
    ['APPLIED', 'Applied'],
    ['INTERVIEWED', 'Interviewed'],
    ['OFFER', 'Offer'],
    ['REJECTED', 'Rejected'],
    ['NO_RESPONSE', 'No Response']
  ];

  statuses.forEach(([val, label]) => {
    const option = document.createElement('option');
    option.value = val;
    option.textContent = label;
    selectElement.appendChild(option);
  });
}
