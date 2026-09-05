document.addEventListener('DOMContentLoaded', () => {
  const API_BASE_URL = 'http://localhost:7070/api/v1/user';

  const showSigninBtn = document.getElementById('showSigninBtn');
  const showSignupBtn = document.getElementById('showSignupBtn');
  const signinSection = document.getElementById('signinSection');
  const signupSection = document.getElementById('signupSection');
  const signinForm = document.getElementById('signinForm');
  const signupForm = document.getElementById('signupForm');
  const signinMessage = document.getElementById('signinMessage');
  const signupMessage = document.getElementById('signupMessage');

  if (showSigninBtn && showSignupBtn && signinSection && signupSection) {
    let isAnimating = false;

    function switchTab(hideSection, showSection, activeBtn, inactiveBtn) {
      if (hideSection.classList.contains('is-hidden') || isAnimating) return;
      isAnimating = true;

      activeBtn.classList.add('active');
      inactiveBtn.classList.remove('active');
      hideSection.classList.add('fade-out');

      setTimeout(() => {
        hideSection.classList.add('is-hidden');
        hideSection.classList.remove('fade-out');

        showSection.classList.remove('is-hidden');
        showSection.classList.add('fade-in');

        setTimeout(() => {
          showSection.classList.remove('fade-in');
          isAnimating = false;
        }, 350);

      }, 200);
    }
    showSignupBtn.addEventListener('click', () => {
      switchTab(signinSection, signupSection, showSignupBtn, showSigninBtn);
      clearMessage(signinMessage);
    });

    showSigninBtn.addEventListener('click', () => {
      switchTab(signupSection, signinSection, showSigninBtn, showSignupBtn);
      clearMessage(signupMessage);
    });
  }

  function showMessage(element, message, type) {
    if (!element) return;
    element.textContent = message;
    element.className = `form-message ${type}`;
  }

  function clearMessage(element) {
    if (!element) return;
    element.textContent = '';
    element.className = 'form-message';
  }

  async function parseResponse(response) {
    const contentType = response.headers.get('content-type') || '';

    if (contentType.includes('application/json')) {
      return response.json();
    }

    return response.text();
  }

  function getErrorMessage(data, fallback) {
    if (data && typeof data === 'object') {
      return data.message || data.error || fallback;
    }

    return data || fallback;
  }

  if (signinForm) {
    signinForm.addEventListener('submit', async (event) => {
      event.preventDefault();
      clearMessage(signinMessage);

      const submitButton = signinForm.querySelector('button[type="submit"]');
      const formData = new FormData(signinForm);
      const rememberMe = formData.get('rememberMe') === 'on';
      const payload = {
        email: formData.get('email').trim(),
        password: formData.get('password')
      };

      submitButton.disabled = true;
      submitButton.textContent = 'Signing in...';

      try {
        const response = await fetch(`${API_BASE_URL}/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        });
        const data = await parseResponse(response);

        if (!response.ok) {
          throw new Error(getErrorMessage(data, 'Unable to sign in. Please check your credentials.'));
        }

        const storage = rememberMe ? localStorage : sessionStorage;
        storage.setItem('jobTrackToken', data);
        showMessage(signinMessage, 'Signed in successfully.', 'success');
      } catch (error) {
        showMessage(signinMessage, error.message, 'error');
      } finally {
        submitButton.disabled = false;
        submitButton.textContent = 'Sign In';
      }
    });
  }

  if (signupForm) {
    signupForm.addEventListener('submit', async (event) => {
      event.preventDefault();
      clearMessage(signupMessage);

      const submitButton = signupForm.querySelector('button[type="submit"]');
      const formData = new FormData(signupForm);
      const password = formData.get('password');
      const confirmPassword = formData.get('confirmPassword');

      if (password !== confirmPassword) {
        showMessage(signupMessage, 'Passwords do not match.', 'error');
        return;
      }

      const payload = {
        name: formData.get('name').trim(),
        email: formData.get('email').trim(),
        password
      };

      submitButton.disabled = true;
      submitButton.textContent = 'Creating account...';

      try {
        const response = await fetch(`${API_BASE_URL}/save`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        });
        const data = await parseResponse(response);

        if (!response.ok) {
          throw new Error(getErrorMessage(data, 'Unable to create account.'));
        }

        signupForm.reset();
        showMessage(signupMessage, 'Account created. Please verify your email before signing in.', 'success');
      } catch (error) {
        showMessage(signupMessage, error.message, 'error');
      } finally {
        submitButton.disabled = false;
        submitButton.textContent = 'Create Account';
      }
    });
  }
});
