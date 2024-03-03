package cz.harag.sandsaga.web.dto;


import java.util.Set;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Patrik Harag
 * @version 2024-03-03
 */
@RegisterForReflection
public class OutClaimDto {

    private Set<OutClaimEntryDto> accepted;
    private Set<OutClaimEntryDto> refused;

    public Set<OutClaimEntryDto> getAccepted() {
        return accepted;
    }

    public void setAccepted(Set<OutClaimEntryDto> accepted) {
        this.accepted = accepted;
    }

    public Set<OutClaimEntryDto> getRefused() {
        return refused;
    }

    public void setRefused(Set<OutClaimEntryDto> refused) {
        this.refused = refused;
    }
}