var connect = require('connect'),
    http = require('http');

connect()
    .use(connect.static('json'))
    .listen(3000);