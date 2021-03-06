package com.it.wecodeyou.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it.wecodeyou.board.model.ReplyUserVO;
import com.it.wecodeyou.board.model.ReplyVO;
import com.it.wecodeyou.board.repository.IReplyMapper;

@Service
public class ReplyService implements IReplyService {

	@Autowired
	IReplyMapper rdao;
	
	@Override
	public Integer insertComment(ReplyVO rvo) {
		return rdao.insertComment(rvo);
	}

	@Override
	public Integer insertReply(ReplyVO rvo) {
		return rdao.insertReply(rvo);
	}

	@Override
	public void remove(Integer replyNo) {
		rdao.remove(replyNo);
		
	}

	@Override
	public void delete(Integer replyNo) {
		rdao.delete(replyNo);
		
	}

	@Override
	public void update(ReplyVO rvo) {
		rdao.update(rvo);
	}

	@Override
	public List<ReplyUserVO> listByArticle(Integer articleNo) {
		return rdao.listByArticle(articleNo);
	}

	@Override
	public List<ReplyUserVO> listByMember(Integer userNo) {
		return rdao.listByMember(userNo);
	}

	@Override
	public ArrayList<ReplyVO> listByUser(Integer replyWriter) {
		return rdao.listByUser(replyWriter);
	}

	@Override
	public Integer getMaxOrder(Integer replyParent) {
		return rdao.getMaxOrder(replyParent);
	}

	@Override
	public Integer getMaxDepth(Integer replyArticleNo) {
		return rdao.getMaxDepth(replyArticleNo);
	}

	@Override
	public Integer getDepth(Integer replyNo) {
		int depth = rdao.getDepth(replyNo);
		
		return depth;
	}



}
