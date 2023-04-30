package com.board.study.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.study.dto.board.BoardRequestDto;
import com.board.study.service.BoardService;

import lombok.RequiredArgsConstructor;
//@RequiredArgsConstructor은 final, @NotNull로 정의된 멤버 변수를 매개변수로하는 생성자를 자동으로 생성 
//-> 해당 Controller 클래스가 객체로 Spring IoC Container에 등록될 때 boardService 객체를 주입받아서 생성됨.
@RequiredArgsConstructor 
@Controller
public class BoardController {
	
	private final BoardService boardService;
	
	//페이징 처리가 되는 목록
	@GetMapping("/board/list")
	public String getBoardListPage(Model model
								, @RequestParam(required = false, defaultValue = "0") Integer page
							    , @RequestParam(required = false, defaultValue = "5") Integer size) 
							    throws Exception{
		try {
			model.addAttribute("resultMap", boardService.findAll(page, size));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "board/list";
	}
	
	//새 게시글 등록 페이지 보여주기
	@GetMapping("/board/write")
	public String getBoardWritePage(Model model, BoardRequestDto boardRequestDto) {
		return "board/write";
	}
	
	//새 게시글 DB에 저장 -> 파일 입출력 부분 추가해줘야함.
	@PostMapping("/board/write/action")
	public String boardWriteAction(Model model, BoardRequestDto boardRequestDto, MultipartHttpServletRequest multiRequest) throws Exception {
		
		try {
			//게시판 + 파일 저장에 실패했으면 오류를 발생시킴
			if (!boardService.save(boardRequestDto, multiRequest)) {
				throw new Exception("#Exception boardWriteAction!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		
		return "redirect:/board/list";
	}
	
	//상세 페이지 -> 파입 입출력 부분 추가해줘야함.
	@GetMapping("/board/view")
	public String getBoardViewPage(Model model, BoardRequestDto boardRequestDto) throws Exception {
		//여기에 try catch 왜 한지 모르겠음. 던질꺼면 던지고, 잡을꺼면 잡지;
		if(boardRequestDto.getId() != null) {
			//boardService.save에서 결과를 게시판 + 파일로 Map 타입으로 반환함.
			model.addAttribute("resultMap", boardService.findById(boardRequestDto.getId()));
		}
		return "board/view";
	}
	
	//상세 페이지에서 수정 -> 파일 입출력부분 추가해줘야함.
	@PostMapping("/board/view/action")
	public String boardViewAction(Model model, BoardRequestDto boardRequestDto, MultipartHttpServletRequest multiRequest) throws Exception{
		try {
			boolean result = boardService.updateBoard(boardRequestDto, multiRequest);
			
			if(!result) {
				throw new Exception("#Excption boardViewAction!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "redirect:/board/list";
	}
	
	//상세 페이지에서 삭제 -> boardService 부분에서 파일도 함께 삭제되도록 수정함.
	@PostMapping("/board/view/delete")
	public String boardViewDeleteAction(Model model, @RequestParam() Long id) throws Exception{
		try {
			boardService.deleteById(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "redirect:/board/list";
	}
	
	//목록에서 삭제(여러개)
	@PostMapping("/board/delete")
	public String boardDeleteAction(Model model, @RequestParam() Long[] deleteId) throws Exception{
		try {
			boardService.deleteAll(deleteId);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "redirect:/board/list";
	}
	
	
	
}
