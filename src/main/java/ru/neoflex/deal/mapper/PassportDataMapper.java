package ru.neoflex.deal.mapper;

import org.mapstruct.*;
import ru.neoflex.deal.model.jsonb.PassportData;

import java.time.LocalDate;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassportDataMapper {

    @Mapping(target = "series", source = "series")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "issueBranch", ignore = true)
    @Mapping(target = "issueDate", ignore = true)
    PassportData toPassportData(String series, String number);

    @Mapping(target = "series", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "issueBranch", source = "issueBranch")
    @Mapping(target = "issueDate", source = "issueDate")
    PassportData toFullPassportData(@MappingTarget PassportData passportData, String issueBranch, LocalDate issueDate);
}

