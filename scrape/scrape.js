const scrape = require('website-scraper');
const SaveToExistingDirectoryPlugin = require('website-scraper-existing-directory');

scrape({
  urls: ['http://localhost:8080/resume-html'],
  directory: '../../romank8k.github.io/',
  plugins: [ new SaveToExistingDirectoryPlugin() ]
}).then((result) => {
  console.log(result);
});
