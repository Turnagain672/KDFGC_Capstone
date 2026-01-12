# Deployment Guide

This guide covers how to deploy the KDFGC website to various hosting platforms.

## Build for Production

Before deploying, create a production build:

```bash
npm run build
```

This creates an optimized production build in the `dist` directory.

## Deployment Options

### Option 1: Netlify (Recommended for Beginners)

Netlify offers free hosting with automatic deployments from GitHub.

#### Setup:
1. Create a free account at [netlify.com](https://netlify.com)
2. Click "Add new site" → "Import an existing project"
3. Connect to GitHub and select the KDFGC_Capstone repository
4. Configure build settings:
   - **Build command**: `npm run build`
   - **Publish directory**: `dist`
   - **Branch**: `main` (or your production branch)
5. Click "Deploy site"

#### Features:
- ✅ Automatic deployments on every push
- ✅ Free SSL certificate (HTTPS)
- ✅ Custom domain support
- ✅ Preview deployments for pull requests
- ✅ Easy rollback to previous versions

### Option 2: Vercel

Similar to Netlify, Vercel offers seamless deployment from GitHub.

#### Setup:
1. Create a free account at [vercel.com](https://vercel.com)
2. Click "Import Project"
3. Connect to GitHub and select the repository
4. Vercel will auto-detect Vite and configure settings
5. Click "Deploy"

#### Features:
- ✅ Automatic deployments
- ✅ Free SSL certificate
- ✅ Edge network (fast global delivery)
- ✅ Preview deployments

### Option 3: GitHub Pages

Free hosting directly from your GitHub repository.

#### Setup:
1. Install gh-pages package:
   ```bash
   npm install --save-dev gh-pages
   ```

2. Update `package.json` to add deploy script:
   ```json
   {
     "scripts": {
       "deploy": "vite build && gh-pages -d dist"
     }
   }
   ```

3. Update `vite.config.ts` to set the base path:
   ```typescript
   export default defineConfig({
     base: '/KDFGC_Capstone/',  // Replace with your repo name
     plugins: [react()],
   })
   ```

4. Deploy:
   ```bash
   npm run deploy
   ```

5. Enable GitHub Pages:
   - Go to repository Settings → Pages
   - Set source to `gh-pages` branch
   - Save

#### Access:
Your site will be available at: `https://Turnagain672.github.io/KDFGC_Capstone/`

### Option 4: Traditional Web Host (cPanel/FTP)

For traditional shared hosting with cPanel or FTP access.

#### Setup:
1. Build the project:
   ```bash
   npm run build
   ```

2. Upload contents of the `dist` directory to your web server:
   - Via FTP client (FileZilla, Cyberduck)
   - Via cPanel File Manager
   - Upload to `public_html` or designated web root

3. Configure your domain to point to the uploaded files

#### Note:
Since this is a single-page application, you may need to configure URL rewrites to handle client-side routing. Add this to `.htaccess` (Apache) or nginx config:

**Apache (.htaccess)**:
```apache
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
```

**Nginx**:
```nginx
location / {
  try_files $uri $uri/ /index.html;
}
```

## Custom Domain Setup

### For Netlify/Vercel:
1. Go to Domain settings in your hosting dashboard
2. Add your custom domain (e.g., kdfgc.ca)
3. Follow DNS configuration instructions
4. Update your domain's DNS records with your registrar

### DNS Records Needed:
For root domain (kdfgc.ca):
- Add A record or ALIAS record pointing to hosting provider

For www subdomain (www.kdfgc.ca):
- Add CNAME record pointing to your hosting provider's domain

## Environment Variables

If you need environment variables (API keys, etc.):

1. Create `.env` file locally (DO NOT commit):
   ```
   VITE_API_KEY=your_api_key_here
   ```

2. For hosting platforms:
   - **Netlify**: Site settings → Environment variables
   - **Vercel**: Project settings → Environment Variables
   - **GitHub Pages**: Use GitHub Secrets + GitHub Actions

3. Access in code:
   ```typescript
   const apiKey = import.meta.env.VITE_API_KEY
   ```

## SSL/HTTPS

- **Netlify/Vercel**: Automatically provided
- **GitHub Pages**: Automatically provided
- **Traditional hosting**: Request SSL certificate from your host or use Let's Encrypt

## Performance Optimization

The build process automatically:
- ✅ Minifies JavaScript and CSS
- ✅ Optimizes images
- ✅ Creates efficient chunk files
- ✅ Generates source maps for debugging

## Monitoring and Analytics

Consider adding:
- **Google Analytics**: Track visitor behavior
- **Sentry**: Monitor errors in production
- **Hotjar**: User behavior tracking

## Continuous Deployment

For automated deployments:

1. Push code to GitHub
2. Hosting platform automatically:
   - Detects changes
   - Runs build
   - Deploys new version
   - Makes it live

## Rollback

If something goes wrong:

- **Netlify/Vercel**: Use dashboard to rollback to previous deployment
- **GitHub Pages**: 
  ```bash
  git revert <commit-hash>
  npm run deploy
  ```

## Troubleshooting

### Build fails on hosting platform
- Check Node.js version is compatible (v18+)
- Verify all dependencies are listed in package.json
- Check build logs for specific errors

### 404 errors on page refresh
- Configure URL rewrites (see Traditional Web Host section)

### Slow loading
- Check if files are being served with gzip/brotli compression
- Enable CDN if available
- Optimize images further

## Support

For deployment issues:
- Check hosting provider documentation
- Review build logs
- Ensure dependencies are up to date
- Test build locally before deploying

## Cost Estimates

- **Netlify/Vercel/GitHub Pages**: FREE for this project size
- **Traditional hosting**: $5-20/month depending on provider
- **Custom domain**: $10-15/year

## Recommended Setup for KDFGC

For the Kelowna District Fish and Game Club, we recommend:

1. **Netlify** for hosting (free, easy to use)
2. **Custom domain** (kdfgc.ca or similar)
3. **Automatic deployments** from main branch
4. **Preview deployments** for testing changes

This provides a professional, reliable setup with minimal technical maintenance.
