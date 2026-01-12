import { useState } from 'react'
import '../styles/Page.css'

function Contact() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: ''
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // TODO: Implement form submission logic
    alert('Thank you for your message! We will get back to you soon.')
    setFormData({ name: '', email: '', subject: '', message: '' })
  }

  return (
    <div className="page contact-page">
      <h1>Contact Us</h1>
      
      <section className="content-section">
        <h2>Get in Touch</h2>
        <p>
          We'd love to hear from you! Whether you have questions about membership, 
          upcoming events, or general inquiries, please don't hesitate to reach out.
        </p>
      </section>

      <section className="content-section">
        <div className="contact-info">
          <div className="contact-method">
            <h3>📧 Email</h3>
            <p><a href="mailto:info@kdfgc.ca">info@kdfgc.ca</a></p>
          </div>

          <div className="contact-method">
            <h3>📞 Phone</h3>
            <p>(250) XXX-XXXX</p>
          </div>

          <div className="contact-method">
            <h3>📍 Address</h3>
            <p>
              Kelowna District Fish & Game Club<br />
              [Address Line 1]<br />
              Kelowna, BC [Postal Code]
            </p>
          </div>
        </div>
      </section>

      <section className="content-section">
        <h2>Send Us a Message</h2>
        <form className="contact-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="subject">Subject *</label>
            <select
              id="subject"
              name="subject"
              value={formData.subject}
              onChange={handleChange}
              required
            >
              <option value="">Select a subject</option>
              <option value="membership">Membership Inquiry</option>
              <option value="events">Events Information</option>
              <option value="general">General Question</option>
              <option value="other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="message">Message *</label>
            <textarea
              id="message"
              name="message"
              value={formData.message}
              onChange={handleChange}
              rows={6}
              required
            />
          </div>

          <button type="submit" className="submit-button">Send Message</button>
        </form>
      </section>
    </div>
  )
}

export default Contact
