const express = require('express');
const app = express();

app.get('/', function (req, res) {
  res.send(Interop.eval('R',
    `svg();
     require(lattice);
     x <- 1:100
     y <- sin(x/10)
     z <- cos(x^1.3/(runif(1)*5+10))
     print(cloud(x~y*z, main="cloud plot"))
     grDevices:::svg.off()
    `));
})

app.get('/js', function (req, res) {
  res.send(Interop.eval('js',
    `'hello from JavaScript'`));
})

app.listen(8080, function () {
  console.log('serving at http://localhost:8080');
});
