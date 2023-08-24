package it.uniroma3.siw.siwproduct.service;

import it.uniroma3.siw.siwproduct.model.Review;
import it.uniroma3.siw.siwproduct.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;

	
	@Transactional
	public boolean createNewReview(Review review) {
		boolean res = false;
		if(!this.reviewRepository.existsByUsernameAndTitleAndRatingAndDescription(review.getUsername(), review.getTitle(), review.getRating(), review.getDescription()))
			res = true;
			reviewRepository.save(review);
		return res;
	}
	
	
	public Review saveReview(Review review) {
		return this.reviewRepository.save(review);
		
	}

	public void deleteReview(Review review) {
		this.reviewRepository.delete(review);
		
	}
	
	public Review findById(Long id) {
		return this.reviewRepository.findById(id).orElse(null);
	}

}
