package cz.harag.sandsaga.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@RegisterForReflection
public class OutClaimEntryDto {

    private Long id;
    private String scenario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public static OutClaimEntryDto convert(InClaimEntryDto dto) {
        OutClaimEntryDto result = new OutClaimEntryDto();
        result.setId(dto.getId());
        result.setScenario(dto.getScenario());
        return result;
    }
}
