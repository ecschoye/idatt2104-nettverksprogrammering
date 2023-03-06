const net = require('net');
const crypto = require('crypto');

const fixedString = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
// Attempt to implement a WebSocket server using the net module if Java version does not work out

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
  connection.on('data', () => {
    let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    WebSocket test page
    <script>
      let ws = new WebSocket('ws://localhost:3001');
      ws.onmessage = event => document.getElementById('messages').innerHTML += event.data + '<br />';
      const message = () => ws.send(document.getElementById('message').value);
    </script>

    <h1>WebSocket server</h1>
    <p>WebSocket server is not implemented yet.</p>
    <label for="message">Message to send:</label><br />
    <input type="text" id="message" /><br />
    <button id="send" onclick="return message();">Send</button><br />
    <div id="messages"></div>

  </body>
    
</html>
`;
    connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
  });
});
httpServer.listen(3000, () => {
  console.log('HTTP server listening on port 3000');
});

const wsAcceptKey = (key) => { 
    crypto.createHash('sha1').update(key + fixedString).digest('base64');
}

// Incomplete WebSocket server

let clients = [];

const wsServer = net.createServer((connection) => {
  console.log('Client connected');

  connection.on('data', (data) => {
    console.log('Data received from client: ', data.toString());
    if (isWebSocketHandshakeRequest(message)) {
        var key = getKey(data);
        var acceptKey = wsAcceptKey(key);

        const responds = `HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: ${acceptKey}\r\n\r\n`;

        connection.write(responds);
        clients.push(connection);
        console.log('Client added to clients list');

      } else {
        console.log('Client sent non-handshake request');
      }
  });

  connection.on('end', () => {
    console.log('Client disconnected');
  });
});

wsServer.on('error', (error) => {
  console.error('Error: ', error);
});
wsServer.listen(3001, () => {
  console.log('WebSocket server listening on port 3001');
});

function isWebSocketHandshakeRequest(message) {
    const lines = message.split('\r\n');
    if (lines[0].startsWith('GET ') &&
        lines.some((line) => line.startsWith('Upgrade: websocket')) &&
        lines.some((line) => line.startsWith('Connection: Upgrade')) &&
        lines.some((line) => line.startsWith('Sec-WebSocket-Key:'))) {
      return true;
    }
    return false;
}

function getKey(data) {
    return data.toString().match(/Sec-WebSocket-Key: (.*)/)[1];
}