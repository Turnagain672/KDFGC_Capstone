import '../styles/Page.css'

function Events() {
  return (
    <div className="page events-page">
      <h1>Events & Activities</h1>
      
      <section className="content-section">
        <h2>Upcoming Events</h2>
        <p>
          Stay tuned for information about upcoming events, workshops, and activities. 
          Check back regularly for updates, or subscribe to our newsletter to receive 
          the latest news directly in your inbox.
        </p>
      </section>

      <section className="content-section">
        <h2>Regular Activities</h2>
        
        <div className="events-list">
          <div className="event-card">
            <h3>Monthly Club Meetings</h3>
            <p className="event-time">First Tuesday of each month, 7:00 PM</p>
            <p>
              Join us for our regular club meetings featuring guest speakers, 
              conservation updates, and planning for upcoming activities.
            </p>
          </div>

          <div className="event-card">
            <h3>Youth Fishing Days</h3>
            <p className="event-time">Various dates throughout spring and summer</p>
            <p>
              Introduce young anglers to the sport of fishing with hands-on instruction 
              and supervised fishing opportunities.
            </p>
          </div>

          <div className="event-card">
            <h3>Hunter Safety Courses</h3>
            <p className="event-time">Offered throughout the year</p>
            <p>
              Comprehensive safety training for new and experienced hunters, 
              including certification programs recognized by provincial authorities.
            </p>
          </div>

          <div className="event-card">
            <h3>Conservation Projects</h3>
            <p className="event-time">Seasonal activities</p>
            <p>
              Participate in habitat restoration, wildlife monitoring, and other 
              hands-on conservation initiatives throughout the year.
            </p>
          </div>

          <div className="event-card">
            <h3>Annual Banquet</h3>
            <p className="event-time">Held annually in the fall</p>
            <p>
              Our premier social event celebrating another successful year, 
              featuring dinner, awards, and guest speakers.
            </p>
          </div>
        </div>
      </section>

      <section className="content-section">
        <h2>Event Calendar</h2>
        <p>
          For a complete calendar of events and activities, please contact the club 
          or check our monthly newsletter. Members receive priority registration for 
          all club events.
        </p>
      </section>
    </div>
  )
}

export default Events
