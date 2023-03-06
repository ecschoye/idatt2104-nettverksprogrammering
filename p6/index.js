const net = require('net');
const crypto = require('crypto');

const GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
// Attempt to implement a WebSocket server using the net module if Java version does not work out

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
  connection.on('data', (data) => {
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
      const message = () => {ws.send(document.getElementById('message').value)};
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
    return crypto.createHash('sha1').update(key + GUID).digest('base64');
}

// Incomplete WebSocket server

let clients = [];

const wsServer = net.createServer((connection) => {
  console.log('Client connected');

  connection.on('data', (data) => {
    console.log('Data received from client: ', data.toString());
    if (isWebSocketHandshakeRequest(data.toString())) {
        var key = getWSKey(data);
        var hsAcceptKey = wsAcceptKey(key);

        const responds = `HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: ${hsAcceptKey}\r\n\r\n`;

        connection.write(responds);
        clients.push(connection);
        console.log('Client added to clients list');

      } else {
        console.log('Client sent non-handshake request');
        let decoded = parseWebSocketMessage(data);
        console.log('Decoded message: ', decoded);
        broadcastWebSocketMessage(decoded);
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
    console.log('Message: ', message)
    const lines = message.split('\r\n');
    if (lines[0].startsWith('GET ') &&
        lines.some((line) => line.startsWith('Upgrade: websocket')) &&
        lines.some((line) => line.startsWith('Connection: Upgrade')) &&
        lines.some((line) => line.startsWith('Sec-WebSocket-Key:'))) {
      return true;
    }
    return false;
}

function getWSKey(data) {
    return data.toString().match(/Sec-WebSocket-Key: (.*)/)[1];
}

function parseWebSocketMessage(data) {
    let payloadLength = data[1] & 127;
    let mask = data.slice(2, 6);
    let payload = data.slice(6, 6 + payloadLength);
    let decoded = '';
    for (let i = 0; i < payload.length; i++) {
      decoded += String.fromCharCode(payload[i] ^ mask[i % 4]);
    }
    return decoded;
  }
  
  function broadcastWebSocketMessage(message) {
    const payload = encodeWebSocketMessage(message);
    clients.forEach((c) => {
      c.write(payload);
    });
  }

  function encodeWebSocketMessage(message) {
    const payload = Buffer.from(message);
    const payloadLength = payload.length;
    const header = Buffer.alloc(2);
    header.writeUInt8(0b10000001, 0);
    header.writeUInt8(payloadLength, 1);
    return Buffer.concat([header, payload]);
  }