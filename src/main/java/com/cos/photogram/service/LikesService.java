package com.cos.photogram.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cos.photogram.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikesService {

	private final LikesRepository likesRepository;

	@Transactional
	public void like(int imageId, int principalId) {
		likesRepository.mLikes(imageId, principalId);
	}

	@Transactional
	public void unLike(int imageId, int principalId) {
		likesRepository.mUnLikes(imageId, principalId);
	}
}
