
package org.example.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(max = 5000, message = "Nội dung tin nhắn tối đa 5000 ký tự")
    private String message;

    private LocalDate startDate;

    private LocalDate endDate;

    private String model;

    /**
     * Tối đa 12 tin nhắn hội thoại gần nhất.
     * Mỗi tin nhắn lịch sử tối đa 2.000 ký tự.
     *
     * Lịch sử chỉ phục vụ hiểu ngữ cảnh, không được xem là
     * dữ liệu giao dịch hoặc dữ liệu tài chính thực tế.
     */
    @Size(max = 12, message = "Lịch sử hội thoại tối đa 12 tin nhắn")
    private List<
            @Size(max = 2000, message = "Mỗi tin nhắn lịch sử tối đa 2000 ký tự")
                    String
            > conversationHistory;
}
