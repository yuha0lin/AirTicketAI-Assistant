const chatHistory = document.getElementById('chat-history');
const userInput = document.getElementById('user-input');
const sendBtn = document.getElementById('send-btn');

sendBtn.addEventListener('click', sendMessage);
userInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        sendMessage();
    }
});

function sendMessage() {
    const message = userInput.value.trim();
    if (message) {
        addMessageToChat('user', message);
        userInput.value = '';
        callAIAPI(message);
    }
}

function addMessageToChat(sender, message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', `${sender}-message`);
    messageElement.textContent = message;
    chatHistory.appendChild(messageElement);
    chatHistory.scrollTop = chatHistory.scrollHeight;
}

async function callAIAPI(prompt) {
    try {
        const response = await fetch('/air_ticket_book/api/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `prompt=${encodeURIComponent(prompt)}`
        });

        if (!response.ok) {
            throw new Error('API请求失败');
        }

        const result = await response.json();
        const aiResponse = result.output.text;
        addMessageToChat('ai', aiResponse);
    } catch (error) {
        console.error('Error:', error);
        addMessageToChat('ai', '抱歉，出现了一些问题。请稍后再试。');
    }
}