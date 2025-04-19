import scrape from 'website-scraper';
import SaveToExistingDirectoryPlugin from 'website-scraper-existing-directory';

const result = await scrape({
  urls: ['http://localhost:8080/resume-html'],
  directory: '../../resume.romankh.me/',
  plugins: [ new SaveToExistingDirectoryPlugin() ]
});

console.log(result);
