var express = require('express');
var app = express();

app.get('/', function (req, res) {
  res.send('<h1>Hello!</h1>');
});

app.listen(8080, function () {
  console.log('serving at http://localhost:8080')
});
