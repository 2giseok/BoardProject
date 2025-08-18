package me.leegiseok.project.dto;





import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content, int page , int size , long totalElements, int totalPageS
)
{
    public  static <T> PageResponse<T> of(Page p) {
        return  new
                PageResponse<>
                (p.getContent(),p.getNumber(),p.getSize(),p.getTotalElements(),
                p.getTotalPages());
    }

}
