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
    @Size(max = 2000, message = "Nội dung tin nhắn tối đa 2000 ký tự")
    private String message;

    private LocalDate startDate;

    private LocalDate endDate;

    private String model;

    /**
     * Tối đa 12 lượt hội thoại gần nhất, chỉ dùng làm ngữ cảnh khi AI chính gián đoạn.
     * Mỗi phần tử có dạng "Người dùng: ..." hoặc "FinHome: ...".
     */
    @Size(max = 12)
    private List<@Size(max = 2000) String> conversationHistory;
}
