package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.jsonb.AppliedOffer;
import ru.neoflex.deal.model.jsonb.PassportData;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScoringDataMapper {

    @Mapping(target = "amount", source = "offer.totalAmount")
    @Mapping(target = "term", source = "offer.term")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    @Mapping(target = "middleName", source = "client.middleName")
    @Mapping(target = "gender", source = "finishRegistration.gender")
    @Mapping(target = "birthdate", source = "client.birthDate")
    @Mapping(target = "passportSeries", source = "passportData.series")
    @Mapping(target = "passportNumber", source = "passportData.number")
    @Mapping(target = "passportIssueDate", source = "finishRegistration.passportIssueDate")
    @Mapping(target = "passportIssueBranch", source = "finishRegistration.passportIssueBranch")
    @Mapping(target = "maritalStatus", source = "finishRegistration.maritalStatus")
    @Mapping(target = "dependentAmount", source = "finishRegistration.dependentAmount")
    @Mapping(target = "employment", source = "finishRegistration.employment")
    @Mapping(target = "accountNumber", source = "finishRegistration.accountNumber")
    @Mapping(target = "isInsuranceEnabled", source = "offer.isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "offer.isSalaryClient")
    ScoringDataDto toScoringDataDto(FinishRegistrationRequestDto finishRegistration,
                                    AppliedOffer offer,
                                    Client client,
                                    PassportData passportData);
}

