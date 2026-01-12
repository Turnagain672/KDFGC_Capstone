import '../styles/Footer.css'

function Footer() {
  const currentYear = new Date().getFullYear()

  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3>Kelowna District Fish & Game Club</h3>
          <p>Promoting conservation and outdoor recreation since establishment.</p>
        </div>
        <div className="footer-section">
          <h4>Quick Links</h4>
          <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/about">About Us</a></li>
            <li><a href="/membership">Membership</a></li>
            <li><a href="/events">Events</a></li>
          </ul>
        </div>
        <div className="footer-section">
          <h4>Contact</h4>
          <p>Email: info@kdfgc.ca</p>
          <p>Phone: (250) XXX-XXXX</p>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; {currentYear} Kelowna District Fish and Game Club. All rights reserved.</p>
      </div>
    </footer>
  )
}

export default Footer
