package cz.harag.sandsaga.web.dto;


import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@RegisterForReflection
public class InClaimDto {

    private List<InClaimEntryDto> completed;

    public List<InClaimEntryDto> getCompleted() {
        return completed;
    }

    public void setCompleted(List<InClaimEntryDto> completed) {
        this.completed = completed;
    }

}