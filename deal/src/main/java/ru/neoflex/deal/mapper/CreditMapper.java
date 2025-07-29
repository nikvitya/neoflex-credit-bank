package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.model.Credit;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto credit);
}