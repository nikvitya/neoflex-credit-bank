package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.model.jsonb.AppliedOffer;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OfferMapper {
    AppliedOffer toAppliedOffer(LoanOfferDto loanOffer);
}

