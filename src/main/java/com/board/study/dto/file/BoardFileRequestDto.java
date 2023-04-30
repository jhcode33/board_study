package com.board.study.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

//사용자로부터 파일 정보를 받아 전달할 DTO객체
@Data
@NoArgsConstructor // final, @NotNull이 붙은 필드를 인자로 받는 생성자를 만들어줌
public class BoardFileRequestDto {
	private Long id;
	private Long[] idArr;
	private String fileId;

}
