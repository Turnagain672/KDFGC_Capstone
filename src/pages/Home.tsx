import '../styles/Page.css'

function Home() {
  return (
    <div className="page home-page">
      <section className="hero">
        <div className="hero-content">
          <h1>Welcome to Kelowna District Fish & Game Club</h1>
          <p className="hero-subtitle">
            Dedicated to conservation, outdoor recreation, and the responsible enjoyment of hunting and fishing
          </p>
        </div>
      </section>

      <section className="content-section">
        <h2>About Our Club</h2>
        <p>
          The Kelowna District Fish and Game Club is a community organization dedicated to promoting 
          conservation, outdoor recreation, and responsible hunting and fishing practices in the 
          Kelowna area. We welcome members of all skill levels who share our passion for the outdoors.
        </p>
      </section>

      <section className="content-section highlights">
        <h2>What We Offer</h2>
        <div className="highlights-grid">
          <div className="highlight-card">
            <h3>🎣 Fishing Programs</h3>
            <p>Learn fishing techniques, conservation practices, and access exclusive fishing locations.</p>
          </div>
          <div className="highlight-card">
            <h3>🦌 Hunting Activities</h3>
            <p>Participate in organized hunts, safety courses, and wildlife management initiatives.</p>
          </div>
          <div className="highlight-card">
            <h3>🌲 Conservation Projects</h3>
            <p>Join habitat restoration, wildlife monitoring, and environmental stewardship programs.</p>
          </div>
          <div className="highlight-card">
            <h3>👥 Community Events</h3>
            <p>Attend social gatherings, educational workshops, and family-friendly outdoor activities.</p>
          </div>
        </div>
      </section>

      <section className="content-section cta">
        <h2>Join Us Today</h2>
        <p>Become a member of the KDFGC and connect with fellow outdoor enthusiasts.</p>
        <a href="/membership" className="cta-button">Learn About Membership</a>
      </section>
    </div>
  )
}

export default Home
