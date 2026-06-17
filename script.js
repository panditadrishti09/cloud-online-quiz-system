// public/script.js
// Shared utility functions (used across pages if needed)

/**
 * Format seconds into MM:SS
 */
function formatTime(seconds) {
  const m = String(Math.floor(seconds / 60)).padStart(2, '0');
  const s = String(seconds % 60).padStart(2, '0');
  return `${m}:${s}`;
}

/**
 * API helper
 */
async function apiPost(endpoint, body) {
  const res = await fetch(endpoint, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  return res.json();
}

/**
 * Cloud Computing Note:
 * SaaS  → this frontend delivered via browser (no install needed)
 * PaaS  → Node.js/Express on Heroku or EC2 with Node installed
 * IaaS  → Raw AWS EC2 instance running the whole stack
 */
