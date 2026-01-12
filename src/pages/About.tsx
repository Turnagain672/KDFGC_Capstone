import '../styles/Page.css'

function About() {
  return (
    <div className="page about-page">
      <h1>About KDFGC</h1>
      
      <section className="content-section">
        <h2>Our Mission</h2>
        <p>
          The Kelowna District Fish and Game Club is committed to promoting the conservation of 
          wildlife and their habitats, supporting sustainable hunting and fishing practices, and 
          fostering a community of outdoor enthusiasts who respect and protect our natural resources.
        </p>
      </section>

      <section className="content-section">
        <h2>Our History</h2>
        <p>
          Founded by passionate sportsmen and conservationists, the KDFGC has been serving the 
          Kelowna community for many years. Our club has played a vital role in local conservation 
          efforts, habitat restoration projects, and promoting responsible outdoor recreation.
        </p>
      </section>

      <section className="content-section">
        <h2>Our Values</h2>
        <ul className="values-list">
          <li><strong>Conservation:</strong> We prioritize the protection and restoration of wildlife habitats.</li>
          <li><strong>Education:</strong> We provide training and resources to promote safe and ethical outdoor practices.</li>
          <li><strong>Community:</strong> We foster a welcoming environment for all who share our passion for the outdoors.</li>
          <li><strong>Stewardship:</strong> We actively participate in environmental initiatives and wildlife management.</li>
          <li><strong>Respect:</strong> We honor wildlife, nature, and fellow outdoor enthusiasts.</li>
        </ul>
      </section>

      <section className="content-section">
        <h2>What We Do</h2>
        <p>
          Our club organizes a wide range of activities throughout the year, including:
        </p>
        <ul>
          <li>Monthly club meetings and educational presentations</li>
          <li>Fishing derbies and tournaments</li>
          <li>Hunting safety courses and certification programs</li>
          <li>Youth outdoor education programs</li>
          <li>Habitat enhancement and conservation projects</li>
          <li>Social events and family gatherings</li>
          <li>Advocacy for wildlife and outdoor recreation</li>
        </ul>
      </section>
    </div>
  )
}

export default About
