package com.ademkose.casesolving.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ademkose.casesolving.exception.BadRequestException;
import com.ademkose.casesolving.model.Presentation;
import com.ademkose.casesolving.repository.PresentationRepository;
import com.ademkose.casesolving.util.Utils;

@Service
public class PresentationManager implements PresentationService {

	private static final Logger LOG = LoggerFactory.getLogger(PresentationManager.class);
	@Autowired
	PresentationRepository presentationRepository;
	public static Map<Long, Integer> talksMap = new HashMap<Long, Integer>();
	public static Map<Long, Integer> tracksMap_database = new HashMap<Long, Integer>();
	public static ArrayList<Presentation> tracks_display_list = new ArrayList<Presentation>();
	@Value("${myproperties.is_networking_day}")
	public boolean is_networking_day;
	@Override
	public void saveThePresentation(Presentation presentation) throws BadRequestException {
		LOG.info("[saveThePresentation]: Presentation " + presentation.toString());
		if (presentation.getMinute() < Utils.MINUMUM_PRESENTATION_MINUTE) {
			LOG.info("[saveThePresentation]: Presentation minute should be grater than "
					+ Utils.MINUMUM_PRESENTATION_MINUTE);
			throw new BadRequestException(
					"Presentation minute should be grater than " + Utils.MINUMUM_PRESENTATION_MINUTE);
		} else if (presentation.getName().length() < Utils.MINUMUM_PRESENTATION_NAME_LENTGH) {
			LOG.info("[saveThePresentation]: Presentation name should be grater than "
					+ Utils.MINUMUM_PRESENTATION_NAME_LENTGH + " char..");
			throw new BadRequestException(
					"Presentation name should be grater than " + Utils.MINUMUM_PRESENTATION_NAME_LENTGH + " char..");
		}
		presentationRepository.save(presentation);
	}

	@Override
	public List<Presentation> getAllPresentation() {
		LOG.info("[getAllPresentation]: new Request ");
		return presentationRepository.findAll();
	}

	public List<Presentation> startToMakeTracks() {
		tracksMap_database.clear();
		talksMap.clear();
		tracks_display_list.clear();
		List<Presentation> list_from_DBtable = presentationRepository.findAll();
		list_from_DBtable.forEach(pr -> {
			talksMap.put(pr.getId(), pr.getMinute());
		});
		tracksMap_database.putAll(talksMap);
		LOG.info("[startToMakeTracks]: " + "talksMap total size : " + talksMap.size());
		int day = 1;
		
		while (talksMap.size() != 0) {
			fillBeforeLunch180minutes();
			addLunch();			
			fillAfterLunch180minutes(is_networking_day);			
			endOftheDAY();
			day++;
		}
		LOG.info("[startToMakeTracks]: " + "talksMap total size : " + talksMap.size());
		LOG.info("[startToMakeTracks]: " + "tracks_list total size : " + tracks_display_list.size());

		tracks_display_list = Utils.setTime(tracks_display_list);
		return tracks_display_list;
	}

	
	public void fillBeforeLunch180minutes() {
		Map<Long, Integer> tracksMap = new HashMap<Long, Integer>();
		int total = 0;
		while (total < Utils.BEFORE_LUNCH_MINUTE) {
			Long get_random_key = takeRandomOneID();
			Integer get_random_value = talksMap.get(get_random_key);
			total = total + get_random_value;
			tracksMap.put(get_random_key, get_random_value);
			talksMap.remove(get_random_key);
			if (total > Utils.BEFORE_LUNCH_MINUTE) {
				total = 0;
				talksMap.putAll(tracksMap);
				tracksMap.clear();
			}
			if (talksMap.size() == 0) {
				break;
			}
		}
		tracksMap.forEach((k, v) -> {
			Optional<Presentation> o = presentationRepository.findById(k);
			tracks_display_list.add(o.get());
		});
	}

	public void fillAfterLunch180minutes(boolean with_networking) {
		Map<Long, Integer> tracksMap = new HashMap<Long, Integer>();
		if (talksMap.size() == 0) {
			return;
		}
		int total = 0;
		int max_min_networking = with_networking ? Utils.BEFORE_LUNCH_MINUTE : Utils.MAX_MINUTE;
		while (true) {
			Long get_random_key = takeRandomOneID();
			Integer get_random_value = talksMap.get(get_random_key);
			total = total + get_random_value;
			tracksMap.put(get_random_key, get_random_value);
			talksMap.remove(get_random_key);
			if (total > Utils.MAX_MINUTE) {
				talksMap.putAll(tracksMap);
				tracksMap.clear();
				total = 0;
				continue;
			}
			if (talksMap.size() == 0) {
				break;
			}
			if (with_networking) {
				if (total < Utils.MAX_MINUTE && talksMap.size() > 0) {
					continue;
				}
				if (total == Utils.MAX_MINUTE && talksMap.size() > 0) {
					break;
				} else if (total >= Utils.BEFORE_LUNCH_MINUTE && talksMap.size() == 0) {
					break;
				}
			} else {
				if (total == Utils.MAX_MINUTE) {
					break;
				}
			}
		}
		tracksMap.forEach((k, v) -> {
			Optional<Presentation> o = presentationRepository.findById(k);
			tracks_display_list.add(o.get());
		});

		if (total >= max_min_networking && with_networking && talksMap.size() == 0 && (Utils.MAX_MINUTE - total) > 0) {
			// here is okay to add networking period
			Presentation networking = new Presentation((long) 0, Utils.NETWORKING_NAME, null, (Utils.MAX_MINUTE - total));
			tracks_display_list.add(networking);
		}
	}
	public static void addLunch() {
		// adding Lunch
		Presentation lunch = new Presentation((long) 0, Utils.LUNCH_NAME, Utils.LUNCH_TIME, Utils.LUNCH_MINUTE);
		tracks_display_list.add(lunch);
	}

	public static void endOftheDAY() {
		// adding endOftheDAY
		Presentation endOftheDAY = new Presentation((long) -1, Utils.EOD_NAME, "", 0);
		tracks_display_list.add(endOftheDAY);
	}

	public static Long takeRandomOneID() {
		// taking random element from the hashmap
		List<Long> valuesList = new ArrayList<Long>(talksMap.keySet());
		Collections.shuffle(valuesList);
		return valuesList.get(0);
	}

	public void loadTestData() {
		

		presentationRepository.save(new Presentation((long) 1, "Architecting Your Codebase", null, 60));
		presentationRepository.save(new Presentation((long) 2, "Overdoing it in Python", null, 45));
		presentationRepository.save(new Presentation((long) 3, "Flavors of Concurrency in Java", null, 30));
		presentationRepository.save(new Presentation((long) 4, "Ruby Errors from Mismatched Gem Versions", null, 45));
		presentationRepository
				.save(new Presentation((long) 5, "JUnit 5 - Shaping the Future of Testing on the JVM", null, 45));
		presentationRepository.save(new Presentation((long) 6, "Cloud Native Java lightning", null, 5));
		presentationRepository.save(new Presentation((long) 7, "Communicating Over Distance", null, 60));
		presentationRepository.save(new Presentation((long) 8, "AWS Technical Essentials", null, 45));
		presentationRepository.save(new Presentation((long) 9, "Continuous Delivery", null, 30));
		presentationRepository.save(new Presentation((long) 10, "Monitoring Reactive Applications", null, 30));
		presentationRepository.save(new Presentation((long) 11, "Pair Program g vs Noise", null, 45));
		presentationRepository.save(new Presentation((long) 12, "Rails Magic", null, 60));
		presentationRepository.save(new Presentation((long) 13, "Microservices Just Right", null, 60));
		presentationRepository.save(new Presentation((long) 14, "Clojure Ate Scala (on my project)", null, 45));
		presentationRepository.save(new Presentation((long) 15, "Perfect Scalability", null, 30));
		presentationRepository.save(new Presentation((long) 16, "Apache Spark", null, 30));
		presentationRepository.save(new Presentation((long) 17, "Async Testing on JVM", null, 60));
		presentationRepository.save(new Presentation((long) 18, "A World Without HackerNews", null, 30));
		presentationRepository.save(new Presentation((long) 19, "User Interface CSS in Apps", null, 30));

	}

}
