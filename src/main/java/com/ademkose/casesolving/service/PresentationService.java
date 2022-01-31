package com.ademkose.casesolving.service;

import java.util.List;

import com.ademkose.casesolving.exception.BadRequestException;
import com.ademkose.casesolving.model.Presentation;

public interface PresentationService {

    void saveThePresentation(Presentation presentation) throws BadRequestException;

	List<Presentation> getAllPresentation();
	List<Presentation> startToMakeTracks();
	
	void loadTestData();
	

}
