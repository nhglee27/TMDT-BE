// Biến toàn cục để giữ kết nối STOMP
let stompClient = null;

// Hàm để thiết lập trạng thái đã kết nối
function setConnected(connected) {
    // Vô hiệu hóa nút "Kết nối" và kích hoạt nút "Ngắt kết nối"
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    // Ẩn/hiện bảng chứa tin nhắn
    document.getElementById('message-container').style.visibility = connected ? 'visible' : 'hidden';
    // Xóa các tin nhắn cũ khi kết nối/ngắt kết nối
    document.getElementById('messages').innerHTML = '';
}

// Hàm để kết nối đến WebSocket server
function connect() {
    // Tạo một đối tượng SockJS trỏ đến endpoint /ws của Spring Boot
    const socket = new SockJS('/ws'); 
    // Tạo một client STOMP trên socket
    stompClient = Stomp.over(socket);

    // Bắt đầu kết nối STOMP
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Đã kết nối: ' + frame);

        // Đăng ký vào topic /topic/messages để nhận tin nhắn từ server
        stompClient.subscribe('/topic/messages', function (message) {
            // Khi nhận được tin nhắn, hiển thị nó ra
            showMessage(JSON.parse(message.body));
        });
    });
}

// Hàm để ngắt kết nối
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Đã ngắt kết nối");
}

// Hàm để gửi tin nhắn
function sendMessage() {
    const name = document.getElementById('name').value;
    const messageContent = document.getElementById('message').value;
    
    // Gửi tin nhắn đến destination /app/chat trên server
    stompClient.send("/app/chat", {}, JSON.stringify({'name': name, 'content': messageContent}));
    
    // Xóa nội dung ô nhập tin nhắn sau khi gửi
    document.getElementById('message').value = '';
}

// Hàm để hiển thị tin nhắn nhận được trong bảng
function showMessage(message) {
    const messageList = document.getElementById('messages');
    const newRow = messageList.insertRow(-1);
    const cell = newRow.insertCell(0);
    cell.textContent = message.name + ": " + message.content;
}

// Gắn các sự kiện vào các phần tử trên trang
document.addEventListener('DOMContentLoaded', function () {
    // Ngăn form gửi theo cách truyền thống
    document.getElementById('message-form').addEventListener('submit', function(e) {
        e.preventDefault();
    });

    // Gắn sự kiện click cho các nút
    document.getElementById('connect').addEventListener('click', function() {
        connect();
    });

    document.getElementById('disconnect').addEventListener('click', function() {
        disconnect();
    });

    document.getElementById('send').addEventListener('click', function() {
        sendMessage();
    });

    // Ban đầu, đặt trạng thái là chưa kết nối
    setConnected(false);
});