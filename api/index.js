'use strict';
const express = require('express'),
	app = express();

var port = process.env.PORT || 8080;

app.use(require('./controllers'));

app.listen(port, function () {
	console.log('Now listening on port ' + port + '...');
});
