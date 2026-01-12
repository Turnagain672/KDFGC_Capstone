import '../styles/Page.css'

function Membership() {
  return (
    <div className="page membership-page">
      <h1>Membership</h1>
      
      <section className="content-section">
        <h2>Join the KDFGC</h2>
        <p>
          Membership in the Kelowna District Fish and Game Club provides access to exclusive events, 
          educational resources, and a community of like-minded outdoor enthusiasts. Whether you're 
          an experienced hunter or angler, or just getting started, we welcome you to join us.
        </p>
      </section>

      <section className="content-section">
        <h2>Membership Benefits</h2>
        <ul>
          <li>Access to club facilities and resources</li>
          <li>Invitations to members-only events and activities</li>
          <li>Discounts on courses and certifications</li>
          <li>Monthly newsletter with club updates and outdoor tips</li>
          <li>Voting rights in club decisions</li>
          <li>Networking with fellow outdoor enthusiasts</li>
          <li>Support for local conservation initiatives</li>
        </ul>
      </section>

      <section className="content-section">
        <h2>Membership Types</h2>
        
        <div className="membership-types">
          <div className="membership-card">
            <h3>Individual Membership</h3>
            <p className="price">$XX / year</p>
            <p>Perfect for individual outdoor enthusiasts</p>
          </div>

          <div className="membership-card">
            <h3>Family Membership</h3>
            <p className="price">$XX / year</p>
            <p>Covers you, your spouse, and children under 18</p>
          </div>

          <div className="membership-card">
            <h3>Youth Membership</h3>
            <p className="price">$XX / year</p>
            <p>For members under 18 years of age</p>
          </div>

          <div className="membership-card">
            <h3>Senior Membership</h3>
            <p className="price">$XX / year</p>
            <p>For members 65 years and older</p>
          </div>
        </div>
      </section>

      <section className="content-section">
        <h2>How to Join</h2>
        <p>
          To become a member, please contact us through our <a href="/contact">contact page</a> or 
          attend one of our monthly meetings. We look forward to welcoming you to the club!
        </p>
      </section>
    </div>
  )
}

export default Membership
