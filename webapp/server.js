var connect = require('connect'),
    http = require('http');

connect()
    .use(connect.logger(':method\: :url'))
    .use(connect.static('json'))
    .use(function(req, res){
       res.end('hello world\n');
    })
    .listen(3000);

