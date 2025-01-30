const net = require('net');

const PORT = 3000;

const server = net.createServer((socket) => {
    console.log('Client connected');

    socket.on('data', (data) => {
        const command = data.toString().trim();
        console.log('Received command:', command);
        
        const response = processCommand(command);

        socket.write(response + '\n');
    });

    socket.on('end', () => {
        console.log('Client disconnected');
    });
});

server.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`);
});

function processCommand(command) {
    const randomNumber = Math.floor(Math.random() * 101);
    return randomNumber;
}