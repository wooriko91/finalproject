package com.it.wecodeyou.board.repository;

import java.util.List;

import com.it.wecodeyou.board.model.ReplyUserVO;
import com.it.wecodeyou.board.model.ReplyVO;

public interface IReplyMapper {
	
	//댓글 삽입문
	public Integer insert(ReplyVO rvo);
	
	//댓글을 DB에서 삭제하지는 않고 is_removed를 true로만 바꿈
	public void remove(Integer replyNo);
	
	//댓글 삭제
	public void delete(Integer replyNo);
	
	//댓글 수정
	public void update(ReplyVO rvo);
	
	//한 게시물의 댓글 목록 출력
	public List<ReplyUserVO> listByArticle(Integer articleNo);
	
	public List<ReplyUserVO> listByMember(Integer memberNo);
}