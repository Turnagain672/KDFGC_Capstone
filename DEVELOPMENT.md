# Development Guide

## Getting Started

### Prerequisites
- Node.js v18 or higher
- npm v9 or higher

### Installation
```bash
npm install
```

### Development
Start the development server:
```bash
npm run dev
```

The application will be available at http://localhost:5173

### Building
Create a production build:
```bash
npm run build
```

The build output will be in the `dist` directory.

### Preview Production Build
Preview the production build locally:
```bash
npm run preview
```

### Linting
Run ESLint to check code quality:
```bash
npm run lint
```

## Project Structure

```
KDFGC_Capstone/
├── public/              # Static assets
│   └── vite.svg        # Favicon
├── src/
│   ├── components/     # Reusable React components
│   │   ├── Header.tsx  # Site header with navigation
│   │   └── Footer.tsx  # Site footer
│   ├── pages/          # Page components
│   │   ├── Home.tsx    # Home page
│   │   ├── About.tsx   # About page
│   │   ├── Membership.tsx  # Membership page
│   │   ├── Events.tsx  # Events page
│   │   └── Contact.tsx # Contact page with form
│   ├── styles/         # CSS stylesheets
│   │   ├── index.css   # Global styles and CSS variables
│   │   ├── App.css     # Main app layout
│   │   ├── Header.css  # Header styles
│   │   ├── Footer.css  # Footer styles
│   │   └── Page.css    # Page-specific styles
│   ├── App.tsx         # Main application component with routing
│   └── main.tsx        # Application entry point
├── .eslintrc.cjs       # ESLint configuration
├── .gitignore          # Git ignore rules
├── index.html          # HTML entry point
├── package.json        # Project dependencies and scripts
├── tsconfig.json       # TypeScript configuration
├── tsconfig.node.json  # TypeScript config for Node
├── vite.config.ts      # Vite configuration
└── README.md           # Project documentation
```

## Technologies Used

- **React 18**: Modern UI library with hooks
- **TypeScript**: Type-safe JavaScript
- **Vite**: Fast build tool and dev server
- **React Router**: Client-side routing
- **ESLint**: Code quality and consistency

## Styling

The application uses CSS custom properties (CSS variables) for consistent theming:

```css
--primary-color: #2d5016    /* Dark green */
--secondary-color: #4a7c2c  /* Medium green */
--accent-color: #8b4513     /* Brown */
--text-color: #333          /* Dark gray */
--bg-color: #f4f4f4         /* Light gray */
```

## Adding New Pages

1. Create a new component in `src/pages/`
2. Import and add the route in `src/App.tsx`
3. Add navigation links in `src/components/Header.tsx` and `src/components/Footer.tsx`

## Deployment

The application can be deployed to any static hosting service:

### Netlify/Vercel
1. Connect your repository
2. Set build command: `npm run build`
3. Set publish directory: `dist`

### GitHub Pages
1. Install gh-pages: `npm install --save-dev gh-pages`
2. Add to package.json scripts:
   ```json
   "deploy": "vite build && gh-pages -d dist"
   ```
3. Run: `npm run deploy`

## Contributing

This is a capstone project. For contributions:
1. Create a feature branch
2. Make your changes
3. Run `npm run lint` to check code quality
4. Run `npm run build` to ensure it builds
5. Submit a pull request

## Future Enhancements

Potential improvements for the future:
- Add a backend API for contact form submissions
- Implement user authentication for members-only content
- Add an event calendar with date picker
- Create an image gallery for club activities
- Add a news/blog section
- Implement member directory (with privacy controls)
- Mobile app using React Native (code sharing with web app)
- Online membership registration and payment
- Integration with social media platforms

## License

This project is developed for the Kelowna District Fish and Game Club.
