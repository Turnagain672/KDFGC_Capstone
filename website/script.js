// Contact Form Validation and Submission
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('contact-form');
    const status = document.getElementById('form-status');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const name = document.getElementById('name').value.trim();
        const email = document.getElementById('email').value.trim();
        const message = document.getElementById('message').value.trim();

        // Validation
        if (name === '') {
            showStatus('Please enter your name', 'error');
            return;
        }

        if (name.length < 2) {
            showStatus('Name must be at least 2 characters', 'error');
            return;
        }

        if (email === '') {
            showStatus('Please enter your email', 'error');
            return;
        }

        if (!isValidEmail(email)) {
            showStatus('Please enter a valid email address', 'error');
            return;
        }

        if (message === '') {
            showStatus('Please enter a message', 'error');
            return;
        }

        if (message.length < 10) {
            showStatus('Message must be at least 10 characters', 'error');
            return;
        }

        // Success - form is valid
        showStatus('Message sent successfully! Thank you for reaching out.', 'success');
        form.reset();
    });

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function showStatus(message, type) {
        status.textContent = message;
        status.className = type;
        
        // Clear status after 5 seconds
        setTimeout(function() {
            status.textContent = '';
            status.className = '';
        }, 5000);
    }

    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});
