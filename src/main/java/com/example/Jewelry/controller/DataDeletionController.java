package com.example.Jewelry.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataDeletionController {
    @GetMapping("/data-deletion")
    public ResponseEntity<String> dataDeletionInstructions() {
        String html = """
            <html>
              <head><title>Hướng dẫn xóa dữ liệu</title></head>
              <body>
                <h1>Hướng dẫn xóa dữ liệu người dùng</h1>
                <p>Nếu bạn đã đăng nhập bằng Facebook và muốn xóa toàn bộ dữ liệu, vui lòng gửi email đến <strong>support@yourdomain.com</strong>.</p>
                <p>Chúng tôi sẽ xử lý yêu cầu trong vòng 7 ngày làm việc.</p>
              </body>
            </html>
        """;
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }
}

