package tendering.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tendering.dto.TenderDTO;
import tendering.kafka.KafkaProducer;
import tendering.model.*;
import tendering.repository.LspResponseRepository;
import tendering.repository.SignupRepository;
import tendering.repository.TenderRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TenderService {

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private KafkaProducer tenderKafkaProducer;

	@Autowired
	private SignupRepository signupRepository;

	@Autowired
	private LspResponseRepository lspResponseRepository;

	// Creates a new tender, saves it, initializes LSP responses, and broadcasts
	// tender info via Kafka.
	@Transactional
	public Tender createTender(TenderDTO tenderDTO, String createdByCompanyName) {

		Optional<Signup> createdBy = signupRepository.findByCompanyName(createdByCompanyName);

		if (createdBy.isPresent()) {
			Tender tender = new Tender();
			tender.setSourceLocation(tenderDTO.getSourceLocation());
			tender.setDestinationLocation(tenderDTO.getDestinationLocation());
			tender.setPickupDate(tenderDTO.getPickupDate());
			tender.setDropDate(tenderDTO.getDropDate());
			tender.setWeight(tenderDTO.getWeight());
			tender.setSpecialInstructions(tenderDTO.getSpecialInstructions());
			tender.setTenderPrice(tenderDTO.getTenderPrice());
			tender.setTenderNo(generateTenderNo(tenderDTO));
			tender.setStatus(TenderStatus.ACTIVE);
			tender.setCreatedBy(createdBy.get().getCompanyName());
			tender.setCreatedAt(LocalDateTime.now());

			Tender savedTender = tenderRepository.save(tender);

			// Store a response entry for each LSP associated with this tender.
			List<Signup> lspUsers = signupRepository.findByRole(UserRole.LSP);
			for (Signup lsp : lspUsers) {
				LspResponse response = LspResponse.builder().tenderNo(savedTender.getTenderNo())
						.lspCompanyName(lsp.getCompanyName()).sourceLocation(savedTender.getSourceLocation())
						.destinationLocation(savedTender.getDestinationLocation())
						.pickupDate(savedTender.getPickupDate()).dropDate(savedTender.getDropDate())
						.weight(savedTender.getWeight()).specialInstructions(savedTender.getSpecialInstructions())
						.tenderPrice(savedTender.getTenderPrice()).createdByCompanyName(createdByCompanyName) // Added
																												// creator
																												// company
																												// name.
						.status(LspResponse.Status.ACTIVE).createdAt(LocalDateTime.now()).build();

				lspResponseRepository.save(response);
			}

			// Broadcast the tender details to Kafka for further processing.
			TenderDTO kafkaDto = new TenderDTO();
			kafkaDto.setTenderNo(savedTender.getTenderNo());
			kafkaDto.setSourceLocation(savedTender.getSourceLocation());
			kafkaDto.setDestinationLocation(savedTender.getDestinationLocation());
			kafkaDto.setPickupDate(savedTender.getPickupDate());
			kafkaDto.setDropDate(savedTender.getDropDate());
			kafkaDto.setWeight(savedTender.getWeight());
			kafkaDto.setTenderPrice(savedTender.getTenderPrice());
			kafkaDto.setSpecialInstructions(savedTender.getSpecialInstructions());
			kafkaDto.setCreatedBy(savedTender.getCreatedBy());

			tenderKafkaProducer.broadcastTender(kafkaDto);

			return savedTender;
		} else {
			throw new NoSuchElementException("Company not found: " + createdByCompanyName);
		}
	}

	// Generates a unique tender number based on source, destination, date, and a
	// sequence count.
	private String generateTenderNo(TenderDTO tenderDTO) {
		String src = tenderDTO.getSourceLocation().substring(0, 3).toUpperCase();
		String dest = tenderDTO.getDestinationLocation().substring(0, 3).toUpperCase();
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));

		String baseTenderNo = "TENDER-" + src + "-" + dest + "-" + date;

		long count = tenderRepository.countByTenderNoStartingWith(baseTenderNo);
		String sequence = String.format("%02d", count + 1);

		return baseTenderNo + "-" + sequence;
	}

	// Retrieves tenders for a specific company filtered by tender status, throwing
	// errors if none found.
	public List<Tender> getTendersByCompanyAndStatus(String companyName, TenderStatus status) {
		if (!signupRepository.existsByCompanyName(companyName)) {
			throw new NoSuchElementException("Company " + companyName + " not found.");
		}

		List<Tender> tenders = tenderRepository.findByCreatedByAndStatus(companyName, status);

		if (tenders.isEmpty()) {
			throw new NoSuchElementException("No " + status + " tenders found for company " + companyName);
		}

		return tenders;
	}
}
